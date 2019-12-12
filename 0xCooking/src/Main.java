import org.rspeer.runetek.api.component.Production;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(name = "0xCooking", desc = "Tries to cook.", category = ScriptCategory.COOKING, developer = "0xRip", version = 0.1)
public class Main extends Script{
    @Override
    public int loop() {
        if (Production.isOpen()) {
            Log.info("Setting amount to all");
            Production.initiate(Production.Amount.ALL);
        }
        return 1500;
    }
}
