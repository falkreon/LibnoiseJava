# Libnoise-Java

This is a Java port of libnoise, a C++ library for generating various kinds of continuous noise.
The original C++ library was written by Jason Bevins and released under LGPL 2.1. This port is
created and maintained by Falkreon (Isaac Ellingson) and released under LGPL 3.0.


Continuous (or "solid") noise is extremely useful for generating 2D or 3D texture, terrain, and
other procedural content. This library provides several kinds of this category of noise, and ways
to modify and combine them to generate data in the exact way it's needed.


Much like the original, this library does not ever actually create images, only on-the-fly
numeric data.