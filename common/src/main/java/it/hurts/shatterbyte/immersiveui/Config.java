package it.hurts.shatterbyte.immersiveui;

import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Range;

public class Config extends ShatterConfig {
    public InventoryCategory inventory = new InventoryCategory();
    public ScreenShakeCategory screenShake = new ScreenShakeCategory();
    public EnchantmentCategory enchantments = new EnchantmentCategory();
    public MiscCategory misc = new MiscCategory();

    public static class InventoryCategory {
        @Comment("Enables the hotbar selector animation.")
        public boolean enableHotbarSelectorAnimation = true;

        @Comment("Enables floating item rotation.")
        public boolean enableFloatingItemRotation = true;

        @Comment("Enables hovering of matching items.")
        public boolean enableMatchingItemHovering = true;

        @Comment("Disables vanilla slot highlighting")
        public boolean disableVanillaSlotHighlighting = true;

        @Comment("Enables particles for rare items.")
        public boolean enableRarityParticles = true;

        @Comment("Affects the speed of the hotbar selector.")
        @Range(min = 1, max = 8, step = 0.1)
        public double hotbarSelectorSpeed = 3d;

        @Comment("Affects the size of the hovered item.")
        @Range(min = 0.5, max = 2, step = 0.1)
        public float hoveredItemScale = 1.4f;

        @Comment("Affects the size of the floating item.")
        @Range(min = 0.5, max = 2, step = 0.1)
        public float floatingItemScale = 1.4f;

        @Comment("Affects the hover amplitude of items, that match to the item that is carried in the cursor.")
        @Range(min = 0.25, max = 2, step = 0.1)
        public float matchingItemHoverAmplitude = 0.8f;

        @Comment("Affects the easing speed, that is applied to the rotation of the floating item.")
        @Range(min = 0.25, max = 2, step = 0.1)
        public float floatingItemEasingSpeed = 0.75f;

        @Comment("Affects the rotation amplitude of the floating item.")
        @Range(min = 0.25, max = 2, step = 0.1)
        public float floatingItemRotationAmplitude = 1f;

        @Comment("Moves the hotbar selector above the items. You might want to disable this, if you use resource packs that change the default selector texture.")
        public boolean renderHotbarSelectorAboveItems = false;
    }

    public static class ScreenShakeCategory {
        @Comment("Enables screen shake.")
        public boolean enableScreenShake = true;

        @Comment("Screen shake timer in ticks.")
        @Range(min = 4, max = 40, step = 1)
        public int shakeTimer = 8;

        @Comment("Screen shake amplitude.")
        @Range(min = 0.5, max = 3, step = 0.1)
        public float shakeAmplitude = 1.25f;
    }

    public static class EnchantmentCategory {
        @Comment("Enables particles in the enchant(ing/ment) table.")
        public boolean enableEnchantParticles = true;

        @Comment("Enables special formatting for items with cursed enchantments.")
        public boolean enableCurseFormatting = true;
    }

    public static class MiscCategory {
        @Comment("Enables wobbly items in advancement toasts.")
        public boolean enableAdvancementToastItems = true;
    }

    @Override
    public String getName() {
        return "immersiveui";
    }

    @Override
    public int getSchemaVersion() {
        return 0;
    }
}
