import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.System.exit;


public class Day9 {

    static StringBuilder unsortedMemory = new StringBuilder();
    static StringBuilder sortedMemory = new StringBuilder();
    static Set<Pair<Integer, Integer>> files = new TreeSet<>();

    private static long part1() {
        long total = 0;

        String string = unsortedMemory.toString();
        int startPointer = 0;
        int endPointer = string.length() - 1;

        while (startPointer < endPointer+1) {

            if (string.charAt(startPointer) == '.') {
                if (string.charAt(endPointer) == '.') {
                    endPointer--;
                    continue;
                }

                sortedMemory.append(string.charAt(endPointer));
                endPointer--;

            } else {
                sortedMemory.append(string.charAt(startPointer));
            }
            startPointer++;
        }

        for (int i = 0; i < sortedMemory.length(); i++) {
            long fromCustomBase = BaseConverter.fromCustomBase(sortedMemory.charAt(i));
            total += (i * fromCustomBase);
        }

        return total;
    }

    private static long part2() {
        long total = 0;

        ArrayList<Pair<Integer, Integer>> reversedFiles = new ArrayList<>(files);
        Collections.reverse(reversedFiles);

        for (var file : reversedFiles) {
            int newIndex = findFirstOccurrenceOfNumberOfDots(unsortedMemory.toString(), ".", file.value());

            if (newIndex == -1) {
                continue;
            }

            String repeat = BaseConverter.toCustomBase(file.key()).repeat(file.value());
            int oldIndex = unsortedMemory.indexOf(repeat, newIndex);

            if (oldIndex < newIndex) {
                continue;
            }

            replaceAtIndex(unsortedMemory, newIndex, repeat);
            replaceAtIndex(unsortedMemory, oldIndex, ".".repeat(file.value()));
        }

        for (int i = 0; i < unsortedMemory.length(); i++) {

            if (unsortedMemory.charAt(i) == '.') {
                continue;
            }

            long fromCustomBase = BaseConverter.fromCustomBase(unsortedMemory.charAt(i));
            total += (i * fromCustomBase);

        }

        return total;
    }

    // very slow, assumption... -edit- was slow indeed :D
    private static int findFirstOccurrenceOfNumberOfDots(String str, String toFind, int n) {
        for (int i = 0; i <= str.length() - n; i++) {
            if (str.substring(i, i + n).equals(toFind.repeat(n))) {
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

    public static void replaceAtIndex(StringBuilder sb, int index, String replacement) {
        if (index < 0 || index + replacement.length() > sb.toString().length()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        sb.replace(index, index + replacement.length(), replacement);
    }

    private static void readInput(List<String> input) {

        String line = input.get(0);

        if (line.length() % 2 == 0) {
            System.out.println("invalid input, length of input is even");
            exit(1);
        }

        for (int i = 0; i < line.length(); i++) {
            long parsedInt = Long.parseLong(String.valueOf(line.charAt(i)));

            if (i % 2 == 0){

                files.add(new Pair(i/2, (int) parsedInt));
            }

            for (int j = 0; j < parsedInt; j++) {
                if (i % 2 == 0) {
                    String customBase = BaseConverter.toCustomBase(i / 2);
                    unsortedMemory.append(customBase);
                } else {
                    unsortedMemory.append(".");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\day9.txt");
        readInput(input);

        System.out.printf("P1 = %s%n", part1());
        long endtime = System.currentTimeMillis();
        System.out.println("P1 took " + (endtime - startTime) + " milliseconds");
        System.out.println();

        startTime = System.currentTimeMillis();
        System.out.printf("P2 = %s%n", part2());
        endtime = System.currentTimeMillis();
        System.out.println("P2 took " + (endtime - startTime) + " milliseconds");
    }
}
