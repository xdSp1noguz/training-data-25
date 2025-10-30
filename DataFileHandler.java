import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Клас DataFileHandler управляє роботою з файлами даних LocalDate.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів LocalDate з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів LocalDate.
     */
    public static LocalDate[] loadArrayFromFile(String filePath) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
        LocalDate[] temporaryArray = new LocalDate[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    LocalDate parsedDate = LocalDate.parse(currentLine, dateFormatter);
                    temporaryArray[currentIndex++] = parsedDate;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        LocalDate[] resultArray = new LocalDate[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів LocalDate у файл.
     * 
     * @param dateArray Масив об'єктів LocalDate.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(LocalDate[] dateArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (LocalDate dateElement : dateArray) {
                fileWriter.write(dateElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
