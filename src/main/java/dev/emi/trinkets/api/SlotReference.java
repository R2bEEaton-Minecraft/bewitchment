package dev.emi.trinkets.api;

import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curios.api.SlotContext;

/** Curios-backed replacement for Trinkets' slot reference. */
public record SlotReference(SlotContext context) {
    public LivingEntity entity() {
        return context.entity();
    }
}
