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
 * Simple case of Blend or Select where the first input is subjected to a bandpass test. If it passes, its values are
 * expressed, and if it fails, an alternate value is expressed. If no second source exists, band-rejected values will
 * be clamped to the upper or lower bound. If a second source exists, band-rejected samples will yield the second source
 * instead of the first.
 */
public class Bandpass extends AbstractModifierModule<Bandpass> {
	protected double upperBound = 1.0;
	protected double lowerBound = 0.0;
	
	public Bandpass setBounds(double lower, double upper) {
		upperBound = upper;
		lowerBound = lower;
		if (lowerBound>upperBound) lowerBound = upperBound;
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		if (sources.length<1) return 0;
		
		double signal = sources[0].getValue(x, y, z);
		
		if (signal>=lowerBound && signal<=upperBound) return signal;
		
		if (sources.length<2) {
			//band-rejected and no second input; return high signals as the upperBound and low signals as the lowerBound
			
			if (signal < lowerBound) {
				return lowerBound;
			} else {
				return upperBound;
			}
		} else {
			return sources[1].getValue(x, y, z);
		}
		
		
	}

}
