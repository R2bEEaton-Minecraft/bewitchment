/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.api.registry.Transformation;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformationStateTest {
	@Test
	void changingAlternateFormInvalidatesDimensionsExactlyOnce() {
		AtomicInteger invalidations = new AtomicInteger();
		TransformationState state = new TransformationState(new Transformation(), invalidations::incrementAndGet);

		state.setAlternateForm(true);
		state.setAlternateForm(true);

		assertTrue(state.isAlternateForm());
		assertEquals(1, invalidations.get());
	}

	@Test
	void changingTransformationInvalidatesDimensionsExactlyOnce() {
		AtomicInteger invalidations = new AtomicInteger();
		Transformation original = new Transformation();
		Transformation replacement = new Transformation();
		TransformationState state = new TransformationState(original, invalidations::incrementAndGet);

		state.setTransformation(replacement);
		state.setTransformation(replacement);

		assertEquals(replacement, state.getTransformation());
		assertEquals(1, invalidations.get());
	}
}
