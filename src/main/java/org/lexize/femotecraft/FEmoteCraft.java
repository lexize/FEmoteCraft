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
        return _emotecraftPlayer.getEmote().getData().getPart(modelPart).isBendable;
    }
    @LuaWhitelist
    public FiguraVec3 getPartBend(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
        Vec3f v;
        if (part.isBendable) {
            v = new Vec3f(part.bendDirection.defaultValue, part.bend.defaultValue, 0);
            return getTransform(modelPart, TransformType.BEND, v, tickDelta);
        }
        return new FiguraVec3();
    }
    @LuaWhitelist
    public FiguraVec3 getPartPosition(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
        Vec3f v;
        v = new Vec3f(part.x.defaultValue, part.y.defaultValue, part.z.defaultValue);
        return getTransform(modelPart, TransformType.POSITION, v, tickDelta);
    }
    @LuaWhitelist
    public FiguraVec3 getPartRotation(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        KeyframeAnimation.StateCollection part = _emotecraftPlayer.getEmote().getData().getPart(modelPart);
        Vec3f v;
        v = new Vec3f(part.pitch.defaultValue, part.yaw.defaultValue, part.roll.defaultValue);
        return getTransform(modelPart, TransformType.ROTATION, v, tickDelta);
    }
    @LuaWhitelist
    public int getEmoteTick() {
        return _emotecraftPlayer.getEmote().getTick();
    }

    private FiguraVec3 getTransform(String modelPart, TransformType type, Vec3f v0, float tickDelta) {

        Vec3f transformVector = AnimUtils.getPlayerAnimLayer(_emotecraftPlayer).get3DTransform(modelPart, type, tickDelta, v0);
        return FiguraVec3.of(transformVector.getX(), transformVector.getY(), transformVector.getZ());
    }

    @LuaWhitelist
    public List<String> getPartList() {
        _emotecraftPlayer = _emoteCraftGetters.getPlayerFromUUID(_avatar.owner);
        return _emotecraftPlayer.getEmote().getData().getBodyParts().keySet().stream().toList();
    }


    public FEmoteCraft() {

    }

    public FEmoteCraft(Avatar avatar) {
        _avatar = avatar;
        _emoteCraftInstance = EmoteInstance.instance;
        _emoteCraftGetters = _emoteCraftInstance.getGetters();
        _emotecraftPlayer = _emoteCraftGetters.getPlayerFromUUID(_avatar.owner);
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
