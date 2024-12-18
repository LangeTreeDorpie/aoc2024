package days;

import Utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Utils.Util.hasDecimals;


public class Day7 implements AdventOfCodeInterface {

    static List<Pair<Long, List<Long>>> equations = new ArrayList<>();
    static boolean concat = false;
    static int counter = 0;

    @Override
    public void readInput(List<String> input) {
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

    @Override
    public long part1() {
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

    @Override
    public long part2() {
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
}
