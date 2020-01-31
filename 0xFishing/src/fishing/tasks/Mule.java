package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.function.Predicate;

import static fishing.ZeroxFishing.*;


public class Mule extends Task {

    @Override
    public boolean validate() {
        InterfaceComponent FriendlistName = Interfaces.getComponent(429, 11, 0);
        InterfaceComponent FriendlistWorld = Interfaces.getComponent(429, 11, 2);
        int fishinglvl = Skills.getCurrentLevel(Skill.FISHING);


        return !muled && fishinglvl > 40 && FriendlistName != null && FriendlistName != null && FriendlistName.getText().equals(MULE_NAME) && FriendlistWorld.getText().equals(MULE_WORLD) && tutProgress == 1000;
    }

    @Override
    public int execute() {

        if (Worlds.getCurrent() != MULE_WORLD_INT) {
            worldHop();
        } else {
            if (!banked) {
                getNotedLobsters();
            } else {
                tradeMule();
            }
        }

        return 0;
    }

    private void worldHop() {
        Log.info("Hopping to: " + MULE_WORLD);
        WorldHopper.hopTo(MULE_WORLD_INT);
        Time.sleepUntil(() -> Worlds.getCurrent() == MULE_WORLD_INT, Random.nextInt(10000, 11000));
    }

    private void getNotedLobsters() {
        if (Fishtype.SHRIMPS.getBankingArea().contains(Players.getLocal())) {
            if (Bank.isOpen()) {

                if (Inventory.getFreeSlots() < 2) {
                    Bank.depositInventory();
                    Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(500, 1500));
                }

                Log.info(Bank.contains(995));

                if (Bank.contains(995)) {
                    Bank.withdrawAll(995);
                    Time.sleepUntil(() -> !Bank.contains(995), Random.nextInt(500, 1000));
                }

                Log.info(!Inventory.contains(378));
                if (!Inventory.contains(378)) {
                    Bank.setWithdrawMode(Bank.WithdrawMode.NOTE);
                    Bank.withdrawAll(377);
                    Time.sleepUntil(() -> Inventory.contains(378), Random.nextInt(500, 1000));
                }

                if (!Bank.contains(995, 377)) {
                    banked = true;
                }

            } else {
                Predicate<SceneObject> bankPred = b -> b.containsAction("Bank");
                SceneObject BANK = SceneObjects.getNearest(bankPred);

                if (!BANK.equals(null)) {
                    BANK.interact("Bank");
                    Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
                }
            }
        } else {
            Area MUSA_POINT = Area.rectangular(2913, 3182, 2964, 3142);
            Area homeTPArea = Area.rectangular(3214, 3225, 3231, 3214);

            if (MUSA_POINT.contains(Players.getLocal())) {
                Spell homeTP = Spell.Modern.HOME_TELEPORT;
                Magic.cast(homeTP);
                Time.sleepUntil(() -> homeTPArea.contains(Players.getLocal()), Random.nextInt(20000, 25000));
            } else{

                Movement.walkToRandomized(Fishtype.SHRIMPS.getBankingArea().getCenter());
            }
        }
    }

        private void tradeMule () {
            if (Trade.isOpen()) {
                if (!Trade.isWaitingForMe()) {
                    Item[] inventory = Inventory.getItems();
                    for (Item i : inventory) {
                        if (i.getId() == 995 || i.getId() == 378) {
                            Trade.offerAll(i.getId());
                            Time.sleep(Random.nextInt(500, 750));
                        }
                    }
                } else {
                    Trade.accept();
                }
            } else {
                Player mule = Players.getNearest(MULE_NAME);
                if (!mule.equals(null)) {
                    mule.interact("Trade with");
                    Time.sleepUntil(() -> Trade.isOpen(), Random.nextInt(7000, 10000));
                }
            }
        }
    }
