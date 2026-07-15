package sh.lemming.accuratefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;

@Mixin(SodiumConfigBuilder.class)
public class SodiumConfigBuilderMixin {
    @ModifyArgs(
            method = "buildGeneralPage",
            at = @At(
                    value = "INVOKE",
                    target = "net/caffeinemc/mods/sodium/api/config/structure/IntegerOptionBuilder.setRange (III)Lnet/caffeinemc/mods/sodium/api/config/structure/IntegerOptionBuilder;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "intValue=260"),
                    to = @At(value = "CONSTANT", args = "intValue=60")
            )
    )
    private void noSnap(Args args) {
        args.setAll(10, 260, 1);
    }
}
