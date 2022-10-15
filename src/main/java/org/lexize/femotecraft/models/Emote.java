package org.lexize.femotecraft.models;

import dev.kosmx.playerAnim.api.AnimUtils;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import io.github.kosmx.emotes.executor.emotePlayer.IEmotePlayer;
import io.github.kosmx.emotes.executor.emotePlayer.IEmotePlayerEntity;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.matrix.FiguraMat3;
import org.moon.figura.math.matrix.FiguraMat4;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LuaWhitelist
public class Emote {
    private final IEmotePlayer _emote;
    private final IEmotePlayerEntity _emotePlayer;
    private Emote(IEmotePlayer emote, IEmotePlayerEntity emotePlayer) {
        _emote = emote;
        _emotePlayer = emotePlayer;
    }

    @LuaWhitelist
    public boolean isEmoteRunning() {
        return _emote.isRunning();
    }
    @LuaWhitelist
    public boolean isEmoteLoopStarted() {
        return _emote.isLoopStarted();
    }
    @LuaWhitelist
    public boolean isPartEnabled(@LuaNotNil String modelPart) {
        return _emote.getData().getPart(modelPart).isEnabled();
    }
    @LuaWhitelist
    public int getEmoteTick() {
        return _emote.getTick();
    }

    @LuaWhitelist
    public String getEmoteUUID() {
        return _emote.getData().getUuid().toString();
    }
    @LuaWhitelist
    public List<String> getEmoteExtraDataKeys() {
        return _emote.getData().extraData.keySet().stream().toList();
    }
    @LuaWhitelist
    public Object getEmoteExtraData(String key) {
        return _emote.getData().extraData.get(key);
    }
    @LuaWhitelist
    public String getEmoteName() {
        return (String) getEmoteExtraData("name");
    }
    @LuaWhitelist
    public String getEmoteDescription() {
        return (String) getEmoteExtraData("description");
    }
    @LuaWhitelist
    public String getEmoteAuthor() {
        return (String) getEmoteExtraData("author");
    }
    @LuaWhitelist
    public List<String> getPartList() {
        return _emote.getData().getBodyParts().keySet().stream().toList();
    }
    @LuaWhitelist
    public KeyframesContainer getKeyframes(@LuaNotNil String modelPart) {
        KeyframeAnimation.StateCollection part = _emote.getData().getPart(modelPart);
        return KeyframesContainer.ofPart(part);
    }

    public static Emote of(IEmotePlayer emote, IEmotePlayerEntity player) {
        return new Emote(emote, player);
    }
}
