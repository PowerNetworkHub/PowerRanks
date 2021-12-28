package nl.svenar.PowerRanks.addons;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import nl.svenar.PowerRanks.Util.Util;

public class AddonDownloader {
    private String addonsURL = "http://addons.powerranks.nl/addons.json";
    private Addons addonlist;

    public AddonDownloader() {
        try {
            String rawJSON = Util.readUrl(addonsURL);

            Gson gson = new Gson();     
            addonlist = gson.fromJson(rawJSON, Addons.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DownloadableAddon> getDownloadableAddons() {
        ArrayList<DownloadableAddon> addons = new ArrayList<DownloadableAddon>();
        for (Addon addon : addonlist.addons) {
            DownloadableAddon downloadableAddon = new DownloadableAddon(addon);
            addons.add(downloadableAddon);
        }
        return addons;
    }

    static class Addon {
        String name;
        String version;
        String powerranksVersion;
        ArrayList<String> description;
        String download;
        boolean autoDownloadable;
        String author;
    }

    static class Addons {
        List<Addon> addons;
    }
}