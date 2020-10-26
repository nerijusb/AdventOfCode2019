import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        System.out.printf("Total energy in the system after %d steps: %d%n", STEPS, new Day12_1().getResult());
    }

    private int getResult() {
        Moon[] moons = readMoons("Day12");
        for (int i = 0; i < STEPS; i++) {
            simulateStep(moons);
        }
        return getTotalEnergy(moons);
    }

    void simulateStep(Moon[] moons) {
        // updates velocity
        applyGravityFor(moons[0], moons);
        applyGravityFor(moons[1], moons);
        applyGravityFor(moons[2], moons);
        applyGravityFor(moons[3], moons);
        // applies it to position
        moons[0].applyVelocity();
        moons[1].applyVelocity();
        moons[2].applyVelocity();
        moons[3].applyVelocity();
    }

    private void applyGravityFor(Moon moon, Moon[] allMoons) {
        for (Moon otherMoon : allMoons) {
            if (otherMoon.is(moon)) {
                continue;
            }
            moon.velocity.x = moon.velocity.x + Integer.compare(otherMoon.position.x, moon.position.x); // 2028
            moon.velocity.y = moon.velocity.y + Integer.compare(otherMoon.position.y, moon.position.y); // 5898
            moon.velocity.z = moon.velocity.z + Integer.compare(otherMoon.position.z, moon.position.z); // 4702
        }
    }

    Moon[] readMoons(String fileName) {
        List<String> moonSources = Inputs.readStrings(fileName);
        return new Moon[] {
            moonFrom(moonSources.get(0), (byte)1),
            moonFrom(moonSources.get(1), (byte)2),
            moonFrom(moonSources.get(2), (byte)3),
            moonFrom(moonSources.get(3), (byte)4)
        };
    }

    private int getTotalEnergy(Moon[] moons) {
        return Arrays.stream(moons)
                .peek(System.out::println)
                .mapToInt(Moon::totalEnergy)
                .sum();
    }

    private Moon moonFrom(String source, byte id) {
        Matcher matcher = MOON_PATTERN.matcher(source);
        if (!matcher.matches()) {
            throw new IllegalStateException("Could not parse moon info: " + source);
        }
        return new Moon(
                id,
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)));
    }

    static class Moon {
        byte id;
        Vector position;
        Vector velocity = new Vector(0, 0, 0);

        public Moon(byte id, int x, int y, int z) {
            this.id = id;
            this.position = new Vector(x, y, z);
        }

        public void applyVelocity() {
            position.x = position.x + velocity.x;
            position.y = position.y + velocity.y;
            position.z = position.z + velocity.z;
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

        public boolean is(Moon other) {
            return this.id == other.id;
        }

        boolean hasSameX(Moon other) {
            return position.x == other.position.x &&
                    velocity.x == other.velocity.x;
        }

        boolean hasSameY(Moon other) {
            return position.y == other.position.y &&
                    velocity.y == other.velocity.y;
        }

        boolean hasSameZ(Moon other) {
            return position.z == other.position.z &&
                    velocity.z == other.velocity.z;
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
