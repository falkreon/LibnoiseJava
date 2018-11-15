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

package blue.endless.libnoise.example;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import blue.endless.libnoise.Interpolate;
import blue.endless.libnoise.Module;
import blue.endless.libnoise.NoiseQuality;
import blue.endless.libnoise.generator.Billow;
import blue.endless.libnoise.generator.Checkerboard;
import blue.endless.libnoise.generator.Noise;
import blue.endless.libnoise.generator.Perlin;
import blue.endless.libnoise.generator.RidgedMulti;
import blue.endless.libnoise.generator.Spheres;
import blue.endless.libnoise.generator.Voronoi;
import blue.endless.libnoise.modifier.Bandpass;
import blue.endless.libnoise.modifier.Blend;
import blue.endless.libnoise.modifier.Blur;
import blue.endless.libnoise.modifier.Multiply;
import blue.endless.libnoise.modifier.RotatePoint;
import blue.endless.libnoise.modifier.Select;
import blue.endless.libnoise.modifier.Turbulence;

public class Example extends JFrame {
private static final long serialVersionUID = -6682388330686106856L;
	
	public static void main(String... args) {
		new Example().setVisible(true);
	}
	
	
	
	public BufferedImage terrainPreview = null;
	
	
	
	public Example() {
		this.setMinimumSize(new Dimension(2048,1024));
		this.setSize(1024, 768);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void paint(Graphics g) {
		if (terrainPreview==null) {
			terrainPreview = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
			generateTerrain(terrainPreview);
		}
		
		g.drawImage(terrainPreview, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	
	public void generateTerrain(BufferedImage im) {
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		double scale = 1/2d;
		
		Module a = new Perlin().setFrequency(1/72.0).setOctaveCount(5);
		Module b = new Perlin().setFrequency(1/64.0).setOctaveCount(5);
		//Module c = new Noise().setFrequency(1/32.0);
		
		
		
		
		Module generator =
		//		new Voronoi().setEnableDistance(false).setFrequency(1/32.0);
				new Noise()
				.setFrequency(1/32.0);
		
		//generator = new Turbulence().setPower(2.0).setSources(generator);
		
		Module generator2 = new Noise()
				.setFrequency(1/16.0);
		
		int period = 64;
		
		generator = new RotatePoint().setAngles(0, 45.0, 0).setSources(generator);
		
		//generator = new Blur().setSources(generator).setPlane(Blur.Plane.XZ);//.setQuality(NoiseQuality.BEST);
		
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				int wholeX = x / period;
				int fractX = x % period;
				double fX = fractX / (double)period;
				int rescaleX = wholeX*period + (int)(Interpolate.sCurve5(fX)*period);
				
				int wholeY = y / period;
				int fractY = y % period;
				double fY = fractY / (double)period;
				double rescaleY = wholeY*period + (int)(Interpolate.sCurve5(fY)*period);
				
				//Shift the origin to the center of the picture so we can get a good idea about what zero crossings look like
				double dx = x - halfWidth;
				double dy = y - halfHeight;
				
				
				//Rescale coords so we can get right up into the pixels
				dx *= scale;
				dy *= scale;
				
				double cell1 = a.getValue(dx, 0, dy) / 2.0 + 0.5;
				double cell2 = b.getValue(dx, 0, dy) / 2.0 + 0.5;
				//double cell3 = c.getValue(dx, 0, dy) / 2.0 + 0.5;
				
				int cell1T = (int)(cell1*3.0);
				int cell2T = (int)(cell2*3.0);
				//int cell3T = (int)(cell3*3.0);
				int cellIndex = cell1T*3 + cell2T;// + cell3T;
				double cell = cellIndex / 9.0;
				
				//if (cell<0.9f) cell = 0.0f;
				//if (cell>0.0f) cell = 1.0f; //threshold
				int col = stages(cell, 20);
				/*
				int cellValue = clamp((int)(cell * 255), 0, 255);
				
				int r = cellValue;
				int g = cellValue;
				int b = cellValue;
				if (cellValue < 64) {
					r = 255;
					g = 128;
					b = 128;
				} else if (cellValue < 128) {
					r = 128;
					g = 255;
					b = 128;
				} else if (cellValue < 192) {
					r = 128;
					g = 128;
					b = 255;
				} else {
					r = 255;
					g = 255;
					b = 128;
				}
				
				int col = 0xFF000000 |
						(r << 16) |
						(g <<  8) |
						(b <<  0);*/
				im.setRGB(x, y, col);
				
				//im.setRGB(x, y, gray(cellValue));
				//System.out.print(".");
			}
			//System.out.println();
		}
	}
	
	public void generateTerrainOld(BufferedImage im) {
		//First, visualize a scaledNoiseField.
		
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		double scale = 1/256d;
		
		Module generator =
				//new Voronoi()
				//.setEnableDistance(false);
				//new Perlin();
				new Billow().setFrequency(0.5);
				//new Perlin().setSeed(0);
				//new Checkerboard();
		
		//generator = new Bandpass().setSources(generator, (x,y,z)->-1).setBounds(0, 256);
		
		Module plains = new Multiply().setSources(new Perlin().setNoiseQuality(NoiseQuality.BEST), (x,y,z)->0.5);
		
		generator = new Blend().setSources(plains, generator, generator);
		//generator = new RotatePoint().setSources(generator).setAngles(0, 12.5, 0);
		/*
		Module a = new Billow();
		Module b = new Spheres();
		
		generator = new Select()
				.setBounds(-1, 0)
				.setSources(a, b, generator);*/
		
		//generator = new Turbulence().setSources(generator);
		
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				//Shift the origin to the center of the picture so we can get a good idea about what zero crossings look like
				double dx = x - halfWidth;
				double dy = y - halfHeight;
				
				//Rescale coords so we can get right up into the pixels
				dx *= scale;
				dy *= scale;
				
				float cell = (float)generator.getValue(dx, 0, dy) / 2f + 0.5f;
				
				//if (cell<0.9f) cell = 0.0f;
				//if (cell>0.0f) cell = 1.0f; //threshold
				int cellValue = clamp((int)(cell * 255), 0, 255);
				
				im.setRGB(x, y, gray(cellValue));
			}
		}
		
	}
	
	public int gray(int value) {
		int safe = value & 0xFF;
		return
			(safe <<  0) |
			(safe <<  8) |
			(safe << 16) |
			(0xFF << 24);
	}
	
	private static final int[] DISTINCT_COLORS = {
			0xFFe6194b, 0xFF3cb44b, 0xFFffe119, 0xFF4363d8, 0xFFf58231, 0xFF911eb4, 0xFF46f0f0, 0xFFf032e6, 0xFFbcf60c,
			0xFFfabebe, 0xFF008080, 0xFFe6beff, 0xFF9a6324, 0xFFfffac8, 0xFF800000, 0xFFaaffc3, 0xFF808000, 0xFFffd8b1,
			0xFF000075, 0xFF808080, 0xFFffffff, 0xFF000000
			//0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF, 0xFFFFFFFF,
	};
	
	//For "value" between 0 and 1
	public int stages(double value, int count) {
		if (count >= DISTINCT_COLORS.length) count = DISTINCT_COLORS.length - 1;
		if (value<0) value = 0;
		if (value>1) value = 1;
		int i = (int)(value * count);
		return DISTINCT_COLORS[i];
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.max(Math.min(value, max), min);
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.max(Math.min(value, max), min);
	}
}
