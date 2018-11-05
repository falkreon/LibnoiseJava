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

package blue.endless.libnoise;

import java.util.Random;

public class Perlin {
	
	protected double m_frequency = 1.0;
	protected double m_lacunarity = 2.0;
	protected NoiseQuality m_noiseQuality = NoiseQuality.STANDARD;
	protected int m_octaveCount = 6;
	double m_persistence = 0.5;
	int m_seed;
	
	public Perlin() {
		this.m_seed = new Random().nextInt();
	}
	
	public Perlin(int seed) {
		this.m_seed = seed;
	}
	
	public Perlin setFrequency(double frequency) {
		this.m_frequency = frequency;
		return this;
	}
	
	public Perlin setLacunarity(double lacunarity) {
		this.m_lacunarity = lacunarity;
		return this;
	}
	
	public Perlin setNoiseQuality(NoiseQuality quality) {
		this.m_noiseQuality = quality;
		return this;
	}
	
	public Perlin setOctaveCount(int octaves) {
		this.m_octaveCount = octaves;
		return this;
	}
	
	public Perlin setPersistence(double persistence) {
		this.m_persistence = persistence;
		return this;
	}

	public double getValue (double x, double y, double z) {
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
			nx = MakeInt32Range (x);
			ny = MakeInt32Range (y);
			nz = MakeInt32Range (z);

			// Get the coherent-noise value from the input value and add it to the
			// final result.
			seed = (m_seed + curOctave) & 0xffffffff;
			signal = ValueNoise.gradientCoherentNoise3D(nx, ny, nz, seed, m_noiseQuality);
			value += signal * curPersistence;

			// Prepare the next octave.
			x *= m_lacunarity;
			y *= m_lacunarity;
			z *= m_lacunarity;
			curPersistence *= m_persistence;
		}

		return value;
	}
	
	private static double MakeInt32Range (double n) {
		if (n >= 1073741824.0) {
			return (2.0 * (n % 1073741824.0)) - 1073741824.0;
		} else if (n <= -1073741824.0) {
			return (2.0 * (n % 1073741824.0)) + 1073741824.0;
		} else {
			return n;
		}
	}
}
