import common.IntCodeComputer;

/**
 * Part two of
 * https://adventofcode.com/2019/day/2
 *
 * @author Nerijus
 */
public class Day02_2 extends Day02_1 {
    public static void main(String[] args) {
        System.out.println("Result (100 * noun + verb): "
                + new Day02_2().getResult("19690720"));
    }

    private int getResult(String targetOutput) {
        int combination = 11;
        while (true) {
            String noun = firstDigit(String.valueOf(combination));
            String verb = secondDigit(String.valueOf(combination));
            IntCodeComputer computer = new IntCodeComputer(0, Inputs.readString("Day02"));
            computer.modify(noun, verb);
            if (computer.run().equals(targetOutput)) {
                return 100 * Integer.parseInt(noun) + Integer.parseInt(verb);
            }
            combination += 1;
        }
    }

    private String firstDigit(String combination) {
        int digitCount = combination.length();
        if (digitCount % 2 == 0) {
            // first half
            return combination.substring(0, digitCount / 2);
        } else {
            // slightly bigger first half
            return combination.substring(0, (digitCount / 2) + 1);
        }
    }

    private String secondDigit(String combination) {
        int digitCount = combination.length();
        if (digitCount % 2 == 0) {
            // second half
            return combination.substring(digitCount / 2, digitCount);
        } else {
            // slightly smaller second half
            return combination.substring((digitCount / 2) + 1, digitCount);
        }
    }
}
