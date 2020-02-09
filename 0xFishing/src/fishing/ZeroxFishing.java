package fishing;

import fishing.data.Fishtype;
import fishing.tasks.*;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;

@ScriptMeta(name = "0xFishing", desc = "Tries to fish.", category = ScriptCategory.FISHING, developer = "0xRip", version = 0.1)

public class ZeroxFishing extends TaskScript implements ChatMessageListener {

    public static boolean muled = false;
    public static boolean banked = false;
    public static String MULE_NAME = "Maxi Riprekt";
    public static String MULE_WORLD = "World 308";
    public static int MULE_WORLD_INT = 308;
    public static final int V_TUTISLAND = 281;
    public static final int V_COOKS_ASSISTANT = 29;
    public static final int V_SHEEP_SHEARER = 179;
    public static final int V_ROMEO_AND_JULLIET = 144;
    public static int tutProgress;
    public static int cookProgress;
    public static int sheepProgress;
    public static int romeoProgress;



    public static Fishtype fishtype = Fishtype.SHRIMPS;

    private static final Task[] TASKS = {new TutIsland(), new Quests(), new Mule(), new Banking(), new Traverse(), new Fish()};

    @Override
    public void onStart() {

        int fishlvl = Skills.getCurrentLevel(Skill.FISHING);
        fishtype = Fishtype.getBestFishType(fishlvl);

        tutProgress = Varps.get(V_TUTISLAND);
        cookProgress = Varps.get(V_COOKS_ASSISTANT);
        sheepProgress = Varps.get(V_SHEEP_SHEARER);
        romeoProgress = Varps.get(V_ROMEO_AND_JULLIET);

        submit(TASKS);

    }

    @Override
    public void onStop() {
        Log.info("Goodbye");
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if(msg.getMessage().equals("Accepted trade")) {
            muled = true;
            Log.info("trade done");
        }

        if (msg.getMessage().contains("has logged o")) {
            muled = false;
            Log.info("mule logged out");
        }
    }

    public static boolean questsFinished() {
        if (cookProgress == 2 && sheepProgress == 21 && romeoProgress == 100) {
            return true;
        } else {
            return false;
        }
    }
}
