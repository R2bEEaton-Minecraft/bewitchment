/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;

/**
 * Renders a Bewitchment boat.
 *
 * <p>The vanilla renderer picks its texture and model from a map keyed by
 * {@link BoatEntity.Type}, which has no entry for modded woods, so it falls back
 * to oak.  Each of these renderers is built for one wood and always answers with
 * that wood's texture; the inherited render logic is otherwise untouched.
 */
public class BWBoatEntityRenderer extends BoatEntityRenderer {
	private final Pair<Identifier, CompositeEntityModel<BoatEntity>> textureAndModel;

	public BWBoatEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer modelLayer, Identifier texture, boolean chest) {
		super(ctx, chest);
		ModelPart part = ctx.getPart(modelLayer);
		this.textureAndModel = Pair.of(texture, chest ? new ChestBoatEntityModel(part) : new BoatEntityModel(part));
	}

	@Override
	public Pair<Identifier, CompositeEntityModel<BoatEntity>> getModelWithLocation(BoatEntity boat) {
		return textureAndModel;
	}
}
