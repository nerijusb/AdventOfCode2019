import common.IntCodeComputer;

/**
 * Part one of
 * https://adventofcode.com/2019/day/9
 *
 * @author Nerijus
 */
public class Day09_1 {
    public static void main(String[] args) {
        System.out.println("BOOST keycode: ");
        new Day09_1().getResult();
    }

    private void getResult() {
        IntCodeComputer computer = new IntCodeComputer(getPuzzleInput(), () -> "1");
        printOutput(computer);
    }

    void printOutput(IntCodeComputer computer) {
        IntCodeComputer.Result result = computer.run();
        while (!result.isEnd()) {
            System.out.println(result.get());
            result = computer.run();
        }
    }

    String getPuzzleInput() {
        return Inputs.readString("Day09");
    }
}
