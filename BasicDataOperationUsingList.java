import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingList реалізує операції з колекціями типу LinkedList для даних LocalDate.
 * Використовує Stream API для обробки даних.
 */
public class BasicDataOperationUsingList {
    private LocalDate dateValueToSearch;
    private LocalDate[] dateArray;
    private List<LocalDate> dateList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * * @param dateValueToSearch Значення для пошуку
     * @param dateArray Масив LocalDate
     */
    BasicDataOperationUsingList(LocalDate dateValueToSearch, LocalDate[] dateArray) {
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
     * Упорядковує масив об'єктів LocalDate за зростанням (Stream API).
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        dateArray = Arrays.stream(dateArray)
                .sorted()
                .toArray(LocalDate[]::new);

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

        LocalDate minValue = Arrays.stream(dateArray)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate maxValue = Arrays.stream(dateArray)
                .max(LocalDate::compareTo)
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

        LocalDate minValue = dateList.stream()
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate maxValue = dateList.stream()
                .max(LocalDate::compareTo)
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
}