package nl.svenar.PowerRanks;

import java.util.regex.Matcher;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker
{
    @SuppressWarnings("unused")
	private PowerRanks plugin;
    private String plugin_url;
    
    public UpdateChecker(final PowerRanks plugin, final String plugin_url) {
        this.plugin = plugin;
        this.plugin_url = plugin_url;
    }
    
    public Boolean getCheckDownloadURL() {
        try {
            final URL url = new URL(this.plugin_url);
            final URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/1.0");
            conn.setConnectTimeout(5000);
            conn.connect();
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Boolean getLinkNow = false;
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (getLinkNow) {
                    final String re1 = ".*?";
                    final String re2 = "(\".*?\")";
                    final Pattern p = Pattern.compile(String.valueOf(re1) + re2, 34);
                    final Matcher m = p.matcher(inputLine);
                    if (m.find()) {
                        final String downloadUrl = "http://www.spigotmc.org/" + m.group(1).toString().replace("\"", "");
                        PowerRanks.log.info(downloadUrl);
                    }
                    getLinkNow = false;
                }
                if (inputLine.contains("<label class=\"downloadButton \">")) {
                    getLinkNow = true;
                }
            }
            in.close();
            return true;
        }
        catch (Exception e) {
            PowerRanks.log.warning("Something went wrong while downloading an update.");
            PowerRanks.log.warning("Please check the plugin's page to see if there are any updates available.");
            PowerRanks.log.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}