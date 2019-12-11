package fishing.tasks;

import fishing.ZeroxFishing;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.task.Task;

public class UpdateFishtype extends Task {
    @Override
    public boolean validate() {
        int FishingLevel = Skills.getLevel(Skill.FISHING);

        return FishingLevel >= ZeroxFishing.FishingLevel;
    }

    @Override
    public int execute() {
        return 0;
    }
}
