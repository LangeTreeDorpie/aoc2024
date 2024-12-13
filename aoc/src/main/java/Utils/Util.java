package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Util {

    public static List<String> readFile(String filePath) throws IOException {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filePath);
        }
    }

    public static String stringsToString(List<String> list) {
        return String.join("\n", list);
    }


    public static boolean hasDecimals(Double d) {
        return d % 1 != 0;
    }

}
