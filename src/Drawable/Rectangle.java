package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

public class Rectangle extends TwoPointsDrawable {
    public Rectangle(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    public Rectangle(JSONObject json) {
        super(json);
    }

    @Override
    public void draw(PApplet app) {
        app.stroke(strokeColor);
        app.fill(fillColor);
        app.strokeWeight(strokeWeight);
        app.rect(startX, startY, endX, endY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("rect(%d,%d,%d,%d);", startX, startY, endX, endY);
    }
}
