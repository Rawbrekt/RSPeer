package combat.tasks;

import combat.ZeroxCombat;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.runetek.api.commons.math.Random;

public class Traverse extends Task {
    @Override
    public boolean validate() {
        return !ZeroxCombat.target.getArea().contains(Players.getLocal()) && Players.getLocal().getHealthPercent() > 20;
    }

    @Override
    public int execute() {
        Movement.walkToRandomized(ZeroxCombat.target.getArea().getCenter());
        return Random.nextInt(1500,6000);
    }
}
