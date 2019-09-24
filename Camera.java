/**
 * Represents a pin-hole camera.
 * 
 * All coordinates are given in world space.
 * Note that for simplicity, this is a right-handed coordinate frame.
 * 
 * @author fabio
 */
public class Camera {
	/**
	 * Origin.
	 */
	public Vec3				origin;
	/**
	 * Right vector.
	 */
	public Vec3				x;
	/**
	 * Up vector.
	 */
	public Vec3				y;
	/**
	 * View vector.
	 */
	public Vec3				z;
	
    /**
     * Field of view along the right(x) axis.
     */
    public double               xfov;
    
    /**
     * Field of view along the right(y) axis.
     */
    public double               yfov;
    
    /**
     * Width of the image in pixels.
     */
    public int                  xResolution;
    /**
     * Height of the image in pixels.
     */
    public int                  yResolution;
    
	/**
	 * Default constructor
	 */
	public Camera() {
		origin = new Vec3(0,0,0);
		
		x = new Vec3(1,0,0);
		y = new Vec3(0,1,0);
		z = new Vec3(0,0,1);
		
		yfov = 45.0 * Math.PI / 180.0;
        xfov = 45.0 * Math.PI / 180.0;
		
		xResolution = yResolution = 512;
	}
	
    /**
     * Initialize frame from ZY. Used by parser.
     */
    public void initFromParser() {
        // normalize forward
        z.setToNormalize();
        
        // orthonormalize the up vector
        y = y.sub(z.scale(y.dot(z))).normalize();
        
        // right is just the cross product
        x = new Vec3();
        x = z.cross(y);
        
        yfov = Math.toRadians(yfov);
        xfov = yfov * (double)xResolution / (double)yResolution;
    }    
}
