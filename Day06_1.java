import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2019/day/6
 *
 * @author Nerijus
 */
public class Day06_1 {
    public static void main(String[] args) {
        System.out.println("Total number of direct and indirect orbits: " + new Day06_1().getResult());
    }

    private int getResult() {
        Orbit com = getOrbitalMap();
        return com.getTotalOrbits();
    }

    Orbit getOrbitalMap() {
        List<OrbitMapping> mappings = readMappings();

        Orbit com = new Orbit(findCenterOfMass(mappings));
        do {
            OrbitMapping orbitMapping = mappings.get(0);
            mappings.remove(orbitMapping);
            Orbit found = com.find(orbitMapping.object);
            if (found != null) {
                // found where to add it
                found.add(orbitMapping);
            } else {
                // back to list
                mappings.add(orbitMapping);
            }
        } while (!mappings.isEmpty());
        return com;
    }

    private List<OrbitMapping> readMappings() {
        return Inputs.readStrings("Day06")
                    .stream()
                    .map(OrbitMapping::new)
                    .collect(Collectors.toList());
    }

    private String findCenterOfMass(List<OrbitMapping> mappings) {
        List<String> objects = mappings.stream().map(m -> m.object).collect(Collectors.toList());
        List<String> orbitingObjects = mappings.stream().map(m -> m.orbitingObject).collect(Collectors.toList());

        objects.removeAll(orbitingObjects);

        return objects.stream().findFirst().orElseThrow(() -> new IllegalStateException("More than one COM found"));
    }

    static class Orbit {
        String object;
        Orbit parent;
        List<Orbit> orbits = new ArrayList<>();

        Orbit(String object) {
            this.object = object;
        }

        void add(OrbitMapping source) {
            Orbit newObject = new Orbit(source.orbitingObject);
            newObject.parent = this;
            this.orbits.add(newObject);
        }

        Orbit find(String object) {
            if (object.equals(this.object)) {
                return this;
            }
            for (Orbit orbit : orbits) {
                Orbit found = orbit.find(object);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }

        int getTotalOrbits() {
            return getTotalOrbits(0);
        }

        private int getTotalOrbits(int depth) {
            if (orbits.isEmpty()) {
                return depth;
            }
            int combined = depth;
            for (Orbit o : orbits) {
                combined = combined + o.getTotalOrbits(depth + 1);
            }
            return combined;
        }
    }

    static class OrbitMapping {
        String object;
        String orbitingObject;

        public OrbitMapping(String source) {
            String[] parts = source.split("\\)");
            this.object = parts[0];
            this.orbitingObject = parts[1];
        }
    }
}
