package com.areanimika.areanimikahax;

import com.areanimika.areanimikahax.commands.BottomCommand;
import com.areanimika.areanimikahax.commands.IncHClipCommand;
import com.areanimika.areanimikahax.commands.SafeVClipCommand;
import com.areanimika.areanimikahax.commands.TopCommand;
import com.areanimika.areanimikahax.modules.AlwaysNoFallPacket;
import com.areanimika.areanimikahax.modules.BlockLogger;
import com.areanimika.areanimikahax.modules.KillEffects;
import com.areanimika.areanimikahax.modules.PanicVClip;
import com.areanimika.areanimikahax.modules.VerticalClip;
import com.areanimika.areanimikahax.utils.EntityDeathDetector;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class Addon extends MeteorAddon {
    public static final Category CATEGORY = new Category("AreanimikaHax");

    @Override
    public void onInitialize() {
        EntityDeathDetector.init();

        Modules.get().add(new VerticalClip());
        Modules.get().add(new AlwaysNoFallPacket());
        Modules.get().add(new BlockLogger());
        Modules.get().add(new KillEffects());
        Modules.get().add(new PanicVClip());

        Commands.add(new SafeVClipCommand());
        Commands.add(new IncHClipCommand());
        Commands.add(new TopCommand());
        Commands.add(new BottomCommand());
    }

    @Override public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.areanimika.areanimikahax";
    }
}
