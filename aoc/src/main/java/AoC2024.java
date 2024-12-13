import java.util.List;

public class AoC2024 {

    static List<Class<?>> days = List.of(
            Day5.class,
            Day6.class,
            Day7.class,
            Day8.class,
//            Day9.class, // takes long
            Day10.class,
            Day11.class,
            Day12.class

    );

    public static void main(String[] args) {

        days.forEach(day -> {
            try {
                System.out.println(day.getSimpleName() + ":");
                AdventOfCodeInterface aoc = (AdventOfCodeInterface) day.getDeclaredConstructor().newInstance();
                List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\" + day.getSimpleName() + ".txt");
                long startTime = System.currentTimeMillis();
                aoc.readInput(input);
                logger(aoc.part1(), startTime, "1");
                logger(aoc.part2(), startTime, "2");

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println();
        });
    }

    private static void logger(long answer, long startTime, String part) {
        System.out.println("P" + part + ": " + answer);
//        System.out.println("P" + part + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

}
