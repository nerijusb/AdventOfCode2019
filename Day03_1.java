import java.util.*;

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
        Set<Point> intersections = getIntersections(wires);
        System.out.println("Intersections: " + intersections.size());

        return intersections.stream()
                .mapToInt(point -> Math.abs(point.x) + Math.abs(point.y))
                .min()
                .orElse(-1);
    }

    Set<Point> getIntersections(List<String> wires) {
        Set<Point> wireOne = getFullPath(wires.get(0).split(","));
        Set<Point> wireTwo = getFullPath(wires.get(1).split(","));

        // leave only intersections
        wireOne.removeIf(point -> !wireTwo.contains(point));
        return wireOne;
    }

    private Set<Point> getFullPath(String[] wireOne) {
        Set<Point> fullPath = new HashSet<>();
        Point endPoint = new Point(0, 0);
        for (String path : wireOne) {
            List<Point> points = trace(endPoint, path);
            fullPath.addAll(points);
            endPoint = points.get(points.size() - 1);
        }
        return fullPath;
    }

    List<Point> trace(Point from, String path) {
        String direction = path.substring(0, 1);
        int distance = Integer.parseInt(path.replace(direction, ""));
        List<Point> points = new ArrayList<>();
        switch (direction) {
            case "U":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Point(from.x, from.y + i));
                }
                break;
            case "D":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Point(from.x, from.y - i));
                }
                break;
            case "L":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Point(from.x - i, from.y));
                }
                break;
            case "R":
                for (int i = 1; i <= distance; i++) {
                    points.add(new Point(from.x + i, from.y));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return points;
    }

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point location = (Point) o;
            return x == location.x && y == location.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
