/**
 * Implements a bone of a certain size.
 * The rotation and gtranslation of the bone are expressed wrt the origin.
 * By definition the pose transform of the bone is derived from 
 * its translation and rotation at time 0.
 */
public class Bone {
    /**
     * Current Translation
     */
    public Vec3 translation;
    
    /**
     * Variation for translation.
     * If left null, no animation is associated with this.
     */
    public Vec3Animation translationVariation;
    
    /**
     * Current Rotation
     */
    public Vec3 rotation;
    
    /**
     * Variation for rotation
     * If left null, no animation is associated with this.
     */
    public Vec3Animation rotationVariation;
    
    /**
     * Bone size
     */
    public Vec3 size;
    
    /**
     * Current transform.
     * This is updated every time animate is called.
     */
    public Mat4 m;
    
    /**
     * Current normal transform
     * This is updated every time animate is called.
     */
    public Mat4 nm;
    
    /**
     * Pose inverse transfrom
     * This is updated every time computePoseTransform is called.
     */
    public Mat4 m0i;
    
    /**
     * Pose inverse normal transfrom
     * This is updated every time computePoseTransform is called.
     */
    public Mat4 nm0i;
    
    /**
     * Bone color for the GUI
     */
    public Color color;
    
    /**
     * Move the bones and animate the transforms at the same time
     */
    public void animate(double time) {
        throw new NotImplementedException();
    }
    
    /**
     * Compute pose transforms
     */
    public void computePoseTransforms() {
        throw new NotImplementedException();
    }
    
    /**
     * Restart animation from 0
     */
    public void restartAnimation() {
        animate(0);
    }
}
