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
import blue.endless.libnoise.ValueNoise;

public class Voronoi implements Module {
	private static final double SQRT_3 = Math.sqrt(3);
	
	protected int m_seed;
	protected double m_frequency = 32;
	protected double m_displacement = 1;
	protected boolean m_enableDistance = true;
	
	public Voronoi() {
		m_seed = new Random().nextInt();
	}
	
	public Voronoi(int seed) {
		m_seed = seed;
	}
	
	public Voronoi setFrequency(double frequency) {
		this.m_frequency = frequency;
		return this;
	}
	
	public Voronoi setDisplacement(double displacement) {
		this.m_displacement = displacement;
		return this;
	}
	
	public Voronoi setEnableDistance(boolean enable) {
		this.m_enableDistance = enable;
		return this;
	}
	
	public Voronoi setSeed(int seed) {
		m_seed = seed;
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		// This method could be more efficient by caching the seed values. Fix
		// later.

		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;
		
		int xInt = (x > 0.0 ? (int) x : (int) x - 1);
		int yInt = (y > 0.0 ? (int) y : (int) y - 1);
		int zInt = (z > 0.0 ? (int) z : (int) z - 1);

		double minDist = 2147483647.0;
		double xCandidate = 0;
		double yCandidate = 0;
		double zCandidate = 0;
		
		// Inside each unit cube, there is a seed point at a random position. Go
		// through each of the nearby cubes until we find a cube with a seed point
		// that is closest to the specified position.
		for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
				for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

					// Calculate the position and distance to the seed point inside of
					// this unit cube.
					double xPos = xCur + ValueNoise.valueNoise3D(xCur, yCur, zCur, m_seed);
					double yPos = yCur + ValueNoise.valueNoise3D(xCur, yCur, zCur, m_seed + 1);
					double zPos = zCur + ValueNoise.valueNoise3D(xCur, yCur, zCur, m_seed + 2);
					double xDist = xPos - x;
					double yDist = yPos - y;
					double zDist = zPos - z;
					double dist = xDist * xDist + yDist * yDist + zDist * zDist;

					if (dist < minDist) {
						// This seed point is closer to any others found so far, so record this seed point.
						minDist = dist;
						xCandidate = xPos;
						yCandidate = yPos;
						zCandidate = zPos;
					}
				}
			}
		}
		
		double value;
		if (m_enableDistance) {
			// Determine the distance to the nearest seed point.
			double xDist = xCandidate - x;
			double yDist = yCandidate - y;
			double zDist = zCandidate - z;
			value = (Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist)) * SQRT_3 - 1.0;
		} else {
			value = 0.0;
		}

		// Return the calculated distance with the displacement value applied.
		return value + (m_displacement * (double) ValueNoise.valueNoise3D((int) xCandidate, (int) yCandidate, (int) zCandidate, m_seed));
	}
}
