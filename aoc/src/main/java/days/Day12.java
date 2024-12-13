package days;

import Utils.BaseConverter;
import Utils.Heading;
import Utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static Utils.HeadingUtils.diagonalMutation;
import static Utils.HeadingUtils.headingMutation;


public class Day12 implements AdventOfCodeInterface {

    static List<Integer> validCorner = List.of(1, 2, 4, 6, 7, 8, 9, 11, 13, 14);
    static List<String> map = new ArrayList<>();
    static Set<Character> uniqueChars = new HashSet<>();
    static Map<Character, Pair<Integer, Integer>> charOwners = new HashMap<>();
    static Set<Pair<Integer, Integer>> visitedArea = new HashSet<>();
    static List<Set<Pair<Integer, Integer>>> areas = new ArrayList<>();

    @Override
    public void readInput(List<String> input) {
        map = input;

        for (String s : map) {
            for (char c : s.toCharArray()) {
                uniqueChars.add(c);
            }
        }

        for (char c : uniqueChars) {
            charOwners.put(c, new Pair<>(0, 0));
        }
    }

    @Override
    public long part1() {
        long total = 0;

        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).length(); j++) {
                Pair<Integer, Integer> currentLocation = new Pair<>(i, j);

                if (visitedArea.contains(currentLocation)) {
                    continue;
                }

                char owner = getCharAtLocation(currentLocation);
                Set<Pair<Integer, Integer>> area = new HashSet<>();

                areas.add(area);

                walkThrough(new Pair<>(i, j), area, owner);

                total += calculateOwnership(owner, area);
            }
        }

        return total;
    }

    @Override
    public long part2() {
        long total = 0;

        for (var area : areas) {

            char owner = getCharAtLocation(area.iterator().next());

            List<String> newMap = toNewMap(area, owner);
            List<String> dottedMap = toDottedMap(newMap, owner);
            increaseDiagonally(dottedMap, owner);

            for (String s : dottedMap) {

                long cornersFound = 0;

                for (int corner : validCorner) {

                    char c = (char) ('0' + corner);
                    long cournerPerCharFound = countCharInString(s, c);

                    if (c == '6' || c == '9') {
                        cornersFound += cournerPerCharFound;
                    }

                    cornersFound += cournerPerCharFound;
                }

                total += ((long) area.size() * cornersFound);
            }
        }

        return total;
    }

    public static long countCharInString(String input, char target) {
        return input.chars()
                .filter(ch -> ch == target)
                .count();
    }

    private static List<String> toNewMap(Set<Pair<Integer, Integer>> area, char owner) {
        List<String> newMap = new ArrayList<>();

        for (int i = 0; i < map.size(); i++) {
            StringBuilder sb = new StringBuilder(map.get(i));

            if (!map.get(i).contains(String.valueOf(owner))) {
                continue;
            }

            for (int j = 0; j < map.get(i).length(); j++) {
                Pair<Integer, Integer> location = new Pair<>(i, j);

                if (area.contains(location)) {
                    sb.setCharAt(j, owner);
                } else {
                    sb.setCharAt(j, '.');
                }
            }

            newMap.add(sb.toString());
        }

        return newMap;
    }

    private static List<String> toDottedMap(List<String> map, char owner) {

        int newMaxHeight = map.size() * 2 + 1;
        int newMaxWidth = map.get(0).length() * 2 + 1;
        List<String> dottedMap = new ArrayList<>();

        for (int i = 0; i < newMaxHeight; i++) {
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < newMaxWidth; j++) {

                if ((i % 2 == 0) || j % 2 == 0) {
                    sb.append("0");
                } else {
                    Pair<Integer, Integer> location = new Pair<>((i / 2), (j / 2));
                    char charAtLocation = getCharAtLocation(location, map);

                    if (charAtLocation == owner) {
                        sb.append(owner);
                    } else {
                        sb.append("0");
                    }

                }
            }
            dottedMap.add(sb.toString());
        }

        return dottedMap;
    }

    private static void increaseDiagonally(List<String> dottedMap, char owner) {

        for (int i = 0; i < dottedMap.size(); i++) {
            for (int j = 0; j < dottedMap.get(i).length(); j++) {
                if (dottedMap.get(i).charAt(j) == owner) {
                    int increase = 1;

                    for (Pair<Integer, Integer> diagonal : diagonalMutation) {
                        Pair<Integer, Integer> location = new Pair<>(i + diagonal.key(), j + diagonal.value());

                        try {
                            increaseMapValue(dottedMap, location.key(), location.value(), increase);

                        } catch (IndexOutOfBoundsException e) {
                        }
                        increase *= 2;
                    }
                }
            }
        }
    }

    private static void increaseMapValue(List<String> map, int i, int j, int increase) {
        StringBuilder sb = new StringBuilder(map.get(i));

        char charAt = map.get(i).charAt(j);
        long fromCustomBase = BaseConverter.fromCustomBase(charAt);

        Character newChar = BaseConverter.toCustomBase(fromCustomBase + increase).charAt(0);

        sb.setCharAt(j, newChar);
        map.set(i, sb.toString());
    }

    private static long calculateOwnership(char owner, Set<Pair<Integer, Integer>> area) {

        long tiles = area.size();
        long fences = 0;

        for (Pair<Integer, Integer> location : area) {

            List<Character> foreignArea = getNeighbours(location);
            foreignArea.removeIf(c -> c == owner);

            fences += foreignArea.size();
        }

        return tiles * fences;
    }

    private static List<Character> getNeighbours(Pair<Integer, Integer> currentLocation) {
        List<Character> neighbours = new ArrayList<>();

        for (Heading direction : Heading.values()) {
            Pair<Integer, Integer> nextLocation = getNextLocation(currentLocation, direction);
            try {
                neighbours.add(getCharAtLocation(nextLocation));

            } catch (IndexOutOfBoundsException e) {
                neighbours.add('.');
            }
        }

        return neighbours;
    }

    private static void walkThrough(Pair<Integer, Integer> currentLocation, Set<Pair<Integer, Integer>> area, char owner) {

        area.add(currentLocation);
        visitedArea.add(currentLocation);

        for (Heading direction : Heading.values()) {
            Pair<Integer, Integer> nextLocation = getNextLocation(currentLocation, direction);
            try {
                char ownerOfNextTile = getCharAtLocation(nextLocation);

                if (ownerOfNextTile != owner) {
                    continue;
                }

                if (area.contains(nextLocation)) {
                    continue;
                }

                walkThrough(nextLocation, area, owner);
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }

    private static Pair<Integer, Integer> getNextLocation(Pair<Integer, Integer> currentLocation, Heading heading) {
        Pair<Integer, Integer> integerIntegerPair = headingMutation.get(heading);
        return new Pair<>(currentLocation.key() + integerIntegerPair.key(), currentLocation.value() + integerIntegerPair.value());
    }

    private static char getCharAtLocation(Pair<Integer, Integer> location) {
        return map.get(location.key()).charAt(location.value());
    }

    private static char getCharAtLocation(Pair<Integer, Integer> location, List<String> customMap) {
        return customMap.get(location.key()).charAt(location.value());
    }
}
