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

public class Spheres implements Module {
	protected double m_frequency = 1.0;
	
	public Spheres() {}
	
	public Spheres setFrequency(double frequency) {
		m_frequency = frequency;
		return this;
	}
	
	@Override
	public double getValue (double x, double y, double z) {
		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;

		double distFromCenter = Math.sqrt(x * x + y * y + z * z);
		double distFromSmallerSphere = distFromCenter - Math.floor(distFromCenter);
		double distFromLargerSphere = 1.0 - distFromSmallerSphere;
		double nearestDist = Math.min(distFromSmallerSphere, distFromLargerSphere);
		return 1.0 - (nearestDist * 4.0); // Puts it in the -1.0 to +1.0 range.
	}
}
