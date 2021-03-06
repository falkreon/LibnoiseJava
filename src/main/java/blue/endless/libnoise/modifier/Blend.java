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

import blue.endless.libnoise.Interpolate;

public class Blend extends AbstractModifierModule<Blend> {

	@Override
	public double getValue(double x, double y, double z) {
		if (sources.length<3) return 0;

		double v0 = sources[0].getValue(x, y, z);
		double v1 = sources[1].getValue(x, y, z);
		double alpha = (sources[2].getValue(x, y, z) + 1.0) / 2.0;
		return Interpolate.linear(v0, v1, alpha);
	}
	
}
