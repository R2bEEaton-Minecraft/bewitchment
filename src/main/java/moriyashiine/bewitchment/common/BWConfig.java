/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BWConfig {
	public static List<String> disabledPoppets = new ArrayList<>();
	public static boolean enableCurses = true;
	public static boolean enablePolymorph = true;

	public static int altarDistributionRadius = 24;

	public static boolean generateSalt = true;
	public static boolean generateSilver = true;

	public static int owlWeight = 10;
	public static int owlMinGroupCount = 1;
	public static int owlMaxGroupCount = 2;

	public static int ravenWeight = 10;
	public static int ravenMinGroupCount = 1;
	public static int ravenMaxGroupCount = 3;

	public static int snakeWeight = 6;
	public static int snakeMinGroupCount = 1;
	public static int snakeMaxGroupCount = 2;

	public static int toadWeight = 10;
	public static int toadMinGroupCount = 1;
	public static int toadMaxGroupCount = 3;

	public static int ghostWeight = 20;
	public static int ghostMinGroupCount = 1;
	public static int ghostMaxGroupCount = 1;

	public static int vampireWeight = 20;
	public static int vampireMinGroupCount = 1;
	public static int vampireMaxGroupCount = 1;

	public static int werewolfWeight = 20;
	public static int werewolfMinGroupCount = 1;
	public static int werewolfMaxGroupCount = 1;

	public static int hellhoundWeight = 6;
	public static int hellhoundMinGroupCount = 1;
	public static int hellhoundMaxGroupCount = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger("Bewitchment Config");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private BWConfig() {
	}

	/**
	 * Reads {@code config/bewitchment.json}, writing it with the defaults when it
	 * is absent, then writing it back so new options appear on upgrade.
	 *
	 * <p>MidnightLib provides this on Fabric.  The file name and flat layout are
	 * kept identical to the one it writes, so a config carries across between the
	 * two versions of the mod.  Unknown keys are ignored and malformed values fall
	 * back to the default rather than stopping the game from loading.
	 */
	public static void load(Path configDirectory) {
		Path path = configDirectory.resolve(Bewitchment.MOD_ID + ".json");
		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
				for (Field field : fields()) {
					JsonElement value = json.get(field.getName());
					if (value != null) {
						try {
							field.set(null, GSON.fromJson(value, field.getGenericType()));
						} catch (Exception exception) {
							LOGGER.warn("Ignoring unreadable config option {}, using the default", field.getName(), exception);
						}
					}
				}
			} catch (Exception exception) {
				LOGGER.error("Could not read {}, falling back to defaults", path, exception);
			}
		}
		save(path);
	}

	private static void save(Path path) {
		JsonObject json = new JsonObject();
		try {
			for (Field field : fields()) {
				json.add(field.getName(), GSON.toJsonTree(field.get(null)));
			}
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException | IllegalAccessException exception) {
			LOGGER.error("Could not write {}", path, exception);
		}
	}

	private static List<Field> fields() {
		List<Field> fields = new ArrayList<>();
		for (Field field : BWConfig.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
				fields.add(field);
			}
		}
		return fields;
	}
}
