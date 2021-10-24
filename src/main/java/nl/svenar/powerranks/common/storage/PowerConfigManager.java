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

package nl.svenar.powerranks.common.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration core that can be implemented in multiple storage methods (for
 * example, but not limited to: YAML and JSON)
 * 
 * @author svenar
 */
public abstract class PowerConfigManager {

    protected Map<String, Object> data;
    protected boolean hasCreatedFile = false;

    /**
     * Constructor to initialize core data.
     */
    public PowerConfigManager() {
        this.data = new HashMap<String, Object>();
    }

    public abstract void save();

    public abstract void reload();

    /**
     * Get the raw data.
     * 
     * @return raw data as loaded from a YAML file
     */
    public Map<String, Object> getRawData() {
        return this.data;
    }

    /**
     * Overwrite the current stored data.
     * 
     * @param data
     */
    public void setRawData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Read an input stream and write it to a file.
     * 
     * @param inputSteam
     * @param outputFile
     * @throws IOException
     */
    public void copyFile(final InputStream inputSteam, final File outputFile) throws IOException {
        final OutputStream out = new FileOutputStream(outputFile);
        final byte[] buf = new byte[1024];
        int len;
        while ((len = inputSteam.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputSteam.close();
    }

    /**
     * Check if an empty config has been made
     * 
     * @return true if new file has been created, false otherwise
     */
    public boolean isNewFile() {
        return this.hasCreatedFile;
    }

    /**
     * Get a key value pair. Create it with the default value if it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return object
     */
    @SuppressWarnings("unchecked")
    public Object getKV(String key, Object defaultValue) {

        String[] keySplit = key.split("\\.");
        Object tmp = null;
        Object output = null;
        int index = 0;

        for (String keyPart : keySplit) {
            boolean isLastKeyPart = index == keySplit.length - 1;

            if (!isLastKeyPart) {
                if (index == 0) {
                    tmp = this.data.get(keyPart);
                } else {
                    if (tmp != null) {
                        if (tmp instanceof HashMap) {
                            Map<String, Object> tmpMap = (HashMap<String, Object>) tmp;
                            tmp = tmpMap.get(keyPart);
                        }
                    }
                }
            } else {
                if (index > 0) {
                    if (tmp != null) {
                        if (tmp instanceof HashMap) {
                            Map<String, Object> tmpMap = (HashMap<String, Object>) tmp;
                            output = tmpMap.get(keyPart);
                        }
                    }
                } else {
                    output = this.data.get(keyPart);
                }
            }

            index++;
        }

        if (output == null) {
            this.setKV(key, defaultValue);
            output = defaultValue;
        }

        return output;
    }

    /**
     * Set a key value pair.
     * 
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void setKV(String key, Object value) {

        String[] keySplit = key.split("\\.");
        Object tmp = null;
        int index = 0;

        for (String keyPart : keySplit) {
            boolean isLastKeyPart = index == keySplit.length - 1;

            if (index == 0) {
                if (this.data.containsKey(keyPart)) {
                    if (this.data.get(keyPart).getClass() != HashMap.class) {
                        this.data.put(keyPart, !isLastKeyPart ? new HashMap<String, Object>() : value);
                    }
                    tmp = this.data.get(keyPart);
                } else {
                    this.data.put(keyPart, new HashMap<String, Object>());
                    tmp = this.data.get(keyPart);
                }
            } else {
                if (!isLastKeyPart) {
                    if (tmp != null) {
                        if (tmp instanceof HashMap) {
                            Map<String, Object> tmpMap = (HashMap<String, Object>) tmp;

                            if (tmpMap.containsKey(keyPart)) {

                                if (tmpMap.get(keyPart).getClass() != HashMap.class) {
                                    tmpMap.put(keyPart, new HashMap<String, Object>());
                                }

                                tmp = tmpMap.get(keyPart);

                            } else {

                                tmpMap.put(keyPart, new HashMap<String, Object>());
                                tmp = tmpMap.get(keyPart);
                            }
                        }
                    }
                } else {
                    if (tmp != null) {
                        if (tmp instanceof HashMap) {
                            Map<String, Object> tmpMap = (HashMap<String, Object>) tmp;
                            tmpMap.put(keyPart, value != null ? value : new HashMap<String, Object>());
                        }
                    }
                }
            }

            index++;
        }
    }

    /**
     * Get a string from the configuration data. Create it with the default value if
     * it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return string
     */
    public String getString(String key, String defaultValue) {
        return this.getKV(key, defaultValue).toString();
    }

    /**
     * Store a string in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        this.setKV(key, value);
    }

    /**
     * Get an integer from the configuration data. Create it with the default value
     * if it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return integer
     */
    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(this.getKV(key, defaultValue).toString());
    }

    /**
     * Store an integer in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setInt(String key, int value) {
        this.setKV(key, value);
    }

    /**
     * Get an float from the configuration data. Create it with the default value if
     * it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return float
     */
    public float getFloat(String key, float defaultValue) {
        return Float.parseFloat(this.getKV(key, defaultValue).toString());
    }

    /**
     * Store an float in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setFloat(String key, float value) {
        this.setKV(key, value);
    }

    /**
     * Get an boolean from the configuration data. Create it with the default value
     * if it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return boolean
     */
    public boolean getBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(this.getKV(key, defaultValue).toString());
    }

    /**
     * Store an boolean in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setBool(String key, boolean value) {
        this.setKV(key, value);
    }

    /**
     * Get an list from the configuration data. Create it with the default value if
     * it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return List of unknwon type or null if the key was not found or is not a
     *         list
     */
    public List<?> getList(String key, List<?> defaultValue) {
        return this.getKV(key, defaultValue) instanceof List ? (List<?>) this.getKV(key, defaultValue) : null;
    }

    /**
     * Store a list in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setList(String key, List<?> value) {
        this.setKV(key, value);
    }

    /**
     * Get an map from the configuration data. Create it with the default value if
     * it doesn't exist.
     * 
     * @param key
     * @param defaultValue
     * @return Map of unknwon types or null if the key was not found or is not a map
     */
    public Map<?, ?> getMap(String key, Map<?, ?> defaultValue) {
        return this.getKV(key, defaultValue) instanceof Map ? (Map<?, ?>) this.getKV(key, defaultValue) : null;
    }

    /**
     * Store a map in the configuration data.
     * 
     * @param key
     * @param value
     */
    public void setMap(String key, Map<?, ?> value) {
        if (value.size() == 1) {
            this.setKV(key + "." + value.keySet().toArray()[0], (Map<?, ?>) value.values().toArray()[0]);
        } else {
            this.setKV(key, value);
        }
    }
}
