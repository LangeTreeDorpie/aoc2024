package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Day11 implements AdventOfCodeInterface {

    static List<Long> stones = new ArrayList<>();

    @Override
    public void readInput(List<String> input) {
        String[] s = input.get(0).split(" ");

        Arrays.stream(s).forEach(stoneString -> {
            long stoneValue = Long.parseLong(stoneString);
            stones.add(stoneValue);
        });
    }

    @Override
    public long part1() {
        return generalizedSolution(stones, 25);
    }

    @Override
    public long part2() {
        return generalizedSolution(stones, 75);
    }

    static List<Long> stoneRules(Long stone) {
        String stoneStr = "" + stone;
        if (stone == 0L) {
            return List.of(1L);
        } else if (stoneStr.length() % 2 == 0) {
            int splitLoc = stoneStr.length() / 2;
            return List.of(Long.parseLong(stoneStr.substring(0, splitLoc)), Long.parseLong(stoneStr.substring(splitLoc)));
        } else {
            return List.of(stone * 2024);
        }
    }

    static long generalizedSolution(List<Long> stones, int blinks) {
        List<Long> nextStones = new ArrayList<>(stones);
        Map<Long, Long> count = new HashMap<>();
        for (Long st : nextStones) {
            count.put(st, count.getOrDefault(st, 0L) + 1);
        }
        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> nextCount = new HashMap<>();
            for (Map.Entry<Long, Long> e : count.entrySet()) {
                nextStones = stoneRules(e.getKey());
                for (Long st : nextStones) {
                    nextCount.put(st, nextCount.getOrDefault(st, 0L) + e.getValue());
                }
            }
            count = nextCount;
        }
        return count.values().stream().mapToLong(l -> l).sum();
    }
}
