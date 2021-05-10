package holdem.utils.dawson;

import holdem.graphic.dawson.Drawable;

public class ArrayHelper {
    public static int getSizeOfLargestString(String[] arr) {
        int largest = 0;
        for (String s : arr) {
            if (s.length() > largest) {
                largest = s.length();
            }
        }
        return largest;
    }

    public static int getWidthOfLargestDrawable(Drawable[] element) {
        int largest = 0;
        for (Drawable e : element) {
            if (e.getWidth() > largest) {
                largest = e.getWidth();
            }
        }
        return largest;
    }
}
