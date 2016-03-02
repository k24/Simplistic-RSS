package com.shirwa.simplistic_rss;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

public class RssReader {
    private String rssUrl;

    public RssReader(String url) {
        if (!url.startsWith("http")) {
            rssUrl = "http://" + url;
        } else {
            rssUrl = url;
        }
    }

    public RssFeed getFeed() throws Exception {
        RssFeed feed = null;

        // Need a SAXParser to read the XML
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        // Creates a new RssHandler which will do all the parsing.
        RssHandler handler = new RssHandler();

        // Pass SaxParser the inputsource and handler that was created.
        saxParser.parse(fetchFeed(rssUrl), handler);
        // Parse the stream
        feed = handler.getRssFeed();

        return feed;
    }

    private InputStream fetchFeed(String url) throws IOException {
        // Support redirection as old
        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(true)
                .build();
        Response response = client.newCall(new Request.Builder()
                .url(rssUrl)
                .build()).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().byteStream();
    }
}
