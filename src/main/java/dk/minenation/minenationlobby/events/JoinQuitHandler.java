package dk.minenation.minenationlobby.events;

import dk.minenation.minenationlobby.MNLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        MNLobby.plugin.bossBar.addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        MNLobby.plugin.bossBar.removePlayer(e.getPlayer());
    }

}
