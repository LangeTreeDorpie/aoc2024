package days;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Utils.Pair;
import org.jcodec.api.NotImplementedException;


public class Day14 implements AdventOfCodeInterface {

    private static final Integer MAP_WIDTH = 101;
    private static final Integer MAP_HEIGHT = 103;

    List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots = new ArrayList<>();

    @Override
    public void readInput(List<String> input) {
        final String regex = "p=(\\d+),(\\d+)\\sv=(-?\\d+),(-?\\d+)";

        String string = String.join("\n", input);

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            Pair<Integer, Integer> robotPosition = new Pair<>(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Pair<Integer, Integer> robotVelocity = new Pair<>(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));

            var robot = new Pair<>(robotPosition, robotVelocity);
            robots.add(robot);
        }
    }

    @Override
    public long part1() {
        long sum = 1;

        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robotsAfterSeconds = new ArrayList<>();

        for (var robot : robots) {
            robotsAfterSeconds.add(robotAfterXSeconds(robot, 100));
        }

        List<Integer> robotPerQuadrant = addRobotsToQuadrant(robotsAfterSeconds);

        for (Integer value : robotPerQuadrant) {
            sum *= value;
        }

//        Map<Pair<Integer, Integer>, Integer> robotsMap = robotsToLocation(robotsAfterSeconds);
//        List<List<Integer>> robotsToMap = robotsToMap(robotsMap);
//
//        for (var robot : robotsToMap) {
//            System.out.println(robot);
//        }

        return sum;
    }

    @Override
    public long part2() {
        long lowestRunValue = Long.MAX_VALUE;
        int lowestRun = 0;

        for (int i = 0; i < 10000; i++) {
            long runValue = 1L;
            List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robotsAfterSeconds = new ArrayList<>();

            for (var robot : robots) {
                robotsAfterSeconds.add(robotAfterXSeconds(robot, i));
            }

            List<Integer> robotPerQuadrant = addRobotsToQuadrant(robotsAfterSeconds);

            for (Integer value : robotPerQuadrant) {
                runValue *= value;
            }

            if (runValue < lowestRunValue) {
                lowestRunValue = runValue;
                lowestRun = i;
            }

//            Map<Pair<Integer, Integer>, Integer> robotsMap = robotsToLocation(robotsAfterSeconds);
//            List<List<Integer>> robotsToMap = robotsToMap(robotsMap);
//            mapToPNG(robotsToMap, i);
        }

        return lowestRun;
    }

    private Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robotAfterXSeconds(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robot, int seconds) {
        Pair<Integer, Integer> robotPosition = robot.key();
        Pair<Integer, Integer> robotVelocity = robot.value();

        int xPos = robotPosition.key();
        int yPos = robotPosition.value();

        int xVel = robotVelocity.key();
        int yVel = robotVelocity.value();

        int xEndPos = xPos + (xVel * seconds);
        int yEndPos = yPos + (yVel * seconds);

        xEndPos = teleportOutOfBoundsRobot(xEndPos, MAP_WIDTH);
        yEndPos = teleportOutOfBoundsRobot(yEndPos, MAP_HEIGHT);

        return new Pair<>(new Pair<>(xEndPos, yEndPos), new Pair<>(xVel, yVel));
    }

    private int teleportOutOfBoundsRobot(int position, int maxPosition) {

        position %= maxPosition;

        if (position < 0) {
            position += maxPosition;
        }

        return position;
    }

    private static List<List<Integer>> robotsToMap(Map<Pair<Integer, Integer>, Integer> robotLocations) {
        List<List<Integer>> newMap = new ArrayList<>();

        for (int i = 0; i < MAP_HEIGHT; i++) {
            List<Integer> line = new ArrayList<>();

            for (int j = 0; j < MAP_WIDTH; j++) {
                Pair<Integer, Integer> location = new Pair<>(j, i);
                line.add(robotLocations.getOrDefault(location, 0));
            }
            newMap.add(line);

        }
        return newMap;
    }

    private Map<Pair<Integer, Integer>, Integer> robotsToLocation(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots) {
        Map<Pair<Integer, Integer>, Integer> robotsMap = new HashMap<>();

        for (var robot : robots) {
            Pair<Integer, Integer> robotPosition = robot.key();

            if (!robotsMap.containsKey(robotPosition)) {
                robotsMap.put(robotPosition, 0);
            }

            Integer atSameLocation = robotsMap.get(robotPosition) + 1;
            robotsMap.put(robotPosition, atSameLocation);
        }

        return robotsMap;
    }

    private List<Integer> addRobotsToQuadrant(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots) {
        int x = MAP_WIDTH / 2;
        int y = MAP_HEIGHT / 2;

        List<Integer> robotsInQuadrant = new ArrayList<>();
        robotsInQuadrant.add(0);
        robotsInQuadrant.add(0);
        robotsInQuadrant.add(0);
        robotsInQuadrant.add(0);

        for (int i = 0; i < robots.size(); i++) {
            Pair<Integer, Integer> coord = robots.get(i).key();
            if (coord.key() < x && coord.value() < y) {
                robotsInQuadrant.set(0, robotsInQuadrant.get(0) + 1);
            }
            if (coord.key() > x && coord.value() < y) {
                robotsInQuadrant.set(1, robotsInQuadrant.get(1) + 1);
            }
            if (coord.key() < x && coord.value() > y) {
                robotsInQuadrant.set(2, robotsInQuadrant.get(2) + 1);
            }
            if (coord.key() > x && coord.value() > y) {
                robotsInQuadrant.set(3, robotsInQuadrant.get(3) + 1);
            }
        }
//        System.out.println(robotsInQuadrant);

        return robotsInQuadrant;
    }


    private void mapToPNG(List<List<Integer>> robotsToMap, int runNumber) {
        BufferedImage image = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);

        for (int y = 0; y < robotsToMap.size(); y++) {
            for (int x = 0; x < robotsToMap.get(y).size(); x++) {

                if (robotsToMap.get(y).get(x) != 0) {
                    g2d.setColor(Color.RED);  // Land (or a specific feature)
                } else {
                    g2d.setColor(Color.WHITE);  // Water or empty space
                }
                // Draw a rectangle for each map cell
                g2d.fillRect(x, y, 1, 1);
            }
        }
        g2d.dispose();

        try {
            File outputFile = new File(".\\aoc\\src\\main\\resources\\trash\\" + runNumber + ".png");
            ImageIO.write(image, "PNG", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving the image: " + e.getMessage());
        }
    }

}
