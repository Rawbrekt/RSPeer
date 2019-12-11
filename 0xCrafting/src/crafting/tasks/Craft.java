package crafting.tasks;

import crafting.ZeroxCrafting;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Production;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Craft extends Task {

    private long anim = 0;
    @Override
    public boolean validate() {
        return shouldCraft();
    }

    @Override
    public int execute() {
        SceneObject FURNACE = SceneObjects.getNearest(16469);
        if (!(FURNACE == null)) {
            if (!isAnimating()){
                if (!Production.isOpen()) {
                    FURNACE.interact("Smelt");
                    Time.sleepUntil(() -> Production.isOpen(),Random.nextInt(750, 1500));
                } else {
                    Production.initiate();
                }
            }
        } else {
            Log.severe("Furnace not found");
        }

        return Random.nextInt(750, 1500);
    }

    private boolean shouldCraft() {

        if (ZeroxCrafting.toCraft.getMat2() == 0) {
            return Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && ZeroxCrafting.location.getFurnaceArea().contains(Players.getLocal());
        } else {
            return Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && Inventory.contains(ZeroxCrafting.toCraft.getMat2()) && ZeroxCrafting.location.getFurnaceArea().contains(Players.getLocal());
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
