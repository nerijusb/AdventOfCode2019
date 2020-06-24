import common.Coordinates;

import java.util.Map;
import java.util.Set;

/**
 * Part one of
 * https://adventofcode.com/2019/day/11
 *
 * @author Nerijus
 */
public class Day11_2 extends Day11_1 {
    public static void main(String[] args) {
        System.out.println("Registration identifier:");
        new Day11_2().printRegistrationId();
    }

    private void printRegistrationId() {
        PaintingRobot robot = new PaintingRobot(new Coordinates(0, 0), "#");
        Map<Coordinates, Panel> paintedPanels = robot.paint();

        // assuming that coordinates could be on any quadrant
        // find values by which to push coordinates to lower-right quadrant
        // so that we could then convert them to array and print
        int minX = minX(paintedPanels.keySet());
        int maxX = maxX(paintedPanels.keySet());
        int minY = minY(paintedPanels.keySet());
        int maxY = maxY(paintedPanels.keySet());

        // change the coordinates so that they all now +x and -y
        paintedPanels.forEach((c, p) -> {
            c.x = c.x + minX;
            c.y = c.y - maxY;
        });

        // make an array we can print
        String[][] identifier = new String[Math.abs(minY - maxY) + 1][maxX + Math.abs(minX) + 1];
        paintedPanels.forEach((key, value) -> identifier[Math.abs(key.y)][key.x] = value.color);

        // print it
        for (String[] row : identifier) {
            for (String color : row) {
                System.out.print(color == null ? "." : color);
            }
            System.out.println();
        }
    }

    private int minY(Set<Coordinates> keySet) {
        return keySet.stream().mapToInt(c -> c.y).min().orElseThrow(() -> new IllegalStateException("Could not find min Y"));
    }

    private int maxY(Set<Coordinates> keySet) {
        return keySet.stream().mapToInt(c -> c.y).max().orElseThrow(() -> new IllegalStateException("Could not find max Y"));
    }

    private int minX(Set<Coordinates> keySet) {
        return keySet.stream().mapToInt(c -> c.x).min().orElseThrow(() -> new IllegalStateException("Could not find min X"));
    }

    private int maxX(Set<Coordinates> keySet) {
        return keySet.stream().mapToInt(c -> c.x).max().orElseThrow(() -> new IllegalStateException("Could not find max X"));
    }
}
