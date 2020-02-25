package crafting.tasks;


import crafting.ZeroxGoldJewel;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

import static crafting.ZeroxGoldJewel.bankArea;
import static crafting.ZeroxGoldJewel.furnaceArea;
import static crafting.ZeroxGoldJewel.toCraft;

public class Traverse extends Task {

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return traverseToBank() || traverseToFurnace();
    }

    @Override
    public int execute() {
        Log.info("Traversing");
        checkRunEnergy();
        Movement.walkToRandomized(traverseToBank() ? bankArea.getCenter() : furnaceArea.getCenter());
        return Random.nextInt(200, 1500);
    }

    private boolean traverseToBank() {
        return (toCraft != null && !bankArea.contains(Players.getLocal()) && (!Inventory.contains(toCraft.getRawMaterial()) || !Inventory.contains(toCraft.getMould())));
    }

    private boolean traverseToFurnace() {
        return (ZeroxGoldJewel.toCraft != null && !furnaceArea.contains(Players.getLocal()) && Inventory.contains(toCraft.getRawMaterial()) && Inventory.contains(toCraft.getMould()));
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }
}
