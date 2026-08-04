package dev.emi.trinkets.api.client;

import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/** Renderer contract retained while Curios provides the accessory inventory. */
public interface TrinketRenderer {
    void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel,
                MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity,
                float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch);

    static void translateToChest(MatrixStack matrices, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity entity) {
        model.body.rotate(matrices);
    }

    static void translateToLeftLeg(MatrixStack matrices, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity entity) {
        model.leftLeg.rotate(matrices);
    }

    static void translateToRightLeg(MatrixStack matrices, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity entity) {
        model.rightLeg.rotate(matrices);
    }
}
