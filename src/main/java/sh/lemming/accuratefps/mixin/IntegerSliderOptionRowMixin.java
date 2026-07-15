package sh.lemming.accuratefps.mixin;

import net.caffeinemc.mods.sodium.api.config.option.SteppedValidator;
import net.caffeinemc.mods.sodium.client.config.structure.IntegerOption;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.lemming.accuratefps.SodiumPatch;

@Mixin(targets = "me.flashyreese.mods.reeses_sodium_options.client.gui.frame.option.IntegerSliderOptionRow")
public class IntegerSliderOptionRowMixin {
    @Inject(
        method = "valueForThumbPosition",
        at = @At("HEAD"),
        cancellable = true
    )
    private void valueForThumbPosition(CallbackInfoReturnable<Integer> cir) {
        SodiumPatch.getValueForThumbPosition(cir, this);
    }
}
