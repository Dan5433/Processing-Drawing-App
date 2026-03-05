package src.Drawable;

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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("startX", startX);
        json.put("startY", startY);
        json.put("endX", endX);
        json.put("endY", endY);
        return json;
    }
}
