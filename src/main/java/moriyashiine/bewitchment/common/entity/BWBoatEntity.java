/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A boat carved from one of Bewitchment's woods.
 *
 * <p>Vanilla derives a boat's item, texture and plank drops from
 * {@link BoatEntity.Type}, which is a closed enum that Forge does not make
 * extensible on 1.20.1.  The wood is therefore carried by the entity type: each
 * boat entity shares its identifier with the item that places it, so
 * {@code bewitchment:juniper_boat} the entity and {@code bewitchment:juniper_boat}
 * the item resolve to one another without a lookup table.
 */
public class BWBoatEntity extends BoatEntity {
	public BWBoatEntity(EntityType<? extends BoatEntity> type, World world) {
		super(type, world);
	}

	public BWBoatEntity(EntityType<? extends BoatEntity> type, World world, double x, double y, double z) {
		this(type, world);
		setPosition(x, y, z);
		prevX = x;
		prevY = y;
		prevZ = z;
	}

	@Override
	public Item asItem() {
		return getBoatItem(this);
	}

	@Nullable
	@Override
	public ItemEntity dropItem(ItemConvertible item) {
		return super.dropItem(substitutePlanks(this, item));
	}

	static Item getBoatItem(BoatEntity boat) {
		Item item = Registries.ITEM.get(Registries.ENTITY_TYPE.getId(boat.getType()));
		return item == Items.AIR ? Items.OAK_BOAT : item;
	}

	/**
	 * Bewitchment boats report the oak variant, because that is the only vanilla
	 * value available.  Vanilla's fall handler drops that variant's planks when a
	 * boat is destroyed by a long fall, so swap them for this wood's planks.
	 */
	static ItemConvertible substitutePlanks(BoatEntity boat, ItemConvertible item) {
		if (item.asItem() != Items.OAK_PLANKS) {
			return item;
		}
		Identifier id = Registries.ENTITY_TYPE.getId(boat.getType());
		String wood = id.getPath().replaceFirst("_(chest_)?boat$", "");
		Item planks = Registries.ITEM.get(new Identifier(id.getNamespace(), wood + "_planks"));
		return planks == Items.AIR ? item : planks;
	}
}
