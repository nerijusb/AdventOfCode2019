import java.util.List;

/**
 * Part one of
 * https://adventofcode.com/2019/day/1
 *
 * @author Nerijus
 */
public class Day01_1 {
    public static void main(String[] args) {
        System.out.println("Calculated fuel: " +
                new Day01_1().calculateTotalFuel(Inputs.readInts("Day01")));
    }

    private int calculateTotalFuel(List<Integer> masses) {
        return masses.stream()
                .mapToInt(this::calculateFuelForMass)
                .sum();
    }

    int calculateFuelForMass(double mass) {
        return (int) Math.floor(mass / 3) - 2;
    }
}
