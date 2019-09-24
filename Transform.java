/**
 * Base class for trasnformations in a hierarchy.
 * @author fabio
 */
public class Transform extends HierarchyNode {
    /**
     * Children of the transform node.
     */
    public HierarchyNode[] children;
    
    /**
     * Current Translation
     */
    public Vec3 translation;
    
    /**
     * Current Rotation
     */
    public Vec3 rotation;
    
    /**
     * Current Scale
     */
    public Vec3 scale;
    
    /**
     * Variation for translation.
     * If left null, no animation is associated with this.
     */
    public Vec3Animation translationVariation;
    
    /**
     * Variation for rotation
     * If left null, no animation is associated with this.
     */
    public Vec3Animation rotationVariation;
    
    /**
     * Variation for scale
     * If left null, no animation is associated with this.
     */
    public Vec3Animation scaleVariation;
    
    /**
     * Empty transform
     */
    public Transform() {
        children = new HierarchyNode[0];
        translation = new Vec3(0,0,0);
        rotation = new Vec3(0,0,0);
        scale = new Vec3(1,1,1);
    }
    
    /**
     * Tesselate all children.
     */
    public void tesselate() {
        for(int i = 0; i < children.length; i ++) {
            children[i].tesselate();
        }
    }
    
    /**
     * Evaluates the state of the graph at the given time.
     * In the case of transforms, this needs to update the
     * transform values for the given time.
     * The values at the given time will be stored in the
     * translation, rotation and scale fields.
     */
    public void animate(double time) {
        throw new NotImplementedException();
    }

    /**
     * Reset the animation at time 0
     */
    public void restartAnimation() {
        throw new NotImplementedException();
    }    
}
