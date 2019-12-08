import common.IntCodeComputer;

/**
 * Part one of
 * https://adventofcode.com/2019/day/2
 *
 * @author Nerijus
 */
public class Day02_1 {
    public static void main(String[] args) {
        System.out.println("Program result (at position 0): " + new Day02_1().getResult());
    }

    private String getResult() {
        IntCodeComputer computer = new IntCodeComputer(0, Inputs.readString("Day02"));
        computer.modify("12", "2");
        return computer.run();
    }
}
