package crafting.tasks;

import crafting.ZeroxCrafting;
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
        Movement.walkToRandomized(traverseToBank() ? ZeroxCrafting.location.getBankArea().getCenter() : ZeroxCrafting.location.getFurnaceArea().getCenter());
        return Random.nextInt(1500,6000);
    }

    private boolean traverseToBank() {

        if (ZeroxCrafting.toCraft.getMat2() == 0) {
            return !(Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && ZeroxCrafting.location.getBankArea().contains(Players.getLocal()));
        } else {
            return !(Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && Inventory.contains(ZeroxCrafting.toCraft.getMat2())) && !ZeroxCrafting.location.getBankArea().contains(Players.getLocal());
        }
    }

    private boolean traverseToFurnace() {
        if (ZeroxCrafting.toCraft.getMat2() == 0) {
            return Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && !ZeroxCrafting.location.getFurnaceArea().contains(Players.getLocal());
        } else {
            return Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && Inventory.contains(ZeroxCrafting.toCraft.getMat2()) && !ZeroxCrafting.location.getFurnaceArea().contains(Players.getLocal());
        }
    }

    public void checkRunEnergy(){
        if(Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()){
            Movement.toggleRun(true);
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }
}
