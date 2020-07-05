import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2019/day/12
 *
 * @author Nerijus
 */
public class Day12_1 {
    private static final Pattern MOON_PATTERN = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");
    private static final int STEPS = 1000;

    public static void main(String[] args) {
        System.out.println(String.format("Total energy in the system after %d steps: %d", STEPS, new Day12_1().getResult()));
    }

    private int getResult() {
        List<Moon> moons = readMoons("Day12");
        for (int i = 0; i < STEPS; i++) {
            simulateStep(moons);
        }
        return getTotalEnergy(moons);
    }

    void simulateStep(List<Moon> moons) {
        applyGravity(moons);
        applyVelocity(moons);
    }

    // updates velocity
    private void applyGravity(List<Moon> allMoons) {
        allMoons.forEach(moon -> applyGravityFor(moon, allMoons));
    }

    private void applyGravityFor(Moon moon, List<Moon> allMoons) {
        for (Moon otherMoon : allMoons) {
            if (otherMoon.equals(moon)) {
                continue;
            }
            moon.velocity.x = moon.velocity.x + Integer.compare(otherMoon.position.x, moon.position.x);
            moon.velocity.y = moon.velocity.y + Integer.compare(otherMoon.position.y, moon.position.y);
            moon.velocity.z = moon.velocity.z + Integer.compare(otherMoon.position.z, moon.position.z);
        }
    }

    // updates position
    private void applyVelocity(List<Moon> moons) {
        for (Moon moon : moons) {
            moon.position.x = moon.position.x + moon.velocity.x;
            moon.position.y = moon.position.y + moon.velocity.y;
            moon.position.z = moon.position.z + moon.velocity.z;
        }
    }

    List<Moon> readMoons(String fileName) {
        return Inputs.readStrings(fileName).stream().map(this::moonFrom).collect(Collectors.toList());
    }

    private int getTotalEnergy(List<Moon> moons) {
        return moons
                .stream()
                .peek(System.out::println)
                .mapToInt(Moon::totalEnergy)
                .sum();
    }

    private Moon moonFrom(String source) {
        Matcher matcher = MOON_PATTERN.matcher(source);
        if (!matcher.matches()) {
            throw new IllegalStateException("Could not parse moon info: " + source);
        }
        return new Moon(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)));
    }

    static class Moon {
        Vector position;
        Vector velocity = new Vector(0, 0, 0);

        public Moon(int x, int y, int z) {
            this.position = new Vector(x, y, z);
        }

        int totalEnergy() {
            return potentialEnergy() * kineticEnergy();
        }

        private int potentialEnergy() {
            return position.absoluteSum();
        }

        private int kineticEnergy() {
            return velocity.absoluteSum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return Objects.equals(position, moon.position) &&
                    Objects.equals(velocity, moon.velocity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, velocity);
        }

        @Override
        public String toString() {
            return "Moon{" +
                    String.format("pos=<x=%d, y=%d, z=%d>, vel=<x=%d, y=%d, z=%d>",
                            position.x, position.y, position.z, velocity.x, velocity.y, velocity.z) +
                    '}';
        }
    }

    static class Vector {
        int x, y, z;

        public Vector(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int absoluteSum() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector vector = (Vector) o;
            return x == vector.x &&
                    y == vector.y &&
                    z == vector.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
