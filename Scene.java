/**
 * Raytracing scene.
 * @author fabio
 */
public class Scene {
	/**
	 * Camera.
	 */
	public Camera				camera;
	/**
	 * Object surfaces.
	 */
	public Transform		    hierarchyRoot;
	/**
	 * Lights.
	 */
	public Light[]				lights;
    
    /**
     * Current time
     */
    public double               time;
	
	/**
	 * Default constructor.
	 * Scene is initialized in the Loader.
	 */
	public Scene() {
		camera = new Camera();
        hierarchyRoot = new Transform();
		lights = new Light[0];
        time = 0;
	}
    
    /**
     * Tesselate all object in a scene and store 
     * tesselated surfaces in the surface data structure.
     * @see HierarchyNode#tesselate(int)
     */
    public void tesselate() {
        hierarchyRoot.tesselate();
    }
    
    /**
     * Evaluates the state of the graph at the given time.
     */
    public void animate(double time) {
        this.time = time;
        hierarchyRoot.animate(time);
    }

    public void restartAnimation() {
        this.time = 0;
        hierarchyRoot.restartAnimation();
    }
}
