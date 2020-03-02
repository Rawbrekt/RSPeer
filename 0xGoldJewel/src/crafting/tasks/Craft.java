package crafting.tasks;

import crafting.ZeroxGoldJewel;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.InterfaceAddress;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import static crafting.ZeroxGoldJewel.furnaceArea;
import static crafting.ZeroxGoldJewel.toCraft;

public class Craft extends Task {

    private static final InterfaceAddress CRAFT_ADDRESS = new InterfaceAddress(() -> Interfaces.getFirst(446, i -> i.getItemId() == toCraft.getFinishedProduct()));
    private long anim = 0;

    @Override
    public boolean validate() {
        return (ZeroxGoldJewel.toCraft != null) && furnaceArea.contains(Players.getLocal()) && Inventory.contains(toCraft.getRawMaterial()) && Inventory.contains(toCraft.getMould());
    }

    @Override
    public int execute() {

        SceneObject FURNACE = SceneObjects.getNearest(16469);
        if (!(FURNACE == null)) {
            if (!isAnimating()) {
                InterfaceComponent jewelleryCreation = Interfaces.getComponent(446,0);
                if (jewelleryCreation == null) {
                    FURNACE.interact("Smelt");
                    Time.sleepUntil(() -> (jewelleryCreation != null), Random.nextInt(750, 1500));
                } else {
                    CRAFT_ADDRESS.resolve().interact("Make");
                }
            }
        }

        return Random.nextInt(750, 1500);

    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }

        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }
}
