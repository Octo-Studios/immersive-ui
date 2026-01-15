package it.hurts.shatterbyte.immersiveui.neoforge.mixin;

import it.hurts.shatterbyte.immersiveui.util.CommonCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
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
import net.p3pp3rf1y.sophisticatedstorage.client.gui.StorageScreen;
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
public abstract class StorageScreenBaseMixin extends AbstractContainerScreen {
    public StorageScreenBaseMixin(AbstractContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Unique
    Map<Slot, Float> backpackProgress = new HashMap<>();

    @Unique
    AtomicReference<Float> timer = new AtomicReference<>(0f);

    @Inject(require = 0, method = "renderBg", at = @At("HEAD"))
    public void shake(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        CommonCode.shakeScreen(guiGraphics, (Screen) (Object) this, timer, 2f);
    }

    @Unique
    AtomicBoolean shouldBurstCompat = new AtomicBoolean(false);

    @Inject(require = 0, method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/StorageScreenBase;renderStack(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/item/ItemStack;ZLjava/lang/String;)V", shift = At.Shift.BEFORE))
    private void renderSize(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
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

    @Inject(require = 0, method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/client/gui/ISlotDecorationRenderer;renderDecoration(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", shift = At.Shift.AFTER))
    public void renderSizeEnd(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        guiGraphics.pose().popMatrix();
    }
}
