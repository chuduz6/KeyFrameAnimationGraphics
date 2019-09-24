/**
 * A collection of triangles or quads. This data structure is the simplest mesh
 * representation and is suitable for efficient drawing. Mesh operation often
 * require better data structure for efficiency.
 * 
 * @author fabio
 * 
 */
public class Mesh extends Surface {
    /**
     * Vertex positions
     */
    public Vec3[] vertexPos;

    /**
     * Vertex normals - might be null, in which case face normals are used
     */
    public Vec3[] vertexNormal;

    /**
     * Faces indices pointing to the vertex ones
     */
    public int[] faceIds;

    /**
     * Quads faces - otherwise triangles
     */
    public boolean quads;

    /**
     * Create an empty mesh
     */
    public Mesh() {
    }

    /**
     * Create a mesh of nV vertices and nF faces, either quads or triangles
     */
    public Mesh(int nV, int nF, boolean quads) {
        allocate(nV, nF, quads);
    }

    /**
     * Create a mesh of nV vertices and nF faces, either quads or triangles
     */
    protected void allocate(int nV, int nF, boolean quads) {
        this.quads = quads;
        vertexPos = new Vec3[nV];
        vertexNormal = new Vec3[nV];
        for (int i = 0; i < nV; i++) {
            vertexPos[i] = new Vec3();
            vertexNormal[i] = new Vec3();
        }
        faceIds = new int[nF * verticesPerFace()];
    }

    /**
     * Make a copy. Not robust! Assumes the mesh is contructed through the above
     * contructor and only copies vertex list in the faces.
     */
    public Mesh copy() {
        if (faceIds.length == 0) {
            return new Mesh(3, 0, false);
        }

        Mesh m = new Mesh(getNumVertices(), faceIds.length, quads);

        for (int v = 0; v < getNumVertices(); v++) {
            m.vertexPos[v] = new Vec3(vertexPos[v]);
            m.vertexNormal[v] = new Vec3(vertexNormal[v]);
        }
        m.faceIds = (int[]) faceIds.clone();
        return m;
    }

    /**
     * Set the given face with the proper vertex indices. The face is a number
     * from 0 to number of faces - 1. This function allows to forget about the
     * packing rule and should be used to create new triangle faces.
     */
    public void setTriangleFace(int face, int v0, int v1, int v2) {
        if (quads) {
            throw new Error("Cannot create a triangle face in a quad mesh");
        }

        faceIds[face * verticesPerFace() + 0] = v0;
        faceIds[face * verticesPerFace() + 1] = v1;
        faceIds[face * verticesPerFace() + 2] = v2;
    }

    /**
     * Set the given face with the proper vertex indices. The face is a number
     * from 0 to number of faces - 1. This function allows to forget about the
     * packing rule and should be used to create new quad faces.
     */
    public void setQuadFace(int face, int v0, int v1, int v2, int v3) {
        if (!quads) {
            throw new Error("Cannot create a quad face in a triangle mesh");
        }

        faceIds[face * verticesPerFace() + 0] = v0;
        faceIds[face * verticesPerFace() + 1] = v1;
        faceIds[face * verticesPerFace() + 2] = v2;
        faceIds[face * verticesPerFace() + 3] = v3;
    }

    /**
     * Retrieve the vertex associated with the given face. This function should
     * be used to indiex vertices from faces since it allows to forget about
     * packing.
     */
    public int getFaceVertexIndex(int face, int vertex) {
        return faceIds[face * verticesPerFace() + vertex];
    }

    /**
     * Returns the number of vertices per face.
     */
    public int verticesPerFace() {
        if (quads)
            return 4;
        return 3;
    }

    /**
     * Returns the number of faces
     */
    public int getNumFaces() {
        return faceIds.length / verticesPerFace();
    }

    /**
     * Returns the number of vertices
     */
    public int getNumVertices() {
        return vertexPos.length;
    }

