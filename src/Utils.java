package src;

import java.io.PrintWriter;

public final class Utils {
    static int clamp(int min, int max, int value) {
        if (value < min)
            value = min;

        if (value > max)
            value = max;

        return value;
    }

    static <T extends Enum<T>> T incrementEnum(T[] values, int currentIndex) {
        int newIndex = currentIndex + 1;

        if (newIndex > values.length - 1) {
            newIndex = 0;
        }

        return values[newIndex];
    }

    static void writeFillColor(PrintWriter writer, int color) {
        writer.printf("fill(%d);", color);
    }

    static void writeStrokeColor(PrintWriter writer, int color) {
        writer.printf("stroke(%d);", color);
    }

    static void writeStrokeWeight(PrintWriter writer, int weight) {
        writer.printf("fill(%d);", weight);
    }
}
