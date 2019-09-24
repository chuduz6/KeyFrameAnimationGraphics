/**
 * Base class for light source.
 * @author fabio
 */
public abstract class Light {
	/**
	 * Light intensity.
	 */
	public Color				intensity;
	
	/**
	 * Default connstructor.
	 */
	public Light() {
		intensity = new Color();
	}
}
