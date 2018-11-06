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

public class RotatePoint extends AbstractModifierModule<RotatePoint> {
	private static final double DEG_TO_RAD = Math.PI / 180.0;
	
	protected double m_x1Matrix = 1;
	protected double m_y1Matrix = 0;
	protected double m_z1Matrix = 0;
	protected double m_x2Matrix = 0;
	protected double m_y2Matrix = 1;
	protected double m_z2Matrix = 0;
	protected double m_x3Matrix = 0;
	protected double m_y3Matrix = 0;
	protected double m_z3Matrix = 1;
	
	public RotatePoint() {
		
	}
	
	@Override
	public double getValue (double x, double y, double z) {
		if (sources.length<1) return 0;

		double nx = (m_x1Matrix * x) + (m_y1Matrix * y) + (m_z1Matrix * z);
		double ny = (m_x2Matrix * x) + (m_y2Matrix * y) + (m_z2Matrix * z);
		double nz = (m_x3Matrix * x) + (m_y3Matrix * y) + (m_z3Matrix * z);
		return sources[0].getValue(nx, ny, nz);
	}

	public RotatePoint setAngles (double xAngle, double yAngle, double zAngle) {
		double xCos, yCos, zCos, xSin, ySin, zSin;
		//TODO: Fastmath is faster
		xCos = Math.cos(xAngle * DEG_TO_RAD);
		yCos = Math.cos(yAngle * DEG_TO_RAD);
		zCos = Math.cos(zAngle * DEG_TO_RAD);
		xSin = Math.sin(xAngle * DEG_TO_RAD);
		ySin = Math.sin(yAngle * DEG_TO_RAD);
		zSin = Math.sin(zAngle * DEG_TO_RAD);

		m_x1Matrix = ySin * xSin * zSin + yCos * zCos;
		m_y1Matrix = xCos * zSin;
		m_z1Matrix = ySin * zCos - yCos * xSin * zSin;
		m_x2Matrix = ySin * xSin * zCos - yCos * zSin;
		m_y2Matrix = xCos * zCos;
		m_z2Matrix = -yCos * xSin * zCos - ySin * zSin;
		m_x3Matrix = -ySin * xCos;
		m_y3Matrix = xSin;
		m_z3Matrix = yCos * xCos;

		//m_xAngle = xAngle;
		//m_yAngle = yAngle;
		//m_zAngle = zAngle;
		return this;
	}

}
