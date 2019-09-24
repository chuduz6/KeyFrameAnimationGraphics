/**
 * Base class for surface.
 * @author fabio
 */
public abstract class Surface extends HierarchyNode {
    /**
     * Material.
     */
    public Material             material;
	/**
	 * Tesselate geometry
	 */
	public Mesh                tesselatedMesh;
    /**
     * Tesselation level
     */
    public int                 tesselationLevel;

    /**
     * @see HierarchyNode#tesselate()
     */
    public abstract void tesselate();   

    /**
     * Evaluates the state of the graph at the given time.
     */
    public void animate(double time) {
    }

    /**
     * Restart animation
     */
    public void restartAnimation() {
    }
}
