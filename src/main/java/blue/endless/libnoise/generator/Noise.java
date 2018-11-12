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
