import common.IntCodeComputer;

import java.util.ArrayList;
import java.util.List;

/**
 * Part one of
 * https://adventofcode.com/2019/day/13
 *
 * @author Nerijus
 */
public class Day13_1 {
    public static void main(String[] args) {
        System.out.printf("Block tiles on screen when the game exits: %d", new Day13_1().getResult());
    }

    private long getResult() {
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day13"));
        List<Tile> tiles = new ArrayList<>();

        while (true) {
            IntCodeComputer.Result xRun = computer.run();
            if (xRun.isEnd()) {
                break;
            }
            tiles.add(new Tile(Integer.parseInt(xRun.get()), Integer.parseInt(computer.run().get()), computer.run().get()));
        }

        return tiles.stream().filter(t -> "2".equals(t.id)).count();
    }

    static class Tile {
        int x;
        int y;
        String id;

        public Tile(int x, int y, String id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        @Override
        public String toString() {
            return String.format("%s (%d,%d)", id, x, y);
        }
    }
}
