/**
 * Lambertian material.
 * 
 * @author fabio
 */
public class Lambert extends Material {
	/**
	 * Diffuse color.
	 */
	public Color				diffuse;
	
    /**
     * An approximate phong material.
     */
    public Phong getApproximatePhong() {
        Phong phong = new Phong();
        phong.diffuse = new Color(diffuse.r, diffuse.g, diffuse.b);
        phong.specular = new Color(0,0,0);
        phong.exponent = 1;
        return phong;
    }
}
