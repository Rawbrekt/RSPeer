package fishing.tasks;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

import static fishing.ZeroxFishing.fishtype;

public class Banking extends Task {

    @Override
    public boolean validate() {
        return shouldBank();
    }

    @Override
    public int execute() {
        if (Bank.isOpen()) {
            if (fishtype.getBait() == 0) {
                if (!Inventory.contains(fishtype.getItem())) {
                    Bank.withdraw(fishtype.getItem(), 1);
                    Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(150, 2000));
                }
                Bank.depositAllExcept(fishtype.getItem());
            } else {

                if (!Inventory.contains(fishtype.getItem())) {
                    Bank.withdraw(fishtype.getItem(), 1);
                    Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(150, 2000));
                }

                if (!Inventory.contains(fishtype.getBait())) {
                    Bank.withdrawAll(fishtype.getBait());
                    Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(150, 2000));
                }
                Bank.depositAllExcept(fishtype.getItem(), fishtype.getBait());

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

        if (fishtype.getBait() == 0) {
            return (fishtype.getBankingArea().contains(Players.getLocal()) && (!Inventory.contains(fishtype.getItem()) || Inventory.isFull()));
        } else {
            return (fishtype.getBankingArea().contains(Players.getLocal()) && (!Inventory.contains(fishtype.getItem()) || !Inventory.contains(fishtype.getBait()) || Inventory.isFull()));
        }

    }
}
