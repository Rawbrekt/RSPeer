package combat.tasks;

import combat.ZeroxCombat;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Fight extends Task {

    Player me = Players.getLocal();
    int lvlAttack = Skills.getLevel(Skill.ATTACK);
    int lvlStrength = Skills.getLevel(Skill.STRENGTH);
    int lvlDefence = Skills.getLevel(Skill.DEFENCE);


    @Override
    public boolean validate() {
        return ZeroxCombat.target.getArea().contains(me) && me.getHealthPercent() > 20 && !me.isAnimating() && (me.getTargetIndex() == -1);
    }

    @Override
    public int execute() {

        if(lvlAttack <ZeroxCombat.goal && lvlStrength >= ZeroxCombat.goal && lvlDefence <=ZeroxCombat.goal) {
            if (!Combat.getAttackStyle().equals(Combat.AttackStyle.ACCURATE)) {
                Log.info("switching to accurate");
                Combat.select(0);
            }
        }

        if(lvlAttack <=ZeroxCombat.goal && lvlStrength <ZeroxCombat.goal && lvlDefence <=ZeroxCombat.goal) {
            if (!Combat.getAttackStyle().equals(Combat.AttackStyle.AGGRESSIVE)) {
                Log.info("switching to aggressive");
                Combat.select(1);
            }
        }

        if(lvlAttack >= ZeroxCombat.goal && lvlStrength >=ZeroxCombat.goal && lvlDefence <=ZeroxCombat.goal) {
            if (!Combat.getAttackStyle().equals(Combat.AttackStyle.DEFENSIVE)) {
                Log.info("switching to defensive");
                Combat.select(3);
            }
        }

        Log.info("Searching " + ZeroxCombat.target.getName());

        Npc target = Npcs.getNearest(x -> x.getName().equals(ZeroxCombat.target.getName()) && (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0);

        if (target != null) {
            Log.info("Attacking " + ZeroxCombat.target.getName());
            target.interact("Attack");
        }
        return Random.nextInt(1500,6000);
    }
}
