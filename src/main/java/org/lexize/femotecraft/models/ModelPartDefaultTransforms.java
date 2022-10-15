package org.lexize.femotecraft.models;

import dev.kosmx.playerAnim.core.util.Vec3f;

public enum ModelPartDefaultTransforms {
    head(Vec3f.ZERO, Vec3f.ZERO,null),
    rightLeg(new Vec3f(-1.9f,12,.1f), Vec3f.ZERO, Vec3f.ZERO),
    rightArm(new Vec3f(-5,2,0), Vec3f.ZERO, Vec3f.ZERO),
    leftArm(new Vec3f(5,2,0), Vec3f.ZERO, Vec3f.ZERO),
    leftLeg(new Vec3f(1.9f,12,.1f), Vec3f.ZERO, Vec3f.ZERO),
    body(Vec3f.ZERO, Vec3f.ZERO,Vec3f.ZERO),
    ;
    public final Vec3f position;
    public final Vec3f rotation;
    public final Vec3f bend;

    ModelPartDefaultTransforms(Vec3f position, Vec3f rotation, Vec3f bend) {
        this.position = position;
        this.rotation = rotation;
        this.bend = bend;
    }
}
