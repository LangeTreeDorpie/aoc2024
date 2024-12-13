import Utils.Util;
import days.*;

import java.io.IOException;
import java.util.List;

public class AoC2024 {

    static List<Class<?>> days = List.of(
            Day1.class,
            Day2.class,
            Day3.class,
            Day4.class,
            Day5.class,
            Day6.class,
            Day7.class,
            Day8.class,
            Day9.class, // takes long
            Day10.class,
            Day11.class,
            Day12.class,
            Day13.class
    );

    public static void main(String[] args) throws IOException {
        runEveryDay();

        runOneDay(new Day13());
    }

    private static void runOneDay(AdventOfCodeInterface day) throws IOException {

        List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\" + day.getClass().getSimpleName() + ".txt");
        long startTime = System.currentTimeMillis();
        day.readInput(input);
        System.out.println(day.getClass().getSimpleName() + ":");
        logger(day.part1(), startTime, "1");
        logger(day.part2(), startTime, "2");
        System.out.println();
    }

    private static void runEveryDay() {
        days.forEach(day -> {
            try {
                runOneDay((AdventOfCodeInterface) day.getDeclaredConstructor().newInstance());

            } catch (Exception e) {
                System.out.println(day.getSimpleName() + ": " + e.getClass().getSimpleName()+ " " + e.getMessage());
            }

        });
    }

    private static void logger(long answer, long startTime, String part) {
        System.out.println("P" + part + ": " + answer);
        System.out.println("P" + part + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
}
