import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Day7 {

    static List<Pair<Long, List<Long>>> equations = new ArrayList<>();
    static boolean concat = false;
    static int counter = 0;

    private static long part1() {
        concat = false;

        long answer = 0;

        for (Pair<Long, List<Long>> equation : equations) {
            counter = 0;

            Long total = equation.key();
            List<Long> integers = equation.value();

            Long sum = total;

            recursiveCalculation(integers, sum, 0);

            if (counter > 0) {
                answer += total;
            }
        }

        return answer;
    }

    private static long part2() {
        concat = true;

        long answer = 0;

        for (Pair<Long, List<Long>> equation : equations) {
            counter = 0;

            Long total = equation.key();
            List<Long> integers = equation.value();

            Long sum = total;

            recursiveCalculation(integers, sum, 0);

            if (counter > 0) {
                answer += total;
            }
        }

        return answer;
    }

    private static void recursiveCalculation(List<Long> values, long sum, int depth) {
        Long value = values.get(depth);

        double outcomeDecimal = (double) sum / value;
        long outcome = (long) outcomeDecimal;
        boolean hasDecimal = hasDecimals(outcomeDecimal);
        depth++;

        if (depth == values.size()) {

            if (!hasDecimal && (outcome == 0 || outcome == 1)) {
                counter++;
            }

        } else {

            if (!hasDecimal) {
                recursiveCalculation(values, (long) outcomeDecimal, depth);
            }
            recursiveCalculation(values, (sum - value), depth);

            if (concat) {

                if (value > sum) {
                    return;
                }

                String valueString = String.valueOf(value);
                String sumString = String.valueOf(sum);

                if (sumString.endsWith(valueString)) {
                    sumString = sumString.substring(0, sumString.length() - valueString.length());

                    if (sumString.isEmpty()) {
                        if (depth == values.size()) {
                            counter++;
                        }

                        return;
                    }
                } else {
                    return;
                }

                recursiveCalculation(values, Long.parseLong(sumString), depth);
            }
        }
    }

    private static void readInput(List<String> input) {
        for (String line : input) {
            String[] split1 = line.split(":");
            Long total = Long.parseLong(split1[0]);
            String[] split2 = split1[1].split(" ");
            List<Long> integers = new ArrayList<>();

            for (String s : split2) {
                if (s.isEmpty()) {
                    continue;
                }

                integers.add(Long.parseLong(s));
            }

            Collections.reverse(integers);

            Pair<Long, List<Long>> pair = new Pair<>(total, integers);
            equations.add(pair);
        }
    }

    private static boolean hasDecimals(Double d) {
        return d % 1 != 0;
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\day7.txt");

        readInput(input);

        System.out.printf("D6 P1 = %s%n", part1());
        long endtime = System.currentTimeMillis();
        System.out.println("D6 P1 took " + (endtime - startTime) + " milliseconds");
        System.out.println();

        startTime = System.currentTimeMillis();
        System.out.printf("D6 P2 = %s%n", part2());
        endtime = System.currentTimeMillis();
        System.out.println("D6 P2 took " + (endtime - startTime) + " milliseconds");

    }
}
