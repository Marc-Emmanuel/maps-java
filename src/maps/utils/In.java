package maps.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class In {

	public static char readChar() {
		char carac = 'q';
		String x = "";
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		try {
			x = d.readLine();
		} catch (Exception e) {}
		carac = x.charAt(0);
		return (carac);
	}

	public static String readLine() {
		String x = "";
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		try {
			x = d.readLine();
		} catch (Exception e) {}
		return (x);
	}

	public static int readInt() {
		int val = 0;
		Integer y;
		String x = "";
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		try {
			x = d.readLine();
		} catch (Exception e) {}
		y = new Integer(x);
		val = y.intValue();
		return (val);
	}

	public static double readDouble() {
		double val = 0.;
		Double y;
		String x = "";
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		try {
			x = d.readLine();
		} catch (Exception e) {}
		y = new Double(x);
		val = y.doubleValue();
		return (val);
	}
}
