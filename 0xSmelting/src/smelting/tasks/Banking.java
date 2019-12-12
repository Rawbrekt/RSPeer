package smelting.tasks;

import smelting.ZeroxSmelting;
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
            if (ZeroxSmelting.toSmelt.getMat2() == 0) {
                if (!Inventory.containsOnly(ZeroxSmelting.toSmelt.getMat1())) {
                    if (Bank.contains(ZeroxSmelting.toSmelt.getMat1())) {
                        Bank.withdraw(ZeroxSmelting.toSmelt.getMat1(), ZeroxSmelting.toSmelt.getAmount1());
                    } else {
                        Log.severe("Out of materials");
                    }
                } else {
                    Bank.depositInventory();
                }
            } else {
                if (!Inventory.containsAll(ZeroxSmelting.toSmelt.getMat1(), ZeroxSmelting.toSmelt.getMat2())) {
                    if (!Inventory.isEmpty()) {
                        Bank.depositInventory();
                        Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(500, 1200));
                    }
                    if (Bank.containsAll(ZeroxSmelting.toSmelt.getMat1(), ZeroxSmelting.toSmelt.getMat2())) {
                        if (!Inventory.contains(ZeroxSmelting.toSmelt.getMat1())) {
                            Bank.withdraw(ZeroxSmelting.toSmelt.getMat1(), ZeroxSmelting.toSmelt.getAmount1());
                            Time.sleepUntil(() -> Inventory.contains(ZeroxSmelting.toSmelt.getMat1()), Random.nextInt(500, 2000));
                        }
                        if (!Inventory.contains(ZeroxSmelting.toSmelt.getMat2())) {
                            Bank.withdraw(ZeroxSmelting.toSmelt.getMat2(), ZeroxSmelting.toSmelt.getAmount2());
                            Time.sleepUntil(() -> Inventory.contains(ZeroxSmelting.toSmelt.getMat2()), Random.nextInt(500, 2000));
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

        if (ZeroxSmelting.toSmelt.getMat2() == 0) {
            return !Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && ZeroxSmelting.location.getBankArea().contains(Players.getLocal());
        } else {
            return !(Inventory.contains(ZeroxSmelting.toSmelt.getMat1()) && Inventory.contains(ZeroxSmelting.toSmelt.getMat2())) && ZeroxSmelting.location.getBankArea().contains(Players.getLocal());
        }
    }
}
