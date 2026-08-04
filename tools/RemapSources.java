import java.nio.file.Path;
import java.nio.file.Files;
import net.fabricmc.lorenztiny.TinyMappingsReader;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.mercury.Mercury;
import org.cadixdev.mercury.remapper.MercuryRemapper;

/**
 * Generates a Forge/SRG-name source copy; it never changes the input tree.
 *
 * <p>Compile this helper with Mercury, Lorenz Tiny and Mapping IO on its classpath,
 * then set {@code REMAPPER_CLASSPATH} to the Loom named Minecraft jar. For 1.20.1
 * use {@code mappings-srg.tiny}, target namespace {@code srg}, the source root and
 * an ignored output directory.</p>
 */
public final class RemapSources {
	public static void main(String[] args) throws Exception {
		if (args.length != 4) throw new IllegalArgumentException("usage: <mappings.tiny> <target-namespace> <input> <output>");
		MemoryMappingTree tree = new MemoryMappingTree();
		Path input = Path.of(args[2]).toAbsolutePath();
		Path output = Path.of(args[3]).toAbsolutePath();
		MappingReader.read(Path.of(args[0]).toAbsolutePath(), tree);
		MappingSet mappings;
		try (TinyMappingsReader reader = new TinyMappingsReader(tree, "named", args[1])) {
			mappings = reader.read(MappingSet.create());
		}
		Mercury mercury = new Mercury();
		mercury.setSourceCompatibilityFromRelease(17);
		mercury.setGracefulClasspathChecks(true);
		mercury.setGracefulJavadocClasspathChecks(true);
		String classpath = System.getenv("REMAPPER_CLASSPATH");
		if (classpath != null && !classpath.isBlank()) {
			for (String entry : classpath.split(java.util.regex.Pattern.quote(System.getProperty("path.separator")))) {
				if (!entry.isBlank()) mercury.getClassPath().add(Path.of(entry));
			}
		}
		if (java.nio.file.Files.isDirectory(input)) mercury.getSourcePath().add(input);
		mercury.getProcessors().add(MercuryRemapper.create(mappings));
		mercury.rewrite(input, output);
		try (var paths = Files.walk(output)) {
			paths.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
				try {
					String source = Files.readString(path);
					source = source.replaceAll("(?m)^import net\\.minecraft\\.[^;]+\\.\\*;\\R", "");
					Files.writeString(path, source);
				} catch (java.io.IOException exception) {
					throw new java.io.UncheckedIOException(exception);
				}
			});
		}
	}
}
