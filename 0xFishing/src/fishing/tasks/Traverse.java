package fishing.tasks;

import fishing.ZeroxFishing;
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
        return traverseToBank() || traverseToFish();
    }

    @Override
    public int execute() {
        checkRunEnergy();

        Movement.walkToRandomized(traverseToBank() ? ZeroxFishing.fishtype.getBANKING_AREA().getCenter() : ZeroxFishing.fishtype.getFISHING_AREA().getCenter());
        return Random.nextInt(1500, 6000);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private boolean traverseToBank() {
        if (ZeroxFishing.fishtype.getBait() == 0) {
            return !ZeroxFishing.fishtype.getBANKING_AREA().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(ZeroxFishing.fishtype.getItem()));
        } else {
            return !ZeroxFishing.fishtype.getBANKING_AREA().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(ZeroxFishing.fishtype.getItem()) || !Inventory.contains(ZeroxFishing.fishtype.getBait()));
        }
    }

    private boolean traverseToFish() {
        if (ZeroxFishing.fishtype.getBait() == 0) {
            return !ZeroxFishing.fishtype.getFISHING_AREA().contains(Players.getLocal()) && Inventory.contains(ZeroxFishing.fishtype.getItem());
        } else {
            return !ZeroxFishing.fishtype.getFISHING_AREA().contains(Players.getLocal()) && Inventory.contains(ZeroxFishing.fishtype.getItem()) && Inventory.contains(ZeroxFishing.fishtype.getBait());
        }
    }

}
