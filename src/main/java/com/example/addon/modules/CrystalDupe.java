package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler.*;
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

    public void sendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
            if (packet.getType() == PlayerInteractEntityC2SPacket.InteractionType.ATTACK) {
            	this.doCritical();
            	
            	/* Lets fake some extra paricles to make the player feel good */
            	Entity e = packet.getEntity(mc.world);
            	Random r = new Random();
                for (int i = 0; i < 10; i++) {
                	mc.particleManager.addParticle(ParticleTypes.CRIT, e.getX(), e.getY() + e.getHeight() / 2, e.getZ(),
                			r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5);
                }
            }
        }
    }
 

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
                }
            }
            
            // Disable the module after clicking
            toggle();
        }
    }
}
