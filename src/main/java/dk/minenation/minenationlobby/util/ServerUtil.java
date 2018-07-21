package dk.minenation.minenationlobby.util;

import com.google.common.collect.Lists;

import java.util.List;

public class ServerUtil {

    private List<MNServer> cachedData = Lists.newArrayList();
    private long cacheDate;


    public List<MNServer> getAllServers() {
        if (System.currentTimeMillis() - cacheDate > 10000) {
            try {
                cachedData = HTTPUtil.getAllServers();
                cacheDate = System.currentTimeMillis();
                return cachedData;
            } catch (Exception e) {
                // todo: log
            }
            List<MNServer> blankArray = Lists.newArrayList();
            return blankArray;
        } else {
            return cachedData;
        }
    }

    public long getCacheDate() {
        return cacheDate;
    }

    public MNServer getServerFromName(String name) {
        MNServer result = null;
        for (MNServer server : getAllServers()) {
            if (server.name.trim().equals(name.trim())) {
                result = server;
                //return server;
            }
        }
        return result;
    }

}
