package src.Drawable;

import processing.core.PApplet;

public class Line extends TwoPointsDrawable {
    public Line(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.strokeWeight(strokeWeight);
        app.line(startX, startY, endX, endY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("line(%d,%d,%d,%d);", startX, startY, endX, endY);
    }
}
