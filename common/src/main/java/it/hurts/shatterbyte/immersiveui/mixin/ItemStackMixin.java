package it.hurts.shatterbyte.immersiveui.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract ItemEnchantments getEnchantments();

//    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE))
//    public void obfuscateCursedItem(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> ci, @Local LocalRef<MutableComponent> mutableComponent) {
//        if (this.getEnchantments().keySet().stream().anyMatch(p -> p.tags().anyMatch(tag -> tag.equals(EnchantmentTags.CURSE)))) {
//            //mutableComponent.set(obfuscate(mutableComponent.get(), 0.25d, Minecraft.getInstance().player.tickCount));
//        }
//    }
}
