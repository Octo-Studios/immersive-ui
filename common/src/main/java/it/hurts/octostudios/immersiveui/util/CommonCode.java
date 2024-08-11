package it.hurts.octostudios.immersiveui.util;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.client.VariableStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Objects;

import static it.hurts.octostudios.immersiveui.client.VariableStorage.*;

public class CommonCode {
    public static void gooeyRenderCode(float partialTick) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - VariableStorage.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;
        }

//        for (ParticleData data : ParticleStorage.getParticlesData().stream().filter(data -> data.getPoseStackSnapshot().last().pose().getRowColumn(2,3) <= SCREEN_ZORDER).toList()) {
//            data.render(data.getPoseStackSnapshot(), partialTick);
//        }
    }

    public static void floatingRenderSize(GuiGraphics guiGraphics, Slot slot, Slot hoveredSlot, Map<Slot, Float> expandingProgress) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null)
            return;

        ItemStack carried = player.containerMenu.getCarried();

        boolean hovering = hoveredSlot == slot && (carried.isEmpty() || ItemStack.isSameItemSameComponents(slot.getItem(), carried));
        float deltaTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks() / 4f;

        expandingProgress.put(slot, Mth.clamp(expandingProgress.getOrDefault(slot, 0f) + deltaTime * (hovering ? 1 : -1), 0, 1f));

        float progress = Easing.lerp(1F, ImmersiveUI.CONFIG.getHoveredItemScale(), Easing.animate(hovering ? Easing.Type.EASE_OUT : Easing.Type.EASE_IN, expandingProgress.get(slot)));
        //if (!hovering) return;

        if (hoveredSlot == slot && !ImmersiveUI.CONFIG.isEnableVanillaSlotHighlighting()) guiGraphics.fillGradient(RenderType.guiOverlay(), slot.x, slot.y, slot.x + 16, slot.y + 16, -2130706433, -2130706433, 0);

        if (!carried.isEmpty() && ItemStack.isSameItemSameComponents(slot.getItem(), carried) && ImmersiveUI.CONFIG.isEnableMatchingItemHovering()) {
            guiGraphics.pose().translate(Mth.sin(Minecraft.getInstance().player.tickCount*0.215f + Objects.hash(slot.x, slot.y))*ImmersiveUI.CONFIG.getMatchingItemHoverAmplitude(), Mth.cos(Minecraft.getInstance().player.tickCount*0.13f + Objects.hash(slot.x, slot.y))*ImmersiveUI.CONFIG.getMatchingItemHoverAmplitude(), 0);
        }

        guiGraphics.pose().translate(slot.x + 8, slot.y + 8, 0);
        guiGraphics.pose().scale(progress, progress, 1f);
        guiGraphics.pose().translate(-slot.x - 8, -slot.y - 8, 0);
    }
}
