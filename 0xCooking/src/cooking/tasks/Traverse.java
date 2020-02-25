package cooking.tasks;

import cooking.ZeroxCooking;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Traverse extends Task {

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return traverseToBank() || traverseToRange() ;
    }

    @Override
    public int execute() {
        checkRunEnergy();
        Movement.walkToRandomized(traverseToBank() ? ZeroxCooking.res.getBANKING_AREA().getCenter() : ZeroxCooking.res.getCOOKING_AREA().getCenter());
        return Random.nextInt(1500, 6000);
    }


    private boolean traverseToBank() {
        return ZeroxCooking.res != null && !ZeroxCooking.res.getBANKING_AREA().contains(Players.getLocal()) && !Inventory.contains(ZeroxCooking.res.getItem());
    }

    private boolean traverseToRange() {
        return ZeroxCooking.res != null && !ZeroxCooking.res.getCOOKING_AREA().contains(Players.getLocal()) && Inventory.contains(ZeroxCooking.res.getItem());
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

}
