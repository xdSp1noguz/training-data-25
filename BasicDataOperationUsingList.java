import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime; // CHANGED: Імпорт змінено на LocalDateTime
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingList реалізує операції з колекціями типу LinkedList для даних LocalDateTime.
 * Використовує Stream API для обробки даних.
 */
public class BasicDataOperationUsingList {
    // CHANGED: Всі поля змінено на LocalDateTime
    private LocalDateTime dateValueToSearch;
    private LocalDateTime[] dateArray;
    private List<LocalDateTime> dateList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * @param dateValueToSearch Значення для пошуку
     * @param dateArray Масив LocalDateTime
     */
    // CHANGED: Аргументи конструктора тепер приймають LocalDateTime
    BasicDataOperationUsingList(LocalDateTime dateValueToSearch, LocalDateTime[] dateArray) {
        this.dateValueToSearch = dateValueToSearch;
        this.dateArray = dateArray;
        // Використовуємо LinkedList згідно з вашим варіантом завдання
        this.dateList = new LinkedList<>(Arrays.asList(dateArray));
    }
    
    /**
     * Виконує комплексні операції з структурами даних.
     */
    public void executeDataOperations() {
        // --- Робота з колекцією List ---
        System.out.println("--- Операції з LinkedList ---");
        findInList();
        locateMinMaxInList();
        
        sortList();
        
        findInList();
        locateMinMaxInList();

        // --- Робота з масивом ---
        System.out.println("\n--- Операції з масивом ---");
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        
        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до окремого файлу
        DataFileHandler.writeArrayToFile(dateArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів LocalDateTime за зростанням (Stream API).
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        dateArray = Arrays.stream(dateArray)
                .sorted()
                .toArray(LocalDateTime[]::new); // CHANGED: Створюємо масив LocalDateTime

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву (Stream API)");
    }

    /**
     * Здійснює пошук конкретного значення в масиві (Stream API).
     */
    void findInArray() {
        long timeStart = System.nanoTime();

        // Логіка пошуку індексу через Stream
        int position = Arrays.stream(dateArray)
                .map(Arrays.asList(dateArray)::indexOf) // Отримуємо індекси
                .filter(i -> dateValueToSearch.equals(dateArray[i])) // Шукаємо збіг
                .findFirst()
                .orElse(-1);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi (Stream API)");

        if (position >= 0) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві (Stream API).
     */
    void locateMinMaxInArray() {
        if (dateArray == null || dateArray.length == 0) {
            System.out.println("Масив є пустим.");
            return;
        }

        long timeStart = System.nanoTime();

        // CHANGED: compareTo тепер працює з LocalDateTime
        LocalDateTime minValue = Arrays.stream(dateArray)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime maxValue = Arrays.stream(dateArray)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "min/max в масивi (Stream API)");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення дати в колекції List (Stream API).
     */
    void findInList() {
        long timeStart = System.nanoTime();

        int position = dateList.stream()
                .map(dateList::indexOf)
                .filter(i -> dateValueToSearch.equals(dateList.get(i)))
                .findFirst()
                .orElse(-1);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в List (Stream API)");        

        if (position >= 0) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в List за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в List.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції List (Stream API).
     */
    void locateMinMaxInList() {
        if (dateList == null || dateList.isEmpty()) {
            System.out.println("Колекція List є пустою.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalDateTime minValue = dateList.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime maxValue = dateList.stream()
                .max(LocalDateTime::compareTo)
                .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "min/max в List (Stream API)");

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List за зростанням (Stream API).
     */
    void sortList() {
        long timeStart = System.nanoTime();

        // Сортуємо і збираємо назад у LinkedList
        dateList = dateList.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування List (Stream API)");
    }

   public static void main(String[] args) {
        String filePath = "list/LocalDate.data"; // Перевірте, чи файл містить час, або змініть логіку парсингу

        try {
            // 1. Читаем файл, чистим от BOM и лишних пробелов
            List<LocalDateTime> rawList = Files.lines(Paths.get(filePath))
                    .map(line -> line.replace("\uFEFF", "").trim()) // Удаляем невидимый BOM
                    .filter(line -> !line.isEmpty()) // Пропускаем пустые строки
                    // CHANGED: Парсинг в LocalDateTime. 
                    // УВАГА: Якщо у файлі лише дати (2023-01-01), використовуйте .map(d -> LocalDate.parse(d).atStartOfDay())
                    .map(LocalDateTime::parse) 
                    .collect(Collectors.toList());

            LocalDateTime[] datesFromFile = rawList.toArray(new LocalDateTime[0]);

            // 2. Определяем дату для поиска
            LocalDateTime searchTarget;
            if (args.length > 0) {
                searchTarget = LocalDateTime.parse(args[0]);
                System.out.println("Шукаємо дату з аргументів: " + searchTarget);
            } else {
                searchTarget = LocalDateTime.now(); 
                System.out.println("Аргумент не передано, шукаємо поточну дату: " + searchTarget);
            }

            System.out.println("Загружено дат из файла: " + datesFromFile.length);

            // 3. Запуск логики
            BasicDataOperationUsingList app = new BasicDataOperationUsingList(searchTarget, datesFromFile);
            app.executeDataOperations();

        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Помилка даних: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}