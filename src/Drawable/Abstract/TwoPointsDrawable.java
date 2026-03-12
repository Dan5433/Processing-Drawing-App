package src.Drawable.Abstract;

import processing.data.JSONObject;

public abstract class TwoPointsDrawable extends Drawable {
    protected int startX;
    protected int startY;

    protected int endX;
    protected int endY;

    public TwoPointsDrawable(int startX, int startY, int endX, int endY, int fillColor, int strokeColor, int strokeWeight) {
        super(fillColor, strokeColor, strokeWeight);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public TwoPointsDrawable(JSONObject json) {
        super(json);
        this.startX = json.getInt("startX");
        this.startY = json.getInt("startY");
        this.endX = json.getInt("endX");
        this.endY = json.getInt("endY");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.setInt("startX", startX);
        json.setInt("startY", startY);
        json.setInt("endX", endX);
        json.setInt("endY", endY);
        return json;
    }
}
