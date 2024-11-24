package com.crystal.bluecore.data;

import com.crystal.bluecore.BlueCore;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ModResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    /**
     * @return The unique identifier of this listener.
     */
    @Override
    public Identifier getFabricId() {
        return Identifier.of(BlueCore.MOD_ID, "crystal");
    }

    /**
     * Performs the reload in the apply executor, or the game engine.
     *
     * @param manager the resource manager
     */
    @Override
    public void reload(ResourceManager manager) {

    }
}
