import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
// import java.util.Hashtable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.text.Collator;
import java.util.Locale;

/**
 * Клас BasicDataOperationUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними Map.</li>
 *   <li>{@link #findByKey()} - Здійснює пошук елемента за ключем в Map.</li>
 *   <li>{@link #findByValue()} - Здійснює пошук елемента за значенням в Map.</li>
 *   <li>{@link #addEntry()} - Додає новий запис до Map.</li>
 *   <li>{@link #removeByKey()} - Видаляє запис з Map за ключем.</li>
 *   <li>{@link #removeByValue()} - Видаляє записи з Map за значенням.</li>
 *   <li>{@link #sortByKey()} - Сортує Map за ключами.</li>
 *   <li>{@link #sortByValue()} - Сортує Map за значеннями.</li>
 * </ul>
 */
public class BasicDataOperationUsingMap {
    // Ключ для пошуку/видалення за віком (2) — nickname == null означає пошук по age
    private final Cat KEY_TO_SEARCH_AND_DELETE = new Cat(2);
    // Додаємо Cat{age=16} -> "Джон"
    private final Cat KEY_TO_ADD = new Cat(16);

    private final String VALUE_TO_SEARCH_AND_DELETE = "Стефанія";
    private final String VALUE_TO_ADD = "Джон";

    // private Hashtable<Cat, String> hashtable;
    private HashMap<Cat, String> hashtable;
    private LinkedHashMap<Cat, String> linkedHashMap;

    /**
     * Компаратор для сортування Map.Entry за значеннями String.
     * Використовує метод String.compareTo() для порівняння імен власників.
     */
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

    /**
     * Внутрішній клас Cat для зберігання інформації про домашню тварину.
     * 
     * Реалізує Comparable<Cat> для визначення природного порядку сортування.
     * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
     */
    public static class Cat implements Comparable<Cat> {
        // Характеристика — вік тварини і кличка (nickname)
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

        public Integer getAge() {
            return age;
        }

        public String getNickname() {
            return nickname;
        }

        /**
         * Природний порядок для Cat — спочатку за кличкою (nickname) за зростанням
         * згідно української локалі (A → Я), потім за віком (age) за зростанням.
         *
         * Null-поведінка:
         * - null nickname вважаємо "більшим", щоб він опинявся в кінці при сортуванні A→Я;
         * - null age вважаємо меншим за будь-який не-null age (тому йде першим серед однакових nickname).
         */
        @Override
        public int compareTo(Cat other) {
            if (other == null) return 1;

            // Collator для української локалі — коректне порівняння кирилиці
            Collator collator = Collator.getInstance(new Locale("uk"));
            collator.setStrength(Collator.PRIMARY); // регистронезависимое сравнение

            // 1) Порівняння по nickname — за зростанням (A → Я)
            if (this.nickname == null && other.nickname == null) {
                // продовжимо до порівняння age
            } else if (this.nickname == null) {
                // розміщуємо null після не-null => вважаємо його "більшим"
                return 1;
            } else if (other.nickname == null) {
                return -1;
            } else {
                // Для порядку від Я до А: інвертуємо аргументы collator (other vs this)
                int nickComp = collator.compare(other.nickname, this.nickname);
                if (nickComp != 0) return nickComp;
            }

            // 2) Якщо nickname рівні (або обидва null) — порівнюємо по age за зростанням
            if (this.age == null && other.age == null) return 0;
            if (this.age == null) return -1;
            if (other.age == null) return 1;
            return Integer.compare(this.age, other.age);
        }

        /**
         * Рівність визначається по полях age та nickname.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Cat cat = (Cat) obj;
            boolean ageEq = age != null ? age.equals(cat.age) : cat.age == null;
            boolean nickEq = nickname != null ? nickname.equals(cat.nickname) : cat.nickname == null;
            return ageEq && nickEq;
        }

        /**
         * Хеш-код на основі age та nickname.
         */
        @Override
        public int hashCode() {
            int result = age != null ? age.hashCode() : 0;
            result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
            return result;
        }

