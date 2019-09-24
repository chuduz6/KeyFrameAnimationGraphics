/**
 * A 4x4 matrix useful when defining transformations.
 * @author fabio
 */
public class Mat4 {
    /**
     * Components
     */
    public double       d[][];
    
    /**
     * Default constrctor - creates an identiy matrix, not a zero one!
     */
    public Mat4() {
        d = new double[4][4];
        d[0][0] = 1;
        d[1][1] = 1;
        d[2][2] = 1;
        d[3][3] = 1;
    }
    
    /**
     * Flatten data in row major order.
     */
    public double[] getFlatDataRowMajor() {
        double r[] = new double[16];
        r[ 0] = d[0][0]; r[ 1] = d[0][1]; r[ 2] = d[0][2]; r[ 3] = d[0][3];  
        r[ 4] = d[1][0]; r[ 5] = d[1][1]; r[ 6] = d[1][2]; r[ 7] = d[1][3];  
        r[ 8] = d[2][0]; r[ 9] = d[2][1]; r[10] = d[2][2]; r[11] = d[2][3];  
        r[12] = d[3][0]; r[13] = d[3][1]; r[14] = d[3][2]; r[15] = d[3][3];  
        return r;
    }
    
    /**
     * Flatten data ni colum major order.
     */
    public double[] getFlatDataColumnMajor() {
        return transpose().getFlatDataRowMajor();
    }
    
    /**
     * Transform a point. Ignores w component (use project for perspective tranforms).
     */
    public Vec3 transform(Vec3 p) {
        Vec3 tp = new Vec3();
        
        tp.x = d[0][0]*p.x + d[0][1]*p.y + d[0][2]*p.z + d[0][3]; 
        tp.y = d[1][0]*p.x + d[1][1]*p.y + d[1][2]*p.z + d[1][3]; 
        tp.z = d[2][0]*p.x + d[2][1]*p.y + d[2][2]*p.z + d[2][3]; 
        
        return tp;
    }
    
    /**
     * Grab the unnormalized 3x3 inverse transpose to trasnform normals.
     * Normls are not normalized after this!
     * See Shirley p. 150
     */
    public Mat4 normalTransform() {
        Mat4 it = new Mat4();
        
        it.d[0][0] = d[1][1]*d[2][2] - d[1][2]*d[2][1];
        it.d[0][1] = d[1][2]*d[2][0] - d[1][0]*d[2][2];
        it.d[0][2] = d[1][0]*d[2][1] - d[1][1]*d[2][0];

        it.d[1][0] = d[0][2]*d[2][1] - d[0][1]*d[2][2];
        it.d[1][1] = d[0][0]*d[2][2] - d[0][2]*d[2][0];
        it.d[1][2] = d[0][1]*d[2][0] - d[0][0]*d[2][1];
        
        it.d[2][0] = d[0][1]*d[1][2] - d[0][2]*d[1][1];
        it.d[2][1] = d[0][2]*d[1][0] - d[0][0]*d[1][2];
        it.d[2][2] = d[0][0]*d[1][1] - d[0][1]*d[1][0];
        
        return it;
    }
    
    /**
     * Multiply this matrix by m0. m0 is on the right.
     */
    public Mat4 multiply(Mat4 m0) {
        Mat4 m = new Mat4();
        for(int i = 0; i < 4; i ++) {
            for(int j = 0; j < 4; j ++) {
                m.d[i][j] = 0;
                for(int k = 0; k < 4; k ++) {
                    m.d[i][j] += d[i][k]*m0.d[k][j];
                }
            }
        }
        return m;
    }
    
    /**
     * transpose
     */
    public Mat4 transpose() {
        Mat4 m = new Mat4();
        for(int i = 0; i < 4; i ++) {
            for(int j = 0; j < 4; j ++) {
                m.d[i][j] = d[j][i];
            }
        }
        return m;        
    }
    
    /**
     * Create translation matrix
     */
    public static Mat4 translationMatrix(Vec3 t) {
        Mat4 m = new Mat4();
        m.d[0][3] = t.x;
        m.d[1][3] = t.y;
        m.d[2][3] = t.z;
        return m;
    }

    /**
     * Create rotation matrix around x
     */
    public static Mat4 xrotationMatrix(double r) {
        double cr = Math.cos(r);
        double sr = Math.sin(r);
        Mat4 m = new Mat4();
        m.d[1][1] = cr;
        m.d[1][2] = -sr;
        m.d[2][1] = sr;
        m.d[2][2] = cr;
        return m;        
    }
    
    /**
     * Create rotation matrix around y
     */
    public static Mat4 yrotationMatrix(double r) {
        double cr = Math.cos(r);
        double sr = Math.sin(r);
        Mat4 m = new Mat4();
        m.d[0][0] = cr;
        m.d[0][2] = sr;
        m.d[2][0] = -sr;
        m.d[2][2] = cr;
        return m;        
    }
    
    /**
     * Create rotation matrix around z
     */
    public static Mat4 zrotationMatrix(double r) {
        double cr = Math.cos(r);
        double sr = Math.sin(r);
        Mat4 m = new Mat4();
        m.d[0][0] = cr;
        m.d[0][1] = -sr;
        m.d[1][0] = sr;
        m.d[1][1] = cr;
        return m;        
    }
    
    /**
     * Create scaling matrix
     */
    public static Mat4 scalingMatrix(Vec3 s) {
        Mat4 m = new Mat4();
        m.d[0][0] = s.x;
        m.d[1][1] = s.y;
        m.d[2][2] = s.z;
        return m;        
    }
    
    /**
     * creates a copy of the current trasnform and return it
     */
    public Mat4 copy() {
        Mat4 m = new Mat4();
        for(int i = 0; i < 4; i ++) {
            for(int j = 0; j < 4; j ++) {
                m.d[i][j] = d[i][j];
            }
        }
        return m;        
    }
}