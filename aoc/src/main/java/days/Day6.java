package days;

import Utils.Heading;
import Utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Utils.HeadingUtils.getNextHeadingClockWise;
import static Utils.HeadingUtils.headingMutation;

public class Day6 implements AdventOfCodeInterface {

    static final int LOOP_LIMIT = 8000;
    static Pair<Integer, Integer> currentLocation;
    static Pair<Integer, Integer> initialLocation;

    static Heading heading;
    static Heading initialHeading;

    static List<String> map;
    static List<String> initialMap;

    @Override
    public void readInput(List<String> input) {

        Map<String, Heading> headingIcon = Map.of(
                ">", Heading.EAST,
                "<", Heading.WEST,
                "^", Heading.NORTH,
                "v", Heading.SOUTH
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

    @Override
    public long part1() {

        patrol(true);
        return countVisitedLocations();
    }

    @Override
    public long part2() {

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
                if (markLocationAsVisited) {
                    markLocationAsVisited(currentLocation);
                }

                while (getIconOnLocation(nextLocation).equals("#")) {
                    heading = getNextHeadingClockWise(heading);
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

    private static String getIconOnLocation(Pair<Integer, Integer> location) {
        return String.valueOf(map.get(location.key()).charAt(location.value()));
    }

    private static Pair<Integer, Integer> getNextLocation(Pair<Integer, Integer> currentLocation, Heading heading) {
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
}
