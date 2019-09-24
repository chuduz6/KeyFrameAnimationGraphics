/**
 * Base class for node in the hierarchy.
 * @author fabio
 */
public abstract class HierarchyNode {
    
    /**
     * Tesselate surfaces.
     */
    public abstract void tesselate();   

    /**
     * Evaluates the state of the graph at the given time.
     */
    public abstract void animate(double time);

    /**
     * Reset the animation at time 0.
     */
    public abstract void restartAnimation();
}
