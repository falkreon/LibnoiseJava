/*
 * Copyright (C) 2018 Falkreon (Isaac Ellingson)
 *
 *     Original libnoise library copyright (C) 2003, 2004 Jason Bevins
 *
 * This file is part of the libnoise-java library.
 *
 * Libnoise-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Libnoise-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with libnoise-java. If not, see <https://www.gnu.org/licenses/>.
 */

package blue.endless.libnoise.generator;

import blue.endless.libnoise.Module;
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
	
	
	public RidgedMulti setFrequency(double frequency) {
		this.m_frequency = frequency;
		return this;
	}
	
	public RidgedMulti setLacunarity(double lacunarity) {
		this.m_lacunarity = lacunarity;
		calcSpectralWeights();
		return this;
	}
	
	public RidgedMulti setNoiseQuality(NoiseQuality quality) {
		this.m_noiseQuality = quality;
		return this;
	}
	
	public RidgedMulti setOctaveCount(int octaves) {
		this.m_octaveCount = octaves;
		return this;
	}
	
	public RidgedMulti setSeed(int seed) {
		this.m_seed = seed;
		return this;
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
