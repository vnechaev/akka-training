package playground.painter;

public class BitmapException extends Exception {
    public BitmapException() {
    }

    public BitmapException(String message) {
        super(message);
    }

    static class Color extends BitmapException {
        public Color() {
        }

        public Color(String message) {
            super(message);
        }
    }

    static class Coordinates extends BitmapException {
        public Coordinates() {
        }

        public Coordinates(String message) {
            super(message);
        }
    }
    static class Dimensions extends BitmapException {
        public Dimensions() {
        }

        public Dimensions(String message) {
            super(message);
        }
    }
}
