import common.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2019/day/15
 *
 * @author Nerijus
 */
public class Day15_2 {

    public static void main(String[] args) {
        System.out.printf("Minutes to fill map with oxygen: %d\n", new Day15_2().getResult());
    }

    int getResult() {
        Day15_1.Result result = new Day15_1().traverseMap();
        String[][] map = result.map;
        Coordinates oxygenSystem = new Coordinates(0, 0);

        // find oxygen system location
        for (int y = 0; y < map.length; y++) {
            String[] row = map[y];
            for (int x = 0; x < row.length; x++) {
                String value = row[x];
                if ("G".equals(value)) {
                    oxygenSystem = new Coordinates(x, y);
                }
            }
        }

        // start filling
        int minutes = 0;
        // initially oxygen system has oxygen
        map[oxygenSystem.y][oxygenSystem.x] = "o";

        List<Coordinates> filled = Collections.singletonList(oxygenSystem);
        while (true) {
            filled = fillAdjacent(filled, map);
            if (!filled.isEmpty()) {
                minutes = minutes + 1;
            } else {
                break;
            }
        }

        return minutes;
    }

    private List<Coordinates> fillAdjacent(List<Coordinates> locations, String[][] map) {
        List<Coordinates> filled = new ArrayList<>();
        for (Coordinates location : locations) {
            List<Coordinates> adjacent = location.allAdjacent();
            for (Coordinates adj : adjacent) {
                // see what we have there
                String status = map[adj.y][adj.x];
                if (".".equals(status) || "S".equals(status)) {
                    // fill with oxygen
                    map[adj.y][adj.x] = "o";
                    filled.add(adj);
                }
            }
        }
        return filled;
    }
}
