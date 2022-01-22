package com.gepardec.hogarama.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LastNRecordCollectorTest {

	private static final int                          MAX_NUM   = 10;
	private final        LastNRecordCollector<String> collector = new LastNRecordCollector<>((MAX_NUM / 2));
	private              List<String>                 list;

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
		assertEquals(collectList.get(collectList.size() - 1), list.get(list.size() - 1));
		assertEquals(collectList.get(0), list.get((list.size() / 2)));
	}

}
