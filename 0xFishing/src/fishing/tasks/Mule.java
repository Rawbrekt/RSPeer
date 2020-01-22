package fishing.tasks;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.task.Task;

import static fishing.ZeroxFishing.MULE_NAME;
import static fishing.ZeroxFishing.MULE_WORLD;
import static fishing.ZeroxFishing.muled;

public class Mule extends Task {

    @Override
    public boolean validate() {
        InterfaceComponent FriendlistName = Interfaces.getComponent(429, 11, 0);
        InterfaceComponent FriendlistWorld = Interfaces.getComponent(429, 11, 2);
        return !muled && FriendlistName != null && FriendlistName != null && FriendlistName.getText().equals(MULE_NAME) && FriendlistWorld.getText().equals(MULE_WORLD);
    }

    @Override
    public int execute() {
        return 0;
    }
}
