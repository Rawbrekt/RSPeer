package crafting;

import crafting.data.Item;
import crafting.data.Location;
import crafting.tasks.Banking;
import crafting.tasks.Craft;
import crafting.tasks.Traverse;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;

@ScriptMeta(name = "0xCrafting", desc = "Tries to craft.", category = ScriptCategory.CRAFTING, developer = "0xRip", version = 0.1)

public class ZeroxCrafting extends TaskScript {


    public static Item toCraft = Item.getItem("BRONZE_BAR");
    public static Location location = Location.getLocation("EDGEVILLE");

    private static final Task[] TASKS = {new Banking(), new Traverse(), new Craft()};

    @Override
    public void onStart() {
        submit(TASKS);

    }
}
