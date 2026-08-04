package dev.emi.trinkets.api.client;

import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/**
 * Registers Bewitchment's worn-accessory renderers with Curios.
 *
 * <p>Trinkets passes the renderer the wearer's model directly, while Curios
 * passes the feature renderer that owns that model, so the adapter unwraps it
 * and forwards everything else unchanged.
 */
public final class TrinketRendererRegistry {
    private TrinketRendererRegistry() {
    }

    public static void registerRenderer(Item item, TrinketRenderer renderer) {
        CuriosRendererRegistry.register(item, () -> new Adapter(renderer));
    }

    private record Adapter(TrinketRenderer renderer) implements ICurioRenderer {
        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, MatrixStack matrices, FeatureRendererContext<T, M> context, VertexConsumerProvider vertexConsumers, int light, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            renderer.render(stack, new SlotReference(slotContext), context.getModel(), matrices, vertexConsumers, light, slotContext.entity(), limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }
}
