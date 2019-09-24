/**
 * Phong material.
 * @author fabio
 */
public class Phong extends Material {
	/**
	 * Diffuse color.
	 */
	public Color				diffuse;
	/**
	 * Specular color.
	 */
	public Color				specular;
	/**
	 * Specular exponent.
	 */
	public double				exponent;
	
    /**
     * An approximate phong material. In this case it is just a copy.
     */
    public Phong getApproximatePhong() {
        Phong phong = new Phong();
        phong.diffuse = new Color(diffuse.r, diffuse.b, diffuse.g);
        phong.specular = new Color(specular.r, specular.g, specular.b);
        phong.exponent = exponent;
        return phong;
    }
}
