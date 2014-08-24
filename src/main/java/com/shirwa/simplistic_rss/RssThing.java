package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

public interface RssThing {
    public void setPubDate(DateTime date);
    public void appendTitle(String s);
    public void appendDescription(String s);
    public void setLink(String s);
}
