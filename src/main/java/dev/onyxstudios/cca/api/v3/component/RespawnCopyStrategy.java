package dev.onyxstudios.cca.api.v3.component;

/** What happens to a component when a player entity is recreated. */
public enum RespawnCopyStrategy {
	/** Carried over through both deaths and dimension changes. */
	ALWAYS_COPY,
	/** Carried over only when the player did not die, such as returning from the End. */
	LOSSLESS_ONLY,
	/** Always starts fresh on the new player. */
	NEVER_COPY;

	public boolean shouldCopy(boolean wasDeath) {
		return switch (this) {
			case ALWAYS_COPY -> true;
			case LOSSLESS_ONLY -> !wasDeath;
			case NEVER_COPY -> false;
		};
	}
}
