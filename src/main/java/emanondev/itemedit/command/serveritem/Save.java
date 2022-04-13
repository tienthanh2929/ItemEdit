package emanondev.itemedit.command.serveritem;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.ServerItemCommand;
import emanondev.itemedit.command.SubCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Save extends SubCmd {

    public Save(ServerItemCommand cmd) {
        super("save", cmd, true, true);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        try {
            if (args.length != 2)
                throw new IllegalArgumentException("Wrong param number");
            if (ItemEdit.get().getServerStorage().getItem(args[1]) == null)
                ItemEdit.get().getServerStorage().setItem(args[1], this.getItemInHand(p).clone());
            else
                throw new IllegalArgumentException();
            sendLanguageString("success", null, p, "%id%", args[1].toLowerCase());
        } catch (Exception e) {
            onFail(p, alias);
        }
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
