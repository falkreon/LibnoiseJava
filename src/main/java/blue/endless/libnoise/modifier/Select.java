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

import static blue.endless.libnoise.Interpolate.*;

/**
 * This is a somewhat complicated modifier which uses its third input to select or fade between the other two. You
 * define "bounds" for values which, if met, will select for the first source input. Failure to fall within the bounds
 * will produce results from the second input. Nonzero edge falloff produces a blend between the two.
 * 
 * <p>Note: It is reccommended to set the edge falloff after setting the bounds if both are being set.
 */
public class Select extends AbstractModifierModule<Select> {
	protected double m_edgeFalloff = 0.0f;
	protected double m_lowerBound = -1.0f;
	protected double m_upperBound = 1.0f;
	
	public Select() {}
	
	public Select setBounds(double lowerBound, double upperBound) {
		m_lowerBound = lowerBound;
		m_upperBound = upperBound;
		if (m_lowerBound > m_upperBound) m_lowerBound = m_upperBound;

		// Make sure that the edge falloff curves do not overlap.
		setEdgeFalloff (m_edgeFalloff);
		
		return this;
	}

	public Select setEdgeFalloff(double edgeFalloff) {
		// Make sure that the edge falloff curves do not overlap.
		double boundSize = m_upperBound - m_lowerBound;
		m_edgeFalloff = (edgeFalloff > boundSize / 2)? boundSize / 2: edgeFalloff;
		
		return this;
	}

	public double getValue(double x, double y, double z) {
		if (sources.length<3) return 0;

		double controlValue = sources[2].getValue(x, y, z);
		double alpha;
		if (m_edgeFalloff > 0.0) {
			if (controlValue < (m_lowerBound - m_edgeFalloff)) {
				// The output value from the control module is below the selector
				// threshold; return the output value from the first source module.
				return sources[0].getValue(x, y, z);

			} else if (controlValue < (m_lowerBound + m_edgeFalloff)) {
				// The output value from the control module is near the lower end of the
				// selector threshold and within the smooth curve. Interpolate between
				// the output values from the first and second source modules.
				double lowerCurve = (m_lowerBound - m_edgeFalloff);
				double upperCurve = (m_lowerBound + m_edgeFalloff);
				alpha = sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
				return linear(sources[0].getValue(x, y, z), sources[1].getValue(x, y, z), alpha);

			} else if (controlValue < (m_upperBound - m_edgeFalloff)) {
				// The output value from the control module is within the selector
				// threshold; return the output value from the second source module.
				return sources[1].getValue (x, y, z);

			} else if (controlValue < (m_upperBound + m_edgeFalloff)) {
				// The output value from the control module is near the upper end of the
				// selector threshold and within the smooth curve. Interpolate between
				// the output values from the first and second source modules.
				double lowerCurve = (m_upperBound - m_edgeFalloff);
				double upperCurve = (m_upperBound + m_edgeFalloff);
				alpha = sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
				return linear(sources[1].getValue(x, y, z), sources[0].getValue(x, y, z), alpha);

			} else {
				// Output value from the control module is above the selector threshold;
				// return the output value from the first source module.
				return sources[0].getValue(x, y, z);
			}
		} else {
			if (controlValue < m_lowerBound || controlValue > m_upperBound) {
				return sources[0].getValue(x, y, z);
			} else {
				return sources[1].getValue(x, y, z);
			}
		}
	}
}
