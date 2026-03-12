package src.Drawable.Abstract;

import processing.data.JSONObject;

public abstract class SinglePointDrawable extends Drawable {
    protected int x;
    protected int y;

    public SinglePointDrawable(int x, int y, int fillColor, int strokeColor, int strokeWeight) {
        super(fillColor, strokeColor, strokeWeight);
        this.x = x;
        this.y = y;
    }

    public SinglePointDrawable(JSONObject json) {
        super(json);
        this.x = json.getInt("x");
        this.y = json.getInt("y");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.setInt("x", x);
        json.setInt("y", y);
        return json;
    }
}
