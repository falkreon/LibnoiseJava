package blue.endless.libnoise.generator;

public interface Module {
	/**
	 * Causes the generator to emit a value between -1 and 1 for the given xyz coordinates. For 2d maps, pass x or z
	 * as zero. For time-evolving (1d or 2d) maps, repurpose one axis as time instead of space.
	 */
	public double getValue(double x, double y, double z);
}
