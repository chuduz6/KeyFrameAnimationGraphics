import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Provides rendering through OpenGL.
 * Uses jogl as the GL layer.
 * @author fabio
 */
public class GLRenderPanel extends JPanel implements GLEventListener {
    /** serialID */
    private static final long serialVersionUID = 132277839319035748L;
    
    /** view mode string used for ui */
    public static final String[] DRAWMODE_TABLE = { "solid", "line", "point", "bones"  };
    /** view mode solid */
    public static final int DRAWMODE_SOLID = 0;
    /** view mode line */
    public static final int DRAWMODE_LINE = 1;
    /** view mode point */
    public static final int DRAWMODE_POINT = 2;
    /** view mode bones */
    public static final int DRAWMODE_BONES = 3;
    
    /** view mode string used for ui */
    public static final String[] LIGHTINGMODE_TABLE = { "none", "flat", "smooth"  };
    /** view mode solid */
    public static final int LIGHTINGMODE_NONE = 0;
    /** view mode line */
    public static final int LIGHTINGMODE_FLAT = 1;
    /** view mode point */
    public static final int LIGHTINGMODE_SMOOTH = 2;
    
	/**
	 * Scene
	 */
	protected Scene scene;
	
	/**
	 * GL drawing canvas
	 */
	protected GLCanvas canvas;

	/**
	 * glu
	 */
	protected GLU glu;
	
    /**
     * View mode
     */
    protected int viewMode;
    
    /**
     * Remove backfaces
     */
    protected boolean viewCull;
    
    /**
     * Orthographic camera
     */
    protected boolean viewPerspective;
	
    /**
     * Lighting mode
     */
    protected int viewLighting;
    
    /**
     * Draw particle as points
     */
    protected boolean particleAsPoints;
    
	/**
	 * Default constructor
	 */
	public GLRenderPanel(Scene nScene) {
		scene = nScene;
        viewMode = DRAWMODE_SOLID;
        viewCull = true;
        viewPerspective = true;
        viewLighting = LIGHTINGMODE_SMOOTH;
        particleAsPoints = false;
		
		// Window size
		setSize(scene.camera.xResolution, scene.camera.yResolution);
		setPreferredSize(new Dimension(scene.camera.xResolution, scene.camera.yResolution));
		
		// Canvas
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		canvas = new GLCanvas(caps);
		
		// Add canvas
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		
		// Link this object as a listener for the canvas
		canvas.addGLEventListener(this);
	}
	
	/**
	 * Set scene
	 */
	public void setScene(Scene nScene) {
		scene = nScene;
		redraw();
	}
    
    /**
     * Set view mode
     */
    public void setViewMode(int nViewMode) {
        viewMode = nViewMode;
        redraw();
    }
    
    /**
     * Set view cull
     */
    public void setViewCull(boolean nViewCull) {
        viewCull = nViewCull;
        redraw();
    }
	
    /**
     * Set view perspective
     */
    public void setViewPerspective(boolean nViewPerspective) {
        viewPerspective = nViewPerspective;
        redraw();
    }
    
    /**
     * Set view lighting
     */
    public void setViewLighting(int nViewLighting) {
        viewLighting = nViewLighting;
        redraw();
    }
    
    /**
     * Set view particles as points
     */
    public void setViewParticles(boolean nViewParticles) {
        particleAsPoints = nViewParticles;
        redraw();
    }
    
	/**
	 * Redraw the view. Should be called after a change occurs in the scene.
	 */
	public void redraw() {
		canvas.repaint();
	}

	/**
	 * GL init code. Anything that will happen only ones should go here.
	 * @see net.java.games.jogl.GLEventListener#init(net.java.games.jogl.GLDrawable)
	 */
	public void init(GLAutoDrawable glD) {
		// get GL
		GL gl = glD.getGL();
		
		// set up GL flags
        
        // culling
        gl.glCullFace(GL.GL_BACK);
        
        // interpolation
        gl.glShadeModel(GL.GL_SMOOTH);
        
        // normalized normals
        gl.glEnable(GL.GL_NORMALIZE);
        
        // depth test
		gl.glEnable(GL.GL_DEPTH_TEST);
        
        // clear
		gl.glClearColor(0,0,0,0);
		gl.glClearDepth(1);
		
		// glu
		glu = new GLU();
	}

