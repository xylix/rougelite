package xylixlike.utils;

import javafx.scene.input.KeyCode;

/**
 * Miscellaneous parsing utilities.
 */
public class Parsing {
    private Parsing() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Parse String to Integer with default value if String isn't a valid Integer.
     * @param s String to parse
     * @param defaultValue int value to default to
     * @return Parsed Integer or the default
     */
    public static int valueOfWithDefault(String s, int defaultValue) {
        return s.matches("-?\\d+") ? Integer.parseInt(s) : defaultValue;
    }

    public static int toDirection(KeyCode key) {
        switch (key) {
            case DOWN:
                return 0;
            case RIGHT:
                return 90;
            case UP:
                return 180;
            case LEFT:
                return 270;
            default:
                throw new IllegalArgumentException();
        }
    }
}
