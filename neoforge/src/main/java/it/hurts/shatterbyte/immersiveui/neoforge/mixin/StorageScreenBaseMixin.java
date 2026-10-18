package it.hurts.shatterbyte.immersiveui.neoforge.mixin;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.immersiveui.util.CommonCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeContainer;
import org.spongepowered.asm.mixin.Mixin;
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
public abstract class StorageScreenBaseMixin extends AbstractContainerScreen {
    @Unique
    Map<Slot, Float> backpackProgress = new HashMap<>();

    @Unique
    AtomicReference<Float> timer = new AtomicReference<>(0f);

    public StorageScreenBaseMixin(AbstractContainerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(require = 0, method = "extractBg", at = @At("HEAD"))
    public void shake(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        CommonCode.shakeScreen(guiGraphics, this, timer, 2f);
    }

    @Unique
    AtomicBoolean shouldBurstCompat = new AtomicBoolean(false);

    @Inject(require = 0, method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;extractStack(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/item/ItemStack;ZLjava/lang/String;)V", shift = At.Shift.BEFORE))
    private void renderSize(GuiGraphicsExtractor guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        guiGraphics.pose().pushMatrix();
        CommonCode.floatingRenderSize(guiGraphics, slot, this.hoveredSlot, backpackProgress);

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

    @Inject(require = 0, method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/ISlotDecorationRenderer;renderDecoration(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/inventory/Slot;)V", shift = At.Shift.AFTER))
    public void renderSizeEnd(GuiGraphicsExtractor guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        guiGraphics.pose().popMatrix();
    }

    @Inject(require = 0, method = "extractSlotHighlightFront", at = @At("HEAD"), cancellable = true)
    private void cancelStackOverlay(GuiGraphicsExtractor guiGraphics, Slot slot, CallbackInfo ci) {
        if (ImmersiveUI.CONFIG.inventory.disableVanillaSlotHighlighting) {
            ci.cancel();
        }
    }
}
