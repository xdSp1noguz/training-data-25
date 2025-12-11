import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime; // Додано
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Клас DataFileHandler управляє роботою з файлами даних.
 * Адаптований для роботи з LocalDateTime, зберігаючи формат файлу yyyy:MM:dd.
 */
public class DataFileHandler {

    // Форматер для формату yyyy:MM:dd
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd");

    /**
     * Завантажує масив об'єктів LocalDateTime з файлу.
     * Читає дату (yyyy:MM:dd) і додає до неї час 00:00 (atStartOfDay).
     */
    public static LocalDateTime[] loadArrayFromFile(String filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return fileReader.lines()
                    .map(currentLine -> currentLine.trim().replaceAll("^\\uFEFF", ""))
                    .filter(currentLine -> !currentLine.isEmpty())
                    // ЗМІНА: Парсимо як LocalDate, а потім перетворюємо в LocalDateTime
                    .map(currentLine -> LocalDate.parse(currentLine, DATE_FORMATTER).atStartOfDay())
                    .toArray(LocalDateTime[]::new); // Повертаємо масив LocalDateTime
        } catch (IOException ioException) {
            throw new RuntimeException("Помилка читання даних з файлу: " + filePath, ioException);
        }
    }

    /**
     * Зберігає масив об'єктів LocalDateTime у файл.
     * При запису час відкидається, зберігається тільки дата у форматі yyyy:MM:dd.
     */
    public static void writeArrayToFile(LocalDateTime[] dateArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            String content = Arrays.stream(dateArray)
                    // ЗМІНА: Форматуємо LocalDateTime назад у рядок "yyyy:MM:dd"
                    .map(dateTime -> dateTime.format(DATE_FORMATTER)) 
                    .collect(Collectors.joining(System.lineSeparator()));
            
            fileWriter.write(content);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}