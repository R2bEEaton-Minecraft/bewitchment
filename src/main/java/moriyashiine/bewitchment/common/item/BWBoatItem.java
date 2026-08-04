/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.entity.BWBoatEntity;
import moriyashiine.bewitchment.common.entity.BWChestBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Places a Bewitchment boat.
 *
 * <p>This mirrors {@link BoatItem#use}, which cannot be reused directly because
 * it hardcodes the vanilla boat entities and a {@link BoatEntity.Type}.  The
 * superclass is still {@code BoatItem} so that dispenser behaviour and other
 * {@code instanceof} checks continue to recognise these as boats; the type
 * passed to it is never read.
 */
public class BWBoatItem extends BoatItem {
	private static final Predicate<Entity> RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit);

	private final Supplier<EntityType<? extends BoatEntity>> entityType;
	private final boolean chest;

	public BWBoatItem(boolean chest, Supplier<EntityType<? extends BoatEntity>> entityType, Settings settings) {
		super(chest, BoatEntity.Type.OAK, settings);
		this.entityType = entityType;
		this.chest = chest;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(stack);
		}
		Vec3d rotation = user.getRotationVec(1);
		List<Entity> riders = world.getOtherEntities(user, user.getBoundingBox().stretch(rotation.multiply(5)).expand(1), RIDERS);
		if (!riders.isEmpty()) {
			Vec3d eyePos = user.getEyePos();
			for (Entity entity : riders) {
				Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
				if (box.contains(eyePos)) {
					return TypedActionResult.pass(stack);
				}
			}
		}
		if (hitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(stack);
		}
		Vec3d pos = hitResult.getPos();
		BoatEntity boat = chest ? new BWChestBoatEntity(entityType.get(), world, pos.x, pos.y, pos.z) : new BWBoatEntity(entityType.get(), world, pos.x, pos.y, pos.z);
		boat.setYaw(user.getYaw());
		if (!world.isSpaceEmpty(boat, boat.getBoundingBox())) {
			return TypedActionResult.fail(stack);
		}
		if (!world.isClient) {
			world.spawnEntity(boat);
			world.emitGameEvent(user, GameEvent.ENTITY_PLACE, pos);
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		return TypedActionResult.success(stack, world.isClient());
	}
}
