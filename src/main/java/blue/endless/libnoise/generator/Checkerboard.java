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

import static blue.endless.libnoise.ValueNoise.makeInt32Range;

import blue.endless.libnoise.Module;

public class Checkerboard implements Module {
	
	@Override
	public double getValue(double x, double y, double z) {
		//Fix aberrant zero-crossing behavior in the dumbest way possible
		if (x<0) x--;
		if (y<0) y--;
		if (z<0) z--;
		
		int ix = (int)(makeInt32Range (x));
		int iy = (int)(makeInt32Range (y));
		int iz = (int)(makeInt32Range (z));
		return ((ix & 1) ^ (iy & 1) ^ (iz & 1))!=0 ? -1.0: 1.0;
	}
	
}
