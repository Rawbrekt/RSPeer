package crafting.tasks;

import crafting.ZeroxGoldJewel;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

import static crafting.ZeroxGoldJewel.bankArea;
import static crafting.ZeroxGoldJewel.toCraft;


public class Banking extends Task {
    @Override
    public boolean validate() {
        if (toCraft.getGem() == 0) {
            return (ZeroxGoldJewel.toCraft != null) && bankArea.contains(Players.getLocal()) && (!Inventory.contains(toCraft.getRawMaterial()) || !Inventory.contains(toCraft.getMould()));
        } else {
            return (ZeroxGoldJewel.toCraft != null) && bankArea.contains(Players.getLocal()) && (!Inventory.contains(toCraft.getRawMaterial()) || !Inventory.contains(toCraft.getMould()) || !Inventory.contains(toCraft.getGem()));
        }

    }

    @Override
    public int execute() {
        ZeroxGoldJewel.currentTask = "Banking";

        if (Bank.isOpen()) {

            if (!Inventory.isEmpty()) {
                Bank.depositAllExcept(toCraft.getRawMaterial(), toCraft.getMould(), toCraft.getGem());
                Time.sleep(Random.nextInt(300, 900));
            }

            if (!Inventory.contains(toCraft.getMould())) {
                Bank.withdraw(toCraft.getMould(), 1);
                Time.sleepUntil(() -> Inventory.contains(toCraft.getMould()), Random.nextInt(600, 1250));
            } else if (toCraft.getGem() == 0) {
                Bank.withdrawAll(toCraft.getRawMaterial());
                Time.sleepUntil(() -> Inventory.isFull(), Random.nextInt(600, 1250));
            } else {

                int barsInv = Inventory.getCount(toCraft.getRawMaterial());
                int gemsInv = Inventory.getCount(toCraft.getGem());

                if (barsInv < 13) {
                    Bank.withdraw(toCraft.getRawMaterial(), (13 - barsInv));
                    Time.sleepUntil(() -> Inventory.getCount(toCraft.getRawMaterial()) == 13, Random.nextInt(600, 1250));
                } else if (barsInv > 13) {
                    Bank.deposit(toCraft.getRawMaterial(),(barsInv - 13));
                    Time.sleepUntil(() -> Inventory.getCount(toCraft.getRawMaterial()) == 13, Random.nextInt(600, 1250));
                }

                if (gemsInv < 13) {
                    Bank.withdraw(toCraft.getGem(), (13 - gemsInv));
                    Time.sleepUntil(() -> Inventory.getCount(toCraft.getGem()) == 13, Random.nextInt(600, 1250));
                } else if (gemsInv > 13) {
                    Bank.withdraw(toCraft.getGem(), (gemsInv - 13));
                    Time.sleepUntil(() -> Inventory.getCount(toCraft.getGem()) == 13, Random.nextInt(600, 1250));
                }
            }

        } else {
            SceneObject BANK = SceneObjects.getNearest(10355);
            if (!BANK.equals(null)) {
                BANK.interact("Bank");
                Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(1500, 3000));
            }

        }
        return Random.nextInt(200, 600);
    }
}
