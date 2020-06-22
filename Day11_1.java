import common.IntCodeComputer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Part one of
 * https://adventofcode.com/2019/day/11
 *
 * @author Nerijus
 */
public class Day11_1 {
    public static void main(String[] args) {
        System.out.println("Panels painted at least once: " + new Day11_1().getResult());
    }

    private int getResult() {
        PaintingRobot robot = new PaintingRobot(new Coordinates(0, 0));
        return robot.paint().size();
    }

    static class PaintingRobot {
        Coordinates position;
        Direction direction = Direction.UP;
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day11"));
        Panel initialPanel;

        public PaintingRobot(Coordinates position) {
            this.position = position;
            this.initialPanel = new Panel(position);
        }

        public PaintingRobot(Coordinates position, String initialColor) {
            this.position = position;
            this.initialPanel = new Panel(position, initialColor);
        }

        Map<Coordinates, Panel> paint() {
            Map<Coordinates, Panel> paintedPanels = new HashMap<>();
            Panel currentPanel = initialPanel;


            while (true) {
                IntCodeComputer.Result colorCodeResult = computer.run(currentPanel::colorCode);
                if (colorCodeResult.isEnd()) {
                    break;
                }
                currentPanel.paint(colorCodeResult.get());
                paintedPanels.put(currentPanel.coordinates, currentPanel);

                IntCodeComputer.Result rotationResult = computer.run();
                if (colorCodeResult.isEnd()) {
                    break;
                }
                Direction newDirection = rotationResult.get().equals("0") ? this.direction.rotateLeft() : this.direction.rotateRight();
                Coordinates nextCoordinates = nextCoordinates(newDirection);
                position = nextCoordinates;
                currentPanel = paintedPanels.get(nextCoordinates) != null ? paintedPanels.get(nextCoordinates) : new Panel(nextCoordinates);
                direction = newDirection;
            }

            return paintedPanels;
        }

        Coordinates nextCoordinates(Direction direction) {
            return position.adjacent(direction);
        }
    }

    static class Coordinates {
        int x;
        int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinates adjacent(Direction direction) {
            switch (direction) {
                case UP:
                    return new Coordinates(x, y + 1);
                case DOWN:
                    return new Coordinates(x, y - 1);
                case LEFT:
                    return new Coordinates(x - 1, y);
                case RIGHT:
                    return new Coordinates(x + 1, y);
                default:
                    throw new IllegalStateException("Unexpected direction: " + direction);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(x=" + x + ", y=" + y + ')';
        }
    }

    static class Panel {
        Coordinates coordinates;
        String color = ".";// black

        public Panel(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public Panel(Coordinates coordinates, String color) {
            this.coordinates = coordinates;
            this.color = color;
        }

        boolean isWhite() {
            return "#".equals(color);
        }

        String colorCode() {
            return isWhite() ? "1" : "0";
        }

        void paint(String colorCode) {
            color = "1".equals(colorCode) ? "#" : ".";
        }

        @Override
        public String toString() {
            return String.format("%s: color='%s'", coordinates.toString(), color);
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        Direction rotate(boolean left) {
            switch (this) {
                case UP:
                    return left ? LEFT : RIGHT;
                case DOWN:
                    return left ? RIGHT : LEFT;
                case LEFT:
                    return left ? DOWN : UP;
                case RIGHT:
                    return left ? UP : DOWN;
                default:
                    throw new IllegalStateException("Unexpected direction: " + this);
            }
        }

        Direction rotateLeft() {
            return rotate(true);
        }

        Direction rotateRight() {
            return rotate(false);
        }
    }
}
