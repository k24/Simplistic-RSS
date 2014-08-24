package com.shirwa.simplistic_rss;


import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

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


public class RssHandler extends DefaultHandler {
    private RssThing thing;
    private RssFeed rssFeed;
    private List<RssItem> rssItemList;
    private RssItem currentItem;
    private boolean parsingTitle;
    private boolean parsingLink;
    private boolean parsingDescription;
    private boolean parsingDate;

    // Temp values
    StringBuilder tempDate;

    public RssHandler() {
        //Initializes a new ArrayList that will hold all the generated RSS items.
        rssItemList = new ArrayList<RssItem>();
        rssFeed = new RssFeed(rssItemList);
        thing = rssFeed;
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }


    //Called when an opening tag is reached, such as <item> or <title>
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // RSS or Atom
        if (qName.equals("item") || qName.equals("entry")) {
            currentItem = new RssItem();
            thing = currentItem;
        } else if (qName.equals("title")) {
            parsingTitle = true;
        } else if (qName.equals("link")) {
            parsingLink = true;
        } else if (qName.equals("description") || qName.equals("summary")) {
            parsingDescription = true;
        } else if (qName.equals("media:thumbnail") ||
                   qName.equals("media:content") ||
                   qName.equals("image")) {
            if (attributes.getValue("url") != null && currentItem != null) {
                currentItem.setImageUrl(attributes.getValue("url"));
            }
        } else if (qName.equals("pubDate")) {
            parsingDate = true;
            tempDate = new StringBuilder();
        }
    }

    //Called when a closing tag is reached, such as </item> or </title>
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("item")) {
            //End of an item so add the currentItem to the list of items.
            rssItemList.add(currentItem);
            currentItem = null;
            thing = null;
        } else if (qName.equals("title")) {
            parsingTitle = false;
        } else if (qName.equals("link")) {
            parsingLink = false;
        } else if (qName.equals("description")) {
            parsingDescription = false;
        } else if (qName.equals("pubDate")) {
            parsingDate = false;
            if (thing != null) {
                thing.setPubDate(Utils.parseDate(tempDate.toString()));
            }
        }
    }

    //Goes through character by character when parsing whats inside of a tag.
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
            //If parsingTitle is true, then that means we are inside a <title> tag so the text is the title of an item.
            if (parsingTitle) {
                if (thing != null)
                    thing.appendTitle(new String(ch, start, length));
            }
            //If parsingLink is true, then that means we are inside a <link> tag so the text is the link of an item.
            else if (parsingLink) {
                if (thing != null)
                    thing.setLink(new String(ch, start, length));
            }
            //If parsingDescription is true, then that means we are inside a <description> tag so the text is the description of an item.
            else if (parsingDescription) {
                if (thing != null)
                    thing.appendDescription(new String(ch, start, length));
            } else if (parsingDate) {
                tempDate.append(ch, start, length);
            }
    }
}


