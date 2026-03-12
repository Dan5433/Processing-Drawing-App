package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;
import src.Drawable.Abstract.SinglePointDrawable;

public class Point extends SinglePointDrawable {
    public Point(int x, int y, int fillColor, int strokeColor, int strokeWeight) {
        super(x, y, fillColor, strokeColor, strokeWeight);
    }

    public Point(JSONObject json) {
        super(json);
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
