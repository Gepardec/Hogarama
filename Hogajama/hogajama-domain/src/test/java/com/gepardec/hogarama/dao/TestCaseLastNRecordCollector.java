package com.gepardec.hogarama.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCaseLastNRecordCollector {

	private static final int MAX_NUM = 10;
	private LastNRecordCollector<String> collector = new LastNRecordCollector<>((MAX_NUM / 2));
	private List<String> list;

	@BeforeEach
	public void setUp() {
		list = new ArrayList<>();
		for (int i = 0; i < MAX_NUM; i++) {
			list.add("val_" + i);
		}
	}

	@Test
	public void testLastNRecord() {
		List<String> collectList = list.stream().collect(collector);
		assertEquals((MAX_NUM / 2), collectList.size());
		assertTrue(collectList.get(collectList.size() - 1).equals(list.get(list.size() - 1)));
		assertTrue(collectList.get(0).equals(list.get((list.size() / 2))));
	}

}
