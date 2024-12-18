package days;

import Utils.Heading;
import Utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static Utils.HeadingUtils.headingMutation;


public class Day10 implements AdventOfCodeInterface {

    private static final int MAX_HEIGHT = 9;
    static List<List<Pair<Integer, Integer>>> hikes = new ArrayList<>();
    static List<String> map = new ArrayList<>();

    @Override
    public void readInput(List<String> input) {
        map = input;

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);

            for (int j = 0; j < line.length(); j++) {
                char currentChar = line.charAt(j);

                if (currentChar == '0') {
                    Pair<Integer, Integer> hikeStartPoint = new Pair<>(i, j);
                    ArrayList<Pair<Integer, Integer>> newHike = new ArrayList<>();
                    newHike.add(hikeStartPoint);
                    hikes.add(newHike);
                }
            }
        }
    }

    @Override
    public long part1() {
        long total = 0;

        for (List<Pair<Integer, Integer>> hike : hikes) {
            for (int i = 0; i < hike.size(); i++) {
                Pair<Integer, Integer> currentLocation = hike.get(i);

                var completeHikes = findCompleteHikes(currentLocation);

                if (completeHikes.containsKey(MAX_HEIGHT)) {
                    total += completeHikes.get(MAX_HEIGHT).size();
                }
            }
        }

        return total;
    }

    @Override
    public long part2() {
        long total = 0;

        for (List<Pair<Integer, Integer>> hike : hikes) {
            for (int i = 0; i < hike.size(); i++) {
                Pair<Integer, Integer> currentLocation = hike.get(i);

                var completeHikes = findAllRoutesForCompleteHikes(currentLocation);

                if (completeHikes.containsKey(MAX_HEIGHT)) {
                    total += completeHikes.get(MAX_HEIGHT).size();
                }
            }
        }

        return total;
    }

    private static Map<Integer, Set<Pair<Integer, Integer>>> findCompleteHikes(Pair<Integer, Integer> currentLocation) {

        Map<Integer, Set<Pair<Integer, Integer>>> hikeLevelMap = new HashMap<>();
        List<Pair<Integer, Integer>> nextHikePaths = findNextHikePaths(currentLocation, 0);
        hikeLevelMap.put(1, new HashSet<>(nextHikePaths));

        for (int i = 1; i < MAX_HEIGHT; i++) {
            Integer nextHeightIndex = i + 1;
            hikeLevelMap.put(nextHeightIndex, new HashSet<>());

            for (var nextHikePath : hikeLevelMap.get(i)) {
                hikeLevelMap.get(nextHeightIndex).addAll(findNextHikePaths(nextHikePath, i));
            }
        }

        return hikeLevelMap;
    }

    private static Map<Integer, List<Pair<Integer, Integer>>> findAllRoutesForCompleteHikes(Pair<Integer, Integer> currentLocation) {

        Map<Integer, List<Pair<Integer, Integer>>> hikeLevelMap = new HashMap<>();
        List<Pair<Integer, Integer>> nextHikePaths = findNextHikePaths(currentLocation, 0);
        hikeLevelMap.put(1, new ArrayList<>(nextHikePaths));

        for (int i = 1; i < MAX_HEIGHT; i++) {
            Integer nextHeightIndex = i + 1;
            hikeLevelMap.put(nextHeightIndex, new ArrayList<>());

            for (var nextHikePath : hikeLevelMap.get(i)) {
                hikeLevelMap.get(nextHeightIndex).addAll(findNextHikePaths(nextHikePath, i));
            }
        }

        return hikeLevelMap;
    }

    private static List<Pair<Integer, Integer>> findNextHikePaths(Pair<Integer, Integer> currentLocation, int currentLocationHeight) {
        List<Pair<Integer, Integer>> nextLocations = new ArrayList<>();

        for (Heading direction : Heading.values()) {
            Pair<Integer, Integer> nextLocation = getNextLocation(currentLocation, direction);
            try {
                Integer nextLocationHeight = getHeightLocation(nextLocation);
                if (stepHasValidIncline(currentLocationHeight, nextLocationHeight)) {
                    nextLocations.add(nextLocation);
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

        return nextLocations;
    }

    private static Pair<Integer, Integer> getNextLocation(Pair<Integer, Integer> currentLocation, Heading heading) {
        Pair<Integer, Integer> integerIntegerPair = headingMutation.get(heading);
        return new Pair<>(currentLocation.key() + integerIntegerPair.key(), currentLocation.value() + integerIntegerPair.value());
    }

    private static boolean stepHasValidIncline(Integer start, Integer end) {
        return (start + 1) == end;
    }

    private static Integer getHeightLocation(Pair<Integer, Integer> location) {
        return Integer.parseInt(String.valueOf(getCharAtLocation(location)));
    }

    private static char getCharAtLocation(Pair<Integer, Integer> location) {
        return map.get(location.key()).charAt(location.value());
    }
}
