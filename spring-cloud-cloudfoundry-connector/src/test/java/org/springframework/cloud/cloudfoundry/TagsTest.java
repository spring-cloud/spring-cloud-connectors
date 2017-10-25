package org.springframework.cloud.cloudfoundry;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagsTest {

	private final Tags EMPTY_TAGS = new Tags();

	@Test
	public void containsOne() {
		Tags tags = new Tags("test1", "test2");
		assertTrue(tags.containsOne(Arrays.asList("test1", "testx")));
		assertTrue(tags.containsOne(Arrays.asList("testx", "test2")));
		assertTrue(tags.containsOne(Arrays.asList("TESTX", "TEST2")));
		assertFalse(tags.containsOne(Arrays.asList("testx", "testy")));
	}

	@Test
	public void containsOneWithEmptyTags() {
		assertFalse(EMPTY_TAGS.containsOne(Arrays.asList("test")));
	}

	@Test
	public void contains() {
		Tags tags = new Tags("test1", "test2");
		assertTrue(tags.contains("test1"));
		assertTrue(tags.contains("test2"));
		assertTrue(tags.contains("TEST2"));
		assertFalse(tags.contains("testx"));
	}

	@Test
	public void containsWithEmptyTags() {
		assertFalse(EMPTY_TAGS.contains("test"));
	}

	@Test
	public void startsWith() {
		Tags tags = new Tags("test");
		assertTrue(tags.startsWith("test-123"));
		assertTrue(tags.startsWith("TEST-123"));
		assertFalse(tags.startsWith("abcd"));
	}

	@Test
	public void startsWithWithEmptyTags() {
		assertFalse(EMPTY_TAGS.startsWith("test"));
	}
}