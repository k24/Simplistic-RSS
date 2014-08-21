package com.shirwa.simplistic_rss;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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

    public List<RssItem> getItems() throws Exception {
        List<RssItem> items = null;
        AndroidHttpClient client = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            //Creates a new RssHandler which will do all the parsing.
            RssHandler handler = new RssHandler();
            //Need to take care of gzip encoded content ourselves
            HttpUriRequest request = new HttpGet(rssUrl);
            AndroidHttpClient.modifyRequestToAcceptGzipResponse(request);
            client = AndroidHttpClient.newInstance(System.getProperty("http" +
                                                                  ".agent"));
            HttpResponse response = client.execute(request);
            InputStream inputStream = AndroidHttpClient.getUngzippedContent
                    (response.getEntity());
            InputSource is = new InputSource(new BufferedInputStream
                    (inputStream));
            //Pass SaxParser the inputsource and handler that was created.
            saxParser.parse(is, handler);
            //Parse the stream
            items = handler.getRssItemList();
        } finally {
            // Close http client last
            if (client != null) {
                client.close();
            }
        }

        return items;
    }
}
