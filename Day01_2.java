import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2019/day/1
 *
 * @author Nerijus
 */
public class Day01_2 extends Day01_1 {
    public static void main(String[] args) {
        System.out.println("Calculated total fuel: " +
                new Day01_2().calculateTotalFuel(Inputs.readInts("Day01")));
    }

    private int calculateTotalFuel(List<Integer> masses) {
        return masses.stream()
                .mapToInt(mass -> {
                    int totalFuel = 0;
                    int requiredFuel = calculateFuelForMass(mass);
                    while (requiredFuel > 0) {
                        totalFuel = totalFuel + requiredFuel;
                        requiredFuel = calculateFuelForMass(requiredFuel);
                    }
                    return totalFuel;
                })
                .sum();
    }
}
