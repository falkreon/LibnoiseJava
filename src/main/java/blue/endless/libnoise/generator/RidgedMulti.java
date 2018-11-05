package blue.endless.libnoise.generator;

import blue.endless.libnoise.NoiseQuality;

import static blue.endless.libnoise.ValueNoise.*;

import java.util.Random;

public class RidgedMulti implements Module {
	public static final int MAX_OCTAVE = 30;
	
	protected double m_frequency = 1.0;
	protected double m_lacunarity = 2.0;
	protected NoiseQuality m_noiseQuality = NoiseQuality.STANDARD;
	protected int m_octaveCount = 6;
	protected int m_seed = 0;
	protected double[] m_pSpectralWeights = new double[30];
	
	public RidgedMulti() {
		m_seed = new Random().nextInt();
		calcSpectralWeights ();
	}
	
	public RidgedMulti(int seed) {
		m_seed = seed;
		calcSpectralWeights();
	}

	// Calculates the spectral weights for each octave.
	private void calcSpectralWeights () {
		// This exponent parameter should be user-defined; it may be exposed in a
		// future version of libnoise.
		double h = 1.0;
		
		double frequency = 1.0;
		for (int i = 0; i < MAX_OCTAVE; i++) {
			// Compute weight for each frequency.
			m_pSpectralWeights[i] = Math.pow(frequency, -h);
			frequency *= m_lacunarity;
		}
	}
	
	// Multifractal code originally written by F. Kenton "Doc Mojo" Musgrave,
	// 1998.  Modified by jas for use with libnoise.
	@Override
	public double getValue (double x, double y, double z) {
		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;

		double signal = 0.0;
		double value  = 0.0;
		double weight = 1.0;

		// These parameters should be user-defined; they may be exposed in a
		// future version of libnoise.
		double offset = 1.0;
		double gain = 2.0;

		for (int curOctave = 0; curOctave < m_octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as a 32-
			// bit integer so that we can pass them to the coherent-noise functions.
			double nx, ny, nz;
			nx = makeInt32Range (x);
			ny = makeInt32Range (y);
			nz = makeInt32Range (z);

			// Get the coherent-noise value.
			int seed = (m_seed + curOctave) & 0x7fffffff;
			signal = gradientCoherentNoise3D (nx, ny, nz, seed, m_noiseQuality);

			// Make the ridges.
			signal = Math.abs(signal);
			signal = offset - signal;

			// Square the signal to increase the sharpness of the ridges.
			signal *= signal;

			// The weighting from the previous octave is applied to the signal.
			// Larger values have higher weights, producing sharp points along the
			// ridges.
			signal *= weight;

			// Weight successive contributions by the previous signal.
			weight = signal * gain;
			if (weight > 1.0) {
				weight = 1.0;
			}
			if (weight < 0.0) {
				weight = 0.0;
			}

			// Add the signal to the output value.
			value += (signal * m_pSpectralWeights[curOctave]);

			// Go to the next octave.
			x *= m_lacunarity;
			y *= m_lacunarity;
			z *= m_lacunarity;
		}

		return (value * 1.25) - 1.0;
	}
}
