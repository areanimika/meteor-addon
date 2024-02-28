package com.areanimika.areanimikahax.modules;

import com.areanimika.areanimikahax.Addon;
import meteordevelopment.meteorclient.events.world.ChunkDataEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;

import java.io.FileWriter;
import java.util.List;

public class BlockLogger extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to log.")
        .defaultValue(Blocks.CHEST)
        .build()
    );

    private final Setting<String> fileName = sgGeneral.add(new StringSetting.Builder()
        .name("file-name")
        .description("Name of the file to save blocks to")
        .defaultValue("block-log.txt")
        .onChanged($ -> {
            onDeactivate();
            onActivate();
        })
        .build()
    );

    private FileWriter fileWriter = null;

    public BlockLogger() {
        super(Addon.CATEGORY, "block-logger", "Logs blocks of specified types. Feature requested by @matteipis.");
    }

    @Override public void onActivate() {
        try {
            fileWriter = new FileWriter(fileName.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void onDeactivate() {
        try {
            if (fileWriter != null) fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onChunkData(ChunkDataEvent event) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = event.chunk.getBottomY(); y <= event.chunk.getTopY(); y++) {
                    BlockState b = event.chunk.getBlockState(new BlockPos(x ,y ,z));
                    Block block = b.getBlock();

                    if(!blocks.get().contains(block)) continue;

                    try {
                        fileWriter.write("%s %d %d %d\n".formatted(Registries.BLOCK.getId(block).toString(), x, y, z));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
