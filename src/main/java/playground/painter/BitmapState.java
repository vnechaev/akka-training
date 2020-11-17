package playground.painter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Arrays;

@JsonDeserialize
public class BitmapState {
    private final int baseX;
    private final int baseY;
    private final int width;
    private final int height;
    private final int[][] pixels;

    public BitmapState(
            int baseX,
            int baseY,
            int width,
            int height) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width];
        for (int[] row : pixels) {
            Arrays.fill(row, 0xFFFFFF);
        }
    }

    @JsonCreator
    public BitmapState(
            @JsonProperty("baseX") int baseX,
            @JsonProperty("baseY") int baseY,
            @JsonProperty("width") int width,
            @JsonProperty("height") int height,
            @JsonProperty("pixels") int[][] pixels
    ) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public void validatePaint(int x, int y, int color) throws BitmapException {
        if (color < 0x000000 || color > 0xFFFFFF) {
            throw new BitmapException.Color("Wrong color");
        }
        if (x < baseX || x > baseX + width || y < baseY || y > baseY + height) {
            throw new BitmapException.Coordinates("Wrong coordinates");
        }
    }

    public void paint(int x, int y, int color) throws BitmapException {
        validatePaint(x, y, color);
        pixels[y - baseY][x - baseX] = color;
    }

    private void validateShow(int x, int y, int w, int h) throws BitmapException {
        if (x < baseX || x > baseX + width || y < baseY || y > baseY + height) {
            throw new BitmapException.Coordinates("Wrong coordinates");
        }
        if (w < 0 || w > width || h < 0 || h > height) {
            throw new BitmapException.Dimensions("Wrong dimensions");
        }
    }

    public int[][] show(int x, int y, int w, int h) throws BitmapException {
        validateShow(x, y, w, h);

        int startX = x - baseX;
        int endX = startX + w;
        int startY = y - baseY;
        int endY = startY + h;

        int[][] show = new int[w][];
        for (int row = startY; row < endY; row++)
            show[row - startY] = Arrays.copyOfRange(pixels[row], startX, endX);
        return show;
    }

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getPixels() {
        return pixels;
    }
}
