/**
 * Part two of
 * https://adventofcode.com/2019/day/14
 *
 * @author Nerijus
 */
public class Day14_2 extends Day14_1 {

    public static void main(String[] args) {
        System.out.printf("Given 1 trillion ORE, the maximum amount of FUEL can be produced: %.0f\n", new Day14_2().getResult());
    }

    private double getResult() {
        double oneTrillion = 1000000000000D;
        double fuelProduced = 0D;
        Day14_1.Nanofactory nanofactory = buildNanofactory("Day14");
        while (true) {
            nanofactory.produce("FUEL", 1);
            if (nanofactory.getOreConsumed() > oneTrillion) {
                break;
            }
            fuelProduced = fuelProduced + 1;
        }
        return fuelProduced;
    }
}
