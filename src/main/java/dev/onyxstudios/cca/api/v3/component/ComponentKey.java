package dev.onyxstudios.cca.api.v3.component;

import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/** Forge replacement for a CCA entity component key. */
public final class ComponentKey<T extends Component> {
	private final Function<Entity, T> factory;
	private final Map<Entity, T> values = Collections.synchronizedMap(new IdentityHashMap<>());

	public ComponentKey(Function<Entity, T> factory) {
		this.factory = factory;
	}

	public T get(Entity entity) {
		return values.computeIfAbsent(entity, factory);
	}

	public Optional<T> maybeGet(Entity entity) {
		return Optional.of(get(entity));
	}

	/** Network synchronization is installed by the Forge networking pass. */
	public void sync(Entity entity) {
	}
}
