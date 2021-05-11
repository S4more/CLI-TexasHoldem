package holdem.hud.dawson;

public class Align {

	/**
	 * Align a given array to the right of a given width and uses a separator to fill the missing space.
     * @return an array with the aligned strings.
	 */
	public static String[] alignRight(String[] list, int width, char separator) {
		String output[] = new String[list.length];
		for (int i = 0; i < list.length; i++) {
			output[i] = alignRight(list[i], width, separator);
		}
		
		return output;
	}

	/**
	 * Aligns a string to the right of a given width and uses a separator to fill the missing space.
	 * @return An aligned string.
	 */
	public static String alignRight(String word, int width, char separator) {
		String output = new String(new char[width - word.length()]).replace('\0', separator);
		output += word;
		return output;
	}

	/**
	 *  Calculates the number of missing spaces to align an object to the middle of the width.
	 * @return the missing spaces.
	 */
	public static int spacesToAlignMiddle(Text text, int width) {
		int over = width - text.getWidth();
		return over / 2;
	}


}
