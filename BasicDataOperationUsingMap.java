import java.util.*;
import java.util.stream.Collectors;

public class BasicDataOperationUsingMap {

    // ИСПРАВЛЕНИЕ: Используем обычный класс вместо record, чтобы работало на старой Java
    public static class Cat {
        private final Integer age;
        private final String nickname;

        public Cat(Integer age, String nickname) {
            this.age = age;
            this.nickname = nickname;
        }

        public Integer age() { return age; }
        public String nickname() { return nickname; }

        @Override
        public String toString() {
            return "Cat{age='" + age + "', nickname='" + nickname + "'}";
        }

        // Обязательно для корректной работы HashMap
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cat cat = (Cat) o;
            return Objects.equals(age, cat.age) &&
                   Objects.equals(nickname, cat.nickname);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, nickname);
        }
    }

    // Вспомогательный класс для замеров времени
    static class PerformanceTracker {
        public static void displayOperationTime(long start, String operation) {
            long duration = System.nanoTime() - start;
            System.out.printf("  -> Час виконання [%s]: %d ns%n", operation, duration);
        }
    }

    // Компаратор
    private static final Comparator<Cat> CAT_COMPARATOR =
            Comparator.comparing(Cat::nickname, Comparator.reverseOrder())
                    .thenComparing(Cat::age);

    // Данные
    private final Cat KEY_TO_ADD = new Cat(16, "Сем");
    private final String VALUE_TO_ADD = "Джон";
    private final Cat KEY_TO_SEARCH_AND_DELETE = new Cat(2, "Барсик");
    private final String VALUE_TO_SEARCH_AND_DELETE = "Стефанія";

    // Мапы
    private HashMap<Cat, String> hashMap;
    private LinkedHashMap<Cat, String> linkedHashMap;

    public BasicDataOperationUsingMap(HashMap<Cat, String> hashMap, LinkedHashMap<Cat, String> linkedHashMap) {
        this.hashMap = hashMap;
        this.linkedHashMap = linkedHashMap;
    }

    public static void main(String[] args) {
        HashMap<Cat, String> map1 = new HashMap<>();
        fillData(map1);

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
        printMap(map, mapName + " (до сортування)");
        Map<Cat, String> sortedMap = sortMap(map);
        printMap(sortedMap, mapName + " (після сортування)");
        findByKey(map, mapName);
        findByValue(map, mapName);
        addEntry(map, mapName);
        removeByKey(map, mapName);
        removeByValue(map, mapName);
    }

    private void printMap(Map<Cat, String> map, String title) {
        System.out.println("\n=== " + title + " ===");
        long start = System.nanoTime();
        map.entrySet().forEach(entry ->
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
        );
        PerformanceTracker.displayOperationTime(start, "виведення");
    }

    private Map<Cat, String> sortMap(Map<Cat, String> map) {
        long start = System.nanoTime();
        Map<Cat, String> sorted = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(CAT_COMPARATOR))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new
                ));
        PerformanceTracker.displayOperationTime(start, "сортування");
        return sorted;
    }

    private void findByKey(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        boolean found = map.containsKey(KEY_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "пошук за ключем");
        if (found) System.out.println("Ключ знайдено: " + KEY_TO_SEARCH_AND_DELETE);
        else System.out.println("Ключ не знайдено.");
    }

    private void findByValue(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        Optional<Map.Entry<Cat, String>> result = map.entrySet().stream()
                .filter(e -> e.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .findFirst();
        PerformanceTracker.displayOperationTime(start, "пошук за значенням");
        result.ifPresent(e -> System.out.println("Знайдено власника: " + e.getKey()));
    }

    private void addEntry(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        map.put(KEY_TO_ADD, VALUE_TO_ADD);
        PerformanceTracker.displayOperationTime(start, "додавання");
        System.out.println("Додано: " + KEY_TO_ADD);
    }

    private void removeByKey(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        map.remove(KEY_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "видалення за ключем");
    }

    private void removeByValue(Map<Cat, String> map, String mapName) {
        long start = System.nanoTime();
        List<Cat> toRemove = map.entrySet().stream()
                .filter(e -> e.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        toRemove.forEach(map::remove);
        PerformanceTracker.displayOperationTime(start, "видалення за значенням");
    }

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