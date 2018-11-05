package blue.endless.libnoise.generator;

import static blue.endless.libnoise.ValueNoise.*;

import java.util.Random;

import blue.endless.libnoise.NoiseQuality;

public class Billow implements Generator {
	protected double m_frequency = 1.0;
	protected double m_lacunarity = 2.0;
	protected int m_octaveCount = 6;
	protected NoiseQuality m_noiseQuality = NoiseQuality.STANDARD;
	protected double m_persistence = 0.5;
	protected int m_seed;
	
	public Billow() {
		m_seed = new Random().nextInt();
	}
	
	public Billow(int seed) {
		m_seed = seed;
	}
	
	public Billow setFrequency(double frequency) {
		this.m_frequency = frequency;
		return this;
	}
	
	public Billow setLacunarity(double lacunarity) {
		this.m_lacunarity = lacunarity;
		return this;
	}
	
	public Billow setOctaveCount(int octaves) {
		this.m_octaveCount = octaves;
		return this;
	}
	
	public Billow setNoiseQuality(NoiseQuality quality) {
		this.m_noiseQuality = quality;
		return this;
	}
	
	public Billow setPersistence(double persistence) {
		this.m_persistence = persistence;
		return this;
	}
	
	public Billow setSeed(int seed) {
		this.m_seed = seed;
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		double value = 0.0;
		double signal = 0.0;
		double curPersistence = 1.0;
		double nx, ny, nz;
		int seed;

		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;

		for (int curOctave = 0; curOctave < m_octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as a 32-
			// bit integer so that we can pass them to the coherent-noise functions.
			nx = makeInt32Range(x);
			ny = makeInt32Range(y);
			nz = makeInt32Range(z);

			// Get the coherent-noise value from the input value and add it to the
			// final result.
			seed = (m_seed + curOctave) & 0xffffffff;
			signal = gradientCoherentNoise3D(nx, ny, nz, seed, m_noiseQuality);
			signal = 2.0 * Math.abs(signal) - 1.0;
			value += signal * curPersistence;
			
			// Prepare the next octave.
			x *= m_lacunarity;
			y *= m_lacunarity;
			z *= m_lacunarity;
			curPersistence *= m_persistence;
		}
		value += 0.5;

		return value;
	}
}
