import java.util.*;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingMap з використанням Java Record та Stream API.
 * Дані адаптовано під варіант користувача.
 */
public class BasicDataOperationUsingMap {

    // 1. Оголошення Record (Змінено порядок: спочатку age, потім nickname, як у ваших даних)
    public record Cat(Integer age, String nickname) {}

    // 2. Компаратор: Кличка за зменшенням (Z-A), при збігу — вік за зростанням (0-9)
    private static final Comparator<Cat> CAT_COMPARATOR = 
            Comparator.comparing(Cat::nickname, Comparator.reverseOrder())
                      .thenComparing(Cat::age);

    // Дані для додавання
    private final Cat KEY_TO_ADD = new Cat(16, "Сем");
    private final String VALUE_TO_ADD = "Джон";
    
    // Ключ для пошуку/видалення (Взяв "Барсик", бо він є у вашому списку з віком 2)
    private final Cat KEY_TO_SEARCH_AND_DELETE = new Cat(2, "Барсик");
    private final String VALUE_TO_SEARCH_AND_DELETE = "Стефанія";

    // Мапи
    private HashMap<Cat, String> hashMap; 
    private LinkedHashMap<Cat, String> linkedHashMap;

    public BasicDataOperationUsingMap(HashMap<Cat, String> hashMap, LinkedHashMap<Cat, String> linkedHashMap) {
        this.hashMap = hashMap;
        this.linkedHashMap = linkedHashMap;
    }
    
    public static void main(String[] args) {
        // Ініціалізація HashMap
        HashMap<Cat, String> map1 = new HashMap<>();
        fillData(map1);

        // Ініціалізація LinkedHashMap
        LinkedHashMap<Cat, String> map2 = new LinkedHashMap<>();
        fillData(map2);

        BasicDataOperationUsingMap app = new BasicDataOperationUsingMap(map1, map2);
        app.executeDataOperations();
    }

    public void executeDataOperations() {
        System.out.println("====== ПОРІВНЯЛЬНИЙ АНАЛІЗ: HashMap vs LinkedHashMap ======");
        
        System.out.println("\n--- Операції з HashMap ---");
        runTestCycle(hashMap, "HashMap");

        System.out.println("\n--- Операції з LinkedHashMap ---");
        runTestCycle(linkedHashMap, "LinkedHashMap");
    }

    private void runTestCycle(Map<Cat, String> map, String mapName) {
        // 1. Друк до сортування
        printMap(map, mapName + " (до сортування)");

        // 2. Сортування
        Map<Cat, String> sortedMap = sortMap(map);
        printMap(sortedMap, mapName + " (після сортування)");

        // 3. Пошук
        findByKey(map, mapName);
        findByValue(map, mapName);

        // 4. Додавання
        addEntry(map, mapName);

        // 5. Видалення
        removeByKey(map, mapName);
        removeByValue(map, mapName);
    }

    // ================= МЕТОДИ (Stream API) =================

    private void printMap(Map<Cat, String> map, String title) {
        System.out.println("\n=== " + title + " ===");
        long start = System.nanoTime();
        
        map.entrySet().forEach(entry -> 
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
        );

        PerformanceTracker.displayOperationTime(start, "виведення (" + title + ")");
    }

    private Map<Cat, String> sortMap(Map<Cat, String> map) {
        long start = System.nanoTime();

        // Сортуємо потік і збираємо в LinkedHashMap
        Map<Cat, String> sorted = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(CAT_COMPARATOR))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new 
                ));

        PerformanceTracker.displayOperationTime(start, "сортування (Stream API)");
        return sorted;
    }

    private void findByKey(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        boolean found = map.containsKey(KEY_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "пошук за ключем");
        
        if (found) {
            System.out.println("Ключ знайдено: " + KEY_TO_SEARCH_AND_DELETE + " -> " + map.get(KEY_TO_SEARCH_AND_DELETE));
        } else {
            System.out.println("Ключ " + KEY_TO_SEARCH_AND_DELETE + " не знайдено.");
        }
    }

    private void findByValue(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        
        Optional<Map.Entry<Cat, String>> result = map.entrySet().stream()
                .filter(e -> e.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .findFirst();

        PerformanceTracker.displayOperationTime(start, "пошук за значенням (Stream API)");
        
        result.ifPresent(e -> System.out.println("Знайдено власника '" + VALUE_TO_SEARCH_AND_DELETE + "': " + e.getKey()));
    }

    private void addEntry(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        map.put(KEY_TO_ADD, VALUE_TO_ADD);
        PerformanceTracker.displayOperationTime(start, "додавання запису");
        System.out.println("Додано: " + KEY_TO_ADD);
    }

    private void removeByKey(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        String removed = map.remove(KEY_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "видалення за ключем");
        
        if (removed != null) System.out.println("Видалено кота: " + KEY_TO_SEARCH_AND_DELETE);
    }

    private void removeByValue(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        
        List<Cat> toRemove = map.entrySet().stream()
                .filter(e -> e.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        toRemove.forEach(map::remove);

        PerformanceTracker.displayOperationTime(start, "видалення за значенням (Stream API)");
        System.out.println("Видалено записів власника '" + VALUE_TO_SEARCH_AND_DELETE + "': " + toRemove.size());
    }

    // ================= ВАШІ ДАНІ =================
    private static void fillData(Map<Cat, String> map) {
        map.put(new Cat(4, "Тум"), "Ярослав");
        map.put(new Cat(12, "Луна"), "Олена");
        map.put(new Cat(2, "Барсик"), "Поліна");
        map.put(new Cat(5, "Боні"), "Тимофій");
        map.put(new Cat(3, "Тайсон"), "Стефанія");
        map.put(new Cat(9, "Ґуфі"), "Андрій");
        map.put(new Cat(7, "Муся"), "Ярослав");
        map.put(new Cat(8, "Чіпо"), "Поліна");
        map.put(new Cat(10, "Сніжок"), "Стефанія");
        map.put(new Cat(14, "Марс"), "Тимофій");
    }
}