package sh.lemming.accuratefps.mixin;
import net.minecraft.client.Options;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sh.lemming.accuratefps.Config;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

@Mixin(Options.class)
public class OptionsMixin {
    @ModifyArgs(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/OptionInstance$IntRange.xmap (Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/client/OptionInstance$SliderableValueSet;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(value = "CONSTANT", args = "intValue=26"),
            to = @At(value = "CONSTANT", args = "intValue=260")
        )
    )
    private void customSnappingLogic(Args args) {
        IntFunction<Integer> sliderProgressToValue = (progress) -> {
            for (int snap : Config.SNAPS) {
                if (Math.abs(progress - snap) <= Config.SNAP_RANGE) {
                    return snap;
                }
            }
            return progress;
        };
        ToIntFunction<Integer> valueToSliderProgress = value -> value;

        args.setAll(sliderProgressToValue, valueToSliderProgress);
    }

    @ModifyArgs(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/OptionInstance$IntRange.<init> (II)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(value = "CONSTANT", args = "intValue=26"),
            to = @At(value = "CONSTANT", args = "intValue=260")
        )
    )
    private void fixedRange(Args args) {
        args.setAll(10, 260);
    }
}
