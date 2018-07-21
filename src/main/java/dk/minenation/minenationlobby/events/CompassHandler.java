package dk.minenation.minenationlobby.events;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dk.minenation.minenationlobby.MNLobby;
import dk.minenation.minenationlobby.util.MNServer;
import dk.minenation.minenationlobby.util.MNUtil;
import dk.minenation.minenationlobby.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompassHandler implements Listener {

    ServerUtil serverUtil = new ServerUtil();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getName().startsWith(MNUtil.color("&b&lServers"))) {
            ItemStack item = e.getCurrentItem();
            if (item.getType() == Material.SIGN) {
                String serverName = MNUtil.stripColor(item.getItemMeta().getDisplayName());
                MNServer server = serverUtil.getServerFromName(serverName);
                e.getWhoClicked().sendMessage(MNUtil.color("&8[&bMinenation&8] &9Sending you to &b" + serverName + "&9!"));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(serverUtil.getServerFromName(serverName).id);

                ((Player) e.getWhoClicked()).sendPluginMessage(MNLobby.plugin, "BungeeCord", out.toByteArray());
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player plr = e.getPlayer();
        Material item = plr.getInventory().getItemInMainHand().getType();

        if (item == Material.COMPASS) {
            // COMPASS, START ACTIONS
            List<MNServer> servers = serverUtil.getAllServers();
            List<MNServer> validServers = Lists.newArrayList();
            long timeSinceCache = System.currentTimeMillis() - serverUtil.getCacheDate();
            for(MNServer server : servers) {
                if (server.online && server.visibility) {
                    validServers.add(server);
                }
            }

            Collections.sort(validServers, new Comparator<MNServer>() {
                public int compare(MNServer server, MNServer server1) {
                    if(server.playerCount > server1.playerCount) {
                        return -1;
                    } else if (server.playerCount == server1.playerCount) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            List<List<MNServer>> pages = listToPages(validServers, 36);

            validServers = pages.get(0);

            Inventory inv = null;

            if (timeSinceCache > 10000) {
                inv = Bukkit.createInventory(null, 54, MNUtil.color("&b&lServers &8- &3Updated " + timeSinceCache/1000 + " seconds ago."));
            } else {
                inv = Bukkit.createInventory(null, 54, MNUtil.color("&b&lServers"));
            }

            for (MNServer server : validServers) {
                ItemStack itemStack = createItemFromServer(server);
                int index = validServers.indexOf(server);

                inv.setItem(index, itemStack);
            }

            ItemStack sepItem = createItem(Material.STAINED_GLASS_PANE, 15, "&r");

            inv.setItem(36, sepItem);
            inv.setItem(37, sepItem);
            inv.setItem(38, sepItem);
            inv.setItem(39, sepItem);
            inv.setItem(40, sepItem);
            inv.setItem(41, sepItem);
            inv.setItem(42, sepItem);
            inv.setItem(43, sepItem);
            inv.setItem(44, sepItem);

            plr.openInventory(inv);

        }
    }

    private ItemStack createItem(Material material, int data, String name) {
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MNUtil.color(name));

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItemFromServer(MNServer server) {
        ItemStack item = new ItemStack(Material.SIGN);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MNUtil.color("&b" + server.name));
        String[] loreList = server.motd.split("\\\\n");
        List<String> lore = Lists.newArrayList();
        for (String motdSection : loreList) {
            lore.add(MNUtil.color("&7" + motdSection));
        }
        lore.add("");
        lore.add(MNUtil.color("&7" + server.playerCount + "/" + server.maxPlayers + " online"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static <T> List<List<T>> listToPages(List<T> c, Integer pageSize) {
        if (c == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();
        int numPages = (int) Math.ceil((double)list.size() / (double)pageSize);
        List<List<T>> pages = new ArrayList<List<T>>(numPages);
        for (int pageNum = 0; pageNum < numPages;)
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        return pages;
    }

}
