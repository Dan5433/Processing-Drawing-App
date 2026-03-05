package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

public class Point extends Drawable {
    protected int x, y;

    public Point(int x, int y, int strokeColor, int strokeWeight) {
        super(0, strokeColor, strokeWeight);
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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("x", x);
        json.put("y", y);
        return json;
    }
}
