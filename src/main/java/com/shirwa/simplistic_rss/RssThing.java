package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

public interface RssThing {
    public void setPubDate(DateTime date);
    public void setTitle(String s);
    public void setDescription(String s);
    public void setAuthor(String s);
    public void setLink(String s);
    public void setEnclosure(String s);
}
