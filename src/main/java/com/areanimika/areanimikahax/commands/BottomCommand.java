package com.areanimika.areanimikahax.commands;

import com.areanimika.areanimikahax.utils.MovementUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BottomCommand extends Command {
    public BottomCommand() {
        super("bottom", "Same as Vertical Clip downwards, but a command.", "down");
    }

    @Override public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Double d = MovementUtils.findVClipTeleportationDifference(mc.player.getY(), true);
            if(d != null) MovementUtils.saferVClip(d);
            else error("Couldn't find a point to teleport to.");

            return SINGLE_SUCCESS;
        });
    }
}
