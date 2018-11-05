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
import java.util.Random;

import javax.swing.JFrame;

import blue.endless.libnoise.generator.Generator;
import blue.endless.libnoise.generator.Voronoi;

public class Example extends JFrame {
private static final long serialVersionUID = -6682388330686106856L;
	
	public static void main(String... args) {
		new Example().setVisible(true);
	}
	
	
	
	public BufferedImage terrainPreview = null;
	
	
	
	public Example() {
		this.setMinimumSize(new Dimension(1024,768));
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
		//First, visualize a scaledNoiseField.
		
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		double scale = 1/1024d;
		
		Generator generator =
				new Voronoi()
				.setEnableDistance(false);
				//= new Perlin();
				//= new Billow();
		
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				//Shift the origin to the center of the picture so we can get a good idea about what zero crossings look like
				float dx = x + halfWidth;
				float dy = y + halfHeight;
				
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
	
	public static float clamp(float value, float min, float max) {
		return Math.max(Math.min(value, max), min);
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.max(Math.min(value, max), min);
	}
}
