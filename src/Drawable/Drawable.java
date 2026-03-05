package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

public abstract class Drawable {
    protected int fillColor;
    protected int strokeColor;
    protected int strokeWeight;

    protected Drawable(int fillColor, int strokeColor, int strokeWeight) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWeight = strokeWeight;
    }

    public abstract void draw(PApplet app);

    public abstract String toProcessingCode();

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.setString("type", this.getClass().getSimpleName());
        return json;
    }

    public int getFillColor() {
        return fillColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getStrokeWeight() {
        return strokeWeight;
    }
}
