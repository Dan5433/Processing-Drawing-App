package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;
import src.Drawable.Abstract.TwoPointsDrawable;

public class Triangle extends TwoPointsDrawable {
    public Triangle(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    public Triangle(JSONObject json) {
        super(json);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.fill(fillColor);
        app.strokeWeight(strokeWeight);
        app.triangle(startX, endY, startX + (endX - startX) / 2f, startY, endX, endY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("triangle(%d,%d,%f,%d,%d,%d);", startX, endY, startX + (endX - startX) / 2f, startY, endX, endY);
    }
}
