package emanondev.itemedit.command.itemedit;

import com.destroystokyo.paper.Namespaced;
import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CanDestroy extends SubCmd {
    public CanDestroy(ItemEditCommand cmd) {
        super("candestroy", cmd, true, true);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        if(args.length < 2) {
            onFail(player, alias);
            return;
        }
        ItemStack itemInHand = getItemInHand(player);
        ItemMeta itemMeta = itemInHand.getItemMeta();
        Set<Namespaced> destroyableKeys = itemMeta.getDestroyableKeys();
        // getDestroyableKeys can return an EmptySet
        if(destroyableKeys.isEmpty())
            destroyableKeys = new HashSet<>();
        if(args[1].equalsIgnoreCase("clear")) {
            destroyableKeys.clear();
        } else if(args.length >= 3) {
            Namespaced namespaced;
            if(!args[2].contains(":")) {
                try {
                    namespaced = Material.valueOf(args[2].toUpperCase(Locale.ENGLISH)).getKey();
                } catch(IllegalArgumentException ex) {
                    onFail(player, alias);
                    return;
                }
            } else {
                String[] split = args[2].split(":");
                if(split.length < 2) {
                    onFail(player, alias);
                    return;
                }
                namespaced = new NamespacedKey(split[0], split[1]);
            }
            switch(args[1]) {
                case "add":
                    destroyableKeys.add(namespaced);
                    break;
                case "remove":
                    destroyableKeys.remove(namespaced);
                    break;
                default:
                    onFail(player, alias);
                    return;
            }
        }
        itemMeta.setDestroyableKeys(destroyableKeys);
        itemInHand.setItemMeta(itemMeta);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        switch(args.length) {
            case 2:
                return Util.complete(args[1], List.of("add", "clear", "remove"));
            case 3:
                switch(args[1]) {
                    case "add":
                        return Util.complete(args[2], Material.class, Material::isBlock);
                    case "remove":
                        ItemStack itemInHand = getItemInHand((Player) sender);
                        if(itemInHand.hasItemMeta()) {
                            return Util.complete(args[2], itemInHand.getItemMeta()
                                    .getDestroyableKeys().stream()
                                    .map(Namespaced::getKey)
                                    .collect(Collectors.toList())
                            );
                        }
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
}
