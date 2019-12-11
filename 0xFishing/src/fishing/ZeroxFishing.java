package fishing;

import fishing.data.Fishtype;
import fishing.tasks.Bank;
import fishing.tasks.Fish;
import fishing.tasks.Traverse;
import fishing.tasks.UpdateFishtype;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;

@ScriptMeta(name = "0xFishing", desc = "Tries to fish.", category = ScriptCategory.FISHING, developer = "0xRip", version = 0.1)

public class ZeroxFishing extends TaskScript {

    public static int FishingLevel = Skills.getLevel(Skill.FISHING);
    public static Fishtype fishtype = Fishtype.getBestFishType(FishingLevel);

    private static final Task[] TASKS = {new UpdateFishtype(), new Bank(), new Traverse(), new Fish()};

    @Override
    public void onStart() {
        submit(TASKS);
    }
}
