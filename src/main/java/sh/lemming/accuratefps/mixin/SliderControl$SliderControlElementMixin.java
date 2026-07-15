package sh.lemming.accuratefps.mixin;

import net.caffeinemc.mods.sodium.api.config.option.SteppedValidator;
import net.caffeinemc.mods.sodium.client.config.structure.IntegerOption;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl$SliderControlElement")
public class SliderControl$SliderControlElementMixin {

    @Inject(
        method = "getValueForThumbPosition",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getValueForThumbPosition(CallbackInfoReturnable<Integer> cir) {
        try {
            java.lang.reflect.Field optionField = this.getClass().getDeclaredField("option");
            optionField.setAccessible(true);
            Object option = optionField.get(this);
            java.lang.reflect.Field idField = option.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            ResourceLocation id = (ResourceLocation) idField.get(option);
            if (id.getPath().equals("general.framerate_limit")) {
                java.lang.reflect.Field thumbPositionField = this.getClass().getDeclaredField("thumbPosition");
                SteppedValidator range = ((IntegerOption) option).getSteppedValidator();
                thumbPositionField.setAccessible(true);
                int thumbPosition = (int) (range.min() + (double) thumbPositionField.get(this) * (double)(range.max() - range.min()));
                for (int snap : sh.lemming.accuratefps.Config.SNAPS) {
                    if (Math.abs(thumbPosition - snap) <= sh.lemming.accuratefps.Config.SNAP_RANGE) {
                        System.out.println("Snapping " + thumbPosition + " to " + snap);
                        cir.setReturnValue(snap);
                        return;
                    }
                }
                System.out.println("Not snapping " + thumbPosition);
                cir.setReturnValue(thumbPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
