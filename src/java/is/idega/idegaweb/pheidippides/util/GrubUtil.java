package is.idega.idegaweb.pheidippides.util;

import java.util.StringTokenizer;

public class GrubUtil {

	public static String formatYears(String string) {
		if (string != null && string.length() > 0) {
			string = string.substring(1, string.length() - 1);
			
			StringBuilder builder = new StringBuilder();
			StringTokenizer tokens = new StringTokenizer(string, ",");
			while (tokens.hasMoreTokens()) {
				builder.append(tokens.nextToken());
				if (tokens.hasMoreTokens()) {
					builder.append(", ");
				}
			}
			
			string = builder.toString();
		}
		
		return string;
	}
}