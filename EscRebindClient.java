package pl.escrebind;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class EscRebindClient implements ClientModInitializer {
    /** Klawisz otwierający menu pauzy. Domyślnie Esc, zmieniasz w Ustawienia -> Sterowanie. */
    public static KeyBinding PAUSE_KEY;

    @Override
    public void onInitializeClient() {
        PAUSE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.escrebind.pause",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_ESCAPE,
                "key.categories.escrebind"
        ));

        // W grze: wybrany klawisz otwiera menu pauzy.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PAUSE_KEY.wasPressed()) {
                if (client.currentScreen == null && client.world != null) {
                    client.openGameMenu(false);
                }
            }
        });

        // W menu pauzy: wybrany klawisz je zamyka.
        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            if (screen instanceof GameMenuScreen) {
                ScreenKeyboardEvents.allowKeyPress(screen).register((s, key, scancode, modifiers) -> {
                    if (PAUSE_KEY.matchesKey(key, scancode)) {
                        s.close();
                        return false;
                    }
                    return true;
                });
            }
        });
    }
}
