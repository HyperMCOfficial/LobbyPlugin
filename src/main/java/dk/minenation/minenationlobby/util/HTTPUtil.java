package dk.minenation.minenationlobby.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HTTPUtil {

    public static String fireGet(String urlParam) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlParam);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static List<MNServer> getAllServers() throws Exception {
        String apiResult = fireGet("https://api.minenation.dk/servers");
        List<MNServer> result = Lists.newArrayList();

        for (JsonElement element : new JsonParser().parse(apiResult).getAsJsonArray()) {
            MNServer server = new Gson().fromJson(element, MNServer.class);
            result.add(server);
        }

        return result;
    }

}
