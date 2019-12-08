import common.IntCodeComputer;

/**
 * Part one of
 * https://adventofcode.com/2019/day/5
 *
 * @author Nerijus
 */
public class Day05_1 {
    public static void main(String[] args) {
        System.out.println("Program diagnostic and output:");
        new IntCodeComputer(() -> "1", Inputs.readString("Day05")).run();
    }
}
