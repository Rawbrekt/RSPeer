package woodcutting.tasks;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Mule extends Task {

    boolean muled = false;
    final String MULE_NAME = "Riprekt";
    final String MULE_WORLD = "World 301";

    @Override
    public boolean validate() {

        InterfaceComponent FriendlistName = Interfaces.getComponent(429, 11, 0);
        InterfaceComponent FriendlistWorld = Interfaces.getComponent(429, 11, 2);

        Log.info(FriendlistWorld.getText());
        Log.info(FriendlistName.getText());

        return !muled && FriendlistName != null && FriendlistName != null && FriendlistName.getText().equals(MULE_NAME) && FriendlistWorld.getText().equals(MULE_WORLD);
    }

    @Override
    public int execute() {

        Log.info("yallah");

        return 5000;
    }
}
