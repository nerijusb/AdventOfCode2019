import common.IntCodeComputer;

/**
 * Part two of
 * https://adventofcode.com/2019/day/9
 *
 * @author Nerijus
 */
public class Day09_2 extends Day09_1 {
    public static void main(String[] args) {
        System.out.println("Coordinates of the distress signal: ");
        new Day09_2().getResult();
    }

    private void getResult() {
        IntCodeComputer computer = new IntCodeComputer(getPuzzleInput(), () -> "2");
        printOutput(computer);
    }
}
