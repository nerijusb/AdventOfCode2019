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
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day05"), () -> "1");
        IntCodeComputer.Result result = computer.run();
        while (!result.isEnd()) {
            System.out.println(result.get());
            result = computer.run();
        }
    }
}
