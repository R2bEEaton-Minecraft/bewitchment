package dev.onyxstudios.cca.api.v3.component;

import moriyashiine.bewitchment.forge.component.BWEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Identifies one component and the entities it attaches to.
 *
 * <p>This mirrors the Cardinal Components key API that Bewitchment is written
 * against.  The key only describes the component; the values live on the entity
 * itself in a Forge capability, so they persist across saves, tick, and
 * synchronize to clients the way they do on Fabric.
 */
public final class ComponentKey<T extends Component> {
	private static final List<ComponentKey<?>> REGISTRY = new ArrayList<>();

	private final Identifier id;
	private final Function<Entity, T> factory;
	private final RespawnCopyStrategy respawnCopyStrategy;
	private final List<Class<? extends Entity>> targets;

	@SafeVarargs
	public static <T extends Component> ComponentKey<T> register(Identifier id, RespawnCopyStrategy respawnCopyStrategy, Function<Entity, T> factory, Class<? extends Entity>... targets) {
		ComponentKey<T> key = new ComponentKey<>(id, respawnCopyStrategy, factory, List.of(targets));
		REGISTRY.add(key);
		return key;
	}

	private ComponentKey(Identifier id, RespawnCopyStrategy respawnCopyStrategy, Function<Entity, T> factory, List<Class<? extends Entity>> targets) {
		this.id = id;
		this.factory = factory;
		this.respawnCopyStrategy = respawnCopyStrategy;
		this.targets = targets;
	}

	/** Every registered key, in registration order. */
	public static List<ComponentKey<?>> all() {
		return REGISTRY;
	}

	public Identifier getId() {
		return id;
	}

	public RespawnCopyStrategy getRespawnCopyStrategy() {
		return respawnCopyStrategy;
	}

	public boolean appliesTo(Entity entity) {
		for (Class<? extends Entity> target : targets) {
			if (target.isInstance(entity)) {
				return true;
			}
		}
		return false;
	}

	public T create(Entity entity) {
		return factory.apply(entity);
	}

	/**
	 * {@return this entity's component}
	 *
	 * @throws NoSuchElementException if this component does not apply to the entity
	 */
	public T get(Entity entity) {
		T component = BWEntityComponents.get(entity, this);
		if (component == null) {
			throw new NoSuchElementException(entity + " has no " + id + " component");
		}
		return component;
	}

	public Optional<T> maybeGet(Entity entity) {
		return Optional.ofNullable(BWEntityComponents.get(entity, this));
	}

	/** Sends this component to every client tracking the entity. */
	public void sync(Entity entity) {
		BWEntityComponents.sync(entity, this);
	}

	/** Moves this component's contents between two entities, used on respawn. */
	public void copy(Entity from, Entity to) {
		T source = BWEntityComponents.get(from, this);
		T target = BWEntityComponents.get(to, this);
		if (source != null && target != null) {
			NbtCompound nbt = new NbtCompound();
			source.writeToNbt(nbt);
			target.readFromNbt(nbt);
		}
	}
}
