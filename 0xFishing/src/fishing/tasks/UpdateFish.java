package fishing.tasks;

import org.rspeer.script.task.Task;

public class UpdateFish extends Task {
    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int execute() {
        //update mijn fishtype
        return 0;
    }

    public boolean shouldSwitch() {
        //current fishing level > goal
        return true;
    }
}
