package src;

import processing.core.PConstants;
import src.Drawable.Drawable;

import java.util.function.Consumer;
import java.util.function.Function;

public enum Tool {
    POINT(
            main -> main.point(main.mouseX - main.PREVIEW_SIZE / 2f, main.mouseY - main.PREVIEW_SIZE / 2f),
            main -> main.point(main.mouseX, main.mouseY),
            main -> new src.Drawable.Point(main.mouseX, main.mouseY,
                    main.getStrokeColor(), main.getStrokeWeight())),

    LINE(
            main ->
                    main.line(main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight(),
                            main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight(),
                            main.mouseX, main.mouseY),
            main -> main.line(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY),
            main -> new src.Drawable.Line(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY,
                    main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight())),
    RECT(
            main ->
                    main.rect(main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight(),
                            main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight(),
                            main.mouseX, main.mouseY),
            main -> main.rect(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY),
            main -> new src.Drawable.Rectangle(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY,
                    main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight())),
    ELLIPSE(
            main -> main.ellipse(main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight(),
                    main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight(), main.mouseX, main.mouseY),
            main -> main.ellipse(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY),
            main -> new src.Drawable.Ellipse(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY,
                    main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight())),
    TRIANGLE(
            main ->
                    main.triangle(main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight() * 2, main.mouseY,
                            main.mouseX - main.PREVIEW_SIZE / 2f - main.getStrokeWeight(),
                            main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight() * 2,
                            main.mouseX, main.mouseY),
            main ->
                    main.triangle(main.getStartX(), main.mouseY,
                            main.getStartX() + (main.mouseX - main.getStartX()) / 2f, main.getStartY(),
                            main.mouseX, main.mouseY),
            main -> new src.Drawable.Triangle(main.getStartX(), main.getStartY(), main.mouseX, main.mouseY,
                    main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight())),
    POLYGON(
            main -> {
                int startX = main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight();
                int startY = main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight();
                float half = (float) (main.PREVIEW_SIZE + main.getStrokeWeight()) / 2;
                main.beginShape();
                main.vertex(startX, main.mouseY);
                main.vertex(startX, main.mouseY - half);
                main.vertex(startX + half, startY);
                main.vertex(main.mouseX, main.mouseY - half);
                main.vertex(main.mouseX, main.mouseY);
                main.endShape(PConstants.CLOSE);
            },
            _ -> {
            },
            main -> new src.Drawable.Polygon(main.getPolygonVerticesArray(), main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight())),
    CURVED_POLYGON(
            main -> {
                int startX = main.mouseX - main.PREVIEW_SIZE - main.getStrokeWeight();
                int startY = main.mouseY - main.PREVIEW_SIZE - main.getStrokeWeight();
                float half = (float) (main.PREVIEW_SIZE + main.getStrokeWeight()) / 2;
                main.beginShape();
                main.curveVertex(startX, main.mouseY);
                main.curveVertex(startX, main.mouseY);
                main.curveVertex(startX, main.mouseY - half);
                main.curveVertex(startX + half, startY);
                main.curveVertex(main.mouseX, main.mouseY - half);
                main.curveVertex(main.mouseX, main.mouseY);
                main.curveVertex(main.mouseX, main.mouseY);
                main.endShape(PConstants.CLOSE);
            },
            _ -> {
            },
            main -> new src.Drawable.CurvedPolygon(main.getPolygonVerticesArray(), main.getFillColor(), main.getStrokeColor(), main.getStrokeWeight()));


    private final Consumer<Main> drawPreview;
    private final Consumer<Main> draw;
    private final Function<Main, Drawable> getDrawable;

    Tool(Consumer<Main> drawPreview, Consumer<Main> draw, Function<Main, Drawable> getDrawable) {
        this.drawPreview = drawPreview;
        this.draw = draw;
        this.getDrawable = getDrawable;
    }

    public void drawPreview(Main main) {
        drawPreview.accept(main);
    }

    public void draw(Main main) {
        draw.accept(main);
    }

    public Drawable getDrawable(Main main) {
        return getDrawable.apply(main);
    }
}
