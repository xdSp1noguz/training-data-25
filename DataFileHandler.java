import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate; // Змінено на LocalDate
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Клас DataFileHandler управляє роботою з файлами даних LocalDate.
 */
public class DataFileHandler {

    // Форматер для формату yyyy:MM:dd
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd");

    /**
     * Завантажує масив об'єктів LocalDate з файлу за допомогою Stream API.
     * * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів LocalDate.
     */
    public static LocalDate[] loadArrayFromFile(String filePath) {
        // Створюємо форматер, бо у вас роздільник ":", а стандартний чекає "-"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd"); 

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return fileReader.lines()
                    .map(currentLine -> currentLine.trim().replaceAll("^\\uFEFF", ""))
                    .filter(currentLine -> !currentLine.isEmpty())
                    // ОСЬ ТУТ ЗМІНА:
                    // Ми використовуємо той самий метод .parse(), 
                    // АЛЕ викликаємо його у класа LocalDate, а не LocalDateTime
                    .map(currentLine -> LocalDate.parse(currentLine, formatter))
                    .toArray(LocalDate[]::new);
        } catch (IOException ioException) {
            throw new RuntimeException("Помилка читання даних з файлу: " + filePath, ioException);
        }
    }

    /**
     * Зберігає масив об'єктів LocalDate у файл.
     * * @param dateArray Масив об'єктів LocalDate.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(LocalDate[] dateArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            String content = Arrays.stream(dateArray)
                    // Використовуємо форматер, щоб зберегти формат yyyy:MM:dd
                    .map(date -> date.format(DATE_FORMATTER)) 
                    .collect(Collectors.joining(System.lineSeparator()));
           
            fileWriter.write(content);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}