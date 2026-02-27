package src.Drawable;

import processing.core.PApplet;

public class Triangle extends Drawable {
    public Triangle(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.fill(fillColor);
        app.strokeWeight(strokeWeight);
        app.triangle(startX, mouseY, startX + (mouseX - startX) / 2f, startY, mouseX, mouseY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("triangle(%d,%d,%f,%d,%d,%d);", startX, mouseY, startX + (mouseX - startX) / 2f, startY, mouseX, mouseY);
    }
}
