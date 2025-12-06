import java.util.*;
import java.util.stream.Collectors;
import java.text.Collator;

/**
 * Клас BasicDataOperationUsingMap з використанням Stream API.
 */
public class BasicDataOperationUsingMap {
    // Ключ для пошуку/видалення за віком (2)
    private final Cat KEY_TO_SEARCH_AND_DELETE = new Cat(2);
    // Ключ для додавання
    private final Cat KEY_TO_ADD = new Cat(16);

    private final String VALUE_TO_SEARCH_AND_DELETE = "Стефанія";
    private final String VALUE_TO_ADD = "Джон";

    // Використовуємо HashMap (замість застарілої Hashtable, як у вашому коді) та LinkedHashMap
    private HashMap<Cat, String> hashtable; 
    private LinkedHashMap<Cat, String> linkedHashMap;

    /**
     * Внутрішній клас Cat.
     * Реалізує Comparable<Cat> для сортування.
     */
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

            // 1. Сортування за нікнеймом
            if (this.nickname == null && other.nickname == null) {
                // pass
            } else if (this.nickname == null) {
                return 1;
            } else if (other.nickname == null) {
                return -1;
            } else {
                int nickComp = collator.compare(other.nickname, this.nickname); // Z->A
                if (nickComp != 0) return nickComp;
            }

