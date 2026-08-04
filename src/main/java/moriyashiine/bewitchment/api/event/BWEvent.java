/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import java.lang.reflect.Array;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/** Loader-neutral callback event used by Bewitchment's public API. */
public final class BWEvent<T> {
	private final Class<T> listenerType;
	private final Function<T[], T> invokerFactory;
	private final CopyOnWriteArrayList<T> listeners = new CopyOnWriteArrayList<>();
	private volatile T invoker;

	private BWEvent(Class<T> listenerType, Function<T[], T> invokerFactory) {
		this.listenerType = listenerType;
		this.invokerFactory = invokerFactory;
		rebuildInvoker();
	}

	public static <T> BWEvent<T> create(Class<T> listenerType, Function<T[], T> invokerFactory) {
		return new BWEvent<>(listenerType, invokerFactory);
	}

	public void register(T listener) {
		listeners.add(listener);
		rebuildInvoker();
	}

	public T invoker() {
		return invoker;
	}

	@SuppressWarnings("unchecked")
	private void rebuildInvoker() {
		T[] snapshot = listeners.toArray((T[]) Array.newInstance(listenerType, listeners.size()));
		invoker = invokerFactory.apply(snapshot);
	}
}
