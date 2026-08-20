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
