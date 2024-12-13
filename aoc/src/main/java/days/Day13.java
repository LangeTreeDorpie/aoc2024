package days;

import java.util.ArrayList;
import java.util.List;

import static Utils.Util.hasDecimals;


public class Day13 implements AdventOfCodeInterface {

    private static List<String> initialInput = new ArrayList<>();

    @Override
    public void readInput(List<String> input) {
        initialInput = input;
    }

    @Override
    public long part1() {
        long sum = 0;

        List<GameConfig> gameConfigs = loadGames(initialInput);

        for (GameConfig config : gameConfigs) {
            sum += playGame(config, 100, 0);
        }

        return sum;
    }

    @Override
    public long part2() {
        long sum = 0;

        List<GameConfig> gameConfigs = loadGames(initialInput);

        for (GameConfig config : gameConfigs) {
            sum += playGame(config, Long.MAX_VALUE, 10000000000000L);
        }

        return sum;
    }

    private long playGame(GameConfig config, long maxPresses, long extraSize) {

        long test = config.buttonA.yOffset;
        long test2 = config.buttonB.yOffset;

        long ax = config.buttonA.xOffset;
        long by = config.buttonB.yOffset;
        long ay = config.buttonB.xOffset * -1;
        long px = config.prize.x + extraSize;
        long py = config.prize.y + extraSize;

        config.buttonA.xOffset *= by;
        px *= by;

        config.buttonA.yOffset *= ay;
        config.buttonB.yOffset *= ay;
        py *= ay;

        long xLeft = config.buttonA.xOffset + config.buttonA.yOffset;
        long a = (px + py) / xLeft;
        double aDouble = (double) (px + py) / xLeft;

        if (hasDecimals(aDouble)) {
            return 0;
        }

        long pxb = (config.prize.x + extraSize) - (a * ax);
        long pyb = (config.prize.y + extraSize) - (a * test);

        long b1 = (pxb / config.buttonB.xOffset);
        double b1Double = (double) pxb / config.buttonB.xOffset;
        long b2 = (pyb / test2);

        if (hasDecimals(b1Double)) {
            return 0;
        }

        if (a < 0 || a > maxPresses) {
            return 0;
        }

        if (b1 != b2) {
            return 0;
        }

        if (b1 < 0 || b1 > maxPresses) {
            return 0;
        }

        return (a * 3) + b1;
    }

    private List<GameConfig> loadGames(List<String> input) {
        String[] sections = String.join("\n", input).split("\n\n"); // Split into 2 parts

        List<GameConfig> gameConfigs = new ArrayList<>();

        for (String section : sections) {
            String[] lines = section.split("\n");
            Button buttonA = parseButton(lines[0]);
            Button buttonB = parseButton(lines[1]);
            Prize prize = parsePrize(lines[2]);
            gameConfigs.add(new GameConfig(buttonA, buttonB, prize));
        }

        return gameConfigs;
    }

    private Button parseButton(String line) {
        String[] parts = line.split(": ")[1].split(", ");
        long xOffset = Integer.parseInt(parts[0].substring(2));
        long yOffset = Integer.parseInt(parts[1].substring(2));
        return new Button(xOffset, yOffset);
    }

    private Prize parsePrize(String line) {
        String[] parts = line.split(": ")[1].split(", ");
        long x = Integer.parseInt(parts[0].substring(2));
        long y = Integer.parseInt(parts[1].substring(2));
        return new Prize(x, y);
    }

    public class Button {
        long xOffset;
        long yOffset;

        public Button(long xOffset, long yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }

    class Prize {
        long x;
        long y;

        public Prize(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    class GameConfig {
        Button buttonA;
        Button buttonB;
        Prize prize;

        public GameConfig(Button buttonA, Button buttonB, Prize prize) {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
            this.prize = prize;
        }
    }
}
