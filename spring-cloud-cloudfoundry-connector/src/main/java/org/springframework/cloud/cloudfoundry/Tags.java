package org.springframework.cloud.cloudfoundry;

import java.util.List;

public class Tags {
	private String[] values;

	public Tags(String... values) {
		this.values = values;
	}

	public String[] getTags() {
		return values;
	}

	public boolean containsOne(List<String> tags) {
		if (tags != null) {
			for (String value : values) {
				for (String tag : tags) {
					if (tag.equalsIgnoreCase(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean contains(String tag) {
		if (tag != null) {
			for (String value : values) {
				if (tag.equalsIgnoreCase(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean startsWith(String tag) {
		if (tag != null) {
			for (String value : values) {
				if (startsWithIgnoreCase(tag, value)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean startsWithIgnoreCase(String string, String prefix) {
		return string.regionMatches(true, 0, prefix, 0, prefix.length());
	}
}
