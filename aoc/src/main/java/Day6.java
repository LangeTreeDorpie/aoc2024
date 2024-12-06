import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Day6 {

    static final int LOOP_LIMIT = 8000;
    static Pair<Integer, Integer> currentLocation;
    static Pair<Integer, Integer> initialLocation;

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    static Direction heading;
    static Direction initialHeading;

    static final Map<Direction, Pair<Integer, Integer>> headingMutation = Map.of(
        Direction.NORTH, new Pair<>(-1, 0),
        Direction.EAST, new Pair<>(0, 1),
        Direction.SOUTH, new Pair<>(1, 0),
        Direction.WEST, new Pair<>(0, -1)
    );

    static List<String> map;
    static List<String> initialMap;

    private static long part1() {

        patrol(true);

        return countVisitedLocations();
    }

    private static long part2() {

        long loopsFound = 0;

        for (var i = 0; i < map.size(); i++) {
            for (var j = 0; j < map.get(i).length(); j++) {
                if (map.get(i).charAt(j) == '.') {
                    addNewObstacle(new Pair<>(i, j));

                    if (!patrol(false)) {
                        loopsFound++;
                    }

                    setPlayField();
                }
            }
        }

        return loopsFound;
    }


    private static boolean patrol(boolean markLocationAsVisited) {

        int steps = 0;

        Pair<Integer, Integer> nextLocation = getNextLocation(currentLocation, heading);

        while (steps < LOOP_LIMIT) {
            steps++;

            try {
                if (markLocationAsVisited){
                    markLocationAsVisited(currentLocation);
                }

                while (getIconOnLocation(nextLocation).equals("#")) {
                    changeHeadingDirection();
                    nextLocation = getNextLocation(currentLocation, heading);
                }

                currentLocation = nextLocation;

                nextLocation = getNextLocation(currentLocation, heading);

            } catch (Exception e) {
                return true;
            }
        }

        return steps != LOOP_LIMIT;
    }

    private static void findStartConditions(List<String> input) {

        Map<String, Direction> headingIcon = Map.of(
                ">", Direction.EAST,
                "<", Direction.WEST,
                "^", Direction.NORTH,
                "v", Direction.SOUTH
        );

        initialMap = input;

        for (var i = 0; i < input.size(); i++) {
            for (var j = 0; j < input.get(i).length(); j++) {

                String current = String.valueOf(input.get(i).charAt(j));

                if (headingIcon.containsKey(current)) {
                    initialLocation = new Pair<>(i, j);
                    initialHeading = headingIcon.get(current);
                    setPlayField();
                    return;
                }
            }
        }
    }

    private static String getIconOnLocation(Pair<Integer, Integer> location) {
        return String.valueOf(map.get(location.key()).charAt(location.value()));
    }

    private static Pair<Integer, Integer> getNextLocation(Pair<Integer, Integer> currentLocation, Direction heading) {
        Pair<Integer, Integer> integerIntegerPair = headingMutation.get(heading);
        return new Pair<>(currentLocation.key() + integerIntegerPair.key(), currentLocation.value() + integerIntegerPair.value());
    }

    private static void markLocationAsVisited(Pair<Integer, Integer> location) {
        map.set(location.key(), map.get(location.key()).substring(0, location.value()) + "X" + map.get(location.key()).substring(location.value() + 1));
    }

    private static void addNewObstacle(Pair<Integer, Integer> location) {
        map.set(location.key(), map.get(location.key()).substring(0, location.value()) + "#" + map.get(location.key()).substring(location.value() + 1));
    }

    private static long countVisitedLocations() {
        long total = 0;
        for (var row : map) {
            total += row.chars().filter(ch -> ch == 'X').count();
        }
        return total;
    }

    private static void setPlayField() {
        map = new ArrayList<>(initialMap);
        currentLocation = new Pair<>(initialLocation.key(), initialLocation.value());
        heading = initialHeading;
    }

    private static void changeHeadingDirection() {
        switch (heading) {
            case NORTH -> heading = Direction.EAST;
            case EAST -> heading = Direction.SOUTH;
            case SOUTH -> heading = Direction.WEST;
            case WEST -> heading = Direction.NORTH;
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<String> input = Util.readFile(".\\aoc\\src\\main\\resources\\day6.txt");

        findStartConditions(input);

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
