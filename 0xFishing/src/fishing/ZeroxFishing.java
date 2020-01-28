package fishing;

import fishing.data.Fishtype;
import fishing.tasks.Banking;
import fishing.tasks.Fish;
import fishing.tasks.Traverse;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;

@ScriptMeta(name = "0xFishing", desc = "Tries to fish.", category = ScriptCategory.FISHING, developer = "0xRip", version = 0.1)

public class ZeroxFishing extends TaskScript {

    public static boolean muled = false;
    public static String MULE_NAME = "";
    public static String MULE_WORLD = "World 301";


    public static Fishtype fishtype = Fishtype.SHRIMPS;

    private static final Task[] TASKS = {new Banking(), new Traverse(), new Fish()};

    @Override
    public void onStart() {

        int fishlvl = Skills.getCurrentLevel(Skill.FISHING);
        fishtype = Fishtype.getBestFishType(fishlvl);

        Log.fine(fishtype);

        /*if (fishlvl < 20) {
            fishtype = Fishtype.SHRIMPS;
        } else if (fishlvl < 40) {
            fishtype = Fishtype.TROUT;
        } else if (fishlvl > 40) {
            fishtype = Fishtype.LOBSTERS;
        }*/

        submit(TASKS);


    }
}
