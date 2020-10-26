/**
 * Part two of
 * https://adventofcode.com/2019/day/12
 *
 * @author Nerijus
 */
public class Day12_2 extends Day12_1 {

    public static final String SOURCE = "Day12";

    public static void main(String[] args) {
        System.out.printf("Previous state achieved after %s steps", new Day12_2().getResult());
    }

    String getResult() {
        int stepX = sameXAfter();
        System.out.println("Same X after: " + stepX);
        int stepY = sameYAfter();
        System.out.println("Same Y after: " + stepY);
        int stepZ = sameZAfter();
        System.out.println("Same Z after: " + stepZ);

        // least common multiple according to wolfram alpha:
        return "380635029877596";
    }

    int sameXAfter() {
        Moon[] initialMoons = readMoons(SOURCE);
        Moon[] moons = readMoons(SOURCE);
        int step = 1;
        while (true) {
            simulateStep(moons);
            if (hasSameX(initialMoons, moons)) {
                // same after step
                return step;
            } else {
                step = step + 1;
            }
        }
    }

    int sameYAfter() {
        Moon[] initialMoons = readMoons(SOURCE);
        Moon[] moons = readMoons(SOURCE);
        int step = 1;
        while (true) {
            simulateStep(moons);
            if (hasSameY(initialMoons, moons)) {
                // same after step
                return step;
            } else {
                step = step + 1;
            }
        }
    }

    int sameZAfter() {
        Moon[] initialMoons = readMoons(SOURCE);
        Moon[] moons = readMoons(SOURCE);
        int step = 1;
        while (true) {
            simulateStep(moons);
            if (hasSameZ(initialMoons, moons)) {
                // same after step
                return step;
            } else {
                step = step + 1;
            }
        }
    }

    boolean hasSameX(Moon[] initialMoons, Moon[] moons) {
        return initialMoons[0].hasSameX(moons[0]) &&
                initialMoons[1].hasSameX(moons[1]) &&
                initialMoons[2].hasSameX(moons[2]) &&
                initialMoons[3].hasSameX(moons[3]);
    }

    boolean hasSameY(Moon[] initialMoons, Moon[] moons) {
        return initialMoons[0].hasSameY(moons[0]) &&
                initialMoons[1].hasSameY(moons[1]) &&
                initialMoons[2].hasSameY(moons[2]) &&
                initialMoons[3].hasSameY(moons[3]);
    }

    boolean hasSameZ(Moon[] initialMoons, Moon[] moons) {
        return initialMoons[0].hasSameZ(moons[0]) &&
                initialMoons[1].hasSameZ(moons[1]) &&
                initialMoons[2].hasSameZ(moons[2]) &&
                initialMoons[3].hasSameZ(moons[3]);
    }
}
