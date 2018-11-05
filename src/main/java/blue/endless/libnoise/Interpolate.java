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

public class Interpolate {
	/** Performs linear interpolation between two values.
	 * 
	 * The alpha value should range from 0.0 to 1.0. If the alpha value is
	 * 0.0, this function returns {@code n0}. If the alpha value is 1.0, this
	 * function returns {@code n1}.
	 *
	 * @param n0 The first value.
	 * @param n1 The second value.
	 * @param a The alpha value.
	 * 
	 * @returns The interpolated value.
	 */
	public static double linear(double n0, double n1, double a) {
		return ((1.0 - a) * n0) + (a * n1);
	}
	
	/** Maps a value onto a cubic S-curve.
	 *
	 * <p>{@code a} should range from 0.0 to 1.0.
	 * 
	 * <p>The derivitive of a cubic S-curve is zero at {@code a} = 0.0 and {@code a} = 1.0
	 * 
	 * @param a The value to map onto a cubic S-curve.
	 * @returns The mapped value.
	 */
	public static double sCurve3 (double a) {
		return (a * a * (3.0 - 2.0 * a));
	}
	
	/** Maps a value onto a quintic S-curve.
	 * 
	 * <p>{@code a} should range from 0.0 to 1.0.
	 * 
	 * <p>The first derivitive of a quintic S-curve is zero at {@code a} = 0.0 and {@code a} = 1.0
	 *
	 * <p>The second derivitive of a quintic S-curve is zero at {@code a} = 0.0 and {@code a} = 1.0
	 *
	 * @param a The value to map onto a quintic S-curve.
	 * @returns The mapped value.
	 */
	public static double sCurve5(double a) {
		double a3 = a * a * a;
		double a4 = a3 * a;
		double a5 = a4 * a;
		return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
	}
}
