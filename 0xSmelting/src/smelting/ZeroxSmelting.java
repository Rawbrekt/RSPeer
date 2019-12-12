package smelting;

import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import smelting.data.Item;
import smelting.data.Location;
import smelting.tasks.Banking;
import smelting.tasks.Smelt;
import smelting.tasks.Traverse;

@ScriptMeta(name = "0xSmelting", desc = "Tries to smelt.", category = ScriptCategory.SMITHING, developer = "0xRip", version = 0.1)

public class ZeroxSmelting extends TaskScript {


    private static final Task[] TASKS = {new Banking(), new Traverse(), new Smelt()};
    public static Item toSmelt = Item.getItem("BRONZE_BAR");
    public static Location location = Location.getLocation("EDGEVILLE");

    @Override
    public void onStart() {
        submit(TASKS);
    }
}
