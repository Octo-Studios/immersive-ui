package it.hurts.shatterbyte.immersiveui.neoforge.client;

import it.hurts.shatterbyte.immersiveui.compat.ExtraScreenData;
import it.hurts.shatterbyte.immersiveui.util.CommonCode;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ScreenRenderListener {
    @SubscribeEvent
    public static void renderScreen(ScreenEvent.Render.Pre e) {
        if (!(e.getScreen() instanceof ExtraScreenData data)) {
            return;
        }

        CommonCode.computeMouseDelta(data.getMouseInfo(), e.getMouseX(), e.getMouseY());
    }
}
