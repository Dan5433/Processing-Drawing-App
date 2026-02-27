package src.Drawable;

import processing.core.PApplet;

public class Ellipse extends Drawable {
    public Ellipse(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.fill(fillColor);
        app.strokeWeight(strokeWeight);
        app.ellipse(startX, startY, mouseX, mouseY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("ellipse(%d,%d,%d,%d);", startX, startY, mouseX, mouseY);
    }
}
