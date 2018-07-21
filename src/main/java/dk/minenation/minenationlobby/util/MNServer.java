package dk.minenation.minenationlobby.util;

public class MNServer {
    public String id;
    public String name;
    public boolean online;
    public String motd;
    public int playerCount;
    public int maxPlayers;
    public boolean visibility;

    public void Server(String id, String name, boolean online, String motd, int playerCount, int maxPlayers, boolean visibility) {
        this.id = id;
        this.name = name;
        this.online = online;
        this.motd = motd;
        this.playerCount = playerCount;
        this.maxPlayers = maxPlayers;
        this.visibility = visibility;
    }
}
