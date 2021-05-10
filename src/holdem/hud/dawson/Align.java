package holdem.hud.dawson;

public class Align {


	public static String[] alignRight(String[] list, int width) {
		String output[] = new String[list.length];
		for (int i = 0; i < list.length; i++) {
			output[i] = alignRight(list[i], width);
			System.out.println(output[i]);
		}
		
		return output;
	}

	public static String[] alignRight(String[] list, int width, char separator) {
		String output[] = new String[list.length];
		for (int i = 0; i < list.length; i++) {
			output[i] = alignRight(list[i], width, separator);
		}
		
		return output;
	}

	public static String alignRight(String word, int width) {
		String output = new String(new char[width - word.length()]).replace("\0", " ");
		output += word;
		return output;
	}
	
	public static String alignRight(String word, int width, char separator) {
		String output = new String(new char[width - word.length()]).replace('\0', separator);
		output += word;
		return output;
	}

	public static int spacesToAlignMiddle(String text, int width) {
		int over = width - text.length();
		return over / 2;
	}

	public static int spacesToAlignMiddle(Text text, int width) {
		int over = width - text.getWidth();
		return over / 2;
	}


}
