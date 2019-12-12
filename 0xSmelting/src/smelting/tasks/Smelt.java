package smelting.tasks;

import smelting.ZeroxSmelting;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Production;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Smelt extends Task {

    private long anim = 0;

    @Override
    public boolean validate() {
        return shouldSmelt();
    }

    @Override
    public int execute() {
        SceneObject FURNACE = SceneObjects.getNearest(16469);
        if (!(FURNACE == null)) {
            if (!isAnimating()) {
                if (!Production.isOpen()) {
                    FURNACE.interact("Smelt");
                    Time.sleepUntil(() -> Production.isOpen(), Random.nextInt(750, 1500));
                } else {
                    Production.initiate();
                }
            }
        } else {
            Log.severe("Furnace not found");
        }

        return Random.nextInt(750, 1500);
    }

    private boolean shouldSmelt() {

        if (ZeroxSmelting.toSmelt.getMat2() == 0) {
            return Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && ZeroxSmelting.location.getFurnaceArea().contains(Players.getLocal());
        } else {
            return Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && Inventory.contains(ZeroxSmelting.toSmelt.getMat2()) && ZeroxSmelting.location.getFurnaceArea().contains(Players.getLocal());
        }
    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }

        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }
}
