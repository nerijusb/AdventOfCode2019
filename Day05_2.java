import common.IntCodeComputer;

/**
 * Part two of
 * https://adventofcode.com/2019/day/5
 *
 * @author Nerijus
 */
public class Day05_2 extends Day05_1 {
    public static void main(String[] args) {
        System.out.println("Diagnostic code for system ID 5:");
        IntCodeComputer computer = new IntCodeComputer(Inputs.readString("Day05"));
        IntCodeComputer.Result result = computer.run(() -> "5");
        while (!result.isEnd()) {
            System.out.println(result.get());
            result = computer.run(() -> "5");
        }
    }
}
