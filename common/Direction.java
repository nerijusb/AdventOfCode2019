package common;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    Direction rotate(boolean left) {
        switch (this) {
            case UP:
                return left ? LEFT : RIGHT;
            case DOWN:
                return left ? RIGHT : LEFT;
            case LEFT:
                return left ? DOWN : UP;
            case RIGHT:
                return left ? UP : DOWN;
            default:
                throw new IllegalStateException("Unexpected direction: " + this);
        }
    }

    public Direction rotateLeft() {
        return rotate(true);
    }

    public Direction rotateRight() {
        return rotate(false);
    }
}
