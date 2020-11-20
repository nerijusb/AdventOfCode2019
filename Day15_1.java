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
        System.out.printf("Fewest number of movement commands to the oxygen system location: %d\n", new Day15_1().getResult());
    }

    private int getResult() {
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day15"));

        Map<Coordinates, String> visited = new HashMap<>();
        LinkedList<Coordinates> currentPath = new LinkedList<>();
        Coordinates currentPosition = new Coordinates(0, 0);
        currentPath.push(currentPosition);
        visited.put(currentPosition, "S");

        // try move
        walk: while (true) {
            rotation: for (Movement movement : Movement.values()) {
                log("Next: " + movement.name());
                Coordinates nextPosition = currentPosition.adjacent(movement.direction);
                if (visited.containsKey(nextPosition)) {
                    String msg = "Already been there, continue rotation";
                    log(msg);
                    continue rotation;
                } else {
                    int nextCode = Integer.parseInt(computer.run(() -> movement.code).get());

                    if (Status.isGoal(nextCode)) {
                        visited.put(nextPosition, "G");
                        log("Found goal position: " + nextPosition.toString());
                        return currentPath.size();
                    } else if (Status.isWall(nextCode)) {
                        log("Wall, continue rotation");
                        visited.put(nextPosition, "#");
                        continue rotation;
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
                print(visited);
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

    void print(Map<Coordinates, String> visited) {
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

        visited.forEach((key, value) -> {
            map[key.y][key.x] = value;
        });

        for (int y = 0; y <= dimension; y++) {
            for (int x = 0; x <= dimension; x++) {
                System.out.print(Objects.requireNonNullElse(map[y][x], " "));
            }
            System.out.println();
        }
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

        private int code;

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
