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
            tiles.add(new Tile(
                    Integer.parseInt(xRun.get()),
                    Integer.parseInt(computer.run().get()),
                    Integer.parseInt(computer.run().get())));
        }

        return tiles.stream().filter(t -> t.id == 2).count();
    }

    static class Tile {
        int x;
        int y;
        int id;

        public Tile(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        boolean isBall() {
            return id == 4;
        }

        boolean isPaddle() {
            return id == 3;
        }

        String displayValue() {
            switch (id) {
                case 0:
                    return " ";
                case 1:
                    return "X";
                case 2:
                    return ".";
                case 3:
                    return "_";
                case 4:
                    return "o";
                default:
                    throw new IllegalStateException("Unexpected id: " + id);
            }
        }

        @Override
        public String toString() {
            return String.format("%d (%d,%d)", id, x, y);
        }
    }
}
