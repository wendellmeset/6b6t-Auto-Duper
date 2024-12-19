package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
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
            // Get the entity the player is looking at
            EntityHitResult hitResult = (EntityHitResult) mc.crosshairTarget;
            
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                // Check if the entity is an End Crystal
                if (hitResult.getEntity() instanceof EndCrystalEntity target) {
                    // Send the attack packet to break the End Crystal
                    sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
            
                    // Optional: Render swing animation if enabled
                    if (swing.get()) {
                        clientSwing(swingHand.get(), Hand.MAIN_HAND);
                    }
                }
            }
            
            // Disable the module after clicking
            toggle();
        }
    }
}
