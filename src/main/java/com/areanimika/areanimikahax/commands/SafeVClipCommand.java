package com.areanimika.areanimikahax.commands;

import com.areanimika.areanimikahax.utils.MovementUtils;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SafeVClipCommand extends Command {

    public SafeVClipCommand() {
        super("svclip", ".vclip but doesn't damage you.");
    }

    @Override public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("blocks", DoubleArgumentType.doubleArg()).executes(context -> {
            MovementUtils.saferVClip(context.getArgument("blocks", Double.class));
            return SINGLE_SUCCESS;
        }));
    }
}
