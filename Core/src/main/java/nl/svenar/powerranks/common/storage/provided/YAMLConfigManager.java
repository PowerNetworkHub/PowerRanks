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

package nl.svenar.powerranks.common.storage.provided;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import nl.svenar.powerranks.common.storage.PowerConfigManager;

import org.yaml.snakeyaml.constructor.Constructor;

/**
 * YAML configuration implementation using PowerConfigManager as base.
 * 
 * @author svenar
 */
public class YAMLConfigManager extends PowerConfigManager {

    private final DumperOptions yamlOptions = new DumperOptions();

    private final LoaderOptions loaderOptions = new LoaderOptions();

    private final Representer yamlRepresenter = new Representer(yamlOptions);
    
    private final Yaml yaml = new Yaml(new Constructor(loaderOptions), yamlRepresenter, yamlOptions, loaderOptions);

    private File configFile;

    /**
     * Create a new empty YAML file or load if one already exists on the system.
     *
     * @param directory
     * @param filename
     * @throws IOException
     */
    public YAMLConfigManager(String directory, String filename) {
        super();

        try {

            File configDir = new File(directory);
            this.configFile = new File(configDir, filename);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            if (!this.configFile.exists()) {
                this.configFile.createNewFile();

                this.hasCreatedFile = true;
            }

            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy a YAML file from the packed JAR or load if one already exists on the
     * system.
     * 
     * @param directory
     * @param filename
     * @param defaults
     * @throws IOException
     */
    public YAMLConfigManager(String directory, String filename, String defaults) {
        super();

        try {
            File configDir = new File(directory);
            this.configFile = new File(configDir, filename);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            if (!this.configFile.exists()) {
                InputStream inputStream = this.getClass().getResourceAsStream(defaults.startsWith("/") ? defaults : "/" + defaults);
                this.copyFile(inputStream, this.configFile);
            }

            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the altered YAML data back to the user's system.
     */
    @Override
    public void save() {
        try {
            this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            // this.yamlOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            // this.yamlRepresenter.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);

            PrintWriter writer = new PrintWriter(this.configFile);
            this.yaml.dump(this.data, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reload the YAML data without saving first.
     */
    @Override
    public void reload() {
        try {
            InputStream inputStream = new FileInputStream(this.configFile);
            this.data = this.yaml.load(inputStream);
            if (this.data == null) {
                this.data = new HashMap<String, Object>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean destroyFile() {
        return this.configFile.delete();
    }
}