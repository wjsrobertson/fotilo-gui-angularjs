package net.xylophones.fotilo.common;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DirectionTest {

    @Test
    public void checkUpFromString() {
        Direction direction = Direction.fromString("up");
        assertThat(direction).isEqualTo(Direction.UP);
    }

    @Test
    public void checkDownFromString() {
        Direction direction = Direction.fromString("Down");
        assertThat(direction).isEqualTo(Direction.DOWN);
    }

    @Test
    public void checkLeftFromString() {
        Direction direction = Direction.fromString("LEFT");
        assertThat(direction).isEqualTo(Direction.LEFT);
    }

    @Test
    public void checkRightFromString() {
        Direction direction = Direction.fromString("right");
        assertThat(direction).isEqualTo(Direction.RIGHT);
    }

    @Test
    public void checkUpLeftFromString() {
        Direction direction = Direction.fromString("up left");
        assertThat(direction).isEqualTo(Direction.UP_LEFT);
    }

    @Test
    public void checkLeftUpFromString() {
        Direction direction = Direction.fromString("left-up");
        assertThat(direction).isEqualTo(Direction.UP_LEFT);
    }

    @Test
    public void checkUpRightFromString() {
        Direction direction = Direction.fromString("right up");
        assertThat(direction).isEqualTo(Direction.UP_RIGHT);
    }

    @Test
    public void checkDownRightFromString() {
        Direction direction = Direction.fromString("down right");
        assertThat(direction).isEqualTo(Direction.DOWN_RIGHT);
    }

    @Test
    public void checkDownLeftFromString() {
        Direction direction = Direction.fromString("down left");
        assertThat(direction).isEqualTo(Direction.DOWN_LEFT);
    }
}
