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

package blue.endless.libnoise.modifier;

import java.util.Random;

import blue.endless.libnoise.generator.Module;
import blue.endless.libnoise.generator.Perlin;

public class Turbulence implements ModifierModule {
	protected double m_power = 1.0;
	protected double m_roughness = 3.0;
	protected int m_seed = 1;
	
	protected Perlin m_xDistortModule = new Perlin();
	protected Perlin m_yDistortModule = new Perlin();
	protected Perlin m_zDistortModule = new Perlin();
	
	protected Module m_sourceModule;
	
	public Turbulence() {
		setSeed(new Random().nextInt());
	}
	
	public Turbulence(int seed) {
		setSeed(seed);
	}
	
	public Turbulence setPower(double power) {
		this.m_power = power;
		return this;
	}
	
	public Turbulence setRoughness(double roughness) {
		this.m_roughness = roughness;
		return this;
	}
	
	public Turbulence setFrequency(double frequency) {
		this.m_xDistortModule.setFrequency(frequency);
		this.m_yDistortModule.setFrequency(frequency);
		this.m_zDistortModule.setFrequency(frequency);
		
		return this;
	}
	
	public Turbulence setSeed(int seed) {
		// Set the seed of each noise::module::Perlin noise modules.  To prevent any
		// sort of weird artifacting, use a slightly different seed for each noise
		// module.
		m_xDistortModule.setSeed(seed    );
		m_yDistortModule.setSeed(seed + 1);
		m_zDistortModule.setSeed(seed + 2);
		
		return this;
	}
	
	@Override
	public Turbulence setSources(Module... generators) {
		m_sourceModule = generators[0];
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		if (m_sourceModule==null) return 0;

		// Get the values from the three noise::module::Perlin noise modules and
		// add each value to each coordinate of the input value.  There are also
		// some offsets added to the coordinates of the input values.  This prevents
		// the distortion modules from returning zero if the (x, y, z) coordinates,
		// when multiplied by the frequency, are near an integer boundary.  This is
		// due to a property of gradient coherent noise, which returns zero at
		// integer boundaries.
		double x0, y0, z0;
		double x1, y1, z1;
		double x2, y2, z2;
		x0 = x + (12414.0 / 65536.0);
		y0 = y + (65124.0 / 65536.0);
		z0 = z + (31337.0 / 65536.0);
		x1 = x + (26519.0 / 65536.0);
		y1 = y + (18128.0 / 65536.0);
		z1 = z + (60493.0 / 65536.0);
		x2 = x + (53820.0 / 65536.0);
		y2 = y + (11213.0 / 65536.0);
		z2 = z + (44845.0 / 65536.0);
		double xDistort = x + (m_xDistortModule.getValue(x0, y0, z0) * m_power);
		double yDistort = y + (m_yDistortModule.getValue(x1, y1, z1) * m_power);
		double zDistort = z + (m_zDistortModule.getValue(x2, y2, z2) * m_power);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return m_sourceModule.getValue(xDistort, yDistort, zDistort);
	}

	

}
