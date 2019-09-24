/**
 * Point light source
 */
public class PointLight extends Light {
	/**
	 * Light position.
	 */
	public Vec3						position;
	
	/**
	 * Default constructor.
	 *
	 */
	public PointLight() {
		super();
		position = new Vec3();
	}
}
