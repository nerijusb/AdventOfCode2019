import common.Coordinates;
import common.Direction;
import common.IntCodeComputer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * Part one of
 * https://adventofcode.com/2019/day/15
 *
 * @author Nerijus
 */
public class Day15_1 {

    public static void main(String[] args) {
        Result result = new Day15_1().traverseMap();
        print(result.map);
        System.out.printf("Fewest number of movement commands to the oxygen system location: %d\n", result.oxygenSystemDistance);
    }

    Result traverseMap() {
        Result result = new Result();
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day15"));

        Map<Coordinates, String> visited = new HashMap<>();
        LinkedList<Coordinates> currentPath = new LinkedList<>();
        Coordinates currentPosition = new Coordinates(0, 0);
        currentPath.push(currentPosition);
        visited.put(currentPosition, "S");

        walk: while (true) {
            rotate: for (Movement movement : Movement.values()) {
                log("Next: " + movement.name());
                Coordinates nextPosition = currentPosition.adjacent(movement.direction);
                if (visited.containsKey(nextPosition)) {
                    log("Already been there, continue rotation");
                    continue rotate;
                } else {
                    int nextCode = Integer.parseInt(computer.run(() -> movement.code).get());

                    if (Status.isGoal(nextCode)) {
                        if (result.oxygenSystemDistance == 0) {
                            result.oxygenSystemDistance = currentPath.size();
                            log("Found oxygen system position: " + nextPosition.toString());
                            visited.put(nextPosition, "G");
                            currentPath.push(nextPosition);
                        }
                    } else if (Status.isWall(nextCode)) {
                        log("Wall, continue rotation");
                        visited.put(nextPosition, "#");
                        continue rotate;
                    } else if (Status.isMoved(nextCode)) {
                        log("Empty, moved " + movement.name() + " to " + nextPosition.toString());
                        visited.put(nextPosition, ".");
                        currentPath.push(nextPosition);
                        currentPosition = nextPosition;
                        continue walk;
                    } else {
                        throw new IllegalStateException("Unexpected response code: " + nextCode);
                    }
                }
            }
            Coordinates from = currentPath.pop();
            Coordinates to = currentPath.peek();
            if (to == null) {
                log("Whole map visited");
                result.map = toMap(visited);
                return result;
            }
            computer.run(() -> movementBackTo(to, from).code); // go back
            currentPosition = to;
            log("Went back to " + currentPosition.toString());
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    private Movement movementBackTo(Coordinates to, Coordinates from) {
        if (from.y > to.y) {
            return Movement.DOWN;
        } else if (from.y < to.y) {
            return Movement.UP;
        } else if (from.x > to.x) {
            return Movement.LEFT;
        } else {
            return Movement.RIGHT;
        }
    }

    static void print(String[][] map) {
        for (String[] row : map) {
            for (String value : row) {
                System.out.print(Objects.requireNonNullElse(value, " "));
            }
            System.out.println();
        }
    }

    private static String[][] toMap(Map<Coordinates, String> visited) {
        int minX = visited.keySet().stream().mapToInt(v -> v.x).min().orElse(0);
        int minY = visited.keySet().stream().mapToInt(v -> v.y).min().orElse(0);

        // shift coordinates to positive numbers
        visited.forEach((key, value) -> {
            key.x = key.x + Math.abs(minX);
            key.y = key.y + Math.abs(minY);
        });

        int maxX = visited.keySet().stream().mapToInt(v -> v.x).max().orElse(0);
        int maxY = visited.keySet().stream().mapToInt(v -> v.y).max().orElse(0);
        int dimension = Math.max(maxY, maxX);

        String[][] map = new String[dimension + 1][dimension + 1];
        visited.forEach((key, value) -> map[key.y][key.x] = value);

        return map;
    }

    static class Result {
        int oxygenSystemDistance;
        String[][] map;
    }

    enum Movement {
        UP("1", Direction.UP),
        DOWN("2", Direction.DOWN),
        LEFT("3", Direction.LEFT),
        RIGHT("4", Direction.RIGHT);

        String code;
        Direction direction;

        Movement(String code, Direction direction) {
            this.code = code;
            this.direction = direction;
        }
    }

    enum Status {
        WALL(0),
        MOVED(1),
        GOAL(2);

        int code;

        Status(int code) {
            this.code = code;
        }

        static boolean isWall(int code) {
            return WALL.code == code;
        }

        static boolean isMoved(int code) {
            return MOVED.code == code;
        }

        static boolean isGoal(int code) {
            return GOAL.code == code;
        }
    }
}
