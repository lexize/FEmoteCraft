package org.lexize.femotecraft;

import dev.kosmx.playerAnim.api.AnimUtils;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.core.util.Vec3f;
import io.github.kosmx.emotes.api.events.client.ClientEmoteAPI;
import io.github.kosmx.emotes.api.events.client.ClientEmoteEvents;
import io.github.kosmx.emotes.api.events.server.ServerEmoteAPI;
import io.github.kosmx.emotes.executor.EmoteInstance;
import io.github.kosmx.emotes.executor.dataTypes.IGetters;
import io.github.kosmx.emotes.executor.emotePlayer.IEmotePlayer;
import io.github.kosmx.emotes.executor.emotePlayer.IEmotePlayerEntity;
import io.github.kosmx.emotes.main.emotePlay.EmotePlayer;
import net.minecraft.client.model.ModelPart;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@LuaWhitelist
public class FEmoteCraft implements FiguraAPI {

    private Avatar _avatar;
    private EmoteInstance _emoteCraftInstance;
    private IGetters _emoteCraftGetters;
    private IEmotePlayerEntity _emotecraftPlayer;

    @LuaWhitelist
    public boolean isEmotePlaying() {
        return _emotecraftPlayer.isPlayingEmote();
    }
    @LuaWhitelist
    public boolean isPartBendable(@LuaNotNil String modelPart) {
        return isEmotePlaying() && _emotecraftPlayer.getEmote().getData().getPart(modelPart).isBendable;
    }
    @LuaWhitelist
    public boolean isPartEnabled(@LuaNotNil String modelPart) {
        return isEmotePlaying() && _emotecraftPlayer.getEmote().getData().getPart(modelPart).isEnabled();
    }
    @LuaWhitelist
    public FiguraVec2 getPartBend(@LuaNotNil String modelPart, Float tickDelta) {
        if (isEmotePlaying()) {
            if (tickDelta == null) tickDelta = 0f;
            KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
            if (part.isEnabled()) {
                Vec3f v;
                if (part.isBendable) {
                    v = new Vec3f(part.bendDirection.defaultValue, part.bend.defaultValue, 0);
                    FiguraVec3 fv = getTransform(modelPart, TransformType.BEND, v, tickDelta);
                    return FiguraVec2.of(fv.x, fv.y);
                }
            }
        }
        return FiguraVec2.of();
    }
    @LuaWhitelist
    public FiguraVec3 getPartPosition(@LuaNotNil String modelPart, Float tickDelta) {
        if (isEmotePlaying()) {
            if (tickDelta == null) tickDelta = 0f;
            KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
            if (part.isEnabled()) {
                Vec3f v;
                v = new Vec3f(part.x.defaultValue, part.y.defaultValue, part.z.defaultValue);
                return getTransform(modelPart, TransformType.POSITION, v, tickDelta);
            }
        }
        return FiguraVec3.of();
    }
    @LuaWhitelist
    public FiguraVec3 getPartRotation(@LuaNotNil String modelPart, Float tickDelta) {
        if (isEmotePlaying()) {
            if (tickDelta == null) tickDelta = 0f;
            KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
            if (part.isEnabled()) {
                Vec3f v;
                v = new Vec3f(part.pitch.defaultValue, part.yaw.defaultValue, part.roll.defaultValue);
                return getTransform(modelPart, TransformType.ROTATION, v, tickDelta);
            }
        }
        return FiguraVec3.of();
    }
    @LuaWhitelist
    public int getEmoteTick() {
        return isEmotePlaying() ? _emotecraftPlayer.getEmote().getTick() : 0;
    }
    @LuaWhitelist
    public void preparePlayerEntity() {
        _emotecraftPlayer = _emoteCraftGetters.getPlayerFromUUID(_avatar.owner);
    }

    private FiguraVec3 getTransform(String modelPart, TransformType type, Vec3f v0, float tickDelta) {

        Vec3f transformVector = AnimUtils.getPlayerAnimLayer(_emotecraftPlayer).get3DTransform(modelPart, type, tickDelta, v0);
        return FiguraVec3.of(transformVector.getX(), transformVector.getY(), transformVector.getZ());
    }

    @LuaWhitelist
    public List<String> getPartList() {
        if (isEmotePlaying()) {
            _emotecraftPlayer = _emoteCraftGetters.getPlayerFromUUID(_avatar.owner);
            return _emotecraftPlayer.getEmote().getData().getBodyParts().keySet().stream().toList();
        }
        return List.of();
    }


    public FEmoteCraft() {

    }

    public FEmoteCraft(Avatar avatar) {
        _avatar = avatar;
        _emoteCraftInstance = EmoteInstance.instance;
        _emoteCraftGetters = _emoteCraftInstance.getGetters();
        preparePlayerEntity();
    }

    @Override
    public FiguraAPI build(Avatar avatar) {
        return new FEmoteCraft(avatar);
    }

    @Override
    public String getName() {
        return "emotecraft";
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        return List.of(
                FEmoteCraft.class
        );
    }
}
