import java.util.*;
import java.util.stream.IntStream;

/**
 * Part one of
 * https://adventofcode.com/2019/day/4
 *
 * @author Nerijus
 */
public class Day04_1 {
    public static void main(String[] args) {
        System.out.println("Different passwords within range that meet criteria: "
                + new Day04_1().getResult());
    }

    private long getResult() {
        String[] range = Inputs.readString("Day04").split("-");
        int from = Integer.parseInt(range[0]);
        int to = Integer.parseInt(range[1]);
        return IntStream.range(from, to)
                .filter(password -> matchesCriteria(String.valueOf(password)))
                .count();
    }

    private boolean matchesCriteria(String password) {
        return isSixDigits(password)
                && twoAdjacentAreSame(password)
                && digitsNeverDecrease(password);
    }

    boolean isSixDigits(String password) {
        return password.length() == 6;
    }

    boolean twoAdjacentAreSame(String password) {
        String[] digits = password.split("");
        for (int i = 0; i < digits.length - 1; i++) {
            if (digits[i].equals(digits[i+1])) {
                return true;
            }
        }
        return false;
    }

    boolean digitsNeverDecrease(String password) {
        String[] digits = password.split("");
        for (int i = 0; i < digits.length - 1; i++) {
            if (Integer.parseInt(digits[i]) > Integer.parseInt(digits[i+1])) {
                return false;
            }
        }
        return true;
    }
}
