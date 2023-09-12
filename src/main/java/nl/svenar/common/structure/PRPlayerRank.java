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

package nl.svenar.common.structure;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import nl.svenar.powerranks.util.Util;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PRPlayerRank {
    
    private String name;
    private HashMap<String, Object> tags;

    public PRPlayerRank() {
        this.tags = new HashMap<>();
    }

    public PRPlayerRank(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, Object> getTags() {
        return this.tags;
    }

    public void addTag(String tagName, Object tagValue) {
        if (tagName == null || tagValue == null) {
            return;
        }
        if (tagName.length() == 0) {
            return;
        }

        if (tagName.equalsIgnoreCase("expires")) {
            long currentTimeMillis = System.currentTimeMillis();
            tagValue = Util.timeStringToSecondsConverter(String.valueOf(tagValue));
            tagValue = currentTimeMillis + ((int) tagValue * 1000);
        }

        this.tags.put(tagName, tagValue);
    }

    public void addTagRaw(String tagName, Object tagValue) {
        if (tagName == null || tagValue == null) {
            return;
        }
        if (tagName.length() == 0) {
            return;
        }

        this.tags.put(tagName, tagValue);
    }
}
