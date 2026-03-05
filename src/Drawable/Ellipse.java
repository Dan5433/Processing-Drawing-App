package src.Drawable;

import processing.core.PApplet;

public class Ellipse extends TwoPointsDrawable {
    public Ellipse(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.fill(fillColor);
        app.strokeWeight(strokeWeight);
        app.ellipse(startX, startY, endX, endY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("ellipse(%d,%d,%d,%d);", startX, startY, endX, endY);
    }
}
