package blue.endless.libnoise.modifier;

import blue.endless.libnoise.generator.Module;

public interface ModifierModule extends Module {
	public ModifierModule setSources(Module... sources);
}
