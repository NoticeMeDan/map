
package com.noticemedan.map.viewmodel;

import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TextSearch {
	private RadixTree<Tuple2> radixTree;

	public <T> TextSearch(Map<String, T> searchMap) {
		this.radixTree = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());
		searchMap.forEach((k, v) -> this.radixTree.put(k.toLowerCase(), Tuple.of(k, v)));
	}

	public <T> List<Tuple2<String, T>> search(String search) {
		return List.ofAll(this.radixTree.getValuesForClosestKeys(search.toLowerCase()))
				.map(kv -> (Tuple2<String, T>) kv);
	}
}
