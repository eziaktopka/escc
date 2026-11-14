package pl.escrebind.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.escrebind.EscRebindClient;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    /**
     * W grze (bez otwartego ekranu) Esc nie otwiera już menu pauzy,
     * chyba że sam Esc jest przypisany jako klawisz pauzy.
     */
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void escrebind$blockEsc(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (window != client.getWindow().getHandle()) return;
        if (key != GLFW.GLFW_KEY_ESCAPE) return;
        if (client.currentScreen != null || client.world == null) return;
        if (EscRebindClient.PAUSE_KEY != null && EscRebindClient.PAUSE_KEY.matchesKey(key, scancode)) return;
        ci.cancel();
    }
}
