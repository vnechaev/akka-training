package playground.painter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface Events {
    public static final String TAG = "painter";

    public int getX();

    public int getY();

    @JsonSerialize
    static class Painted implements Events {
        private final int x;
        private final int y;
        private final int c;

        @JsonCreator
        public Painted(
                @JsonProperty("x") int x,
                @JsonProperty("y") int y,
                @JsonProperty("c") int c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getC() {
            return c;
        }

        @Override
        public String toString() {
            return "Painted{" +
                    "x=" + x +
                    ", y=" + y +
                    ", c=" + c +
                    '}';
        }
    }
}
