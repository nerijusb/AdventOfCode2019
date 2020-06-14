import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2019/day/10
 *
 * @author Nerijus
 */
public class Day10_2 extends Day10_1 {
    public static void main(String[] args) {
        // print full sequence
        new Day10_2().vaporize(new Position(36,26));
        System.out.println("200th asteroid x * 100 + y = " + (8 * 100 + 29));
    }

    private void vaporize(Position station) {
        String[][] map = getPuzzleInput();
        List<AsteroidInfo> visibleAsteroids = getVisibleAsteroids(map, station);
        int counter = 0;
        while (!visibleAsteroids.isEmpty()) {
            for (AsteroidInfo asteroid : visibleAsteroids) {
                // vaporize
                counter = counter + 1;
                System.out.println(String.format("The #%d asteroid to be vaporized is at %d,%d at angle %f",
                        counter, asteroid.position.y, asteroid.position.x, asteroid.angle));
                map[asteroid.position.x][asteroid.position.y] = ".";
            }
            visibleAsteroids = getVisibleAsteroids(map, station);
        }
    }
}
