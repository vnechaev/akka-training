package playground.painter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class PixelPaintedEvent {
    private final String bucket;
    private final int x;
    private final int y;
    private final int c;

    @JsonCreator
    public PixelPaintedEvent(
            @JsonProperty("bucket") String bucket,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("c") int c) {
        this.bucket = bucket;
        this.x = x;
        this.y = y;
        this.c = c;
    }

    public String getBucket() {
        return bucket;
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
}

