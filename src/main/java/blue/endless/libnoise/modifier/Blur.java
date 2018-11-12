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
