package com.areanimika.areanimikahax.commands;

import com.areanimika.areanimikahax.utils.MovementUtils;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class IncHClipCommand extends Command {
    public IncHClipCommand() {
        super("ihclip", "Lets you .hclip further, by doing it in multiple steps.");
    }

    @Override public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(argument("blocks", DoubleArgumentType.doubleArg())
                .then(argument("atATime", DoubleArgumentType.doubleArg())
                .executes(context -> {
            double blocks = context.getArgument("blocks", Double.class);
            double atATime = context.getArgument("atATime", Double.class);

            MovementUtils.incHorizontalClip(blocks, atATime);

            return SINGLE_SUCCESS;
        })));
    }
}
