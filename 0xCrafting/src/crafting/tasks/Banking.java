package crafting.tasks;

import crafting.ZeroxCrafting;
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
                if (ZeroxCrafting.toCraft.getMat2() == 0) {
                    if (!Inventory.containsOnly(ZeroxCrafting.toCraft.getMat1())) {
                        if (Bank.contains(ZeroxCrafting.toCraft.getMat1())) {
                            Bank.withdraw(ZeroxCrafting.toCraft.getMat1(), ZeroxCrafting.toCraft.getAmount1());
                        } else {
                            Log.severe("Out of materials");
                        }
                    } else {
                        Bank.depositInventory();
                    }
                } else {
                    if (!Inventory.containsAll(ZeroxCrafting.toCraft.getMat1(),ZeroxCrafting.toCraft.getMat2())) {
                        if (!Inventory.isEmpty()) {
                            Bank.depositInventory();
                            Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(500,1200));
                        }
                        if (Bank.containsAll(ZeroxCrafting.toCraft.getMat1(), ZeroxCrafting.toCraft.getMat2())) {
                            if (!Inventory.contains(ZeroxCrafting.toCraft.getMat1())) {
                                Bank.withdraw(ZeroxCrafting.toCraft.getMat1(), ZeroxCrafting.toCraft.getAmount1());
                                Time.sleepUntil(() -> Inventory.contains(ZeroxCrafting.toCraft.getMat1()),Random.nextInt(500, 2000));
                            }
                             if (!Inventory.contains(ZeroxCrafting.toCraft.getMat2())) {
                                Bank.withdraw(ZeroxCrafting.toCraft.getMat2(), ZeroxCrafting.toCraft.getAmount2());
                                 Time.sleepUntil(() -> Inventory.contains(ZeroxCrafting.toCraft.getMat2()),Random.nextInt(500, 2000));
                            }
                        } else {
                            Log.severe("Out of materials");
                    }
                }
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

        if (ZeroxCrafting.toCraft.getMat2() == 0) {
            return !Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && ZeroxCrafting.location.getBankArea().contains(Players.getLocal());
        } else {
            return !(Inventory.contains(ZeroxCrafting.toCraft.getMat1()) && Inventory.contains(ZeroxCrafting.toCraft.getMat2())) && ZeroxCrafting.location.getBankArea().contains(Players.getLocal());
        }
    }
}
