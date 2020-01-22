package function;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import stream.Stream;

public class Predicates {

	private Predicates() {
		super();
	}

	public static <T> Predicate<T> distinct() {
		return new Predicate<T>() {
			final Set<T> keys = new HashSet<>();

			@Override
			public boolean test(final T t) {
				return keys.add(t);
			}
		};
	}

	public static void main(final String[] args) {
		List<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(3);
		list.add(5);
		list.add(7);
		list.add(3);
		list.add(11);
		list.add(3);
		list.add(13);
		System.out.println(Stream.of(list));
		System.out.println(Stream.of(list).distinct());

	}
}
