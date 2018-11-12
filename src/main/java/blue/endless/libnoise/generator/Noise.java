package blue.endless.libnoise.generator;

import java.util.Random;

import blue.endless.libnoise.Module;
import blue.endless.libnoise.NoiseQuality;
import blue.endless.libnoise.ValueNoise;

public class Noise implements Module {
	protected int seed = 1;
	protected NoiseQuality noiseQuality = NoiseQuality.STANDARD;
	
	public Noise() {
		seed = new Random().nextInt();
	}
	
	public Noise setQuality(NoiseQuality q) {
		this.noiseQuality = q;
		return this;
	}
	
	public Noise setSeed(int seed) {
		this.seed = seed;
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		return ValueNoise.valueCoherentNoise3D(x, y, z, seed, noiseQuality);
	}

}
