import common.Coordinates;
import common.IntCodeComputer;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Part two of
 * https://adventofcode.com/2019/day/13
 *
 * @author Nerijus
 */
public class Day13_2 extends Day13_1 {
    public static void main(String[] args) {
        new Day13_2().play();
    }

    private void play() {
        Screen screen = new Screen();
        Coordinates ballPosition = new Coordinates(0, 0);
        Coordinates paddlePosition = new Coordinates(0, 0);

        Supplier<String> joystick = () -> {
            if (paddlePosition.x == ballPosition.x) {
                return "0";
            }
            if (paddlePosition.x > ballPosition.x) {
                return "-1"; // move left
            }
            return "1"; // move right
        };
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day13"), joystick);
        computer.modify(0, "2");

        while (true) {
            IntCodeComputer.Result xRun = computer.run();
            if (xRun.isEnd()) {
                break;
            }
            int x = Integer.parseInt(xRun.get());
            int y = Integer.parseInt(computer.run().get());
            int value = Integer.parseInt(computer.run().get());
            if (x == -1) {
                // score change
                screen.feed(value);
            } else {
                Tile tile = new Tile(x, y, value);
                screen.feed(tile);
                if (tile.isBall()) {
                    ballPosition.x = tile.x;
                    ballPosition.y = tile.y;
                }
                if (tile.isPaddle()) {
                    paddlePosition.x = tile.x;
                    paddlePosition.y = tile.y;
                }
            }
            screen.print();
        }
    }

    static class Screen {
        public static final int HEIGHT = 30;
        public static final int WIDTH = 40;

        String[][] pixels = new String[HEIGHT][WIDTH];

        int score = 0;

        void print() {
            System.out.println("Score: " + score);
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    System.out.print(Objects.requireNonNullElse(pixels[y][x], " "));
                }
                System.out.println();
            }
        }

        void feed(Tile tile) {
            pixels[tile.y][tile.x] = tile.displayValue();
        }

        void feed(int score) {
            this.score = score;
        }
    }
}