        /**
         * Формат виводу: Cat{age='4', nickname='Тум'} або Cat{age='16'} якщо nickname == null
         */
        @Override
        public String toString() {
            if (nickname == null) {
                return "Cat{age='" + (age != null ? age.toString() : "null") + "'}";
            }
            return "Cat{age='" + (age != null ? age.toString() : "null") + "', nickname='" + nickname + "'}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт з готовими даними.
     * 
     * @param hashtable Hashtable з початковими даними (ключ: Cat, значення: ім'я власника)
     * @param linkedHashMap LinkedHashMap з початковими даними (ключ: Cat, значення: ім'я власника)
     */
    BasicDataOperationUsingMap(HashMap<Cat, String> hashtable, LinkedHashMap<Cat, String> linkedHashMap) {
        this.hashtable = hashtable;
        this.linkedHashMap = linkedHashMap;
    }
    
    /**
     * Виконує комплексні операції з Map.
     * 
     * Метод виконує різноманітні операції з Map: пошук, додавання, видалення та сортування.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з Hashtable
        System.out.println("========= Операції з Hashtable =========");
        System.out.println("Початковий розмір Hashtable: " + hashtable.size());
        
        // Пошук до сортування
        findByKeyInHashtable();
        findByValueInHashtable();

        printHashtable();
        sortHashtable();
        printHashtable();

        // Пошук після сортування
        findByKeyInHashtable();
        findByValueInHashtable();

        addEntryToHashtable();
        
        removeByKeyFromHashtable();
        removeByValueFromHashtable();
               
        System.out.println("Кінцевий розмір Hashtable: " + hashtable.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashMap.size());
        
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        printLinkedHashMap();

        // Сортуємо LinkedHashMap за ключами (використовує природний порядок Cat.compareTo())
        sortLinkedHashMap();
        printLinkedHashMap();
        
        addEntryToLinkedHashMap();
        
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();
        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashMap.size());
    }


    // ===== Методи для Hashtable =====

    /**
     * Виводить вміст Hashtable без сортування.
     * Hashtable не гарантує жодного порядку елементів.
     */
    private void printHashtable() {
        System.out.println("\n=== Пари ключ-значення в Hashtable ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в Hashtable");
    }

    /**
     * Сортує Hashtable за ключами.
     * Використовує Collections.sort() з природним порядком Cat (Cat.compareTo()).
     * Перезаписує hashtable відсортованими даними.
     */
    private void sortHashtable() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Cat
        List<Cat> sortedKeys = new ArrayList<>(hashtable.keySet());
        Collections.sort(sortedKeys);
        
        // Створюємо нову Hashtable з відсортованими ключами
        HashMap<Cat, String> sortedHashtable = new HashMap<>();
        for (Cat key : sortedKeys) {
            sortedHashtable.put(key, hashtable.get(key));
        }
        
        // Перезаписуємо оригінальну hashtable
        hashtable = sortedHashtable;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Hashtable за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в Hashtable.
     * Використовує Cat.hashCode() та Cat.equals() для пошуку.
     */
    void findByKeyInHashtable() {
        long timeStart = System.nanoTime();

        String foundOwner = null;
        Cat foundKey = null;

        // Якщо nickname в ключі не вказано -> виконуємо пошук по age
        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getAge() != null && KEY_TO_SEARCH_AND_DELETE.getAge() != null
                        && k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    foundOwner = entry.getValue();
                    foundKey = k;
                    break;
                }
            }
        } else if (KEY_TO_SEARCH_AND_DELETE.getAge() == null) {
            // Пошук лише за nickname (age == null)
            for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getNickname() != null
                        && k.getNickname().equals(KEY_TO_SEARCH_AND_DELETE.getNickname())) {
                    foundOwner = entry.getValue();
                    foundKey = k;
                    break;
                }
            }
        } else {
            // Якщо задані і nickname і age — використовуємо containsKey (повне співпадіння)
            boolean found = hashtable.containsKey(KEY_TO_SEARCH_AND_DELETE);
            if (found) {
                foundOwner = hashtable.get(KEY_TO_SEARCH_AND_DELETE);
                foundKey = KEY_TO_SEARCH_AND_DELETE;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в Hashtable");

        if (foundOwner != null) {
            System.out.println("Елемент з ключем '" + (foundKey != null ? foundKey : KEY_TO_SEARCH_AND_DELETE) + "' знайдено. Власник: " + foundOwner);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в Hashtable.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashtable() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        List<Map.Entry<Cat, String>> entries = new ArrayList<>(hashtable.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Cat, String> searchEntry = new Map.Entry<Cat, String>() {
            public Cat getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в Hashtable");

        if (position >= 0) {
            Map.Entry<Cat, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cat: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    /**
     * Додає новий запис до Hashtable.
     */
    void addEntryToHashtable() {
        long timeStart = System.nanoTime();

        // Дозволяємо додавання як по age, так і по nickname (якщо age == null)
        if (KEY_TO_ADD.getNickname() != null && KEY_TO_ADD.getAge() == null) {
            // Перевіряємо чи вже існує ключ з таким nickname
            boolean exists = false;
            for (Cat k : hashtable.keySet()) {
                if (k != null && k.getNickname() != null && k.getNickname().equals(KEY_TO_ADD.getNickname())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("Ключ з nickname='" + KEY_TO_ADD.getNickname() + "' вже існує в Hashtable. Додавання пропущено.");
            } else {
                hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);
                System.out.println("Додано новий запис: Cat='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
            }
        } else {
            // Стандартне додавання (за age або за повним ключем)
            hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);
            System.out.println("Додано новий запис: Cat='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
        }

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до Hashtable");
    }

    /**
     * Видаляє запис з Hashtable за ключем.
     */
    void removeByKeyFromHashtable() {
        long timeStart = System.nanoTime();
        List<Cat> toRemove = new ArrayList<>();

        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getAge() != null && KEY_TO_SEARCH_AND_DELETE.getAge() != null
                        && k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    toRemove.add(k);
                }
            }
        } else {
            String removedValue = hashtable.remove(KEY_TO_SEARCH_AND_DELETE);
            if (removedValue != null) {
                System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
            }
        }

        for (Cat k : toRemove) {
            String removed = hashtable.remove(k);
            System.out.println("Видалено запис з ключем '" + k + "'. Власник був: " + removed);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з Hashtable");
    }

    /**
     * Видаляє записи з Hashtable за значенням.
     */
    void removeByValueFromHashtable() {
        long timeStart = System.nanoTime();

        List<Cat> keysToRemove = new ArrayList<>();
        for (Map.Entry<Cat, String> entry : hashtable.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (Cat key : keysToRemove) {
            hashtable.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з Hashtable");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    /**
     * Виводить вміст LinkedHashMap.
     * LinkedHashMap зберігає порядок додавання елементів.
     */
    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");

        long timeStart = System.nanoTime();
        for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в LinkedHashMap");
    }

    /**
     * Сортує linkedHashMap за ключами за природним порядком Cat (Cat.compareTo()).
     * Результат — нова LinkedHashMap з відсортованими парами.
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Збираємо ключі і сортуємо їх за natural order (Cat.compareTo)
        List<Cat> sortedKeys = new ArrayList<>(linkedHashMap.keySet());
        Collections.sort(sortedKeys);

        // Відтворюємо LinkedHashMap у відсортованому порядку
        LinkedHashMap<Cat, String> sorted = new LinkedHashMap<>();
        for (Cat k : sortedKeys) {
            sorted.put(k, linkedHashMap.get(k));
        }
        linkedHashMap = sorted;

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в LinkedHashMap.
     * Використовує послідовний перебір елементів.
     */
    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();
        String foundOwner = null;
        Cat foundKey = null;

        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getAge() != null && KEY_TO_SEARCH_AND_DELETE.getAge() != null
                        && k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    foundOwner = entry.getValue();
                    foundKey = k;
                    break;
                }
            }
        } else if (KEY_TO_SEARCH_AND_DELETE.getAge() == null) {
            // Пошук лише за nickname (age == null)
            for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getNickname() != null
                        && k.getNickname().equals(KEY_TO_SEARCH_AND_DELETE.getNickname())) {
                    foundOwner = entry.getValue();
                    foundKey = k;
                    break;
                }
            }
        } else {
            boolean found = linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);
            if (found) {
                foundOwner = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
                foundKey = KEY_TO_SEARCH_AND_DELETE;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (foundOwner != null) {
            System.out.println("Елемент з ключем '" + (foundKey != null ? foundKey : KEY_TO_SEARCH_AND_DELETE) + "' знайдено. Власник: " + foundOwner);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в TreeMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в LinkedHashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        List<Map.Entry<Cat, String>> entries = new ArrayList<>(linkedHashMap.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Cat, String> searchEntry = new Map.Entry<Cat, String>() {
            public Cat getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

        if (position >= 0) {
            Map.Entry<Cat, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cat: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Додає новий запис до LinkedHashMap.
     */
    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        if (KEY_TO_ADD.getNickname() != null && KEY_TO_ADD.getAge() == null) {
            boolean exists = false;
            for (Cat k : linkedHashMap.keySet()) {
                if (k != null && k.getNickname() != null && k.getNickname().equals(KEY_TO_ADD.getNickname())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("Ключ з nickname='" + KEY_TO_ADD.getNickname() + "' вже існує в LinkedHashMap. Додавання пропущено.");
            } else {
                linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
                System.out.println("Додано новий запис: Cat='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "' в LinkedHashMap");
            }
        } else {
            linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
            System.out.println("Додано новий запис: Cat='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "' в LinkedHashMap");
        }

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");
    }

    /**
     * Видаляє запис з LinkedHashMap за ключем.
     */
    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();
        List<Cat> toRemove = new ArrayList<>();

        if (KEY_TO_SEARCH_AND_DELETE.getNickname() == null) {
            for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
                Cat k = entry.getKey();
                if (k != null && k.getAge() != null && KEY_TO_SEARCH_AND_DELETE.getAge() != null
                        && k.getAge().equals(KEY_TO_SEARCH_AND_DELETE.getAge())) {
                    toRemove.add(k);
                }
            }
        } else {
            String removedValue = linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);
            if (removedValue != null) {
                System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
            }
        }

        for (Cat k : toRemove) {
            String removed = linkedHashMap.remove(k);
            System.out.println("Видалено запис з ключем '" + k + "'. Власник був: " + removed);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");
    }

    /**
     * Видаляє записи з LinkedHashMap за значенням.
     */
    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Cat> keysToRemove = new ArrayList<>();
        for (Map.Entry<Cat, String> entry : linkedHashMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Cat key : keysToRemove) {
            linkedHashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "' в LinkedHashMap");
    }

    public static void main(String[] args) {
        HashMap<Cat, String> hashtable = new HashMap<>();
        hashtable.put(new Cat(4, "Тум"), "Ярослав");
        hashtable.put(new Cat(12, "Луна"), "Олена");
        hashtable.put(new Cat(2, "Барсик"), "Поліна");
        hashtable.put(new Cat(5, "Боні"), "Тимофій");
        hashtable.put(new Cat(3, "Тайсон"), "Стефанія");
        hashtable.put(new Cat(9, "Ґуфі"), "Андрій");
        hashtable.put(new Cat(7, "Муся"), "Ярослав");
        hashtable.put(new Cat(8, "Чіпо"), "Поліна");
        hashtable.put(new Cat(10, "Сніжок"), "Стефанія");
        hashtable.put(new Cat(14, "Марс"), "Тимофій");

        LinkedHashMap<Cat, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(new Cat(4, "Тум"), "Ярослав");
        linkedHashMap.put(new Cat(12, "Луна"), "Олена");
        linkedHashMap.put(new Cat(2, "Барсик"), "Поліна");
        linkedHashMap.put(new Cat(5, "Боні"), "Тимофій");
        linkedHashMap.put(new Cat(3, "Тайсон"), "Стефанія");
        linkedHashMap.put(new Cat(9, "Ґуфі"), "Андрій");
        linkedHashMap.put(new Cat(7, "Муся"), "Ярослав");
        linkedHashMap.put(new Cat(8, "Чіпо"), "Поліна");
        linkedHashMap.put(new Cat(10, "Сніжок"), "Стефанія");
        linkedHashMap.put(new Cat(14, "Марс"), "Тимофій");

        BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(hashtable, linkedHashMap);
        operations.executeDataOperations();
    }
}
