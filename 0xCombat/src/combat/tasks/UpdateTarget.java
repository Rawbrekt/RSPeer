package combat.tasks;

import combat.ZeroxCombat;
import combat.data.Target;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class UpdateTarget extends Task {

    int goal;

    @Override
    public boolean validate() {
        int att = Skills.getLevel(Skill.ATTACK);
        int str = Skills.getLevel(Skill.STRENGTH);
        int def = Skills.getLevel(Skill.DEFENCE);

        int min = Math.min(att, Math.min(str,def));
        goal = (Math.round(min/10)+1)*10;
        return !(goal == ZeroxCombat.goal);
    }

    @Override
    public int execute() {

        ZeroxCombat.goal = goal;
        ZeroxCombat.target = Target.getBestTarget(goal);

        Log.info("Changed target to: " + ZeroxCombat.target);

        return Random.nextInt(1500,6000);
    }
}
