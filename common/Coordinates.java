package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSame(Coordinates coordinates) {
        return isSameY(coordinates) && isSameX(coordinates);
    }

    public boolean isSameY(Coordinates coordinates) {
        return Integer.valueOf(coordinates.y).equals(y);
    }

    public boolean isSameX(Coordinates coordinates) {
        return Integer.valueOf(coordinates.x).equals(x);
    }

    public Coordinates adjacent(Direction direction) {
        switch (direction) {
            case UP:
                return new Coordinates(x, y + 1);
            case DOWN:
                return new Coordinates(x, y - 1);
            case LEFT:
                return new Coordinates(x - 1, y);
            case RIGHT:
                return new Coordinates(x + 1, y);
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
    }

    public List<Coordinates> allAdjacent() {
        List<Coordinates> coordinates = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            coordinates.add(adjacent(direction));
        }
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ')';
    }
}
