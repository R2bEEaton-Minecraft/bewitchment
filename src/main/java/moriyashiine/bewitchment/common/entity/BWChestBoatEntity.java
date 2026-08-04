/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** The chest-carrying counterpart to {@link BWBoatEntity}. */
public class BWChestBoatEntity extends ChestBoatEntity {
	public BWChestBoatEntity(EntityType<? extends BoatEntity> type, World world) {
		super(type, world);
	}

	public BWChestBoatEntity(EntityType<? extends BoatEntity> type, World world, double x, double y, double z) {
		this(type, world);
		setPosition(x, y, z);
		prevX = x;
		prevY = y;
		prevZ = z;
	}

	@Override
	public Item asItem() {
		return BWBoatEntity.getBoatItem(this);
	}

	@Nullable
	@Override
	public ItemEntity dropItem(ItemConvertible item) {
		return super.dropItem(BWBoatEntity.substitutePlanks(this, item));
	}
}
