/**
 * Sphere.
 * @author fabio
 */
public class Sphere extends Surface {
	/**
	 * Center.
	 */
	public Vec3						position;
	/**
	 * Radius.
	 */
	public double					radius;
	
	/**
	 * Tesselate surface
	 */
	public void tesselate() {
        // create vertices on a unit sphere
        int uSub = (int)Math.pow(2,tesselationLevel+2);
        int vSub = (int)Math.pow(2,tesselationLevel+2);
        int nVertices = (uSub+1)*(vSub+1);
        //int nFaces = (uSub)*(vSub); // quads
        int nFaces = (uSub)*(vSub) * 2; // triangle
        
        Vec3[][] vT = new Vec3[uSub+1][vSub+1];
        for(int iu = 0; iu < uSub+1; iu ++) {
            for(int iv = 0; iv < vSub+1; iv ++) {
                vT[iu][iv] = new Vec3();
                if(iv == 0) {
                    vT[iu][iv].x = 0;
                    vT[iu][iv].y = -1;
                    vT[iu][iv].z = 0;                    
                } else if(iv == vSub) {
                    vT[iu][iv].x = 0;
                    vT[iu][iv].y = 1;
                    vT[iu][iv].z = 0;                                        
                } else {
                    double u = (double)iu/(double)(uSub);
                    double v = (double)iv/(double)(vSub);
                    vT[iu][iv].x = Math.sin((1-v)*Math.PI)*Math.cos(u*Math.PI*2);
                    vT[iu][iv].y = Math.cos((1-v)*Math.PI);
                    vT[iu][iv].z = Math.sin((1-v)*Math.PI)*Math.sin(u*Math.PI*2);
                }
            }
        }
        
        // allocate
	    //tesselatedMesh = new Mesh(nVertices,nFaces,true); // quads
        tesselatedMesh = new Mesh(nVertices,nFaces,false); // triangles
        
        // copy vertices/normals in array
        for(int iu = 0; iu < uSub+1; iu ++) {
            for(int iv = 0; iv < vSub+1; iv ++) {
                tesselatedMesh.vertexPos[iu*(vSub+1)+iv].set(vT[iu][iv]);
                tesselatedMesh.vertexPos[iu*(vSub+1)+iv].setToScale(radius);
                tesselatedMesh.vertexPos[iu*(vSub+1)+iv].setToAdd(position);
                tesselatedMesh.vertexNormal[iu*(vSub+1)+iv].set(vT[iu][iv]);
                tesselatedMesh.vertexNormal[iu*(vSub+1)+iv].setToNormalize();
            }
        }
        
        // create faces
        /*
        // quads
        int curF = 0;
        for(int iu = 0; iu < uSub; iu ++) {
            for(int iv = 0; iv < vSub; iv ++) {
                tesselatedMesh.faces[curF].setQuadVertices(
                        iu*(vSub+1)+iv,iu*(vSub+1)+iv+1,
                        (iu+1)*(vSub+1)+iv+1,(iu+1)*(vSub+1)+iv);
                curF ++;
            }
        }
        */
        
        //triangles
        int curF = 0;
        for(int iu = 0; iu < uSub; iu ++) {
            for(int iv = 0; iv < vSub; iv ++) {
                tesselatedMesh.setTriangleFace(curF,
                        iu*(vSub+1)+iv,iu*(vSub+1)+iv+1,
                        (iu+1)*(vSub+1)+iv+1);
                curF ++;
                tesselatedMesh.setTriangleFace(curF,
                        iu*(vSub+1)+iv,(iu+1)*(vSub+1)+iv+1,
                        (iu+1)*(vSub+1)+iv);
                curF ++;
            }
        }
	}
}
