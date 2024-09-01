package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ModSounds {

    public static final SoundEvent MUSIC_ONESIES = registerSounds("music_onesies");
    public static final SoundEvent MUSIC_OCTOPUS = registerSounds("music_octopus");
    public static final SoundEvent MUSIC_BLUEY_THEME = registerSounds("music_bluey_theme");

    public static final RegistryKey<JukeboxSong> ONESIES = registerJukeboxSong("onesies");
    public static final RegistryKey<JukeboxSong> OCTOPUS = registerJukeboxSong("octopus");
    public static final RegistryKey<JukeboxSong> BLUEY_THEME = registerJukeboxSong("bluey_theme");

    /**
     * 注册物体音效，环境音乐等方法
     * @param id 音效的命名空间
     * @return 注册成功
     */
    private static SoundEvent registerSounds(String id) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(BlueCore.MOD_ID, id),
                SoundEvent.of(Identifier.of(BlueCore.MOD_ID, id)));
    }

    /**
     * 注册唱片机歌曲
     * @param id 唱片机歌曲的命名空间
     * @return 注册成功
     */
    private static RegistryKey<JukeboxSong> registerJukeboxSong(String id) {
        return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(BlueCore.MOD_ID, id));
    }

    /**
     *
     * @param key 唱片机歌曲
     * @param soundEvent 音效，音乐来源
     * @param description 音乐的描述
     * @param lengthInSeconds 播放音乐时长
     * @param comparatorOutput 提供红石信号强度
     */
    private static void register(Registerable<JukeboxSong> registry, RegistryKey<JukeboxSong> key, SoundEvent soundEvent, String description, float lengthInSeconds, int comparatorOutput) {
        registry.register(key, new JukeboxSong(RegistryEntry.of(soundEvent), Text.translatable(description), lengthInSeconds, comparatorOutput));
    }

    public static void registerSoundsInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Sounds Success");
    }

    public static void bootstrap (Registerable<JukeboxSong> registry) {
        register(registry, ONESIES, MUSIC_ONESIES, "jukebox_song.bluecore.onesies", 204.0f, 15);
        register(registry, OCTOPUS, MUSIC_OCTOPUS, "jukebox_song.bluecore.octopus", 161.0f, 14);
        register(registry, BLUEY_THEME, MUSIC_BLUEY_THEME, "jukebox_song.bluecore.bluey_theme", 25.0f, 13);
    }
}