    /**
     * Tesselate.
     */
    public void tesselate() {
        tesselatedMesh = this;
        
        if (quads) {
            // Quads tesslation
    
            // subdivide until needed
            // for each level
            for (int l = 1; l <= tesselationLevel; l++) {
                // create new mesh and store away old one
                Mesh currentTesselatedMesh = tesselatedMesh;
                tesselatedMesh = new Mesh(
                        currentTesselatedMesh.getNumFaces() * 9,
                        currentTesselatedMesh.getNumFaces() * 4, true);
                // for each face ...
                for (int f = 0; f < currentTesselatedMesh.getNumFaces(); f++) {
                    // create new vertices
                    Vec3 v0 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 0)];
                    Vec3 v2 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 1)];
                    Vec3 v4 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 2)];
                    Vec3 v6 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 3)];
                    Vec3 v1 = v0.add(v2).scale(0.5);
                    Vec3 v3 = v2.add(v4).scale(0.5);
                    Vec3 v5 = v4.add(v6).scale(0.5);
                    Vec3 v7 = v6.add(v0).scale(0.5);
                    Vec3 v8 = v1.add(v5).scale(0.5);
                    // add vertices to the current list
                    tesselatedMesh.vertexPos[f * 9 + 0] = v0;
                    tesselatedMesh.vertexPos[f * 9 + 1] = v1;
                    tesselatedMesh.vertexPos[f * 9 + 2] = v2;
                    tesselatedMesh.vertexPos[f * 9 + 3] = v3;
                    tesselatedMesh.vertexPos[f * 9 + 4] = v4;
                    tesselatedMesh.vertexPos[f * 9 + 5] = v5;
                    tesselatedMesh.vertexPos[f * 9 + 6] = v6;
                    tesselatedMesh.vertexPos[f * 9 + 7] = v7;
                    tesselatedMesh.vertexPos[f * 9 + 8] = v8;
                    // compute normal for each vertex
                    Vec3 normal_vec0 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 0)].normalize();
                    Vec3 normal_vec2 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 1)].normalize();
                    Vec3 normal_vec4 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 2)].normalize();
                    Vec3 normal_vec6 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 3)].normalize();
                    Vec3 normal_vec1 = normal_vec0.add(normal_vec2).normalize();
                    Vec3 normal_vec3 = normal_vec2.add(normal_vec4).normalize();
                    Vec3 normal_vec5 = normal_vec4.add(normal_vec6).normalize();
                    Vec3 normal_vec7 = normal_vec6.add(normal_vec0).normalize();
                    Vec3 normal_vec8 = normal_vec7.add(normal_vec3).normalize();
                    // add normal of vertices to the current list                   
                    tesselatedMesh.vertexNormal[f * 9 + 0] = normal_vec0;
                    tesselatedMesh.vertexNormal[f * 9 + 1] = normal_vec1;
                    tesselatedMesh.vertexNormal[f * 9 + 2] = normal_vec2;
                    tesselatedMesh.vertexNormal[f * 9 + 3] = normal_vec3;
                    tesselatedMesh.vertexNormal[f * 9 + 4] = normal_vec4;
                    tesselatedMesh.vertexNormal[f * 9 + 5] = normal_vec5;
                    tesselatedMesh.vertexNormal[f * 9 + 6] = normal_vec6;
                    tesselatedMesh.vertexNormal[f * 9 + 7] = normal_vec7;
                    tesselatedMesh.vertexNormal[f * 9 + 8] = normal_vec8;
        
                    // compute new faces
                    tesselatedMesh.setQuadFace(f * 4 + 0, f * 9 + 0,
                            f * 9 + 1, f * 9 + 8, f * 9 + 7);
                    tesselatedMesh.setQuadFace(f * 4 + 1, f * 9 + 1,
                            f * 9 + 2, f * 9 + 3, f * 9 + 8);
                    tesselatedMesh.setQuadFace(f * 4 + 2, f * 9 + 3,
                            f * 9 + 4, f * 9 + 5, f * 9 + 8);
                    tesselatedMesh.setQuadFace(f * 4 + 3, f * 9 + 5,
                            f * 9 + 6, f * 9 + 7, f * 9 + 8);
                }
            }
        } else {
            // Triangle tesselation
            
            // subdivide until needed
            // for each level
            for (int l = 1; l <= tesselationLevel; l++) {
                // create new mesh and store away old one
                Mesh currentTesselatedMesh = tesselatedMesh;
                tesselatedMesh = new Mesh(
                        currentTesselatedMesh.getNumFaces() * 6,
                        currentTesselatedMesh.getNumFaces() * 4, false);
                // for each face ...
                for (int f = 0; f < currentTesselatedMesh.getNumFaces(); f++) {
                    // create new vertices
                    Vec3 v0 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 0)];
                    Vec3 v2 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 1)];
                    Vec3 v4 = currentTesselatedMesh.vertexPos[currentTesselatedMesh
                            .getFaceVertexIndex(f, 2)];
                    Vec3 v1 = v0.add(v2).scale(0.5);
                    Vec3 v3 = v2.add(v4).scale(0.5);
                    Vec3 v5 = v4.add(v0).scale(0.5);
                    // add vertices to the current list
                    tesselatedMesh.vertexPos[f * 6 + 0] = v0;
                    tesselatedMesh.vertexPos[f * 6 + 1] = v1;
                    tesselatedMesh.vertexPos[f * 6 + 2] = v2;
                    tesselatedMesh.vertexPos[f * 6 + 3] = v3;
                    tesselatedMesh.vertexPos[f * 6 + 4] = v4;
                    tesselatedMesh.vertexPos[f * 6 + 5] = v5;
                    //compute normal for each vertex
                    Vec3 normal_vec0 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 0)];
                    Vec3 normal_vec2 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 1)];
                    Vec3 normal_vec4 = currentTesselatedMesh.vertexNormal[currentTesselatedMesh.getFaceVertexIndex(f, 2)];
                    Vec3 normal_vec1 = normal_vec0.add(normal_vec2).scale(0.5);
                    Vec3 normal_vec3 = normal_vec2.add(normal_vec4).scale(0.5);
                    Vec3 normal_vec5 = normal_vec4.add(normal_vec0).scale(0.5);
                    // add normal of vertices to the current list                   
                    tesselatedMesh.vertexNormal[f * 6 + 0] = normal_vec0;
                    tesselatedMesh.vertexNormal[f * 6 + 1] = normal_vec1;
                    tesselatedMesh.vertexNormal[f * 6 + 2] = normal_vec2;
                    tesselatedMesh.vertexNormal[f * 6 + 3] = normal_vec3;
                    tesselatedMesh.vertexNormal[f * 6 + 4] = normal_vec4;
                    tesselatedMesh.vertexNormal[f * 6 + 5] = normal_vec5;
                    
                    //compute new faces
                    tesselatedMesh.setTriangleFace(f * 4 + 0, f * 6 + 0,
                            f * 6 + 1, f * 6 + 5);
                    tesselatedMesh.setTriangleFace(f * 4 + 1, f * 6 + 1,
                            f * 6 + 2, f * 6 + 3);
                    tesselatedMesh.setTriangleFace(f * 4 + 2, f * 6 + 3,
                            f * 6 + 4, f * 6 + 5);
                    tesselatedMesh.setTriangleFace(f * 4 + 3, f * 6 + 1,
                            f * 6 + 3, f * 6 + 5);
                }

            }

        }
        
    }
    
    public void initFromParser() {
        if(vertexNormal != null) {
            for(int i = 0; i < vertexNormal.length; i ++) {
                vertexNormal[i].setToNormalize();
            }
        }
    }
}
