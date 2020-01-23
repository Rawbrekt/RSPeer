package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;


import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static fishing.ZeroxFishing.fishtype;

public class Traverse extends Task {

    private Area MUSA_POINT = Area.rectangular(2913, 3182, 2964, 3142);
    private Area MUSA_DOCKS = Area.rectangular(2949, 3149, 2961, 3144);
    private Area PORT_SARIM = Area.rectangular(3024, 3226, 3030, 3210);

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return traverseToBank() || traverseToFish();
    }

    @Override
    public int execute() {
        checkRunEnergy();

        if (fishtype.equals(Fishtype.LOBSTERS)) {
            if (traverseToBank()) {
                if (MUSA_POINT.contains(Players.getLocal())) {
                    if (MUSA_DOCKS.contains(Players.getLocal())) {
                        if (Dialog.isOpen()) {
                            if (Dialog.canContinue()) {
                                Dialog.processContinue();
                            } else {
                                Dialog.process("Yes please");
                                Dialog.process("Can I journey on this ship?");
                                Dialog.process("Search away, I have nothing to hide.");
                                Dialog.process("Ok.");
                            }

                        } else {
                            Predicate<Npc> SeamanPred = s -> s.getName().contains("Seaman");
                            Npc Seaman = Npcs.getNearest(SeamanPred);
                            if (Seaman != null) {
                                Seaman.interact("Pay-fare");
                            }
                        }

                    } else {
                        Movement.walkToRandomized(MUSA_DOCKS.getCenter());
                    }
                } else {
                    if (Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait())) {
                        Movement.walkToRandomized(fishtype.getBankingArea().getCenter());
                    } else {
                        Movement.walkToRandomized(fishtype.getAltBankingArea().getCenter());
                    }
                }

            } else {
                if (Inventory.getCount(fishtype.getBait()) > 60) {
                    if (!MUSA_POINT.contains(Players.getLocal())) {
                        if (PORT_SARIM.contains(Players.getLocal())) {
                            if (Dialog.isOpen()) {
                                if (Dialog.canContinue()) {
                                    Dialog.processContinue();
                                } else {
                                    Dialog.process("Yes please");
                                }

                            } else {

                                Predicate<Npc> SeamanPred = s -> s.getName().contains("Seaman");
                                Npc Seaman = Npcs.getNearest(SeamanPred);
                                if (Seaman != null) {
                                    Seaman.interact("Pay-fare");
                                }

                            }
                        } else {
                            Movement.walkToRandomized(PORT_SARIM.getCenter());
                        }
                    } else {
                        Log.info("walking to fish");
                        Movement.walkToRandomized(fishtype.getFishingArea().getCenter());
                    }
                }
            }

        } else {
            Movement.walkToRandomized(traverseToBank() ? fishtype.getBankingArea().getCenter() : fishtype.getFishingArea().getCenter());
        }


        return Random.nextInt(1500, 6000);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private boolean traverseToBank() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()));
        } else {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()) || !Inventory.contains(fishtype.getBait()));
        }
    }

    private boolean traverseToFish() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem());
        } else {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait());
        }
    }

}
