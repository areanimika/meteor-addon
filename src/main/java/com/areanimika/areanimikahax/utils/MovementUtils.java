package com.areanimika.areanimikahax.utils;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class MovementUtils {

    public static void saferVClip(double blocks) {
        // Fall damage prevention.
        if(blocks < 0) mc.player.jump();

        int packetsRequired = (int) Math.ceil(Math.abs(blocks / 10));

        if (packetsRequired > 20) packetsRequired = 1;

        if (mc.player.hasVehicle()) {
            for (int packetNumber = 0; packetNumber < (packetsRequired - 1); packetNumber++) {
                mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
            }

            mc.player.getVehicle().setPosition(mc.player.getVehicle().getX(), mc.player.getVehicle().getY() + blocks, mc.player.getVehicle().getZ());
            mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
        } else {
            for (int packetNumber = 0; packetNumber < (packetsRequired - 1); packetNumber++) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + blocks, mc.player.getZ(), mc.player.isOnGround()));
            mc.player.setPosition(mc.player.getX(), mc.player.getY() + blocks, mc.player.getZ());
        }
    }

    public static void incHorizontalClip(double blocks, double atATime) {
        int packetsRequired = (int) Math.ceil(Math.abs(blocks / atATime));

        double blocksAtATime = blocks / packetsRequired;

        for (int i = 0; i < packetsRequired; i++) {
            horizontalClip(blocksAtATime);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
        }
    }

    public static void horizontalClip(double blocks) {
        Vec3d forward = Vec3d.fromPolar(0, mc.player.getYaw()).normalize();

        if (mc.player.hasVehicle()) {
            Entity vehicle = mc.player.getVehicle();
            vehicle.setPosition(vehicle.getX() + forward.x * blocks, vehicle.getY(), vehicle.getZ() + forward.z * blocks);
        }

        mc.player.setPosition(mc.player.getX() + forward.x * blocks, mc.player.getY(), mc.player.getZ() + forward.z * blocks);
    }

    public static boolean isPlayerNotCollidingWithBlocksVertically(double newY) {
        Box fromBB = mc.player.getBoundingBox();
        Box toBB = mc.player.getBoundingBox().offset(0, newY - mc.player.getY(), 0);

        Iterable<VoxelShape> iterable = mc.world.getCollisions(mc.player, toBB.contract(1.0E-5F));
        VoxelShape voxelShape = VoxelShapes.cuboid(fromBB.contract(1.0E-5F));

        for(VoxelShape voxelShape2 : iterable) {
            if (!VoxelShapes.matchesAnywhere(voxelShape2, voxelShape, BooleanBiFunction.AND)) {
                return false;
            }
        }

        return true;
    }
}
