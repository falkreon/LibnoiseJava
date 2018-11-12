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

/**
 * Takes one module as an input and multisamples it to create a blur effect. Due to box sampling, this creates
 * significantly smoother edges along diagonals than orthogonals. Also, this is pretty much the *slowest* way to
 * create smooth noise, so only use this as a last resort.
 */
public class Blur extends AbstractModifierModule<Blur> {
	
	protected double scale = 0.5;
	protected Plane plane = null;
	
	
	public Blur setScale(double scale) {
		this.scale = scale;
		return this;
	}
	
	
	public Blur setPlane(Plane samplePlane) {
		this.plane = samplePlane;
		return this;
	}
	
	
	@Override
	public double getValue(double x, double y, double z) {
		if (plane==null) return getCubeValue(x, y, z);
		
		switch(plane) {
		default:
		case XY: return getXYValue(x, y, z);
		case XZ: return getXZValue(x, y, z);
		case YZ: return getYZValue(x, y, z);
		}
	}
		
	protected double getCubeValue(double x, double y, double z) {
		double half = scale/2.0;
		
		//Pick sampling points in a cube with sides of length 'scale', centered on the original sampling point
		double x0 = x - half;
		double x1 = x + half;
		double y0 = y - half;
		double y1 = y + half;
		double z0 = z - half;
		double z1 = z + half;

		//perform a non-weighted average of the samples at these points to get a 3D box blur
		double a = sources[0].getValue(x0, y0, z0);
		double b = sources[0].getValue(x1, y0, z0);
		double c = sources[0].getValue(x0, y1, z0);
		double d = sources[0].getValue(x1, y1, z0);
		double e = sources[0].getValue(x0, y0, z1);
		double f = sources[0].getValue(x1, y0, z1);
		double g = sources[0].getValue(x0, y1, z1);
		double h = sources[0].getValue(x1, y1, z1);
		return ( a + b + c + d + e + f + g + h ) / 8.0;
	}
	
	protected double getXYValue(double x, double y, double z) {
		double half = scale/2.0;
		
		//Pick sampling points in a cube with sides of length 'scale', centered on the original sampling point
		double x0 = x - half;
		double x1 = x + half;
		double y0 = y - half;
		double y1 = y + half;

		//perform a non-weighted average of the samples at these points to get a 3D box blur
		double a = sources[0].getValue(x0, y0, z);
		double b = sources[0].getValue(x1, y0, z);
		double c = sources[0].getValue(x0, y1, z);
		double d = sources[0].getValue(x1, y1, z);
		return ( a + b + c + d) / 4.0;
	}
	
	public double getXZValue(double x, double y, double z) {
		double half = scale/2.0;
		
		//Pick sampling points in a cube with sides of length 'scale', centered on the original sampling point
		double x0 = x - half;
		double x1 = x + half;
		double z0 = z - half;
		double z1 = z + half;

		//perform a non-weighted average of the samples at these points to get a 3D box blur
		double a = sources[0].getValue(x0, y, z0);
		double b = sources[0].getValue(x1, y, z0);
		double c = sources[0].getValue(x0, y, z1);
		double d = sources[0].getValue(x1, y, z1);
		return ( a + b + c + d ) / 4.0;
	}
	
	public double getYZValue(double x, double y, double z) {
		double half = scale/2.0;
		
		//Pick sampling points in a cube with sides of length 'scale', centered on the original sampling point
		double y0 = y - half;
		double y1 = y + half;
		double z0 = z - half;
		double z1 = z + half;

		//perform a non-weighted average of the samples at these points to get a 3D box blur
		double a = sources[0].getValue(x, y0, z0);
		double b = sources[0].getValue(x, y1, z0);
		double c = sources[0].getValue(x, y0, z1);
		double d = sources[0].getValue(x, y1, z1);
		return ( a + b + c + d ) / 4.0;
	}
	
	public static enum Plane {
		XY,
		XZ,
		YZ;
	}
}
