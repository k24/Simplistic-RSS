package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class RssItem implements RssThing {
    static final String tagPattern = "<[^>]*>";
    // < = &lt;, > = &gt; URL in Group 3
    static final Pattern imgPattern =
            Pattern.compile("(&lt;|<)img.*?src=(\"|')(.*?)(\"|')",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    // Empty paragraphs
    static final Pattern emptyParagraphs =
            Pattern.compile(
                    "(((<|&lt;)(p)(>|&gt;))\\s*((<|&lt;)/p(>|&gt;))|(<|&lt;)p/(>|&gt;))",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    static final Pattern manyNewlines =
            // Two or more newlines gets truncated to one
            Pattern.compile(
                    "(((<|&lt;)/?br/?(>|&gt;))\\s*){2,}",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    static final Pattern newlinesFollowingParagraph =
            // <p/><br/>, remove all such br
            Pattern.compile(
                    "((<|&lt;)/?p/?(>|&gt;))(\\s*(<|&lt;)/?br/?(>|&gt;))+",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    // Bloat patterns, are removed from description
    static final Pattern[] bloatPatterns = new Pattern[]{
            // Remove feedflare div
            Pattern.compile(
                    "(<|&lt;)div class=('|\")feedflare('|\").*?/div(>|&gt;)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            // Remove feedsportal links
            Pattern.compile(
                    "(<|&lt;)a((?!/a).)*feedsportal.*?/a(>|gt;)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            // Remove links containing zero size images
            Pattern.compile(
                    "(<|&lt;)a((?!/a).)*width=('|\")1('|\")((?!/a).)*/a(>|&gt;)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            // Remove zero size images
            Pattern.compile(
                    "(<|&lt;)img((?!/((>|&gt;)|img)).)*width=('|\")1('|\").*?/(img)?(>|&gt;)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL),

    };

    String title;
    // There are several possible content tags.
    HashMap<String, String> content = new HashMap<String, String>();
    String cleanDescription;
    String link;
    String author;
    String enclosure;
    String imageUrl;
    DateTime pubDate;
    boolean notLookedForImg = true;
    // Description but only first X chars without formatting
    int snippetLen = 200;
    String snippet;
    String plainTitle;

    public String getAuthor() {
        return author;
    }

    public void parserSetAuthor(String author) {
        this.author = author;
    }

    public String getEnclosure() {
        return enclosure;
    }

    @Override
    public void parserSetEnclosure(String enclosure) {
        // Only if no enclosure already exists, and new is not null
        if (enclosure != null && !enclosure.isEmpty() && (this.enclosure == null || this.enclosure.isEmpty()))
            this.enclosure = enclosure;
    }

    public DateTime getPubDate() {
        return pubDate;
    }

    public void parserSetPubDate(final DateTime pubDate) {
        this.pubDate = pubDate;
    }

    public void parserSetTitle(String title) {
        if (this.title == null) {
            this.title = "";
        }
        this.title += title;
    }

    public void parserSetContent(String tag, String description) {
        if (!content.containsKey(tag)) {
            content.put(tag, "");
        }
        content.put(tag, content.get(tag) + description);
    }

    // Returns the longest content tag
    public String getDescription() {
        int longest = 0;
        String longestTag = null;
        for (String tag: content.keySet()) {
            if (content.get(tag).length() > longest)
                longestTag = tag;
        }

        if (longestTag != null) {
            return content.get(longestTag);
        } else {
            return null;
        }
    }


    /**
     * @return description without bloat, ads, and spam
     */
    public String getCleanDescription() {
        String description = getDescription();
        if (cleanDescription == null && description != null) {
            cleanDescription = description;
            for (Pattern p : bloatPatterns) {
                cleanDescription = p.matcher(cleanDescription).replaceAll("");
            }
            // We might have introduced some empty lines now
            // Remove empty paragraphs
            cleanDescription = emptyParagraphs.matcher(cleanDescription).replaceAll("");
            // Replace many newlines with just one
            cleanDescription = manyNewlines.matcher(cleanDescription)
                    .replaceAll("<br/>");
            // Get rid of newlines following paragraphs,
            // first group is paragraph
            cleanDescription = newlinesFollowingParagraph.matcher
                    (cleanDescription).replaceAll("$1");
        }

        return cleanDescription;
    }

    public String getImageUrl() {
        if (imageUrl != null) {
            return imageUrl;
        }
        String description = getDescription();
        if (notLookedForImg && description != null) {
            notLookedForImg = false;
            // Try and find an image in the item
            Matcher m = imgPattern.matcher(getCleanDescription());
            if (m.find()) {
                imageUrl = m.group(3);
            }
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Return a list of all images in the body
     */
    public List<String> getAllImageUrls() {
        ArrayList<String> urlList = new ArrayList<String>();
        String description = getDescription();
        if (description != null) {
            Matcher m = imgPattern.matcher(description);
            while (m.find()) {
                urlList.add(m.group(3));
            }
        }
        return urlList;
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

    public void parserSetLink(String link) {
        if (link != null && !link.isEmpty())
            this.link = link;
    }

    public String getSnippet() {
        String description = getDescription();
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
                // Unicode ellipsis instead of three dots ...
                snippet = snippet.substring(0, snippetLen) + "\u2026";
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
