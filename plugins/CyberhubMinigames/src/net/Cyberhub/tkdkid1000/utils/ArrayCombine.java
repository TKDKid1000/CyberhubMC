package net.Cyberhub.tkdkid1000.utils;

public class ArrayCombine {

	public static String combine(String[] array, int start) {
		StringBuilder sb = new StringBuilder();
		for (int x=start; x<array.length; x++) {
			sb.append(array[x]);
			sb.append(" ");
		}
		return sb.toString();
	}
}
