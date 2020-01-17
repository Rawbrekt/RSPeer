package combat.tasks;

import combat.ZeroxCombat;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class Fight extends Task {

    Player me = Players.getLocal();
    int lvlAttack = Skills.getLevel(Skill.ATTACK);
    int lvlStrength = Skills.getLevel(Skill.STRENGTH);
    int lvlDefence = Skills.getLevel(Skill.DEFENCE);
    private int toggleNextRun = 20;


    @Override
    public boolean validate() {
        return ZeroxCombat.target.getArea().contains(me) && me.getHealthPercent() > 20 && !me.isAnimating() && (me.getTargetIndex() == -1);
    }

    @Override
    public int execute() {

        changeAttackStyle();

        Log.info("Searching " + ZeroxCombat.target.getName());
        checkRunEnergy();

        final Predicate<Npc> npcPred = x -> x.getName().contains(ZeroxCombat.target.getName())
                && ((x.getTarget() != null && x.getTarget().equals(me)) || x.getTargetIndex() == -1)
                && x.getHealthPercent() > 0;
        Npc target = Npcs.getNearest(npcPred);

        /*Npc target = Npcs.getNearest(x -> x.getName().equals(ZeroxCombat.target.getName()) && (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0);*/

        if (target != null) {
            Log.info("Attacking " + ZeroxCombat.target.getName());
            target.interact("Attack");
        }
        return Random.nextInt(1500,6000);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private void changeAttackStyle() {
        if (lvlStrength < ZeroxCombat.goal) {
            Combat.select(1);
            Time.sleep(Random.nextInt(100,1500));
            return;
        }

        if (lvlAttack < ZeroxCombat.goal) {
            Combat.select(0);
            Time.sleep(Random.nextInt(100,1500));
            return;
        }

        if (lvlDefence < ZeroxCombat.goal) {
            Combat.select(3);
            Time.sleep(Random.nextInt(100,1500));
            return;
        }

    }
}
