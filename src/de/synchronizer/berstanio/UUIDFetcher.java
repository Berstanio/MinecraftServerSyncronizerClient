package de.synchronizer.berstanio;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;


public class UUIDFetcher {

    /**
     * @author ThexXTURBOXx https://github.com/ThexXTURBOXx/McUUIDFetcher/blob/master/UUIDFetcher.java
     * @author berstanio
     */

    /**
     * @param playername The name of the player
     * @return The UUID of the given player
     */
    public static UUID getUUID(String playername) {
        String output = callURL("https://api.mojang.com/users/profiles/minecraft/" + playername);

        StringBuilder result = new StringBuilder();

        readData(output, result, 7);

        String u = result.toString();

        StringBuilder uuid = new StringBuilder();

        for(int i = 0; i <= 31; i++) {
            uuid.append(u.charAt(i));
            if(i == 7 || i == 11 || i == 15 || i == 19) {
                uuid.append("-");
            }
        }

        return UUID.fromString(uuid.toString());
    }

    public static String[] getSignituareAndValue(UUID uuid){
        String output = callURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-","") + "?unsigned=false");
        StringBuilder stringBuilder = new StringBuilder();
        readData(output, stringBuilder, 47 + "Berstanio".length()/*Bukkit.getPlayer(uuid).getName().length()*/ + uuid.toString().replace("-","").length());
        String signituare = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        readData(output, stringBuilder, 76 + "Berstanio".length()/*Bukkit.getPlayer(uuid).getName().length()*/ + uuid.toString().replace("-","").length() + signituare.length());
        String value = stringBuilder.toString();
        String[] valueAndSignutare = new String[2];
        valueAndSignutare[0] = signituare;
        valueAndSignutare[1] = value;
        return valueAndSignutare;
    }


    private static void readData(String toRead, StringBuilder result, int i) {
        while(i < 200000) {
            if(!String.valueOf(toRead.charAt(i)).equalsIgnoreCase("\"")) {

                result.append(String.valueOf(toRead.charAt(i)));

            } else {
                break;
            }

            i++;
        }
    }

    private static String callURL(String URL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn;
        InputStreamReader in = null;
        try {
            URL url = new URL(URL);
            urlConn = url.openConnection();

            if (urlConn != null) urlConn.setReadTimeout(60 * 1000);

            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                int cp;

                while ((cp = bufferedReader.read()) != -1) {
                    sb.append((char) cp);
                }

                bufferedReader.close();
            }

            assert in != null;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
