package src.Drawable;

import processing.core.PApplet;

public class Point extends Drawable {
    protected int x, y;

    public Point(int x, int y, int strokeColor, int fillColor, int strokeWeight) {
        super(fillColor, strokeColor, strokeWeight);
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.strokeWeight(strokeWeight);
        app.point(x, y);
    }

    @Override
    public String toProcessingCode() {
        return String.format("point(%d,%d);", x, y);
    }
}
