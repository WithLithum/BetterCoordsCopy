package de.hustender.bcs.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public abstract class CopyCoordinatesMixin {

    @Shadow
    protected abstract void debugLog(String message, Object... params);

    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(
            method = "processF3",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Keyboard;setClipboard(Ljava/lang/String;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void onCopyCoords(int key, CallbackInfoReturnable<Boolean> cir) {
        if(key != 67) return;
        if(client.player == null || client.player.hasReducedDebugInfo()) return;
        if(client.player.networkHandler == null) return;
        String coords = client.player.getBlockX() + " " + client.player.getBlockY() + " " + client.player.getBlockZ();
        this.client.keyboard.setClipboard(coords);
        cir.setReturnValue(true);
    }

}
