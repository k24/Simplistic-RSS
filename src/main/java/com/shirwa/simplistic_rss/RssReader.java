package com.shirwa.simplistic_rss;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.HttpParams;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

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

    public RssFeed getFeed() throws Exception {
        RssFeed feed = null;
        AndroidHttpClient client = null;
        try {
            // Need a SAXParser to read the XML
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // Creates a new RssHandler which will do all the parsing.
            RssHandler handler = new RssHandler();
            // Need to take care of gzip encoded content ourselves
            HttpUriRequest request = new HttpGet(rssUrl);
            AndroidHttpClient.modifyRequestToAcceptGzipResponse(request);
            client = AndroidHttpClient
                    .newInstance(System.getProperty("http" + ".agent"));
            Log.d("JONAS", "Request: " + request.getMethod());
            // For some shit reason, I need to set redirects to true myself
            HttpParams params = request.getParams();
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
            request.setParams(params);
            // Actually send it...
            HttpResponse response = client.execute(request);
            Log.d("JONAS", "Response: " + response.getStatusLine()
                    .getStatusCode() + ": " + response.getStatusLine().getReasonPhrase());
            // Decode the response
            InputStream inputStream =
                    AndroidHttpClient.getUngzippedContent(response.getEntity());
            InputSource is =
                    new InputSource(new BufferedInputStream(inputStream));
            // Pass SaxParser the inputsource and handler that was created.
            saxParser.parse(is, handler);
            // Parse the stream
            feed = handler.getRssFeed();
        } finally {
            // Close http client last
            if (client != null) {
                client.close();
            }
        }

        return feed;
    }
}
