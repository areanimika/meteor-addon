package com.areanimika.areanimikahax.modules;

import com.areanimika.areanimikahax.Addon;
import com.areanimika.areanimikahax.utils.MovementUtils;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class VerticalClip extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> maxDistance = sgGeneral.add(new DoubleSetting.Builder()
            .name("max-distance")
            .defaultValue(100)
            .min(0)
            .build()
    );

    private final Setting<ShapeMode> shapeMode = sgGeneral.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .defaultValue(ShapeMode.Both)
            .build()
    );

    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder()
            .name("side-color")
            .defaultValue(new SettingColor(255, 255, 255, 50))
            .build()
    );

    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder()
            .name("line-color")
            .defaultValue(new SettingColor(255, 255, 255, 255))
            .build()
    );

    /* Only on key */
    private final SettingGroup sgOnlyOnKey = settings.createGroup("Only On Key Settings");

    private final Setting<Boolean> onlyOnKeyEnabled = sgOnlyOnKey.add(new BoolSetting.Builder()
            .name("only-on-key-enabled")
            .description("Use this, so vertical clipping happens only when you hold a key.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Keybind> onlyOnKeyKeybind = sgOnlyOnKey.add(new KeybindSetting.Builder()
            .name("keybind")
            .description("Makes it so a keybind needs to be pressed for vertical clipping to work.")
            .visible(onlyOnKeyEnabled::get)
            .defaultValue(Keybind.none())
            .build()
    );

    public VerticalClip() {
        super(Addon.CATEGORY, "vertical-clip", "Allows you to vertically teleport through blocks by clicking on block's up/down side. Works the same as .vclip command.");
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        if(onlyOnKeyEnabled.get() && !(onlyOnKeyKeybind.get().isPressed() && onlyOnKeyKeybind.get().isValid())) return;
        if (mc.crosshairTarget == null || !(mc.crosshairTarget instanceof BlockHitResult result)) return;
        if (result.isInsideBlock()) return;

        BlockPos bp = result.getBlockPos();
        Direction side = result.getSide();
        if (side != Direction.UP && side != Direction.DOWN) return;

        BlockState state = mc.world.getBlockState(bp);
        VoxelShape shape = state.getOutlineShape(mc.world, bp);

        if (shape.isEmpty()) return;
        Box box = shape.getBoundingBox();

        Double difference = findDifference(bp.getY(), side == Direction.UP);

        if(difference != null && Math.abs(difference) <= maxDistance.get())
            event.renderer.sideHorizontal(bp.getX() + box.minX, bp.getY() + (side == Direction.DOWN ? box.minY : box.maxY), bp.getZ() + box.minZ, bp.getX() + box.maxX, bp.getZ() + box.maxZ, sideColor.get(), lineColor.get(), shapeMode.get());
    }

    @EventHandler
    public void onClick(InteractBlockEvent event) {
        if(onlyOnKeyEnabled.get() && !(onlyOnKeyKeybind.get().isPressed() && onlyOnKeyKeybind.get().isValid())) return;
        if(event.result.isInsideBlock()) return;

        BlockPos bp = event.result.getBlockPos();
        Direction side = event.result.getSide();
        if (side != Direction.UP && side != Direction.DOWN) return;

        Double difference = findDifference(bp.getY(), side == Direction.UP);
        if(difference == null) return;
        if(Math.abs(difference) > maxDistance.get()) return;

        MovementUtils.saferVClip(difference);
    }

    private Double findDifference(int originY, boolean up) {
        if(up) {
            //                          | Can't use maxDistance here, because then it could TP into the void.
            for (int y = originY; y >= mc.world.getBottomY(); y--) {
                double diffHere = y - mc.player.getY();
                if(MovementUtils.isPlayerNotCollidingWithBlocksVertically(mc.player.getY() + diffHere)) return diffHere;
            }
        } else {
            for(int y = originY; y <= originY + maxDistance.get(); y++) {
                double diffHere =  y - mc.player.getY();
                if(MovementUtils.isPlayerNotCollidingWithBlocksVertically(mc.player.getY() + diffHere)) return diffHere;
            }
        }

        return null;
    }
}
