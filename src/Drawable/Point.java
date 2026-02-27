package src.Drawable;

import processing.core.PApplet;

public class Point extends Drawable {
    public Point(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.strokeWeight(strokeWeight);
        app.point(mouseX, mouseY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("point(%d,%d);", mouseX, mouseY);
    }
}
