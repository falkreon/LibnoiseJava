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

public class ScalePoint extends AbstractModifierModule<ScalePoint> {
	protected double m_xScale = 1;
	protected double m_yScale = 1;
	protected double m_zScale = 1;
	
	public ScalePoint() {}
	
	public ScalePoint setScale(double xScale, double yScale, double zScale) {
		this.m_xScale = xScale;
		this.m_yScale = yScale;
		this.m_zScale = zScale;
		
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		if (sources.length<1) return 0;
		
		return sources[0].getValue(x * m_xScale, y * m_yScale, z * m_zScale);
	}
}
