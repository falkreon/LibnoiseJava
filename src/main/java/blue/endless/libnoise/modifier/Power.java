package blue.endless.libnoise.modifier;

public class Power extends AbstractModifierModule<Power> {

	@Override
	public double getValue(double x, double y, double z) {
		if (sources.length<2) return 1; //Just about anything to the zeroth power is 1
		return Math.pow( sources[0].getValue(x, y, z), sources[1].getValue(x, y, z) );
	}

}
