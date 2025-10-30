import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Клас BasicDataOperationUsingSet реалізує операції з множиною HashSet для LocalDate.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив LocalDate.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві LocalDate.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить граничні значення в масиві.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині LocalDate.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeArrayAndSet()} - Аналізує елементи масиву та множини.</li>
 * </ul>
 */
public class BasicDataOperationUsingSet {
    LocalDate dateValueToSearch;
    LocalDate[] dateArray;
    Set<LocalDate> dateSet = new HashSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
 * @param dateValueToSearch Значення для пошуку
 * @param dateArray Масив LocalDate
     */
    BasicDataOperationUsingSet(LocalDate dateValueToSearch, LocalDate[] dateArray) {
        this.dateValueToSearch = dateValueToSearch;
        this.dateArray = dateArray;
        this.dateSet = new HashSet<>(Arrays.asList(dateArray));
    }
    
    /**
     * Запускає комплексний аналіз даних з використанням множини HashSet.
     * 
     * Метод завантажує дані, виконує операції з множиною та масивом LocalDate.
     */
    public void executeDataAnalysis() {
        // спочатку аналізуємо множину дати та часу
        findInSet();
        locateMinMaxInSet();
        analyzeArrayAndSet();

        // потім обробляємо масив
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(dateArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів LocalDate за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    private void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(dateArray);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву дати i часу");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    private void findInArray() {
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
     * Здійснює пошук конкретного значення в множині дати та часу.
     */
    private void findInSet() {
        long timeStart = System.nanoTime();

        boolean elementExists = this.dateSet.contains(dateValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в HashSet дати i часу");

        if (elementExists) {
            System.out.println("Елемент '" + dateValueToSearch + "' знайдено в HashSet");
        } else {
            System.out.println("Елемент '" + dateValueToSearch + "' відсутній в HashSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині LocalDate.
     */
    private void locateMinMaxInSet() {
        if (dateSet == null || dateSet.isEmpty()) {
            System.out.println("HashSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalDate minValue = Collections.min(dateSet);
        LocalDate maxValue = Collections.max(dateSet);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в HashSet");

        System.out.println("Найменше значення в HashSet: " + minValue);
        System.out.println("Найбільше значення в HashSet: " + maxValue);
    }

    /**
     * Аналізує та порівнює елементи масиву та множини.
     */
    private void analyzeArrayAndSet() {
        System.out.println("Кiлькiсть елементiв в масивi: " + dateArray.length);
        System.out.println("Кiлькiсть елементiв в HashSet: " + dateSet.size());

        boolean allElementsPresent = true;
        for (LocalDate dateElement : dateArray) {
            if (!dateSet.contains(dateElement)) {
                allElementsPresent = false;
                break;
            }
        }

        if (allElementsPresent) {
            System.out.println("Всi елементи масиву наявні в HashSet.");
        } else {
            System.out.println("Не всi елементи масиву наявні в HashSet.");
        }
    }
}