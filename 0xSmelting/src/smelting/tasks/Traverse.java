package smelting.tasks;

import smelting.ZeroxSmelting;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import java.util.concurrent.ThreadLocalRandom;

public class Traverse extends Task {

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return traverseToBank() || traverseToFurnace();
    }

    @Override
    public int execute() {
        checkRunEnergy();
        Movement.walkToRandomized(traverseToBank() ? ZeroxSmelting.location.getBankArea().getCenter() : ZeroxSmelting.location.getFurnaceArea().getCenter());
        return Random.nextInt(1500, 6000);
    }

    private boolean traverseToBank() {

        if (ZeroxSmelting.toSmelt.getMat2() == 0) {
            return !(Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && ZeroxSmelting.location.getBankArea().contains(Players.getLocal()));
        } else {
            return !(Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && Inventory.contains(ZeroxSmelting.toSmelt.getMat2())) && !ZeroxSmelting.location.getBankArea().contains(Players.getLocal());
        }
    }

    private boolean traverseToFurnace() {
        if (ZeroxSmelting.toSmelt.getMat2() == 0) {
            return Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && !ZeroxSmelting.location.getFurnaceArea().contains(Players.getLocal());
        } else {
            return Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && Inventory.contains(ZeroxSmelting.toSmelt.getMat2()) && !ZeroxSmelting.location.getFurnaceArea().contains(Players.getLocal());
        }
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }
}
