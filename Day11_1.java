import common.Coordinates;
import common.Direction;
import common.IntCodeComputer;

import java.util.HashMap;
import java.util.Map;

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
}
