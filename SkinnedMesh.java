/**
 * Class representing a skinned mesh defined by the geometry stored in skin and 
 * deformed by the bones stored in the bone array.
 * The skin is in its rest pose, while the bone rest posed is defined for t = 0.
 * The skin and the bones are defined in the same coordinate system (i.e. the hierarchy parent system).
 * 
 * The vertexweights are computed automagically by the comnpute vertex weights function.
 * In this simple case, the weights depend on the distance between the bone represented as a capsule.
 * The values of autoWeightsMaxDistance defined the clamping distance for the influence of various bones.
 * 
 * @author fabio
 */
public class SkinnedMesh extends Surface {
    /**
     * Mesh geometry in the rest pose
     */
    public Mesh         skin;
    
    /**
     * Flattened list of bones
     */
    public Bone   bones[];
    
    /**
     * Vertex weights.
     * The first index is the vertex index, 
     * while the second is the bone index.
     */
    public double vertexWeights[][];
    
    /**
     * Min distance for automatic weighting
     */
    public double autoWeightsMaxDistance = 2;

    /**
     * Tesselate the base mesh
     */
    public void tesselate() {
        skin.tesselationLevel = tesselationLevel;
        skin.tesselate();
        // copy tesselated base mesh in surface one
        tesselatedMesh = skin.tesselatedMesh.copy();
        
        // compute bones transforms
        for (int b = 0; b < bones.length; b++) {
            bones[b].computePoseTransforms();
        }
        
        // compute weights based on vertex/bone distance
        computeVertexWeights();
    }

    /**
     * Compute the vertex weights based on the location of the vertex.
     */
    private void computeVertexWeights() {
        throw new NotImplementedException();
    }

    /**
     * Compute the bone distance by evaluating the distance from
     * a capsule aligned aligned along the y axis from 0..1.
     * The point provided has to be in the space of the capsule.
     * We also pass in the capsule size so we can rescale approprately the weight of the axes.
     */
    public double distanceFromCapsule(Vec3 Pl, Vec3 size) {
        throw new NotImplementedException();
    }
    
    /**
     * Animate the mesh by computing the deformation of the skin.tesselatedMesh vertices and
     * storing it in the this.teseelatedMesh vertices.
     */
    public void animate(double time) {
        throw new NotImplementedException();
    }

    /**
     * Restart animation
     */
    public void restartAnimation() {
        tesselatedMesh = skin.tesselatedMesh.copy();
        for (int b = 0; b < bones.length; b++) {
            bones[b].restartAnimation();
        }
    }    
}