            // 2. Сортування за віком
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
            return (nickname == null) ? "Cat{age='" + age + "'}" : "Cat{age='" + age + "', nickname='" + nickname + "'}";
        }
    }

    /**
     * Конструктор
     */
    BasicDataOperationUsingMap(HashMap<Cat, String> hashtable, LinkedHashMap<Cat, String> linkedHashMap) {
        this.hashtable = hashtable;
        this.linkedHashMap = linkedHashMap;
    }
    
    public void executeDataOperations() {
        // --- Hashtable (HashMap) ---
        System.out.println("========= Операції з HashMap (ex-Hashtable) =========");
        
        // 3.3 Друк через Stream API
        printMap("HashMap до сортування", hashtable);

        // 3.4 Сортування через Stream API
        sortHashtable();
        printMap("HashMap після сортування", hashtable);

        // Пошук
        findByKey(hashtable, "HashMap");
        findByValueUsingStream(hashtable, "HashMap"); // 3.5

        // Додавання
        addEntry(hashtable, "HashMap");
        
        // Видалення
        removeByKey(hashtable, "HashMap");
        removeByValueUsingStream(hashtable, "HashMap"); // 3.2

        // --- LinkedHashMap ---
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        
        printMap("LinkedHashMap до сортування", linkedHashMap);
        
        sortLinkedHashMap();
        printMap("LinkedHashMap після сортування", linkedHashMap);
        
        findByKey(linkedHashMap, "LinkedHashMap");
        findByValueUsingStream(linkedHashMap, "LinkedHashMap");

        addEntry(linkedHashMap, "LinkedHashMap");
        
        removeByKey(linkedHashMap, "LinkedHashMap");
        removeByValueUsingStream(linkedHashMap, "LinkedHashMap");
    }

    // =========================================================
    // Реалізація методів з використанням Stream API
    // =========================================================

    /**
     * 3.3 Виправлена операція друку (Stream API forEach)
     */
    private void printMap(String title, Map<Cat, String> map) {
        System.out.println("\n=== " + title + " ===");
        long timeStart = System.nanoTime();

        map.entrySet().forEach(entry -> 
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue())
        );

        PerformanceTracker.displayOperationTime(timeStart, "виведення (" + title + ")");
    }

    /**
     * 3.4 Сортування HashMap за ключами (Stream API).
     * Результат збирається в LinkedHashMap, щоб зберегти порядок,
     * і присвоюється назад змінній hashtable.
     */
    private void sortHashtable() {
        long timeStart = System.nanoTime();

        hashtable = hashtable.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Використовує Cat.compareTo
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Важливо: зберігаємо порядок
                )); // LinkedHashMap є підкласом HashMap, тому це присвоєння працює (або вимагає кастингу, якщо типи суворі)
        
        // Оскільки поле оголошено як HashMap, а collect повертає LinkedHashMap (який є HashMap),
        // Java дозволяє це. Але краще, щоб поле було типу Map. Тут залишаємо як є.

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap (Stream API)");
    }

    /**
     * 3.4 Сортування LinkedHashMap за ключами (Stream API).
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashMap = linkedHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap (Stream API)");
    }

    /**
     * 3.5 Пошук елемента за значенням (Stream API).
     * Замість Comparator та BinarySearch використовуємо filter().findFirst().
     */
    private void findByValueUsingStream(Map<Cat, String> map, String mapName) {
        long timeStart = System.nanoTime();

        Optional<Map.Entry<Cat, String>> result = map.entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .findFirst();

        PerformanceTracker.displayOperationTime(timeStart, "пошук за значенням (Stream API) в " + mapName);

        result.ifPresentOrElse(
            entry -> System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cat: " + entry.getKey()),
            () -> System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в " + mapName)
        );
    }

    /**
     * 3.2 та 3.5 Видалення елементів за значенням (Stream API).
     */
    private void removeByValueUsingStream(Map<Cat, String> map, String mapName) {
        long timeStart = System.nanoTime();

        // 1. Знаходимо ключі
        List<Cat> keysToRemove = map.entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 2. Видаляємо
        keysToRemove.forEach(map::remove);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням (Stream API) з " + mapName);
        System.out.println("Видалено записів: " + keysToRemove.size());
    }

    /**
     * Пошук за ключем (адаптований під вашу логіку з age/nickname).
     * Тут також можна використати Stream API для фільтрації.
     */
    private void findByKey(Map<Cat, String> map, String mapName) {
        long timeStart = System.nanoTime();

        // Використовуємо Stream для пошуку навіть за частковим ключем (age або nickname)
        Optional<Map.Entry<Cat, String>> foundEntry = map.entrySet().stream()
            .filter(entry -> {
                Cat k = entry.getKey();
                if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
                    return k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge());
                } else if (KEY_TO_SEARCH_AND_DELETE.getAge() == null) {
                    return k.getNickname().equals(KEY_TO_SEARCH_AND_DELETE.getNickname());
                } else {
                    return k.equals(KEY_TO_SEARCH_AND_DELETE);
                }
            })
            .findFirst();

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в " + mapName);

        foundEntry.ifPresentOrElse(
            entry -> System.out.println("Ключ знайдено: " + entry.getKey() + " -> " + entry.getValue()),
            () -> System.out.println("Ключ не знайдено.")
        );
    }

    /**
     * Додавання запису (стандартне, без змін логіки, лише універсалізація).
     */
    private void addEntry(Map<Cat, String> map, String mapName) {
        long timeStart = System.nanoTime();
        
        // Логіка перевірки існування (можна теж через stream anyMatch)
        boolean exists = map.keySet().stream().anyMatch(k -> 
            (KEY_TO_ADD.getNickname() != null && KEY_TO_ADD.getAge() == null) 
            ? k.getNickname().equals(KEY_TO_ADD.getNickname())
            : k.equals(KEY_TO_ADD)
        );

        if (exists) {
            System.out.println("Ключ вже існує в " + mapName + ". Пропуск.");
        } else {
            map.put(KEY_TO_ADD, VALUE_TO_ADD);
            System.out.println("Додано в " + mapName + ": " + KEY_TO_ADD);
        }
        PerformanceTracker.displayOperationTime(timeStart, "додавання в " + mapName);
    }

    /**
     * Видалення за ключем.
     */
    private void removeByKey(Map<Cat, String> map, String mapName) {
        long timeStart = System.nanoTime();

        List<Cat> toRemove = map.keySet().stream()
            .filter(k -> {
                if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
                    return k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge());
                }
                return k.equals(KEY_TO_SEARCH_AND_DELETE);
            })
            .collect(Collectors.toList());

        toRemove.forEach(k -> {
            String val = map.remove(k);
            System.out.println("Видалено з " + mapName + ": " + k + " (Власник: " + val + ")");
        });

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з " + mapName);
    }

    public static void main(String[] args) {
        // Заповнення даними
        HashMap<Cat, String> map1 = new HashMap<>();
        fillData(map1);

        LinkedHashMap<Cat, String> map2 = new LinkedHashMap<>();
        fillData(map2);

        BasicDataOperationUsingMap app = new BasicDataOperationUsingMap(map1, map2);
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