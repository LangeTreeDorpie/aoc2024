import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {

    static List<Pair<Integer, Integer>> rulePairs = new ArrayList<>();
    static List<List<Integer>> validBooks = new ArrayList<>();
    static List<List<Integer>> inValidBooks = new ArrayList<>();

    private static int part1() {

        int total = 0;

        for (var book : validBooks) {
            total += getValueOfMiddlePage(book);
        }

        return total;
    }

    private static int part2() {

        int total = 0;

        for (var book : inValidBooks) {
            total += getValueOfMiddlePage(generateValidBook(book));
        }

        return total;
    }

    private static boolean isBookValid(List<Integer> book) {

        for (var rulePair : rulePairs) {

            int i = book.indexOf(rulePair.key());
            int j = book.indexOf(rulePair.value());

            if ((i == -1) || (j == -1)) {
                continue;
            }

            if (i > j) {
                return false;
            }
        }
        return true;
    }

    private static Integer getValueOfMiddlePage(List<Integer> book) {
        int size = book.size();
        return book.get(size/2);
    }

    private static void readRules(List<String> input) {
        final String regex = "(\\d+)\\|(\\d+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(Util.stringsToString(input));

        while (matcher.find()) {

            int first = Integer.parseInt(matcher.group(1));
            int second = Integer.parseInt(matcher.group(2));

            rulePairs.add(new Pair<>(first, second));
        }
    }

    private static void createBooks(List<String> input) {
        List<List<Integer>> books = new ArrayList<>();

        input.forEach(line -> {
            if (!line.contains(",")) {
                return;
            }

            List<Integer> book = new ArrayList<>();
            String[] bookPages = line.split(",");

            Arrays.stream(bookPages).forEach(page -> book.add(Integer.parseInt(page)));

            books.add(book);

            if (isBookValid(book)) {
                validBooks.add(book);
            } else {
                inValidBooks.add(book);
            }

        });
    }

    private static List<Integer> generateValidBook(List<Integer> book) {

        List<Integer> newBook = new ArrayList<>();

        for (var page : book) {

            int i = 0;
            newBook.add(page);

            while (!isBookValid(newBook)) {
                newBook.remove(page);

                newBook.add(i, page);
                i++;
            }
        }

        return newBook;
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\day4.txt");

        readRules(input);
        createBooks(input);

        System.out.printf("D4 P1 = %s%n", part1());
        long endtime = System.currentTimeMillis();
        System.out.println("D4 P1 took " + (endtime - startTime) + " milliseconds");
        System.out.println();

        startTime = System.currentTimeMillis();
        System.out.printf("D4 P2 = %s%n", part2());
        endtime = System.currentTimeMillis();
        System.out.println("D4 P2 took " + (endtime - startTime) + " milliseconds");
    }
}
