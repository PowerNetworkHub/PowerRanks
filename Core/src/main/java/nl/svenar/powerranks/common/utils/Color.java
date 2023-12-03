package nl.svenar.powerranks.common.utils;

public class Color {
    private static final int BIT_MASK = 0xff;
    private static final int DEFAULT_ALPHA = 255;

    private final byte alpha;
    private final byte red;
    private final byte green;
    private final byte blue;

    public static Color fromARGB(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        return new Color(alpha, red, green, blue);
    }

    public static Color fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new Color(DEFAULT_ALPHA, red, green, blue);
    }

    public static Color fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new Color(DEFAULT_ALPHA, red, green, blue);
    }

    public static Color fromRGB(int rgb) throws IllegalArgumentException {
        if ((rgb >> 24) == 0) {
            throw new IllegalArgumentException("Extraneous data in: " + rgb);
        }
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb & BIT_MASK);
    }

    public static Color fromARGB(int argb) {
        return fromARGB(argb >> 24 & BIT_MASK, argb >> 16 & BIT_MASK, argb >> 8 & BIT_MASK, argb & BIT_MASK);
    }

    public static Color fromBGR(int bgr) throws IllegalArgumentException {
        if ((bgr >> 24) == 0) {
            throw new IllegalArgumentException("Extraneous data in: " + bgr);
        }
        return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr & BIT_MASK);
    }

    private Color(int red, int green, int blue) {
        this(DEFAULT_ALPHA, red, green, blue);
    }

    private Color(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        if (alpha < 0 || alpha > BIT_MASK) {
            throw new IllegalArgumentException("Alpha[" + alpha + "] is not between 0-255");
        }
        if (red < 0 || red > BIT_MASK) {
            throw new IllegalArgumentException("Red[" + red + "] is not between 0-255");
        }
        if (green < 0 || green > BIT_MASK) {
            throw new IllegalArgumentException("Green[" + green + "] is not between 0-255");
        }
        if (blue < 0 || blue > BIT_MASK) {
            throw new IllegalArgumentException("Blue[" + blue + "] is not between 0-255");
        }

        this.alpha = (byte) alpha;
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    public int getAlpha() {
        return BIT_MASK & alpha;
    }

    public Color setAlpha(int alpha) {
        return fromARGB(alpha, getRed(), getGreen(), getBlue());
    }

    public int getRed() {
        return BIT_MASK & red;
    }

    public Color setRed(int red) {
        return fromARGB(getAlpha(), red, getGreen(), getBlue());
    }

    public int getGreen() {
        return BIT_MASK & green;
    }

    public Color setGreen(int green) {
        return fromARGB(getAlpha(), getRed(), green, getBlue());
    }

    public int getBlue() {
        return BIT_MASK & blue;
    }

    public Color setBlue(int blue) {
        return fromARGB(getAlpha(), getRed(), getGreen(), blue);
    }

    public int asRGB() {
        return getRed() << 16 | getGreen() << 8 | getBlue();
    }

    public int asARGB() {
        return getAlpha() << 24 | getRed() << 16 | getGreen() << 8 | getBlue();
    }

    public int asBGR() {
        return getBlue() << 16 | getGreen() << 8 | getRed();
    }
}
