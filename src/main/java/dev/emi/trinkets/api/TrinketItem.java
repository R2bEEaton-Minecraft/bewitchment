package dev.emi.trinkets.api;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

/**
 * Compatibility base class that preserves Bewitchment's Trinkets item hooks
 * while exposing every item as a Curios accessory on Forge.
 */
public class TrinketItem extends Item implements ICurioItem {
    public TrinketItem(Settings settings) {
        super(settings);
    }

    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        return ICurioItem.super.getAttributeModifiers(slot.context(), uuid, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        tick(stack, new SlotReference(slotContext), slotContext.entity());
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return getModifiers(stack, new SlotReference(slotContext), slotContext.entity(), uuid);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NbtCompound nbt) {
        return CuriosApi.createCurioProvider(new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                TrinketItem.this.curioTick(slotContext, stack);
            }

            @Override
            public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
                return TrinketItem.this.getAttributeModifiers(slotContext, uuid, stack);
            }
        });
    }
}
