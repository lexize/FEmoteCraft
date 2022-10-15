package org.lexize.femotecraft.models;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;
import java.util.Map;

@LuaWhitelist
public class KeyframesContainer {

    @LuaWhitelist
    public Map<Integer, Keyframe> pX = new HashMap<>();
    @LuaWhitelist
    public Map<Integer, Keyframe> pY = new HashMap<>();
    @LuaWhitelist
    public Map<Integer, Keyframe> pZ = new HashMap<>();

    @LuaWhitelist
    public Map<Integer, Keyframe> rX = new HashMap<>();
    @LuaWhitelist
    public Map<Integer, Keyframe> rY = new HashMap<>();
    @LuaWhitelist
    public Map<Integer, Keyframe> rZ = new HashMap<>();

    @LuaWhitelist
    public Map<Integer, Keyframe> bendValue;
    @LuaWhitelist
    public Map<Integer, Keyframe> bendDirection;

    public static KeyframesContainer ofPart(KeyframeAnimation.StateCollection part) {
        KeyframesContainer c = new KeyframesContainer();

        for (KeyframeAnimation.KeyFrame kf:
             part.x.getKeyFrames()) {
            c.pX.put(kf.tick, new Keyframe(kf));
        }

        for (KeyframeAnimation.KeyFrame kf:
                part.y.getKeyFrames()) {
            c.pY.put(kf.tick, new Keyframe(kf));
        }

        for (KeyframeAnimation.KeyFrame kf:
                part.z.getKeyFrames()) {
            c.pZ.put(kf.tick, new Keyframe(kf));
        }

        for (KeyframeAnimation.KeyFrame kf:
                part.pitch.getKeyFrames()) {
            c.rX.put(kf.tick, new Keyframe(kf));
        }

        for (KeyframeAnimation.KeyFrame kf:
                part.yaw.getKeyFrames()) {
            c.rY.put(kf.tick, new Keyframe(kf));
        }

        for (KeyframeAnimation.KeyFrame kf:
                part.roll.getKeyFrames()) {
            c.rZ.put(kf.tick, new Keyframe(kf));
        }

        if (part.isBendable) {
            c.bendValue = new HashMap<>();
            c.bendDirection = new HashMap<>();
            for (KeyframeAnimation.KeyFrame kf:
                    part.bend.getKeyFrames()) {
                c.bendValue.put(kf.tick, new Keyframe(kf));
            }

            for (KeyframeAnimation.KeyFrame kf:
                    part.bendDirection.getKeyFrames()) {
                c.bendDirection.put(kf.tick, new Keyframe(kf));
            }
        }
        return c;
    }

    @LuaWhitelist
    public Object __index(String k) {
        return switch (k) {
            case "pX", "x" -> pX;
            case "pY", "y" -> pY;
            case "pZ", "z" -> pZ;

            case "rX", "pitch" -> rX;
            case "rY", "yaw"   -> rY;
            case "rZ", "roll"  -> rZ;

            case "bend", "bendValue" -> bendValue;
            case "direction", "bendDirection" -> bendDirection;
            default -> null;
        };
    }
}
