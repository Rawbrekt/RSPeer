package combat;

import combat.data.Target;
import combat.tasks.Fight;
import combat.tasks.Traverse;
import combat.tasks.UpdateTarget;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;

@ScriptMeta(name = "0xCombat", desc = "Tries to fight.", category = ScriptCategory.COMBAT, developer = "0xRip", version = 0.1)

public class ZeroxCombat extends TaskScript {

    public static int att = Skills.getLevel(Skill.ATTACK);
    public static int str = Skills.getLevel(Skill.STRENGTH);
    public static int def = Skills.getLevel(Skill.DEFENCE);

    static int min = Math.min(att, Math.min(str,def));
    public static int goal = (Math.round(min/10)+1)*10;
    public static Target target = Target.getBestTarget(goal);

    private static final Task[] TASKS = {new UpdateTarget(), new Traverse(), new Fight()};


    @Override
    public void onStart() {
        Log.info("goal: " + goal + " target: " + target);
        submit(TASKS);
    }

}