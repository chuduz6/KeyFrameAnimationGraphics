/**
 * Class representing a Bezier spline with keyframes.
 * @author fabio
 */
public class KeyframeBezierSpline extends Vec3Animation {
    /**
     * Bezier control points.
     * Each four control points defines a segment from keyframeTime k to keyframeTime k+1
     * There are (keyframeTimes.length-1)*4 control points
     */
	public Vec3[] keyframeControlPoints;
    
    /**
     * Time of the keyframes.
     * Each keyframe pair k,k+1 defines a segment controlled
     * by the 4 control points k*4, (k*4+3.
     * There are (keyframeTimes.length-1) * 4 control points.
     */
    public double[] keyframeTimes;
	
    /** 
     * Evaluate the spline at time t.
     * It first find the proper segments given the time frames defined.
     * It then evaluate the spline segment after normalizing the t value for this segment.
     */
    public Vec3 evaluate(double t) {
        throw new NotImplementedException();
    }    
}
