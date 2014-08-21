package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

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

    public DateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(final DateTime pubDate) {
        this.pubDate = pubDate;
    }

    DateTime pubDate;
    boolean notLookedForImg = true;

    // Description but only first X chars without formatting
    int snippetLen = 200;
    String snippet;
    String plainTitle;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
        if (snippet == null && description != null && !description.isEmpty()) {
            snippet = description.replaceAll(tagPattern, "")
                    .replaceAll("\\s+", " ");
            if (snippetLen > snippet.length()) {
                snippetLen = snippet.length();
            }
            if (snippetLen == 0) {
                // done
                snippet = null;
            } else {
                snippet = snippet.substring(0, snippetLen) + "...";
                snippet = android.text.Html.fromHtml(snippet).toString().trim();
            }
        }

        return snippet;
    }

    public String getPlainTitle() {
        if (plainTitle == null && this.title != null) {
            plainTitle =
                    android.text.Html.fromHtml(this.title).toString().trim();
        }
        return plainTitle;
    }
}
