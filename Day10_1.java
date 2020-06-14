import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2019/day/10
 *
 * @author Nerijus
 */
public class Day10_1 {
    public static void main(String[] args) {
        System.out.println("Max asteroids can be detected: " + new Day10_1().getResult());
    }

    private int getResult() {
        return getAsteroidPositions()
                .stream()
                .mapToInt(this::countVisibleAsteroidsFrom)
                .max()
                .orElseThrow(() -> new IllegalStateException("Could not find max visible asteroids"));
    }

    private int countVisibleAsteroidsFrom(Position station) {
        List<Position> asteroids = getAsteroidPositions();
        return asteroids.stream()
                .filter(a -> !a.isSame(station))
                .map(a -> lineOfSight(station, a))
                .collect(Collectors.toSet())
                .size();
    }

    // calculate a 'line of sight' identifier, that should be equal for the same directional line
    private String lineOfSight(Position station, Position asteroid) {
        // check if on same x or y first
        if (station.x.equals(asteroid.x)) {
            return sign(asteroid.y - station.y) + "x";
        }
        if (station.y.equals(asteroid.y)) {
            return sign(asteroid.x - station.x) + "y";
        }
        // forms triangle. Get third point
        Position t = new Position(station.x, asteroid.y);

        int line1 = t.x - asteroid.x;
        int line2 = t.y - station.y;
        float ratio = (float)line1 / (float)line2;

        return sign(line1) + sign(line2) + ratio;
    }

    private String sign(int number) {
        return number < 0? "-" : "+";
    }

    private String[][] getPuzzleInput() {
        List<String> rows = Inputs.readStrings("Day10");
        String[][] map = new String[rows.size()][rows.get(0).length()];
        for (int x = 0; x < rows.size(); x++) {
            String[] row = rows.get(x).split("");
            for (int y = 0; y < row.length; y++) {
                map[x][y] = row[y];
            }
        }
        return map;
    }

    private List<Position> getAsteroidPositions() {
        String[][] map = getPuzzleInput();
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                String type = map[x][y];
                if ("#".equals(type)) {
                    positions.add(new Position(x, y));
                }
            }
        }
        return positions;
    }

    private static class Position {
        Integer x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isSame(Position position) {
            return position.x.equals(x)
                    && position.y.equals(y);
        }
    }
}
