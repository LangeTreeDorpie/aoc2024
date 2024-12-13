package Utils;

import java.util.List;
import java.util.Map;

public class HeadingUtils {

    public static final Map<Heading, Pair<Integer, Integer>> headingMutation = Map.of(
            Heading.NORTH, new Pair<>(-1, 0),
            Heading.EAST, new Pair<>(0, 1),
            Heading.SOUTH, new Pair<>(1, 0),
            Heading.WEST, new Pair<>(0, -1)
    );

    public static final List<Pair<Integer, Integer>> diagonalMutation = List.of(
            new Pair<>(-1, -1),
            new Pair<>(-1, 1),
            new Pair<>(1, -1),
            new Pair<>(1, 1)
    );

    public static Heading getNextHeadingClockWise(Heading heading) {
        switch (heading) {
            case NORTH -> {
                return Heading.EAST;
            }
            case EAST -> {
                return Heading.SOUTH;
            }
            case SOUTH -> {
                return Heading.WEST;
            }
            case WEST -> {
                return Heading.NORTH;
            }
            default -> throw new IllegalStateException("Unexpected value: " + heading);
        }
    }
}
