package sh.lemming.accuratefps;

import net.caffeinemc.mods.sodium.api.config.option.SteppedValidator;
import net.caffeinemc.mods.sodium.client.config.structure.IntegerOption;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class SodiumPatch {
    public static void getValueForThumbPosition(CallbackInfoReturnable<Integer> cir, Object thiz) {
        try {
            java.lang.reflect.Field optionField = thiz.getClass().getDeclaredField("option");
            optionField.setAccessible(true);
            Object option = optionField.get(thiz);
            java.lang.reflect.Field idField = option.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            ResourceLocation id = (ResourceLocation) idField.get(option);
            if (id.getPath().equals("general.framerate_limit")) {
                java.lang.reflect.Field thumbPositionField = thiz.getClass().getDeclaredField("thumbPosition");
                SteppedValidator range = ((IntegerOption) option).getSteppedValidator();
                thumbPositionField.setAccessible(true);
                int thumbPosition = (int) (range.min() + (double) thumbPositionField.get(thiz) * (double)(range.max() - range.min()));
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
