package org.springframework.cloud.cloudfoundry;

import java.util.Arrays;
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
				if (tags.contains(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains(String tag) {
		return tag != null && Arrays.asList(values).contains(tag);
	}

	public boolean startsWith(String tag) {
		if (tag != null) {
			for (String value : values) {
				if (tag.startsWith(value)) {
					return true;
				}
			}
		}
		return false;
	}
}
