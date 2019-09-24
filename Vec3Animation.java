/**
 * Base class for all animation curves.
 * @author fabio
 */
public abstract class Vec3Animation {
    /** 
     * Evaluates the spline at time t.
     */
    public abstract Vec3 evaluate(double t);
}
