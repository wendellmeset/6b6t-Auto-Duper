package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;

public class CrystalDupe extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    // Adding the delay setting
    private final Setting<Double> delay = sgGeneral.add(new DoubleSetting.Builder()
        .name("delay")
        .description("Second delay before chest breaking.")
        .defaultValue(0.15d)
        .range(0.05d, 5.0d) // Range from 0.05 seconds to 5 seconds
        .sliderRange(0.05d, 1.0d) // Slider range for convenience
        .build()
    );

    private long lastActionTime;

    public CrystalDupe() {
        super(Categories.Misc, "Crystal-Dupe", "Does 6B6T crystal dupe timing automatically.");
    }

    @Override
    public void onActivate() {
        lastActionTime = System.currentTimeMillis();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        // Calculate elapsed time
        double elapsedTime = (System.currentTimeMillis() - lastActionTime) / 1000.0;

        if (elapsedTime >= delay.get()) {
            // Perform the left mouse button click
            MinecraftClient.getInstance().interactionManager.attackBlock(MinecraftClient.getInstance().player.getBlockPos(), MinecraftClient.getInstance().player.getHorizontalFacing());

            // Disable the module after clicking
            toggle();
        }
    }
}
