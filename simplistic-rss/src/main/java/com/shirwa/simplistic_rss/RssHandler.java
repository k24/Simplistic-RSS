package com.shirwa.simplistic_rss;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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


public class RssHandler {
    private RssThing thing;
    private RssFeed rssFeed;
    private List<RssItem> rssItemList;
    private RssItem currentItem;
    Stack<String> currentTag;

    // Temp values
    String tempDate;

    public RssHandler() {
        //Initializes a new ArrayList that will hold all the generated RSS items.
        rssItemList = new ArrayList<>();
        rssFeed = new RssFeed(rssItemList);
        currentTag = new Stack<>();
        thing = rssFeed;
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }

    private boolean isEnclosure(String qName, String rel) {
        return qName.equals("enclosure") ||
                qName.equals("link") && rel != null;
    }

    private boolean isAuthor(String qName) {
        return qName.equals("author") || qName.equals("dc:creator") || qName.equals("dc:author");
    }

    private boolean isDescription(String qName) {
        return qName.equals("description") || qName.equals("summary") || qName.equals("content") || qName.equals("content:encoded") || qName.equals("body") || qName.equals("fullitem") || qName.equals("xhtml:body");
    }

    public void startElement(XmlPullParser parser) throws IOException, XmlPullParserException {
        String qName = parser.getName();

        currentTag.push(qName);
        // RSS or Atom
        if (qName.equals("item") || qName.equals("entry")) {
            currentItem = new RssItem();
            thing = currentItem;
        } else if (qName.equals("title")) {
            String text = readText(parser);
            if (thing != null) thing.parserSetTitle(text);
        } else if (isDescription(qName)) {
            String text = readText(parser);
            if (thing != null)
                thing.parserSetContent(currentTag.peek(), text);
        } else if (qName.equals("media:thumbnail") ||
                qName.equals("media:content") ||
                qName.equals("image")) {
            if (parser.getAttributeValue(null, "url") != null && currentItem != null) {
                currentItem.setImageUrl(parser.getAttributeValue(null, "url"));
            }
        } else if (qName.equals("pubDate")) {
            tempDate = readText(parser);
        } else if (isAuthor(qName)) {
            String text = readText(parser);
            if (thing != null)
                thing.parserSetAuthor(text);
        } else if (isEnclosure(qName, parser.getAttributeValue(null, "rel"))) {
            if (parser.getAttributeValue(null, "url") != null)
                thing.parserSetEnclosure(parser.getAttributeValue(null, "url"));
            else if (parser.getAttributeValue(null, "href") != null)
                thing.parserSetEnclosure(parser.getAttributeValue(null, "href"));
            String text = readText(parser);
            if (thing != null)
                thing.parserSetEnclosure(text);
        } else if (qName.equals("link")) {
            if (parser.getAttributeValue(null, "url") != null)
                thing.parserSetLink(parser.getAttributeValue(null, "url"));
            else if (parser.getAttributeValue(null, "href") != null)
                thing.parserSetLink(parser.getAttributeValue(null, "href"));
            String text = readText(parser);
            if (thing != null)
                thing.parserSetLink(text);
        }

    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.next() == XmlPullParser.TEXT) {
            return parser.getText();
        }

        return ""; // Unexpected
    }

    public void endElement(XmlPullParser parser) {
        String qName = parser.getName();
        currentTag.pop();
        if (qName.equals("item") || qName.equals("entry")) {
            //End of an item so add the currentItem to the list of items.
            rssItemList.add(currentItem);
            currentItem = null;
            thing = null;
        } else if (qName.equals("pubDate")) {
            if (thing != null) {
                thing.parserSetPubDate(Utils.parseDate(tempDate));
            }
        }
    }
}


