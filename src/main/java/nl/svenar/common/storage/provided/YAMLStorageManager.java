/**
 * This file is part of PowerRanks, licensed under the MIT License.
 *
 * Copyright (c) svenar (Sven) <powerranks@svenar.nl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

package nl.svenar.common.storage.provided;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

/**
 * YAML storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class YAMLStorageManager extends PowerStorageManager {

    private final DumperOptions yamlOptions = new DumperOptions();
    private final LoaderOptions loaderOptions = new LoaderOptions();
    private final Representer yamlRepresenter = new Representer(yamlOptions);
    private final Yaml yaml = new Yaml(new Constructor(loaderOptions), yamlRepresenter, yamlOptions, loaderOptions);

    private File ranksFile, playersFile;

    /**
     * Initialize this storage method by creating the required files
     * 
     * @param directory
     * @param ranksFileName
     * @param playersFileName
     */
    public YAMLStorageManager(String directory, String ranksFileName, String playersFileName) {
        File targetDir = new File(directory);
        this.ranksFile = new File(targetDir, ranksFileName);
        this.playersFile = new File(targetDir, playersFileName);

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        if (!this.ranksFile.exists()) {
            try {
                this.ranksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.playersFile.exists()) {
            try {
                this.playersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.loadRanks();
        this.loadPlayers();
    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public String getType() {
        return "YAML";
    }

    /**
     * Check if the storage method is connected. Returns true if a file base storage
     * is used.
     * 
     * Only used by external storage implementations like databases.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return true;
    }

    /**
     * Load all ranks from the storage method, can be retrieved by calling
     * getRanks()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadRanks() {
        boolean success = true;
        InputStream inputStream = null;

        this.setRanks(new ArrayList<PRRank>());

        try {
            inputStream = new FileInputStream(this.ranksFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(inputStream)) {
            Object obj = this.yaml.load(inputStream);
            if (Objects.nonNull(obj)) {
                if (obj instanceof LinkedHashMap) {
                    for (Entry<?, ?> entry : ((LinkedHashMap<?, ?>) obj).entrySet()) {
                        if (entry.getKey() instanceof String && entry.getValue() instanceof LinkedHashMap) {
                            String rankName = (String) entry.getKey();
                            Map<String, Object> rankData = (Map<String, Object>) entry.getValue();
                            rankData.put("name", rankName);

                            PRRank rank = this.getSerializer().deserialize(rankData, PRRank.class);
                            this.addRank(rank);
                        }

                    }
                } else {
                    success = false;
                }

            } else {
                success = false;
            }
        } else {
            success = false;
        }

        if (!success) {
            this.setRanks(new ArrayList<PRRank>());
        }
    }

    /**
     * Load all players from the storage method, can be retrieved by calling
     * getPlayers()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadPlayers() {
        boolean success = true;
        InputStream inputStream = null;

        this.setPlayers(new ArrayList<PRPlayer>());

        try {
            inputStream = new FileInputStream(this.playersFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(inputStream)) {
            Object obj = this.yaml.load(inputStream);
            if (Objects.nonNull(obj)) {
                if (obj instanceof LinkedHashMap) {
                    for (Entry<?, ?> entry : ((LinkedHashMap<?, ?>) obj).entrySet()) {
                        if (entry.getKey() instanceof String && entry.getValue() instanceof LinkedHashMap) {
                            String playerUUID = (String) entry.getKey();
                            Map<String, Object> playerData = (Map<String, Object>) entry.getValue();
                            playerData.put("uuid", playerUUID);

                            PRPlayer player = this.getSerializer().deserialize(playerData, PRPlayer.class);
                            this.addPlayer(player);
                        }

                    }
                } else {
                    success = false;
                }

            } else {
                success = false;
            }
        } else {
            success = false;
        }

        if (!success) {
            this.setPlayers(new ArrayList<PRPlayer>());
        }
    }

    /**
     * Save all ranks using the storage managers storage solution.
     */
    @Override
    public void saveRanks() {
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        try {
            PrintWriter writer = new PrintWriter(this.ranksFile);
            Map<String, Object> ranks2 = new HashMap<String, Object>();

            for (PRRank r : this.getRanks()) {
                Map<String, Object> serializedRank = this.getSerializer().serialize(r);
                for (Entry<String, Object> entry : serializedRank.entrySet()) {
                    if (entry.getValue().equals(r.getName())) {
                        serializedRank.remove(entry.getKey());
                        break;
                    }
                }
                ranks2.put(r.getName(), serializedRank);
            }

            this.yaml.dump(ranks2, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all players using the storage managers storage solution.
     */
    @Override
    public void savePlayers() {
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        try {
            PrintWriter writer = new PrintWriter(this.playersFile);
            Map<String, Object> players2 = new HashMap<String, Object>();

            for (PRPlayer p : this.getPlayers()) {
                Map<String, Object> serializedPlayer = this.getSerializer().serialize(p);
                for (Entry<String, Object> entry : serializedPlayer.entrySet()) {
                    if (entry.getValue().equals(p.getUUID().toString())) {
                        serializedPlayer.remove(entry.getKey());
                        break;
                    }
                }
                players2.put(p.getUUID().toString(), serializedPlayer);
            }

            this.yaml.dump(players2, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a single rank to the storage managers storage solution.
     * 
     * @param rank
     */
    @Override
    public void saveRank(PRRank rank) {
        this.addRank(rank);
        this.saveRanks();
    }

    /**
     * Add a single player to the storage managers storage solution.
     * 
     * @param player
     */
    @Override
    public void savePlayer(PRPlayer player) {
        this.addPlayer(player);
        this.savePlayers();
    }

    /**
     * Delete all data from the storage manager.
     */
    @Override
    public void removeAllData() {
        this.ranksFile.delete();
        this.playersFile.delete();
    }
}
