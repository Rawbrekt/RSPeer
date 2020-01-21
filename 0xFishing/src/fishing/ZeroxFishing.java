package fishing;

import fishing.data.Fishtype;
import fishing.tasks.Banking;
import fishing.tasks.Fish;
import fishing.tasks.Traverse;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;

@ScriptMeta(name = "0xFishing", desc = "Tries to fish.", category = ScriptCategory.FISHING, developer = "0xRip", version = 0.1)

public class ZeroxFishing extends TaskScript {

    public static Fishtype fishtype = Fishtype.TROUT;

    private static final Task[] TASKS = {new Banking(), new Traverse(), new Fish()};

    @Override
    public void onStart() {
        submit(TASKS);
    }
}
