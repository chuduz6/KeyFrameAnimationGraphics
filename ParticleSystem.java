import java.util.Random;

/**
 * A simple class for handling particle systems.
 * 
 * For this assignment we will simply the memory management by assuming that
 * there are always numParticles stored in the particles array.
 * This means that is a particle dies, another one takes its place.
 * 
 * Particles are generated from the source which initializes the various
 * particle properties. The particle dynamics follows the Newton law
 * under the force generate by the sum of the forces array.
 * 
 * Particles are killed based on their age.
 * In partcular particle alwwys live to a minAge and always die at maxAge.
 * For any age in the middle, particles die randomly.
 * 
 * The value lastTime is used to keep track of the elapsed time
 * from last update.
 */
public class ParticleSystem extends HierarchyNode {
    /**
     * Desired number of particles
     */
    public int                      numParticles;
    
    /**
     * Particle geometry
     */
    public Surface                  geometry;
    
    /**
     * List of particles
     */
    public ParticleState[]          particles;
    
    /**
     * Source of the particle system
     */
    public ParticleSource           source;
    
    /**
     * Forces acting on the poarticles
     */
    public ParticleForce            forces[];
    
    /**
     * Minimum particle age
     */
    public double                   ageMin;

    /**
     * Maximum particle age
     */
    public double                   ageMax;
    
    /**
     * Last time
     */
    private double                  lastTime;
    
    /**
     * Random number generator for the particle systems.
     */
    public final Random              random = new Random(0);

    /**
     * Just tesselate the geometry and initialize the system by running
     * the animation at time 0.
     */
    public void tesselate() {
        geometry.tesselate();
        animate(0);
    }

    /**
     * Creates/Kills particle based on their age.
     * Also updates the dynamics when necessary.
     * If the particles array is null, this function should
     * just create a bunch of new particles and not run their dynamics.
     */
    public void animate(double time) {
        throw new NotImplementedException();
    }
    
    /**
     * Check if the particle idx is dead based on its age.
     */
    public boolean isParticleDead(double time, int idx) {
        throw new NotImplementedException();
    }
    
    /**
     * Run particle dynamics and update the particles.
     */
    public void updateParticleState(double time, int idx) {
        throw new NotImplementedException();
    }

    /**
     * Reset the animation.
     */
    public void restartAnimation() {
        particles = null;
    }
}

/**
 * Describe all the propeties associated with this particle.
 */
class ParticleState {
    /** Particle position */
    public Vec3 position;
    /** Particle velocity */
    public Vec3 velocity;
    /** Particle acceleration */
    public Vec3 acceleration;
    /** Particle color */
    public Color color;
    /** Particle birthday */
    public double creationTime;
    /** Particle mass */
    public double mass;
    /** Particle size */
    public double size;
    
    /**
     * Initialize a new particle born at time time.
     */
    public ParticleState(double time) {
        position = new Vec3();
        velocity = new Vec3();
        acceleration = new Vec3();
        color = new Color(1,1,1);
        creationTime = time;
        mass = 1;
        size = 1;
    }
}

/** Abstract class for particle sources */
abstract class ParticleSource {
   public abstract void createParticle(ParticleState state, Random random);
}

/** A simple particle source that generates particles at position position
 * with a random velocity direction and random velocity magnitude between 
 * velocityMin and velocityMax.
 * Particles are also embellished with random colors calculated as
 * the sum of color and a random color of magniture colorVariation.
 */
class ParticlePointSource extends ParticleSource {
    /** course position */
    public Vec3 position;
    /** minimum velocity magniture */
    public double velocityMin;
    /** maximum velocity magniture */
    public double velocityMax;
    /** particle base color */
    public Color color;
    /** variation to add to the particle base color */
    public Color colorVariation;
    
    /** initialiaze the particle */
    public void createParticle(ParticleState state, Random random) {
        throw new NotImplementedException();
    }
}

/** A force applied to the particles */
abstract class ParticleForce {
    /** Evaluate the force applied to the particles */
    public abstract Vec3 evaluate(ParticleState state, Random random);
}

/** A constant force applied to the particles */
class ParticleConstantForce extends ParticleForce {
    /** Force vector */
    public Vec3 force;
    
    /** In this case simply copy the force */
    public Vec3 evaluate(ParticleState state, Random random) {
        return force;
    }
}