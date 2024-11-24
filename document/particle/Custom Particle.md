## Custom Particle

### File

---

#### Space
1. Local Position
    - When enabled, The particle will always move in local space relative to the emitter. When attached to an entity, this means that all particles will move along with the entity. 
    - When disabled，particles are emitted relative to the emitter, then simulate independently of the emitter in the world
    - Enabling this will prevent collisions with the world from working. 
2. Local Rotation
    - Rotate the local space along with the entity that it is attached to. See local position
    - Local position needs to be enabled for local rotation to work
3. Local Velocity
    - when enabled, the emitter's velocity will be added to the initial particle velocity.

### Emitter

---

1. Spawn Amount
   * Mode 
     - Steady
       + Particles are spawned steadily during the lifetime of the emitter 
     - Instant 
       + All particles are spawned instantly at the start of the emitter's lifetime 
     - Manual 
       + Particles are spawned manually, independent of the emitter. This is used for some vanilla effects, and for particle effects that are triggered by events using the "particle" type.
   * Rate
     - ***Molang Per Emitter*** Evaluated each tick 
     - How often a particle is emitted in particles/second. 
   * Amount
     - ***Molang Per Emitter*** Evaluated each tick 
     - How many particles are spawned at once?
   * Maximum 
     - ***Molang Per Emitter*** Evaluated each tick 
     - The Maximum number of particles that can be active before the emitter stops spawning new ones.
2. Emitter Lifetime
   * Control how long the emitter lasts and whether it should loop.
   * Mode 
     - Looping 
       + The Emitter will loop until it is removed. 
     - Once 
       + Emitter will execute once, and once the lifetime ends or the number of particles allowed to emit have emitted, the emitter expires. 
     - Expression 
       + Emitter will turn "on" when the activation expression is non-zero, and will turn "off" when it's zero. This is useful for situations like driving an entity-attached emitter from an entity variable
   * Active Time 
     - ***Molang Per Emitter*** Evaluated each loop 
     - How long the emitter will be active for. 
   * Sleep Time 
     - ***Molang Per Emitter*** Evaluated each loop 
     - How long the emitter will pause and not emit particles when used in looping mode. 
   * Activation 
     - ***Molang Per Emitter*** Evaluated each tick
     - When the expression is non-zero, the emitter will start emitting particles
   * Expiration 
     - ***Molang Per Emitter*** Evaluated each tick
     - Emitter will expire if the expression is non-zero
3. Spawn Shape
   * Control at which position and in which shape particles are spawned.
   * Mode 
     - Point 
       + Emit particles at a single point at the position of the emitter.
     - Sphere 
       + Emit particle in a spherical shape. 
     - Box 
       + Emit particles in a box shape 
     - Disc 
       + Emit particles in a disc shape. 
     - Entity Bounding Box 
       + Emit particles in a box which size adapts to the collision box of the entity that the particle is played on. Only works on particles that are played client-side.
   * Offset 
     - ***Molang Per Particle*** Evaluated once
     - Specify the offset from the emitter to emit each particle. 
   * Radius 
     - ***Molang Per Particle*** Evaluated once
     - Sphere or disc radius. 
   * Box Size 
     - ***Molang Per Particle*** Evaluated once
     - Half-dimensions of the box formed around the emitter. 
   * Plane Normal 
     - ***Molang Per Particle*** Evaluated once 
     - Specifies the normal of the disc plane, the disc will be perpendicular to this direction
   * Surface Only 
     - Emit only from the surface of the shape.
### Texture & UV

---

#### Texture
1. Texture
   - Path to the texture, starting from the texture pack. Example∶ textures/particle/snowflake. 
   - The path should always use forward slashes, and should not include the file extension. 
2. Image 
   - In VS Code, the image gets loaded from the provided texture path automatically. To use this, ensure that you open the entire texture pack folder as a project in VSCode, not just the individual particle file. 
   - In the web app, you need to upload your PNG file to preview it, unless you are using one of the included vanilla particle files. 
   - Painting on the texture is supported using the provided editing tools.