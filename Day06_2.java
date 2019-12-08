import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2019/day/6
 *
 * @author Nerijus
 */
public class Day06_2 extends Day06_1 {
    public static void main(String[] args) {
        System.out.println("Minimum number of orbital transfers required: " + new Day06_2().getResult());
    }

    private int getResult() {
        Orbit com = getOrbitalMap();
        Orbit me = com.find("YOU");
        List<Orbit> pathToMe = getPathTo(me);
        Orbit santa = com.find("SAN");
        List<Orbit> pathToSanta = getPathTo(santa);

        List<Orbit> commonPath = findCommon(pathToMe, pathToSanta);
        List<Orbit> differenceToMe = findDifference(commonPath, pathToMe);
        List<Orbit> differenceToSanta = findDifference(commonPath, pathToSanta);

        return differenceToMe.size() + differenceToSanta.size();
    }

    private List<Orbit> findCommon(List<Orbit> one, List<Orbit> two) {
        List<Orbit> commonPath = new ArrayList<>();
        for (Orbit orbit : one) {
            if (two.contains(orbit)) {
                commonPath.add(orbit);
            } else {
                break;
            }
        }
        return commonPath;
    }

    private List<Orbit> findDifference(List<Orbit> from, List<Orbit> to) {
        List<Orbit> diff = new ArrayList<>(to);
        diff.removeAll(from);
        return diff;
    }

    private List<Orbit> getPathTo(Orbit orbit) {
        List<Orbit> path = new ArrayList<>();
        Orbit currentOrbit = orbit;
        while (currentOrbit.parent != null) {
            path.add(currentOrbit.parent);
            currentOrbit = currentOrbit.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
