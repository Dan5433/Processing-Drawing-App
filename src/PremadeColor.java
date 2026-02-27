package src;

public enum PremadeColor {
    BLACK(0xff000000),
    RED(0xffff0000),
    ORANGE(0xffff8000),
    YELLOW(0xffffff00),
    GREEN(0xff00ff00),
    CYAN(0xff00ffff),
    BLUE(0xff0000ff),
    VIOLET(0xff8000ff),
    MAGENTA(0xffff00ff),
    WHITE(0xffffffff);

    private final int colour;

    PremadeColor(int hex) {
        this.colour = hex;
    }

    public static int getMax() {
        return values().length - 1;
    }

    public int getColor() {
        return colour;
    }
}
