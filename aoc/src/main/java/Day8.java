import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class Day8 implements AdventOfCodeInterface {
    static Map<String, List<Pair<Integer, Integer>>> antennaPositions = new HashMap<>();
    static Set<Pair<Integer, Integer>> antiAntennaPositions = new HashSet<>();

    static Integer MAX_ROW;
    static Integer MAX_COLUMN;

    public long part1() {
        antiAntennaPositions.clear();

        for(Map.Entry<String, List<Pair<Integer, Integer>>> entry : antennaPositions.entrySet()) {
            List<Pair<Integer, Integer>> positions = entry.getValue();
            for(Pair<Integer, Integer> position : positions){

                placeAntiAntenna(positions, position, false);
            }
        }

        return antiAntennaPositions.size();
    }

    public long part2() {
        antiAntennaPositions.clear();

        for(Map.Entry<String, List<Pair<Integer, Integer>>> entry : antennaPositions.entrySet()) {
            List<Pair<Integer, Integer>> positions = entry.getValue();
            for(Pair<Integer, Integer> position : positions){

                placeAntiAntenna(positions, position, true);
            }
        }

        return antiAntennaPositions.size();
    }

    private static void placeAntiAntenna(List<Pair<Integer, Integer>> positions, Pair<Integer, Integer> positionA, boolean loopAntiAntennas){

        for(Pair<Integer, Integer> positionB : positions){
            if (Objects.equals(positionA, positionB)){
                continue;
            }

            Pair<Integer, Integer> delta = calculateDelta(positionA, positionB);
            Pair<Integer, Integer> newAntiAntennaLocation = calculatePosition(positionB, delta);

            if (loopAntiAntennas){
                antiAntennaPositions.add(positionA);

                while (!isOutOfBounds(newAntiAntennaLocation)){
                    antiAntennaPositions.add(newAntiAntennaLocation);
                    newAntiAntennaLocation = calculatePosition(newAntiAntennaLocation, delta);
                }

            } else {
                if (isOutOfBounds(newAntiAntennaLocation)){
                    continue;
                }

                antiAntennaPositions.add(newAntiAntennaLocation);
            }
        }
    }

    private static Pair<Integer, Integer> calculateDelta(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return new Pair<>(b.key() - a.key(), b.value() -  a.value());
    }

    private static Pair<Integer, Integer> calculatePosition(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
        return new Pair<>(b.key() + a.key(), b.value() +  a.value());
    }

    private static boolean isOutOfBounds(Pair<Integer, Integer> position){
        return position.key() < 0 || position.key() >= MAX_COLUMN || position.value() < 0 || position.value() >= MAX_ROW;
    }

    public void readInput(List<String> input){

        MAX_ROW = input.size();
        MAX_COLUMN = input.get(0).length();

        int i = 0;
        for(String line : input){
          for(int j = 0; j < line.length(); j++){
            if(line.charAt(j) != '.'){
                String character = String.valueOf(line.charAt(j));
                if(!antennaPositions.containsKey(character)) {
                    List<Pair<Integer, Integer>> positions = new ArrayList<>();
                    antennaPositions.put(character, positions);
                }
                antennaPositions.get(character).add(new Pair<>(i, j));
            }
          }
          i++;
        }
    }
}

