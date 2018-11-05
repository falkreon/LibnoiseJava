package blue.endless.libnoise.modifier;

import blue.endless.libnoise.generator.Module;

public abstract class AbstractModifierModule<E extends AbstractModifierModule<?>> implements ModifierModule {

	protected Module[] sources;

	@SuppressWarnings("unchecked")
	@Override
	public E setSources(Module... sources) {
		this.sources = sources;
		return (E)this;
	}

}
