package net.frontlinesms.test.ui;

import static thinlet.Thinlet.DEFAULT_ENGLISH_BUNDLE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.frontlinesms.FrontlineSMSConstants;

public class UiTestUtils {
	public static void initThinletI18n() {
		DEFAULT_ENGLISH_BUNDLE = new MostlyKeyReturningMap(
				FrontlineSMSConstants.DATEFORMAT_YMD, /* -> */ "d/M/yyyy");
	}
}

class MostlyKeyReturningMap implements Map<String, String> {
	private final HashMap<String, String> someValues = new HashMap<String, String>();
	
	MostlyKeyReturningMap(String... keyValuePairs) {
		for (int i = 0; i < keyValuePairs.length; i+=2) {
			someValues.put(keyValuePairs[i], keyValuePairs[i+1]);
		}
	}
	
	public void clear() {}
	public boolean containsKey(Object _) { return true; }
	public boolean containsValue(Object _) { return true; }
	@SuppressWarnings("unchecked")
	public Set<java.util.Map.Entry<String, String>> entrySet() { return unsupported(Set.class); }
	public String get(Object key) {
		if(someValues.containsKey(key)) return someValues.get(key);
		else return key.toString();
	}
	public boolean isEmpty() { return false; }
	@SuppressWarnings("unchecked")
	public Set<String> keySet() { return unsupported(Set.class); }
	public String put(String key, String value) { return key; }
	public void putAll(Map<? extends String, ? extends String> arg0) {}
	public String remove(Object arg0) { return unsupported(String.class); }
	public int size() { return unsupported(int.class); }
	@SuppressWarnings("unchecked")
	public Collection<String> values() { return unsupported(Collection.class); }
	private <T> T unsupported(Class<T> returnClass) {
		throw new RuntimeException("This map has everything in it, so we can't really return that.");
	}
}
