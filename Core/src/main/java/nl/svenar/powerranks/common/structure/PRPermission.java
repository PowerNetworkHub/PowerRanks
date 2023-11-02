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

package nl.svenar.powerranks.common.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Structure to store permission data.
 * 
 * @author svenar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PRPermission {

    private String name;
    private boolean value;

    public PRPermission() {
        this("", false);
    }

    public PRPermission(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the permission node
     * 
     * @return String with the permission node
     */
    public String getName() {
        return this.name;
    }

    /**
     * Update the permissoin node
     * 
     * @param name
     */
    public void setName(String name) {
        this.setValue(!name.startsWith("-"));
        if (name.startsWith("-")) {
            this.name = name.replaceFirst("-", "");
        } else {
            this.name = name;
        }
    }

    /**
     * Get if a permission is allowed or not
     * 
     * @return true if allowed, false otherwise
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Change if a permission is allowed or not
     * 
     * @param value
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "permission:" + name + ", value:" + value;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + toString().hashCode();
        return result;
    }
}
