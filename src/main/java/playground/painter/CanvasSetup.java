package playground.painter;

public class CanvasSetup {
    private final int shards;
    private final int tileWidth;
    private final int tileHeight;

    public CanvasSetup(int shards, int tileWidth, int tileHeight) {
        this.shards = shards;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getNumberOfShards() {
        return shards;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
