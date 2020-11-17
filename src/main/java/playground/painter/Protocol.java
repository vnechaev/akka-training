package playground.painter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface Protocol {
    int getX();

    int getY();

    @JsonSerialize
    class Show implements Protocol {
        private final int x;
        private final int y;
        private final int w;
        private final int h;

        @JsonCreator
        public Show(
                @JsonProperty("x") int x,
                @JsonProperty("y") int y,
                @JsonProperty("w") int w,
                @JsonProperty("h") int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }

        @Override
        public String toString() {
            return "Show{" +
                    "x=" + x +
                    ", y=" + y +
                    ", w=" + w +
                    ", h=" + h +
                    '}';
        }
    }

    @JsonSerialize
    class Paint implements Protocol {
        private final int x;
        private final int y;
        private final int c;

        @JsonCreator
        public Paint(
                @JsonProperty("x") int x,
                @JsonProperty("y") int y,
                @JsonProperty("c") int c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public int getC() {
            return c;
        }

        @Override
        public String toString() {
            return "Paint{" +
                    "x=" + x +
                    ", y=" + y +
                    ", c=" + c +
                    '}';
        }
    }
}
