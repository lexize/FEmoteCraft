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
import org.lexize.femotecraft.models.Emote;
import org.lexize.femotecraft.models.Keyframe;
import org.lexize.femotecraft.models.KeyframesContainer;
import org.lexize.femotecraft.models.ModelPartDefaultTransforms;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;

import java.util.*;

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
        return ModelPartDefaultTransforms.valueOf(modelPart).bend != null;
    }
    @LuaWhitelist
    public FiguraVec2 getPartBend(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        FiguraVec3 v = getTransform(modelPart, TransformType.BEND, tickDelta);
        return FiguraVec2.of(v.x,v.y);
    }
    @LuaWhitelist
    public FiguraVec3 getPartPosition(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        return getTransform(modelPart, TransformType.POSITION, tickDelta);
    }
    @LuaWhitelist
    public FiguraVec3 getPartRotation(@LuaNotNil String modelPart, Float tickDelta) {
        if (tickDelta == null) tickDelta = 0f;
        return getTransform(modelPart, TransformType.ROTATION, tickDelta);
    }
    private FiguraVec3 getTransform(String modelPart, TransformType type, float tickDelta) {
        Vec3f v0;
        ModelPartDefaultTransforms emote = ModelPartDefaultTransforms.valueOf(modelPart);
        v0 = switch (type) {
            case POSITION -> emote.position;
            case ROTATION -> emote.rotation;
            case BEND -> emote.bend;
        };
        Vec3f transformVector = AnimUtils.getPlayerAnimLayer(_emotecraftPlayer).get3DTransform(modelPart, type, tickDelta, v0);
        return FiguraVec3.of(transformVector.getX(), transformVector.getY(), transformVector.getZ());
    }

    @LuaWhitelist
    public Emote getCurrentEmote() {
        preparePlayerEntity();
        return Emote.of(_emotecraftPlayer);
    }
    @LuaWhitelist
    public void preparePlayerEntity() {
        _emotecraftPlayer = _emoteCraftGetters.getPlayerFromUUID(_avatar.owner);
    }


    public FEmoteCraft() {

    }

    public FEmoteCraft(Avatar avatar) {
        _avatar = avatar;
        _emoteCraftInstance = EmoteInstance.instance;
        _emoteCraftGetters = _emoteCraftInstance.getGetters();
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
                FEmoteCraft.class,
                Emote.class,
                Keyframe.class,
                KeyframesContainer.class
        );
    }
}
