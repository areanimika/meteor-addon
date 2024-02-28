package com.areanimika.areanimikahax.mixin;

import com.areanimika.areanimikahax.mixinterface.ILightningEntity;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//                                            | More important than the "Ambient" module.
@Mixin(value = LightningEntityRenderer.class, priority = 900)
public class LightningRendererMixin {
    @Unique private static Color colorOverride = null;

    @Inject(method = "render(Lnet/minecraft/entity/LightningEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at=@At("HEAD"))
    public void onDrawBatchFromRender(LightningEntity lightningEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        ILightningEntity entity = (ILightningEntity) lightningEntity;
        colorOverride = entity.ama$getColorOverride();
    }

    @Inject(method = "drawBranch", at = @At(value = "HEAD"), cancellable = true)
    private static void onSetLightningVertex(Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float g, int i, float h, float j, float k, float l, float m, float n, float o, boolean bl, boolean bl2, boolean bl3, boolean bl4, CallbackInfo ci) {
        if(colorOverride == null) return;

        vertexConsumer.vertex(matrix4f, f + (bl ? o : -o), (float)(i * 16), g + (bl2 ? o : -o)).color(colorOverride.r / 255f, colorOverride.g / 255f, colorOverride.b / 255f, colorOverride.a / 255f).next();
        vertexConsumer.vertex(matrix4f, h + (bl ? n : -n), (float)((i + 1) * 16), j + (bl2 ? n : -n)).color(colorOverride.r / 255f, colorOverride.g / 255f, colorOverride.b / 255f, colorOverride.a / 255f).next();
        vertexConsumer.vertex(matrix4f, h + (bl3 ? n : -n), (float)((i + 1) * 16), j + (bl4 ? n : -n)).color(colorOverride.r / 255f, colorOverride.g / 255f, colorOverride.b / 255f, colorOverride.a / 255f).next();
        vertexConsumer.vertex(matrix4f, f + (bl3 ? o : -o), (float)(i * 16), g + (bl4 ? o : -o)).color(colorOverride.r / 255f, colorOverride.g / 255f, colorOverride.b / 255f, colorOverride.a / 255f).next();

        ci.cancel();
    }
}
