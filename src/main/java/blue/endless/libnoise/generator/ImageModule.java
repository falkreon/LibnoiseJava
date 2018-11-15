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

import java.awt.image.BufferedImage;

import blue.endless.libnoise.Interpolate;
import blue.endless.libnoise.Module;
import blue.endless.libnoise.NoiseQuality;

/**
 * Supplies the image as an intensity map that repeats infinitely along the XZ plane. Y coordinates are ignored for this
 * module.
 */
public class ImageModule implements Module {
	protected int[] imageData = new int[1];
	protected int width = 1;
	protected int height = 1;
	protected double pixelsPerUnit = 1;
	protected double scale = 1/255.0;
	
	protected double xofs = 0;
	protected double zofs = 0;
	
	protected NoiseQuality quality = NoiseQuality.BEST;
	
	public ImageModule setImage(BufferedImage im) {
		width = im.getWidth();
		height = im.getHeight();
		imageData = new int[width*height];
		imageData = im.getRGB(0, 0, width, height, imageData, 0, width);
		for(int i=0; i<imageData.length; i++) {
			int argb = imageData[i];
			int r = (argb >> 16) & 0xFF;
			int g = (argb >>  8) & 0xFF;
			int b = (argb >>  0) & 0xFF;
			imageData[i] = (r+g+b) / 3;
		}
		return this;
	}
	
	public ImageModule setImage(int width, int height, int[] data) {
		this.width = width;
		this.height = height;
		this.imageData = new int[width*height];
		System.arraycopy(data, 0, imageData, 0, Math.min(data.length, imageData.length));
		
		return this;
	}
	
	public ImageModule setPixelsPerUnit(double d) {
		this.pixelsPerUnit = d;
		return this;
	}
	
	public ImageModule setQuality(NoiseQuality quality) {
		this.quality = quality;
		return this;
	}
	
	@Override
	public double getValue(double x, double y, double z) {
		//offset and scale the coordinates
		double imX = (x + xofs) * pixelsPerUnit;
		double imZ = (z + zofs) * pixelsPerUnit;
		
		//wrap the coordinates to the image
		double xWrapped = (((imX % width) + width) % width);
		double zWrapped = (((imZ % height) + height) % height);
		//double xWrapped = imX % width;
		//double zWrapped = imZ % height;
		
		//Grab floored integer pixel locations
		int ix = (int)xWrapped;
		int iz = (int)zWrapped;
		
		//Grab the fractional distance across the pixel
		double fx = xWrapped - ix;
		double fz = zWrapped - iz;
		
		//Sample the four pixels around the location
		double a = sample(ix,   iz);
		double b = sample(ix+1, iz);
		double c = sample(ix,   iz+1);
		double d = sample(ix+1, iz+1);

		switch (quality) {
			case FAST:
				//use linear scale for fx and fz
				break;
			case STANDARD:
				fx = Interpolate.sCurve3(fx);
				fz = Interpolate.sCurve3(fz);
				break;
			case BEST:
				fx = Interpolate.sCurve5(fx);
				fz = Interpolate.sCurve5(fz);
				break;
		}

		//Return a bilinear interpolation of these pixels based on our fractional distances
		double ab = Interpolate.linear(a, b, fx);
		double cd = Interpolate.linear(c, d, fx);
		
		return Interpolate.linear(ab, cd, fz) * scale;
		
		//return a;
	}
	
	//Aggressively bounds-checked and wrapped
	protected int sample(int x, int z) {
		int ofs = (z%height)*width + (x%width);
		if (ofs>=imageData.length) ofs = imageData.length-1;
		if (ofs<0) ofs=0;
		
		return imageData[ofs];
	}
}
