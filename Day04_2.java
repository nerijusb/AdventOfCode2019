import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Part two of
 * https://adventofcode.com/2019/day/4
 *
 * @author Nerijus
 */
public class Day04_2 extends Day04_1 {
    public static void main(String[] args) {
        System.out.println("Different passwords within range that meet criteria: "
                + new Day04_2().getResult());
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
                && digitsNeverDecrease(password)
                && twoAdjacentAreSame(password)
                && twoAdjacentAreNotPartOfLargerGroup(password);
    }

    private boolean twoAdjacentAreNotPartOfLargerGroup(String password) {
        String[] digits = password.split("");
        for (int i = 0; i < digits.length - 1; i++) {
            if (i == 0) {
                if (digits[i].equals(digits[i+1]) && !digits[i].equals(digits[i+2])) {
                    return true;
                }
            } else if (i <= 3) {
                if (!digits[i-1].equals(digits[i]) && digits[i].equals(digits[i+1]) && !digits[i].equals(digits[i+2])) {
                    return true;
                }
            } else {
                if (!digits[i-1].equals(digits[i]) && digits[i].equals(digits[i+1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
