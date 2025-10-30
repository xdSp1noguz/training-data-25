import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Клас BasicDataOperationUsingList реалізує операції з колекціями типу ArrayList для даних LocalDate.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив елементів LocalDate.</li>
 *   <li>{@link #findInArray()} - Здійснює пошук елемента в масиві LocalDate.</li>
 *   <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в масиві.</li>
 *   <li>{@link #sortList()} - Сортує колекцію List з LocalDate.</li>
 *   <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 *   <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationUsingList {
    private LocalDate dateValueToSearch;
    private LocalDate[] dateArray;
    private List<LocalDate> dateList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param dateTimeValueToSearch Значення для пошуку
     * @param dateArray Масив LocalDate
     */
    BasicDataOperationUsingList(LocalDate dateValueToSearch, LocalDate[] dateArray) {
        this.dateValueToSearch = dateValueToSearch;
        this.dateArray = dateArray;
        this.dateList = new LinkedList<>(Arrays.asList(dateArray));
    }
    
    /**
     * Виконує комплексні операції з структурами даних.
     * 
     * Метод завантажує масив і список об'єктів LocalDate, 
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations() {
        // спочатку працюємо з колекцією List
        findInList();
        locateMinMaxInList();
        
        sortList();
        
        findInList();
        locateMinMaxInList();

        // потім обробляємо масив дати та часу
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        
        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до окремого файлу
        DataFileHandler.writeArrayToFile(dateArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів LocalDate за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(dateArray);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву дати");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    void findInArray() {
        long timeStart = System.nanoTime();

        int position = Arrays.binarySearch(this.dateArray, dateValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi дати");

        if (position >= 0) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві дати та часу.
     */
    void locateMinMaxInArray() {
        if (dateArray == null || dateArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalDate minValue = dateArray[0];
        LocalDate maxValue = dateArray[0];

        for (LocalDate currentDate : dateArray) {
            if (currentDate.isBefore(minValue)) {
                minValue = currentDate;
            }
            if (currentDate.isAfter(maxValue)) {
                maxValue = currentDate;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в масивi");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення дати та часу в колекції ArrayList.
     */
    void findInList() {
        long timeStart = System.nanoTime();

        int position = Collections.binarySearch(this.dateList, dateValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в List дати");        

        if (position >= 0) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в ArrayList за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в ArrayList.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції ArrayList з датами.
     */
    void locateMinMaxInList() {
        if (dateList == null || dateList.isEmpty()) {
            System.out.println("Колекція ArrayList є пустою або не ініціалізованою.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalDate minValue = Collections.min(dateList);
        LocalDate maxValue = Collections.max(dateList);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в List");

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами LocalDate за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        Collections.sort(dateList);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування ArrayList дати");
    }
}