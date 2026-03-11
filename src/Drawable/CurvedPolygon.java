package src.Drawable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class CurvedPolygon extends Drawable {
    protected PVector[] vertices;

    public CurvedPolygon(PVector[] vertices, int fillColor, int strokeColor, int strokeWeight) {
        super(fillColor, strokeColor, strokeWeight);
        this.vertices = vertices;
    }

    public CurvedPolygon(JSONObject json) {
        super(json);

        JSONArray jsonVertices = json.getJSONArray("vertices");
        vertices = new PVector[jsonVertices.size()];
        for (int i = 0; i < jsonVertices.size(); i++) {
            JSONObject vertex = jsonVertices.getJSONObject(i);
            vertices[i] = new PVector(vertex.getInt("x"), vertex.getInt("y"));
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        JSONArray jsonVertices = new JSONArray();
        for (PVector vertex : vertices) {
            JSONObject jsonVertex = new JSONObject();
            jsonVertex.setInt("x", (int) vertex.x);
            jsonVertex.setInt("y", (int) vertex.y);
            jsonVertices.append(jsonVertex);
        }
        json.setJSONArray("vertices", jsonVertices);
        return json;
    }

    @Override
    public void draw(PApplet app) {
        app.fill(fillColor);
        app.stroke(strokeColor);
        app.strokeWeight(strokeWeight);
        app.beginShape();
        for (PVector vertex : vertices) {
            app.curveVertex(vertex.x, vertex.y);
        }
        app.endShape(PConstants.CLOSE);
    }

    @Override
    public String toProcessingCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("beginShape();\n");
        for (PVector vertex : vertices) {
            sb.append(String.format("curveVertex(%d,%d);\n", vertex.x, vertex.y));
        }
        sb.append("endShape(CLOSE);\n");
        return sb.toString();
    }
}
