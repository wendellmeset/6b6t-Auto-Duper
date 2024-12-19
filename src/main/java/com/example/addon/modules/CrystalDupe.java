package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler.*;
import net.minecraft.entity.Entity;
import meteordevelopment.meteorclient.settings.SettingGroup;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.EntityHitResult;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Categories;
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
            // ATTACK!!
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.currentScreen == null) { // Only if no GUI is open
                // Simulate pressing the left mouse button
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    
                // Simulate releasing the left mouse button
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
            
            // Disable the module after clicking
            toggle();
        }
    }
}
