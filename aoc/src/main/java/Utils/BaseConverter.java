package Utils;

public class BaseConverter {

    public static String toCustomBase(long number) {
        return new String(Character.toChars(0x1000 + (int) number));
    }

    public static long fromCustomBase(char baseInput) {
        return (long) baseInput - 0x1000;
    }
}
