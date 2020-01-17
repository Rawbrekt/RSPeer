package fishing.tasks;

import fishing.ZeroxFishing;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

public class Fish extends Task {

    private long anim = 0;

    @Override
    public boolean validate() {
        if (ZeroxFishing.fishtype.getFISHING_AREA().contains(Players.getLocal()) && Inventory.contains(ZeroxFishing.fishtype.getItem())) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int execute() {
        Npc fishSpot = Npcs.getNearest("Fishing Spot");
        if (!(fishSpot == null) && !isAnimating() && ZeroxFishing.fishtype.getFISHING_AREA().contains(Players.getLocal())) {
            fishSpot.interact(ZeroxFishing.fishtype.getMethod());
        }
        return Random.nextInt(3000, 7050);
    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }
        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }


}
