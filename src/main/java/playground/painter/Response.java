package playground.painter;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface Response {
    enum Status {
        OK, ERROR
    }

    public Status getResponse();

    @JsonSerialize
    class OK implements Response {
        @JsonCreator
        public OK() {
        }

        public Status getResponse() {
            return Status.OK;
        }
    }

    @JsonSerialize
    class SHOW implements Response {
        private final int[][] bitmap;

        @JsonCreator
        public SHOW(@JsonProperty("bitmap") int[][] bitmap) {
            this.bitmap = bitmap;
        }

        public Status getResponse() {
            return Status.OK;
        }

        public int[][] getBitmap() {
            return bitmap;
        }
    }

    @JsonSerialize
    class ERROR implements Response {
        public final String message;

        @JsonCreator
        public ERROR(@JsonProperty("message") String message) {
            this.message = message;
        }

        public Status getResponse() {
            return Status.ERROR;
        }

    }
}