	/**
	 * draw the current scene using GL
	 * @see net.java.games.jogl.GLEventListener#display(net.java.games.jogl.GLDrawable)
	 */
	public void display(GLAutoDrawable glD) {
		// get GL
		GL gl = glD.getGL();
		
		// clear
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        // set display mode
        switch(viewMode) {
            case DRAWMODE_SOLID:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                break;
            case DRAWMODE_LINE:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                break;
            case DRAWMODE_POINT:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_POINT);
                break;
            case DRAWMODE_BONES:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                break;
            default:                
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                break;
        }
		
        // set culling
        if(viewCull) {
            gl.glEnable(GL.GL_CULL_FACE);
        } else {
            gl.glDisable(GL.GL_CULL_FACE);            
        }
        
        // clear matrices
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // set lights
        drawLights(gl,glu,scene.lights);
        
		// set camera projection
		drawCamera(gl, glu, scene.camera);
        
		// hierarchically draw using gl
        drawNode(gl, scene.hierarchyRoot);
	}
    
    /**
     * GL view commands.
     */
    protected void drawCamera(GL gl, GLU glu, Camera c) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if(viewPerspective) {
            glu.gluPerspective(2 * scene.camera.yfov * 180 / Math.PI, 
                    scene.camera.xfov/scene.camera.yfov, 0.01, 10000);
        } else {
            double ofov = Math.tan(scene.camera.yfov);
            gl.glOrtho(-2*ofov,2*ofov,-2*ofov,2*ofov,0.01,10000);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(c.origin.x,
                      c.origin.y,
                      c.origin.z,
                      c.origin.add(scene.camera.z.negate()).x,
                      c.origin.add(scene.camera.z.negate()).y,
                      c.origin.add(scene.camera.z.negate()).z,
                      c.y.x,
                      c.y.y,
                      c.y.z);        
    }
    
    /**
     * GL lighting command.
     */
    protected void drawLights(GL gl, GLU glu, Light[] lights) {
        if(viewLighting == LIGHTINGMODE_NONE) {
            // disable lighting
            gl.glDisable(GL.GL_LIGHTING);
        } else {
            // enable lighting
            gl.glEnable(GL.GL_LIGHTING);
            
            // set lights
            for(int i = 0; i < Math.min(lights.length,8); i ++) {
                gl.glEnable(GL.GL_LIGHT0+i);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_AMBIENT,
                        new float[]{0,0,0,1},0);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_DIFFUSE,
                        new float[]{(float)lights[i].intensity.r,(float)lights[i].intensity.g,(float)lights[i].intensity.b,1},0);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_SPECULAR,
                       new float[]{(float)lights[i].intensity.r,(float)lights[i].intensity.g,(float)lights[i].intensity.b,1},0);
                if(lights[i] instanceof PointLight) {
                    gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_POSITION,
                           new float[]{(float)((PointLight)lights[i]).position.x,
                                       (float)((PointLight)lights[i]).position.y,
                                       (float)((PointLight)lights[i]).position.z,1},0);
                }
            }
        }
    }
    
    /**
     * GL hierarchy commands.
     */
    protected void drawNode(GL gl, HierarchyNode n) {
        if(n instanceof Transform) {
            Transform t = (Transform)n;
            drawTransform(gl, t);
        } else if(n instanceof SkinnedMesh && viewMode == DRAWMODE_BONES) {
            SkinnedMesh s = (SkinnedMesh)n; 
            drawMeshGeometryWithBoneColor(gl,s.tesselatedMesh,s);
            drawBones(gl, s.bones);
        } else if(n instanceof Surface) {
            Surface s = (Surface)n; 
            drawMaterial(gl,s.material);
            drawMeshGeometry(gl,s.tesselatedMesh);
        } else if(n instanceof ParticleSystem) {
            ParticleSystem s = (ParticleSystem)n;
            drawParticleSystem(gl,s);
        } else {
            System.out.println("Should never have gotten here!");
        }
    }

    /**
     * Draw bones
     */
    private void drawBones(GL gl, Bone[] bones) {
        gl.glPushAttrib(GL.GL_LIGHTING_BIT);
        gl.glDisable(GL.GL_LIGHTING);
        for(int b = 0; b < bones.length; b ++) {
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glMultMatrixd(bones[b].m.getFlatDataColumnMajor(),0);
            //gl.glScaled(bones[b].size.x, bones[b].size.y, bones[b].size.z);
            gl.glBegin(GL.GL_LINES);
            gl.glColor3d(bones[b].color.r, bones[b].color.g, bones[b].color.b);
            
            gl.glVertex3d(1,0,1);
            gl.glVertex3d(1,1,1);
            gl.glVertex3d(-1,0,1);
            gl.glVertex3d(-1,1,1);
            gl.glVertex3d(1,0,-1);
            gl.glVertex3d(1,1,-1);
            gl.glVertex3d(-1,0,-1);
            gl.glVertex3d(-1,1,-1);
            
            gl.glVertex3d(1,0,1);
            gl.glVertex3d(-1,0,1);
            gl.glVertex3d(-1,0,1);
            gl.glVertex3d(-1,0,-1);
            gl.glVertex3d(-1,0,-1);
            gl.glVertex3d(1,0,-1);
            gl.glVertex3d(1,0,-1);
            gl.glVertex3d(1,0,1);
            
            gl.glVertex3d(1,1,1);
            gl.glVertex3d(-1,1,1);
            gl.glVertex3d(-1,1,1);
            gl.glVertex3d(-1,1,-1);
            gl.glVertex3d(-1,1,-1);
            gl.glVertex3d(1,1,-1);
            gl.glVertex3d(1,1,-1);
            gl.glVertex3d(1,1,1);
            
            gl.glEnd();
            gl.glPopMatrix();
        }
        gl.glPopAttrib();
    }

    /**
     * Draws a particle system
     */
    private void drawParticleSystem(GL gl, ParticleSystem s) {
        if(particleAsPoints) {
            gl.glPushAttrib(GL.GL_LIGHTING_BIT);
            gl.glDisable(GL.GL_LIGHTING);
            gl.glColor3d(1,1,1);
            gl.glBegin(GL.GL_POINTS);
            for(int p = 0; p < s.particles.length; p ++) {
                ParticleState ps = s.particles[p];
                gl.glColor3d(ps.color.r,ps.color.g,ps.color.b);
                gl.glNormal3d(1,1,1);
                gl.glVertex3d(ps.position.x,ps.position.y,ps.position.z);
            }
            gl.glEnd();
            gl.glPopAttrib();
        } else {
            gl.glMatrixMode(GL.GL_MODELVIEW);
            Mesh m = s.geometry.tesselatedMesh;
            Phong phong = new Phong();
            phong.specular = new Color();
            phong.exponent = 10;
            phong.diffuse = new Color(1,1,1);
            drawMaterial(gl,phong);
            for(int p = 0; p < s.particles.length; p ++) {
                ParticleState ps = s.particles[p];
                gl.glPushMatrix();
                gl.glTranslated(ps.position.x,ps.position.y,ps.position.z);
                gl.glScaled(ps.size,ps.size,ps.size);
                phong.diffuse.set(ps.color);
                drawMaterial(gl,phong);
                drawMeshGeometry(gl,m);
                gl.glPopMatrix();
            }
        }
    }

    /**
     */
    private void drawMeshGeometry(GL gl, Mesh m) {
        boolean useFaceNormal;
        if(viewLighting != LIGHTINGMODE_SMOOTH || m.vertexNormal == null) {
            useFaceNormal = true;
        } else {
            useFaceNormal = false;
        }
        for(int f = 0; f < m.getNumFaces(); f ++) {
            if(m.quads) {
                gl.glBegin(GL.GL_QUADS);                                    
            } else {
                gl.glBegin(GL.GL_TRIANGLES);
            }
            if(useFaceNormal) {
                Vec3 v0 = m.vertexPos[m.getFaceVertexIndex(f,1)].sub(m.vertexPos[m.getFaceVertexIndex(f,0)]);
                Vec3 v1 = m.vertexPos[m.getFaceVertexIndex(f,2)].sub(m.vertexPos[m.getFaceVertexIndex(f,0)]);
                Vec3 faceN = v0.cross(v1).normalize();
                gl.glNormal3d(faceN.x,faceN.y,faceN.z);
            }
            for(int v = 0; v < ((m.quads)?4:3); v ++) {
                if(!useFaceNormal) {
                    Vec3 N = m.vertexNormal[m.getFaceVertexIndex(f, v)];
                    gl.glNormal3d(N.x,N.y,N.z);
                }
                Vec3 P = m.vertexPos[m.getFaceVertexIndex(f, v)];
                gl.glVertex3d(P.x,P.y,P.z);
            }
            gl.glEnd();
        }
    }

    /**
     */
    private void drawMeshGeometryWithBoneColor(GL gl, Mesh m, SkinnedMesh sm) {
        gl.glPushAttrib(GL.GL_LIGHTING_BIT);
        gl.glDisable(GL.GL_LIGHTING);
        for(int f = 0; f < m.getNumFaces(); f ++) {
            if(m.quads) {
                gl.glBegin(GL.GL_QUADS);                                    
            } else {
                gl.glBegin(GL.GL_TRIANGLES);
            }
            Color color = new Color();
            for(int v = 0; v < ((m.quads)?4:3); v ++) {
                int idx = m.getFaceVertexIndex(f, v);
                color.set(0,0,0);
                for(int b = 0; b < sm.bones.length; b ++) {
                    color.setToAdd(sm.bones[b].color.scale(sm.vertexWeights[idx][b]));
                }
                gl.glColor3d(color.r,color.g,color.b);
                Vec3 P = m.vertexPos[idx];
                gl.glVertex3d(P.x,P.y,P.z);
            }
            gl.glEnd();
        }
        gl.glPopAttrib();
    }

    /**
     * @param gl
     * @param glu
     * @param t
     */
    private void drawTransform(GL gl, Transform t) {
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glTranslated(t.translation.x, t.translation.y, t.translation.z);
        gl.glScaled(t.scale.x, t.scale.y, t.scale.z);
        gl.glRotated(t.rotation.x * 180 / Math.PI, 1, 0, 0);
        gl.glRotated(t.rotation.y * 180 / Math.PI, 0, 1, 0);
        gl.glRotated(t.rotation.z * 180 / Math.PI, 0, 0, 1);
        for(int i = 0; i < t.children.length; i ++) {
            drawNode(gl,t.children[i]);
        }
        gl.glPopMatrix();
    }
    
    /**
     * GL material and color commands
     */
    public void drawMaterial(GL gl, Material m) {
        Phong p = m.getApproximatePhong();
        gl.glColor3d(p.diffuse.r, p.diffuse.g, p.diffuse.b);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT_AND_DIFFUSE, 
                new float[]{(float)p.diffuse.r, (float)p.diffuse.g, (float)p.diffuse.b, 0},0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR, 
                new float[]{(float)p.specular.r, (float)p.specular.g, (float)p.specular.b, 0},0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS, 
                new float[]{(float)p.exponent},0);
    }

	/**
	 * Called after display changed - - for now unused since we locked it previously.
	 * @see net.java.games.jogl.GLEventListener#displayChanged(net.java.games.jogl.GLDrawable, boolean, boolean)
	 */
	public void displayChanged(GLAutoDrawable glD, boolean arg1, boolean arg2) {
	}

	/**
	 * Window resize - for now unused since we locked window size previously.
	 * @see net.java.games.jogl.GLEventListener#reshape(net.java.games.jogl.GLDrawable, int, int, int, int)
	 */
	public void reshape(GLAutoDrawable glD, int arg1, int arg2, int arg3, int arg4) {
	}
	
}