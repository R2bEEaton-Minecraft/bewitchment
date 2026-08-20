/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.api.registry.Transformation;

final class TransformationState {
	private final Runnable dimensionsChanged;
	private Transformation transformation;
	private boolean alternateForm;

	TransformationState(Transformation transformation, Runnable dimensionsChanged) {
		this.transformation = transformation;
		this.dimensionsChanged = dimensionsChanged;
	}

	/**
	 * Wraps a dimensions refresh so that it runs twice.
	 *
	 * <p>Vanilla computes the new eye height <em>before</em> it stores the new
	 * dimensions, so anything deriving eye height from the entity's current
	 * height still sees the form we are leaving.  Minecraft Comes Alive does
	 * exactly that: with a villager or player model selected it clamps eye
	 * height to the player's height, which pins a vampire returning from bat
	 * form to a bat's camera.  The size is already correct by the second pass,
	 * which costs nothing and recomputes the eye height against it.
	 */
	static Runnable refreshingTwice(Runnable dimensionsChanged) {
		return () -> {
			dimensionsChanged.run();
			dimensionsChanged.run();
		};
	}

	Transformation getTransformation() {
		return transformation;
	}

	void setTransformation(Transformation transformation) {
		if (this.transformation == transformation) {
			return;
		}
		this.transformation = transformation;
		dimensionsChanged.run();
	}

	boolean isAlternateForm() {
		return alternateForm;
	}

	void setAlternateForm(boolean alternateForm) {
		if (this.alternateForm == alternateForm) {
			return;
		}
		this.alternateForm = alternateForm;
		dimensionsChanged.run();
	}
}
