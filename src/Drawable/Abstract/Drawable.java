package src.Drawable.Abstract;

import processing.core.PApplet;
import processing.data.JSONObject;

public abstract class Drawable {
    public static final String DRAWABLE_TYPE_JSON_KEY = "type";
    protected int fillColor;
    protected int strokeColor;
    protected int strokeWeight;

    protected Drawable(int fillColor, int strokeColor, int strokeWeight) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWeight = strokeWeight;
    }

    public Drawable(JSONObject json) {
        this.fillColor = json.getInt("fillColor");
        this.strokeColor = json.getInt("strokeColor");
        this.strokeWeight = json.getInt("strokeWeight");
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.setString(DRAWABLE_TYPE_JSON_KEY, this.getClass().getSimpleName());
        json.setInt("fillColor", fillColor);
        json.setInt("strokeColor", strokeColor);
        json.setInt("strokeWeight", strokeWeight);
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

    public abstract void draw(PApplet app);

    public abstract String toProcessingCode();
}
