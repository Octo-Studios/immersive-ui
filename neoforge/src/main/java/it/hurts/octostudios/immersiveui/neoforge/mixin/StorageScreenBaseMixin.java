package it.hurts.octostudios.immersiveui.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;
import it.hurts.octostudios.immersiveui.client.VariableStorage;
import it.hurts.octostudios.immersiveui.compat.ExtraScreenData;
import it.hurts.octostudios.immersiveui.util.CommonCode;
import it.hurts.octostudios.octolib.client.particle.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeContainer;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(StorageScreenBase.class)
public abstract class StorageScreenBaseMixin {
    @Shadow protected abstract boolean isHovering(Slot slot, double mouseX, double mouseY);

    @Shadow public abstract int getLeftX();

    @Shadow public abstract int getTopY();

    @Shadow private boolean initializing;
    @Unique
    Map<Slot, Float> backpackProgress = new HashMap<>();

    @Unique
    Slot myHoveredSlot;

//    @Unique
//    private MouseInfo mouseInfo = new MouseInfo();
//    @Unique
//    private RenderInfo renderInfo = new RenderInfo();
//
//    @Inject(require = 0, method = "render", at = @At("HEAD"))
//    public void resetOldMouse(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
//        float deltaTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
//        mouseInfo.deltaX = (mouseInfo.oX - mouseX) / deltaTime / 20f;
//        mouseInfo.deltaY = (mouseInfo.oY - mouseY) / deltaTime / 20f;
//    }

    @Unique
    AtomicReference<Float> timer = new AtomicReference<>(0f);

    @Inject(require = 0, method = "renderBg", at = @At("HEAD"))
    public void shake(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        CommonCode.shakeScreen(guiGraphics, (Screen) (Object) this, timer, 2f);
    }

    @Inject(require = 0, method = "render", at = @At("TAIL"))
    public void resetOldMouse2(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        MouseInfo mouseInfo = ((ExtraScreenData) this).getMouseInfo();
        mouseInfo.oX = mouseX; mouseInfo.oY = mouseY;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getLeftX(), getTopY(), 0);
        ParticleSystem.renderScreenParticles((Screen) (Object) this, guiGraphics, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
        guiGraphics.pose().popPose();
    }

    @Inject(require = 0, method = "renderSuper", at = @At("HEAD"))
    private void renderHead(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        myHoveredSlot = null;
    }

    @Inject(require = 0, method = "renderSuper", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", shift = At.Shift.BEFORE))
    private void hoveredSlot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, @Local Slot slot) {
        if (this.isHovering(slot, mouseX, mouseY)) {
            myHoveredSlot = slot;
        }
    }

    @Inject(require = 0, method = "renderUpgradeSlots", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", shift = At.Shift.BEFORE))
    private void hoveredSlot2(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci, @Local Slot slot) {
        if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
            myHoveredSlot = slot;
        }
    }

    @Inject(require = 0, method = "renderStorageInventorySlots(Lnet/minecraft/client/gui/GuiGraphics;IIZ)V", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", shift = At.Shift.BEFORE))
    private void hoveredSlot2(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean canShowHover, CallbackInfo ci, @Local Slot slot) {
        if (canShowHover && this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
            myHoveredSlot = slot;
        }
    }

    @Unique
    AtomicBoolean shouldBurstCompat = new AtomicBoolean(false);

    @Inject(require = 0, method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;renderStack(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/item/ItemStack;ZLjava/lang/String;)V", shift = At.Shift.BEFORE))
    private void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        CommonCode.floatingRenderSize(guiGraphics, slot, myHoveredSlot, backpackProgress);

        StorageScreenBase screen = (StorageScreenBase) (Object) this;
        Optional<UpgradeContainerBase<?, ?>> upgrade = ((StorageContainerMenuBase<?>)screen.getMenu()).getSlotUpgradeContainer(slot);
        if (upgrade.isEmpty()) {
            return;
        }

        if (upgrade.get() instanceof CookingUpgradeContainer<?,?> cookingUpgradeContainer && slot.getSlotIndex() == 1) {
            CookingLogicContainer<?> logic = cookingUpgradeContainer.getSmeltingLogicContainer();
            float litProgress = (float) (logic.getBurnTimeFinish()-Minecraft.getInstance().level.getGameTime()) / logic.getBurnTimeTotal();
            CommonCode.renderFurnaceParticles(screen, slot, litProgress == 0, logic.isCooking(), shouldBurstCompat);
        }
    }
}
