import common.Coordinates;

import java.util.*;
import java.util.List;

/**
 * Part one of
 * https://adventofcode.com/2019/day/3
 *
 * @author Nerijus
 */
public class Day03_1 {
    public static void main(String[] args) {
        System.out.println("Manhattan distance from the central port to the closest intersection: "
                + new Day03_1().getResult());
    }

    private int getResult() {
        List<String> wires = Inputs.readStrings("Day03");
        Set<Coordinates> intersections = getIntersections(wires);
        System.out.println("Intersections: " + intersections.size());

        return intersections.stream()
                .mapToInt(point -> Math.abs(point.x) + Math.abs(point.y))
                .min()
                .orElse(-1);
    }

    Set<Coordinates> getIntersections(List<String> wires) {
        Set<Coordinates> wireOne = getFullPath(wires.get(0).split(","));
        Set<Coordinates> wireTwo = getFullPath(wires.get(1).split(","));

        // leave only intersections
        wireOne.removeIf(point -> !wireTwo.contains(point));
        return wireOne;
    }

    private Set<Coordinates> getFullPath(String[] wireOne) {
        Set<Coordinates> fullPath = new HashSet<>();
        Coordinates endPoint = new Coordinates(0, 0);
        for (String path : wireOne) {
            List<Coordinates> points = trace(endPoint, path);
            fullPath.addAll(points);
            endPoint = points.get(points.size() - 1);
        }
        return fullPath;
    }

    List<Coordinates> trace(Coordinates from, String path) {
        String direction = path.substring(0, 1);
        int distance = Integer.parseInt(path.replace(direction, ""));
        List<Coordinates> points = new ArrayList<>();
        switch (direction) {
            case "U":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Coordinates(from.x, from.y + i));
                }
                break;
            case "D":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Coordinates(from.x, from.y - i));
                }
                break;
            case "L":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Coordinates(from.x - i, from.y));
                }
                break;
            case "R":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Coordinates(from.x + i, from.y));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return points;
    }
}
