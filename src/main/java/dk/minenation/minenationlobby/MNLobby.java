package dk.minenation.minenationlobby;

import dk.minenation.minenationlobby.events.CompassHandler;
import dk.minenation.minenationlobby.events.JoinQuitHandler;
import dk.minenation.minenationlobby.util.MNUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MNLobby extends JavaPlugin implements PluginMessageListener {

    public static MNLobby plugin;
    public BossBar bossBar = null;
    int bossBarId = 1;
    int bossBarUpdateCount = 0;
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new CompassHandler(), this);
        this.getServer().getPluginManager().registerEvents(new JoinQuitHandler(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        bossBar = getServer().createBossBar(MNUtil.color("&fWelcome to &bMineNation.dk&f!"), BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);

        for (Player player : getServer().getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (bossBarUpdateCount == 100) {
                    bossBarUpdateCount = 0;
                    switch (bossBarId) {
                        case 1:
                            bossBarId = 2;
                            bossBar.setTitle(MNUtil.color("&fHead to our panel, &bhttps://minenation.dk&f, to create a server!"));
                            bossBar.setProgress(1.0);
                            break;

                        case 2:
                            bossBarId = 3;
                            bossBar.setTitle(MNUtil.color("&fServers are only available to alpha testers."));
                            bossBar.setProgress(1.0);
                            break;

                        case 3:
                            bossBarId = 1;
                            bossBar.setTitle(MNUtil.color("&fWelcome to &bMineNation.dk&f!"));
                            bossBar.setProgress(1.0);
                            break;
                    }
                }

                bossBar.setProgress(bossBar.getProgress() - 0.01);
                bossBarUpdateCount = bossBarUpdateCount + 1;
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDisable() {
        bossBar.removeAll();
        executorService.shutdownNow();
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // this does nothing important to me
    }
}
