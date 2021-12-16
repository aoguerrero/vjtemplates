package io.github.aoguerrero.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.text.CaseUtils;

public final class MintAddonUtils {

	private MintAddonUtils() {
	}

	public static String classNameLowerCase(String className) {
		return className.substring(0, 1).toLowerCase() + className.substring(1);
	}

	public static String toCamelCaseLo(String input) {
		return CaseUtils.toCamelCase(input, false, '_');
	}

	public static String toCamelCaseUp(String input) {
		return CaseUtils.toCamelCase(input, true, '_');
	}

	public static String joinParamNames(ArrayList<Map<String, String>> items) {
		StringJoiner joiner = new StringJoiner(", ");
		items.forEach(item -> joiner.add(toCamelCaseLo(item.get("name"))));
		return joiner.toString();
	}

	public static String joinParams(ArrayList<Map<String, String>> items) {
		StringJoiner joiner = new StringJoiner(", ");
		items.forEach(item -> joiner.add(item.get("type") + " " + toCamelCaseLo(item.get("name"))));
		return joiner.toString();
	}

	public static String javaToMintType(String type) {
		if (type.equals("Boolean") || type.equals("bolean")) {
			return "setYesNo";
		}
		return "setText";
	}

	public static String getGetterPrefix(String type) {
		if (type.equals("boolean")) {
			return "is";
		}
		return "get";

	}

}
