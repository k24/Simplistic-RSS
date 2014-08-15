package com.shirwa.simplistic_rss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright (C) 2014 Shirwa Mohamed <shirwa99@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RssItem {
    static final String tagPattern = "<[^>]*>";
    // < = &lt;, > = &gt; URL in Group 3
    static final Pattern imgPattern =
            Pattern.compile("(&lt;|<)img.*?src=(\"|')(.*?)(\"|')",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    String title;
    String description;
    String link;
    String imageUrl;
    boolean notLookedForImg = true;

    // Description but only first X chars without formatting
    final int snippetLen = 200;
    String snippet;

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        if (imageUrl != null) {
            return imageUrl;
        }
        if (notLookedForImg && description != null) {
            notLookedForImg = false;
            // Try and find an image in the item
            Matcher m = imgPattern.matcher(description);
            if (m.find()) {
                imageUrl = m.group(3);
            }
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void appendDescription(String description) {
        if (this.description == null) {
            this.description = "";
        }
        this.description += description;
    }

    public void appendTitle(String title) {
        if (this.title == null) {
            this.title = "";
        }
        this.title += title;
    }

    public String getSnippet() {
        if (snippet == null) {
            snippet = description.replaceAll(tagPattern, "")
                              .replaceAll("\\s+", " ")
                              .substring(0, snippetLen) + "...";
        }

        return snippet;
    }
}
