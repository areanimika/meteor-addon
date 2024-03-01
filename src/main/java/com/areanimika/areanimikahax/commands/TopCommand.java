package com.areanimika.areanimikahax.commands;

import com.areanimika.areanimikahax.utils.MovementUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class TopCommand extends Command {
    public TopCommand() {
        super("top", "Same as Vertical Clip upwards, but a command.", "up");
    }

    @Override public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Double d = MovementUtils.findVClipTeleportationDifference(mc.player.getY(), false);
            if(d != null) MovementUtils.saferVClip(d);
            else error("Couldn't find a point to teleport to.");

            return SINGLE_SUCCESS;
        });
    }
}
