import java.util.*;
import java.text.Collator;
import java.util.Locale;

/**
 * Фінальна робоча версія.
 * Виправлено всі помилки синтаксису та логіки.
 */
public class BasicDataOperationUsingMap {
    
    // Константи для пошуку та додавання
    private final Cat KEY_TO_SEARCH_AND_DELETE = new Cat(2);
    private final Cat KEY_TO_ADD = new Cat(16);
    private final String VALUE_TO_SEARCH_AND_DELETE = "Стефанія";
    private final String VALUE_TO_ADD = "Джон";

    private Hashtable<Cat, String> hashtable;
    private LinkedHashMap<Cat, String> linkedHashMap;

    // === Клас для замірів часу ===
    static class PerformanceTracker {
        public static void displayOperationTime(long start, String operation) {
            long duration = System.nanoTime() - start;
            System.out.printf("  -> Час виконання [%s]: %d ns%n", operation, duration);
        }
    }

    // === Компаратор для значень (String) ===
    static class OwnerValueComparator implements Comparator<Map.Entry<Cat, String>> {
        @Override
        public int compare(Map.Entry<Cat, String> e1, Map.Entry<Cat, String> e2) {
            String v1 = e1.getValue();
            String v2 = e2.getValue();
            if (v1 == null && v2 == null) return 0;
            if (v1 == null) return -1;
            if (v2 == null) return 1;
            return v1.compareTo(v2);
        }
    }

    // === Внутрішній клас Cat ===
    public static class Cat implements Comparable<Cat> {
        private final Integer age;
        private final String nickname;

        public Cat(Integer age) {
            this.age = age;
            this.nickname = null;
        }

        public Cat(Integer age, String nickname) {
            this.age = age;
            this.nickname = nickname;
        }

        public Integer getAge() { return age; }
        public String getNickname() { return nickname; }

        @Override
        public int compareTo(Cat other) {
            if (other == null) return 1;
            Collator collator = Collator.getInstance(new Locale("uk"));
            collator.setStrength(Collator.PRIMARY);

            // Сортування: Nickname (Z-A), потім Age (0-9)
            if (this.nickname == null && other.nickname == null) {
                // pass
            } else if (this.nickname == null) {
                return 1;
            } else if (other.nickname == null) {
                return -1;
            } else {
                int nickComp = collator.compare(other.nickname, this.nickname);
                if (nickComp != 0) return nickComp;
            }
            
            if (this.age == null && other.age == null) return 0;
            if (this.age == null) return -1;
            if (other.age == null) return 1;
            return Integer.compare(this.age, other.age);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Cat cat = (Cat) obj;
            return Objects.equals(age, cat.age) && Objects.equals(nickname, cat.nickname);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, nickname);
        }

