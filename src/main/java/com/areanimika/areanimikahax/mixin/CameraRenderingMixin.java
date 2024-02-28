package com.areanimika.areanimikahax.mixin;

import com.areanimika.areanimikahax.event.CameraOffsetEvent;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraRenderingMixin {

    @Shadow protected abstract void moveBy(double x, double y, double z);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V", shift = At.Shift.AFTER))
    public void onRender(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        CameraOffsetEvent off = MeteorClient.EVENT_BUS.post(CameraOffsetEvent.get());
        moveBy(off.x, off.y, off.z);
    }
}
