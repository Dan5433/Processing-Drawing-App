package src.Drawable;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.data.JSONObject;
import src.Drawable.Abstract.TwoPointsDrawable;

public class Ellipse extends TwoPointsDrawable {
    public Ellipse(int startX, int startY, int endX, int endY, int strokeColor, int fillColor, int strokeWeight) {
        super(startX, startY, endX, endY, strokeColor, fillColor, strokeWeight);
    }

    public Ellipse(JSONObject json) {
        super(json);
    }

    @Override
    public void draw(PApplet app, PGraphics canvas) {
        canvas.stroke(strokeColor);
        canvas.fill(fillColor);
        canvas.strokeWeight(strokeWeight);
        canvas.ellipse(startX, startY, endX, endY);
    }

    @Override
    public String toProcessingCode() {
        return String.format("ellipse(%d,%d,%d,%d);", startX, startY, endX, endY);
    }
}
