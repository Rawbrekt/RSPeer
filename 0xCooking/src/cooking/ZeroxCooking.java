package cooking;

import cooking.data.GUI;
import cooking.data.Resources;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import cooking.tasks.Banking;
import cooking.tasks.Cook;
import cooking.tasks.Traverse;

@ScriptMeta(name = "0xCooking", desc = "Tries to cook.", category = ScriptCategory.COOKING, developer = "0xRip", version = 0.1)

public class ZeroxCooking extends TaskScript {

    public static Resources res;

    private static final Task[] TASKS = {new Banking(), new Traverse(), new Cook()};
    @Override
    public void onStart() {
        new GUI().setVisible(true);
        submit(TASKS);
    }
}
