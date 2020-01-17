package woodcutting;

import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import woodcutting.tasks.Mule;

@ScriptMeta(name = "0xWoodcutting", desc = "Tries to woodcut.", category = ScriptCategory.WOODCUTTING, developer = "0xRip", version = 0.1)
public class ZeroxWoodcutting extends TaskScript {

    private static final Task[] TASKS = {new Mule()};
    @Override
    public void onStart() {
        submit(TASKS);
    }
}