        @Override
        public String toString() {
            return "Cat{age='" + (age != null ? age : "null") + "', nickname='" + (nickname != null ? nickname : "null") + "'}";
        }
    }

    // === Конструктор ===
    BasicDataOperationUsingMap(Hashtable<Cat, String> hashtable, LinkedHashMap<Cat, String> linkedHashMap) {
        this.hashtable = hashtable;
        this.linkedHashMap = linkedHashMap;
    }
    
    // === Основна логіка ===
    public void executeDataOperations() {
        System.out.println("========= Операції з Hashtable =========");
        findByKeyInHashtable();
        findByValueInHashtable();
        printHashtable();
        sortHashtable();
        printHashtable();
        addEntryToHashtable();
        removeByKeyFromHashtable();
        removeByValueFromHashtable();
                
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();
        printLinkedHashMap();
        sortLinkedHashMap();
        printLinkedHashMap();
        addEntryToLinkedHashMap();
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();
    }

    // --- Hashtable Methods ---
    private void printHashtable() {
        System.out.println("\n=== Hashtable ===");
        long start = System.nanoTime();
        hashtable.forEach((k, v) -> System.out.println("  " + k + " -> " + v));
        PerformanceTracker.displayOperationTime(start, "виведення Hashtable");
    }

    private void sortHashtable() {
        long start = System.nanoTime();
        List<Cat> sortedKeys = new ArrayList<>(hashtable.keySet());
        Collections.sort(sortedKeys);
        Hashtable<Cat, String> sortedMap = new Hashtable<>();
        for (Cat key : sortedKeys) sortedMap.put(key, hashtable.get(key));
        hashtable = sortedMap;
        PerformanceTracker.displayOperationTime(start, "сортування Hashtable");
    }

    void findByKeyInHashtable() {
        // Пошук
        long start = System.nanoTime();
        String found = null;
        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
                if (Objects.equals(entry.getKey().getAge(), KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    found = entry.getValue(); break;
                }
            }
        } else if (hashtable.containsKey(KEY_TO_SEARCH_AND_DELETE)) {
            found = hashtable.get(KEY_TO_SEARCH_AND_DELETE);
        }
        PerformanceTracker.displayOperationTime(start, "пошук ключа в Hashtable");
        if (found != null) System.out.println("Знайдено: " + found);
    }

    void findByValueInHashtable() {
        long start = System.nanoTime();
        boolean found = hashtable.containsValue(VALUE_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "пошук значення в Hashtable");
        if (found) System.out.println("Власника знайдено.");
    }

    void addEntryToHashtable() {
        long start = System.nanoTime();
        hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);
        PerformanceTracker.displayOperationTime(start, "додавання в Hashtable");
    }

    void removeByKeyFromHashtable() {
        long start = System.nanoTime();
        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            hashtable.keySet().removeIf(k -> Objects.equals(k.getAge(), KEY_TO_SEARCH_AND_DELETE.getAge()));
        } else {
            hashtable.remove(KEY_TO_SEARCH_AND_DELETE);
        }
        PerformanceTracker.displayOperationTime(start, "видалення ключа з Hashtable");
    }

    void removeByValueFromHashtable() {
        long start = System.nanoTime();
        hashtable.values().removeIf(v -> v.equals(VALUE_TO_SEARCH_AND_DELETE));
        PerformanceTracker.displayOperationTime(start, "видалення значення з Hashtable");
    }

    // --- LinkedHashMap Methods ---
    private void printLinkedHashMap() {
        System.out.println("\n=== LinkedHashMap ===");
        long start = System.nanoTime();
        linkedHashMap.forEach((k, v) -> System.out.println("  " + k + " -> " + v));
        PerformanceTracker.displayOperationTime(start, "виведення LinkedHashMap");
    }

    private void sortLinkedHashMap() {
        long start = System.nanoTime();
        List<Map.Entry<Cat, String>> entries = new ArrayList<>(linkedHashMap.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        LinkedHashMap<Cat, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Cat, String> entry : entries) sortedMap.put(entry.getKey(), entry.getValue());
        linkedHashMap = sortedMap;
        PerformanceTracker.displayOperationTime(start, "сортування LinkedHashMap");
    }

    void findByKeyInLinkedHashMap() {
        long start = System.nanoTime();
        String found = null;
        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
                if (Objects.equals(entry.getKey().getAge(), KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    found = entry.getValue(); break;
                }
            }
        } else if (linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE)) {
            found = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
        }
        PerformanceTracker.displayOperationTime(start, "пошук ключа в LinkedHashMap");
        if (found != null) System.out.println("Знайдено: " + found);
    }

    void findByValueInLinkedHashMap() {
        long start = System.nanoTime();
        boolean found = linkedHashMap.containsValue(VALUE_TO_SEARCH_AND_DELETE);
        PerformanceTracker.displayOperationTime(start, "пошук значення в LinkedHashMap");
        if (found) System.out.println("Власника знайдено.");
    }

    void addEntryToLinkedHashMap() {
        long start = System.nanoTime();
        linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
        PerformanceTracker.displayOperationTime(start, "додавання в LinkedHashMap");
    }

    void removeByKeyFromLinkedHashMap() {
        long start = System.nanoTime();
        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            linkedHashMap.keySet().removeIf(k -> Objects.equals(k.getAge(), KEY_TO_SEARCH_AND_DELETE.getAge()));
        } else {
            linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);
        }
        PerformanceTracker.displayOperationTime(start, "видалення ключа з LinkedHashMap");
    }

    void removeByValueFromLinkedHashMap() {
        long start = System.nanoTime();
        linkedHashMap.values().removeIf(v -> v.equals(VALUE_TO_SEARCH_AND_DELETE));
        PerformanceTracker.displayOperationTime(start, "видалення значення з LinkedHashMap");
    }

    // === MAIN ===
    public static void main(String[] args) {
        Hashtable<Cat, String> hashtable = new Hashtable<>();
        fillData(hashtable);
        LinkedHashMap<Cat, String> linkedHashMap = new LinkedHashMap<>();
        fillData(linkedHashMap);

        BasicDataOperationUsingMap app = new BasicDataOperationUsingMap(hashtable, linkedHashMap);
        app.executeDataOperations();
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