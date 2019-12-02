/**
 * Part two of
 * https://adventofcode.com/2019/day/2
 *
 * @author Nerijus
 */
public class Day02_2 extends Day02_1 {
    public static void main(String[] args) {
        System.out.println("Result (100 * noun + verb): "
                + new Day02_2().getResult(19690720));
    }

    private int getResult(int targetOutput) {
        int combination = 11;
        while (true) {
            int noun = firstDigit(combination);
            int verb = secondDigit(combination);
            int result = new Program(noun, verb).run();
            if (result == targetOutput) {
                return 100 * noun + verb;
            }
            combination += 1;
        }
    }

    private int firstDigit(int combination) {
        int digitCount = String.valueOf(combination).length();
        if (digitCount % 2 == 0) {
            // first half
            return Integer.parseInt(String.valueOf(combination).substring(0, digitCount / 2));
        } else {
            // slightly bigger first half
            return Integer.parseInt(String.valueOf(combination).substring(0, (digitCount / 2) + 1));
        }
    }

    private int secondDigit(int combination) {
        int digitCount = String.valueOf(combination).length();
        if (digitCount % 2 == 0) {
            // second half
            return Integer.parseInt(String.valueOf(combination).substring(digitCount / 2, digitCount));
        } else {
            // slightly smaller second half
            return Integer.parseInt(String.valueOf(combination).substring((digitCount / 2) + 1, digitCount));
        }
    }
}
