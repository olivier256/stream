package stream;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import function.BiConsumer;
import function.Consumer;
import function.Function;
import function.IntFunction;
import function.Predicate;
import function.Predicates;
import function.Supplier;

public class Stream<T> {

	private final Iterator<T> iterator;

	private Stream(final Iterator<T> stream) {
		this.iterator = stream;
	}

	public static <T> Stream<T> of(final List<T> list) {
		return new Stream<>(list.iterator());
	}

	public Stream<T> filter(final Predicate<T> p) {
		List<T> out = new LinkedList<>();
		while (iterator.hasNext()) {
			T t = iterator.next();
			if (p.test(t)) {
				out.add(t);
			}
		}
		return new Stream<>(out.iterator());
	}

	public <R> Stream<R> map(final Function<T, R> f) {
		List<R> result = new LinkedList<>();
		while (iterator.hasNext()) {
			T t = iterator.next();
			R r = f.apply(t);
			result.add(r);
		}
		return new Stream<>(result.iterator());
	}

	public Stream<T> distinct() {
		Predicate<T> distinct = Predicates.distinct();
		return filter(distinct);
	}

	public int count() {
		int n = 0;
		while (iterator.hasNext()) {
			n++;
		}
		return n;
	}

	public <R, A> R collect(final Collector<T, A, R> collector) {
		Supplier<A> supplier = collector.supplier();
		A a = supplier.get();
		BiConsumer<A, T> accumulator = collector.accumulator();
		while (iterator.hasNext()) {
			T t = iterator.next();
			accumulator.accept(a, t);
		}
		Function<A, R> finisher = collector.finisher();
		return finisher.apply(a);
	}

	public void forEach(final Consumer<T> consumer) {
		while (iterator.hasNext()) {
			T t = iterator.next();
			consumer.apply(t);
		}
	}

	public T[] toArray(final IntFunction<T[]> f) {
		T[] a = f.apply(count());
		int i = 0;
		while (iterator.hasNext()) {
			T t = iterator.next();
			a[i] = t;
			i++;
		}
		return a;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			T t = iterator.next();
			sb.append(t.toString() + ", ");
		}
		return sb.toString();
	}

	public static void main(final String[] args) {
		List<Integer> list = Arrays.asList(1, 2, 3, 5, 7, 11, 3, 13, 17, 19, 21, 23, 3, 29, 31, 37);

		final Collector<Integer, List<Integer>, List<Integer>> asList = Collectors.toList();
		List<Integer> collectAsList = Stream.of(list).distinct().collect(asList);
		System.out.println(collectAsList);

		Collector<Integer, StringBuilder, String> asString = Collectors.joining();
		String collectAsString = Stream.of(list).distinct().collect(asString);
		System.out.println(collectAsString);

		Stream.of(list).forEach(new Consumer<Integer>() {

			@Override
			public void apply(final Integer t) {
				System.out.println(t);

			}

		});

		Stream.of(list).toArray(new IntFunction<Integer[]>() {

			@Override
			public Integer[] apply(final int value) {
				return new Integer[value];
			}

		});
	}
}
