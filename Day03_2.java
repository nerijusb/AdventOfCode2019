import java.util.*;
import java.util.function.Function;

/**
 * Part two of
 * https://adventofcode.com/2019/day/3
 *
 * @author Nerijus
 */
public class Day03_2 extends Day03_1 {
    public static void main(String[] args) {
        System.out.println("Fewest combined steps the wires must take to reach an intersection: "
                + new Day03_2().getResult());
    }

    private int getResult() {
        List<String> wires = Inputs.readStrings("Day03");
        Set<Point> intersections = getIntersections(wires);
        String[] wireOne = wires.get(0).split(",");
        String[] wireTwo = wires.get(1).split(",");
        return intersections.stream()
                .mapToInt(point -> stepsTo(point, wireOne) + stepsTo(point, wireTwo))
                .min()
                .orElse(-1);
    }

    private int stepsTo(Point intersection, String[] wire) {
        return trace(wire, (currentPoint) -> !currentPoint.equals(intersection)).size();
    }

    private List<Point> trace(String[] path, Function<Point, Boolean> callback) {
        List<Point> points = new ArrayList<>();
        Point lastPoint = new Point(0, 0);
        for (String part : path) {
            String direction = part.substring(0, 1);
            int distance = Integer.parseInt(part.replace(direction, ""));
            switch (direction) {
                case "U":
                    for (int i = 1; i <= distance; i++) {
                        Point p = new Point(lastPoint.x, lastPoint.y + i);
                        points.add(p);
                        if (!callback.apply(p)) {
                            return points;
                        }
                    }
                    lastPoint = points.get(points.size() - 1);
                    break;
                case "D":
                    for (int i = 1; i <= distance; i++) {
                        Point p = new Point(lastPoint.x, lastPoint.y - i);
                        points.add(p);
                        if (!callback.apply(p)) {
                            return points;
                        }
                    }
                    lastPoint = points.get(points.size() - 1);
                    break;
                case "L":
                    for (int i = 1; i <= distance; i++) {
                        Point p = new Point(lastPoint.x - i, lastPoint.y);
                        points.add(p);
                        if (!callback.apply(p)) {
                            return points;
                        }
                    }
                    lastPoint = points.get(points.size() - 1);
                    break;
                case "R":
                    for (int i = 1; i <= distance; i++) {
                        Point p = new Point(lastPoint.x + i, lastPoint.y);
                        points.add(p);
                        if (!callback.apply(p)) {
                            return points;
                        }
                    }
                    lastPoint = points.get(points.size() - 1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + direction);
            }
        }
        return points;
    }
}
