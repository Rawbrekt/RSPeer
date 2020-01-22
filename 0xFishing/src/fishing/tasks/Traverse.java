package fishing.tasks;

import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import java.util.concurrent.ThreadLocalRandom;

import static fishing.ZeroxFishing.fishtype;

public class Traverse extends Task {

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return traverseToBank() || traverseToFish();
    }

    @Override
    public int execute() {
        checkRunEnergy();

        Movement.walkToRandomized(traverseToBank() ? fishtype.getBankingArea().getCenter() : fishtype.getFishingArea().getCenter());
        return Random.nextInt(1500, 6000);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private boolean traverseToBank() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()));
        } else {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()) || !Inventory.contains(fishtype.getBait()));
        }
    }

    private boolean traverseToFish() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem());
        } else {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait());
        }
    }

}
