package fishing.tasks;

import fishing.ZeroxFishing;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Banking extends Task {

    @Override
    public boolean validate() {
        return shouldBank();
    }

    @Override
    public int execute() {
        if (Bank.isOpen()) {
            if (ZeroxFishing.fishtype.getBait() == 0) {
                if (!Inventory.contains(ZeroxFishing.fishtype.getItem())) {
                    Bank.withdraw(ZeroxFishing.fishtype.getItem(), 1);
                    Time.sleepUntil(() -> Inventory.contains(ZeroxFishing.fishtype.getItem()), Random.nextInt(150, 2000));
                }
                Bank.depositAllExcept(ZeroxFishing.fishtype.getItem());
            } else {

                if (!Inventory.contains(ZeroxFishing.fishtype.getItem())) {
                    Bank.withdraw(ZeroxFishing.fishtype.getItem(), 1);
                    Time.sleepUntil(() -> Inventory.contains(ZeroxFishing.fishtype.getItem()), Random.nextInt(150, 2000));
                }

                if (!Inventory.contains(ZeroxFishing.fishtype.getBait())) {
                    Bank.withdrawAll(ZeroxFishing.fishtype.getBait());
                    Time.sleepUntil(() -> Inventory.contains(ZeroxFishing.fishtype.getItem()), Random.nextInt(150, 2000));
                }
                Bank.depositAllExcept(ZeroxFishing.fishtype.getItem(), ZeroxFishing.fishtype.getBait());

            }
        } else {
            SceneObject BANK = SceneObjects.getNearest(10355);
            if (!BANK.equals(null)) {
                BANK.interact("Bank");
                Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
            }
        }
        return Random.nextInt(750, 1500);
    }

    private boolean shouldBank() {

        if (ZeroxFishing.fishtype.getBait() == 0) {
            return (ZeroxFishing.fishtype.getBANKING_AREA().contains(Players.getLocal()) && (!Inventory.contains(ZeroxFishing.fishtype.getItem()) || Inventory.isFull()));
        } else {
            return (ZeroxFishing.fishtype.getBANKING_AREA().contains(Players.getLocal()) && (!Inventory.contains(ZeroxFishing.fishtype.getItem()) || !Inventory.contains(ZeroxFishing.fishtype.getBait()) || Inventory.isFull()));
        }

    }
}
