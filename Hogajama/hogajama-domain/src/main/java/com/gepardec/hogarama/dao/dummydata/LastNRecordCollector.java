package com.gepardec.hogarama.dao.dummydata;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class LastNRecordCollector <T> implements Collector<T, Deque<T>, List<T>> {

	private int lastNRecord = 0;

	public LastNRecordCollector(int lastNRecord) {
		this.lastNRecord = lastNRecord;
	}

	@Override
	public Supplier<Deque<T>> supplier() {
		return ArrayDeque::new;
	}

	@Override
	public BiConsumer<Deque<T>, T> accumulator() {
		return (list, t) -> {
			if (list.size() == lastNRecord) {
				list.removeFirst();
			}
			list.add(t);
		};
	}

	@Override
	public BinaryOperator<Deque<T>> combiner() {
		return (sourceList, targetList) -> {
			while (targetList.size() < lastNRecord && !sourceList.isEmpty()) {
				targetList.addFirst(sourceList.removeLast());
			}
			return targetList;
		};
	}

	@Override
	public Function<Deque<T>, List<T>> finisher() {
		return ArrayList::new;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}
}
