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

public class ValueNoise {
	
	//All noise uses "Version 2" constants
	private static final int X_NOISE_GEN = 1619;
	private static final int Y_NOISE_GEN = 31337;
	private static final int Z_NOISE_GEN = 6971;
	private static final int SEED_NOISE_GEN = 1013;
	private static final int SHIFT_NOISE_GEN = 8;
	
	public static int intValueNoise3D(int x, int y, int z, int seed) {
		// All constants are primes and must remain prime in order for this noise
		// function to work correctly.
		int n = (
			X_NOISE_GEN    * x
			+ Y_NOISE_GEN    * y
			+ Z_NOISE_GEN    * z
			+ SEED_NOISE_GEN * seed)
			& 0x7fffffff;
		n = (n >> 13) ^ n;
		
		return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
	}
	
	public static double valueNoise3D (int x, int y, int z, int seed) {
		return 1.0 - ((double)intValueNoise3D (x, y, z, seed) / 1073741824.0);
	}
	
	public static double valueCoherentNoise3D (double x, double y, double z, int seed, NoiseQuality noiseQuality) {
	  // Create a unit-length cube aligned along an integer boundary.  This cube
	  // surrounds the input point.
	  int x0 = (x > 0.0? (int)x: (int)x - 1);
	  int x1 = x0 + 1;
	  int y0 = (y > 0.0? (int)y: (int)y - 1);
	  int y1 = y0 + 1;
	  int z0 = (z > 0.0? (int)z: (int)z - 1);
	  int z1 = z0 + 1;

	  // Map the difference between the coordinates of the input value and the
	  // coordinates of the cube's outer-lower-left vertex onto an S-curve.
	  double xs = 0, ys = 0, zs = 0;
	  switch (noiseQuality) {
	    case FAST:
	      xs = (x - (double)x0);
	      ys = (y - (double)y0);
	      zs = (z - (double)z0);
	      break;
	    case STANDARD:
	      xs = Interpolate.sCurve3(x - (double)x0);
	      ys = Interpolate.sCurve3(y - (double)y0);
	      zs = Interpolate.sCurve3(z - (double)z0);
	      break;
	    case BEST:
	      xs = Interpolate.sCurve5(x - (double)x0);
	      ys = Interpolate.sCurve5(y - (double)y0);
	      zs = Interpolate.sCurve5(z - (double)z0);
	      break;
	  }

	  // Now calculate the noise values at each vertex of the cube.  To generate
	  // the coherent-noise value at the input point, interpolate these eight
	  // noise values using the S-curve value as the interpolant (trilinear
	  // interpolation.)
	  double n0, n1, ix0, ix1, iy0, iy1;
	  n0   = valueNoise3D (x0, y0, z0, seed);
	  n1   = valueNoise3D (x1, y0, z0, seed);
	  ix0  = Interpolate.linear(n0, n1, xs);
	  n0   = valueNoise3D (x0, y1, z0, seed);
	  n1   = valueNoise3D (x1, y1, z0, seed);
	  ix1  = Interpolate.linear(n0, n1, xs);
	  iy0  = Interpolate.linear(ix0, ix1, ys);
	  n0   = valueNoise3D (x0, y0, z1, seed);
	  n1   = valueNoise3D (x1, y0, z1, seed);
	  ix0  = Interpolate.linear(n0, n1, xs);
	  n0   = valueNoise3D (x0, y1, z1, seed);
	  n1   = valueNoise3D (x1, y1, z1, seed);
	  ix1  = Interpolate.linear(n0, n1, xs);
	  iy1  = Interpolate.linear(ix0, ix1, ys);
	  return Interpolate.linear(iy0, iy1, zs);
	}
	
	public static double gradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
		// Randomly generate a gradient vector given the integer coordinates of the
		// input value. This implementation generates a random number and uses it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed) & 0xffffffff;
		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = RandomVectors.table[(vectorIndex << 2)];
		double yvGradient = RandomVectors.table[(vectorIndex << 2) + 1];
		double zvGradient = RandomVectors.table[(vectorIndex << 2) + 2];

		// Set up us another vector equal to the distance between the two vectors
		// passed to this function.
		double xvPoint = (fx - (double) ix);
		double yvPoint = (fy - (double) iy);
		double zvPoint = (fz - (double) iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector. The resulting value is gradient noise. Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) * 2.12;
	}
	
	public static double gradientCoherentNoise3D (double x, double y, double z, int seed, NoiseQuality noiseQuality) {
		// Create a unit-length cube aligned along an integer boundary.  This cube
		// surrounds the input point.
		int x0 = (x > 0.0? (int)x: (int)x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0? (int)y: (int)y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0? (int)z: (int)z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		switch (noiseQuality) {
		case FAST:
			xs = (x - (double)x0);
			ys = (y - (double)y0);
			zs = (z - (double)z0);
			break;
		case STANDARD:
			xs = Interpolate.sCurve3(x - (double)x0);
			ys = Interpolate.sCurve3(y - (double)y0);
			zs = Interpolate.sCurve3(z - (double)z0);
			break;
		case BEST:
			xs = Interpolate.sCurve5(x - (double)x0);
			ys = Interpolate.sCurve5(y - (double)y0);
			zs = Interpolate.sCurve5(z - (double)z0);
			break;
		}
		
		// Now calculate the noise values at each vertex of the cube.  To generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0   = gradientNoise3D(x, y, z, x0, y0, z0, seed);
		n1   = gradientNoise3D(x, y, z, x1, y0, z0, seed);
		ix0  = Interpolate.linear(n0, n1, xs);
		n0   = gradientNoise3D (x, y, z, x0, y1, z0, seed);
		n1   = gradientNoise3D (x, y, z, x1, y1, z0, seed);
		ix1  = Interpolate.linear(n0, n1, xs);
		iy0  = Interpolate.linear(ix0, ix1, ys);
		n0   = gradientNoise3D (x, y, z, x0, y0, z1, seed);
		n1   = gradientNoise3D (x, y, z, x1, y0, z1, seed);
		ix0  = Interpolate.linear(n0, n1, xs);
		n0   = gradientNoise3D (x, y, z, x0, y1, z1, seed);
		n1   = gradientNoise3D (x, y, z, x1, y1, z1, seed);
		ix1  = Interpolate.linear(n0, n1, xs);
		iy1  = Interpolate.linear(ix0, ix1, ys);

		return Interpolate.linear(iy0, iy1, zs);
	}
	
	public static double makeInt32Range (double n) {
		if (n >= 1073741824.0) {
			return (2.0 * (n % 1073741824.0)) - 1073741824.0;
		} else if (n <= -1073741824.0) {
			return (2.0 * (n % 1073741824.0)) + 1073741824.0;
		} else {
			return n;
		}
	}
}
