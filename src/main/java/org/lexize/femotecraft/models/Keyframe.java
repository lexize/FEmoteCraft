package org.lexize.femotecraft.models;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class Keyframe {

    @LuaWhitelist
    public int tick;
    @LuaWhitelist
    public float value;
    @LuaWhitelist
    public String ease;

    public Keyframe(KeyframeAnimation.KeyFrame kf) {
        tick = kf.tick;
        value = kf.value;
        ease = kf.ease.name();
    }

    @LuaWhitelist
    public Object __index(String k) {
        return switch(k) {
            case "tick" -> tick;
            case "value" -> value;
            case "ease" -> ease;

            default -> null;
        };
    }
}
