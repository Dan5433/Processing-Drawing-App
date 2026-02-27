package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

public abstract class Drawable {
    int startX;
    int startY;

    int mouseX;
    int mouseY;

    int strokeColor;
    int fillColor;
    int strokeWeight;

    abstract public void draw(PApplet app);

    public abstract String toProcessingCode();

    public Drawable(int startX, int startY, int mouseX, int mouseY, int strokeColor, int fillColor, int strokeWeight) {
        this.startX = startX;
        this.startY = startY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.strokeWeight = strokeWeight;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        //TODO: implement json saving
//        json.setString("type", class.getName());
//        json.setInt("x1", x1);
//        json.setInt("y1", y1);
//        json.setInt("x2", x2);
//        json.setInt("y2", y2);
//        json.setInt("fill", fillColor);
//        json.setInt("stroke", strokeColor);
//        json.setInt("weight", strokeWeight);
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
