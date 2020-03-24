package fishlooting.tasks;

import org.rspeer.runetek.api.ClientSupplier;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import java.util.concurrent.ThreadLocalRandom;

import static fishlooting.ZeroxFishlooting.*;

public class Traverse extends Task {

    private int toggleNextRun = 20;


    @Override
    public boolean validate() {
        return (traverseToBank() || traverseToFish()) && tutProgress == 1000;
    }

    @Override
    public int execute() {

        currentTask = "Walking";

        String username = ClientSupplier.get().getUsername();
        String status = "running";
        updateStatus(username, status);

        if (Dialog.canContinue()) {
            Dialog.processContinue();
        }

        if (Worlds.getCurrent() == MULE_WORLD_INT) {
            WorldHopper.randomHop(world -> world.getId() != Game.getClient().getCurrentWorld()
                    && !world.isBounty()
                    && !world.isDeadman()
                    && !world.isHighRisk()
                    && !world.isLastManStanding()
                    && !world.isMembers()
                    && !world.isPVP()
                    && !world.isSeasonDeadman()
                    && !world.isSkillTotal()
                    && !world.isTournament()
            );
        } else {
            checkRunEnergy();
            Movement.walkToRandomized(traverseToBank() ? bankArea.getCenter() : lootArea.getCenter());
        }
        return Random.nextInt(750, 1500);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private boolean traverseToBank() {
        return Inventory.isFull() && !bankArea.contains(Players.getLocal());
    }

    private boolean traverseToFish() {
        return !Inventory.isFull() && !lootArea.contains(Players.getLocal());
    }
}
