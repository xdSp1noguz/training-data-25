import java.time.LocalDate;
import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Клас BasicDataOperationUsingQueue реалізує роботу з колекціями типу Queue для LocalDate.
 * 
 * <p>Основні функції класу:</p>
 * <ul>
 *   <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив LocalDate.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві LocalDate.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить мінімальне і максимальне значення в масиві.</li>
 *   <li>{@link #findInQueue()} - Пошук значення в черзі LocalDate.</li>
 *   <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в черзі.</li>
 *   <li>{@link #performQueueOperations()} - Виконує операції peek і poll з чергою.</li>
 * </ul>
 * 
 */
public class BasicDataOperationUsingQueue {
    private LocalDate dateValueToSearch;
    private LocalDate[] dateArray;
    private Queue<LocalDate> dateQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
 * @param dateValueToSearch Значення для пошуку
 * @param dateArray Масив LocalDate
     */
    BasicDataOperationUsingQueue(LocalDate dateValueToSearch, LocalDate[] dateArray) {
        this.dateValueToSearch = dateValueToSearch;
        this.dateArray = dateArray;
        this.dateQueue = new PriorityQueue<>(Arrays.asList(dateArray));
    }
    
    /**
     * Запускає комплексну обробку даних з використанням черги.
     * 
     * Метод завантажує дані, виконує операції з чергою та масивом LocalDate.
     */
    public void runDataProcessing() {
        // спочатку обробляємо чергу дати та часу
        findInQueue();
        locateMinMaxInQueue();
        performQueueOperations();

        // потім працюємо з масивом
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(dateArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Сортує масив об'єктiв LocalDate та виводить початковий i вiдсортований масиви.
     * Вимiрює та виводить час, витрачений на сортування масиву в наносекундах.
     */
    private void performArraySorting() {
        // вимірюємо тривалість упорядкування масиву дати та часу
        long timeStart = System.nanoTime();

        Arrays.sort(dateArray);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву дати i часу");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    private void findInArray() {
        // відстежуємо час виконання пошуку в масиві
        long timeStart = System.nanoTime();
        
        int position = Arrays.binarySearch(this.dateArray, dateValueToSearch);
        
        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi дати i часу");

        if (position >= 0) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві LocalDate.
     */
    private void locateMinMaxInArray() {
        if (dateArray == null || dateArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // відстежуємо час на визначення граничних значень
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
     * Здійснює пошук конкретного значення в черзі дати та часу.
     */
    private void findInQueue() {
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        boolean elementExists = this.dateQueue.contains(dateValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в Queue дати i часу");

        if (elementExists) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі LocalDate.
     */
    private void locateMinMaxInQueue() {
        if (dateQueue == null || dateQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

        LocalDate minValue = Collections.min(dateQueue);
        LocalDate maxValue = Collections.max(dateQueue);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в Queue");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою LocalDate.
     */
    private void performQueueOperations() {
        if (dateQueue == null || dateQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        LocalDate headElement = dateQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        headElement = dateQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = dateQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}