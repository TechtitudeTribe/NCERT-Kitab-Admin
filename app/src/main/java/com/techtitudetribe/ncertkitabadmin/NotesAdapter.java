package com.techtitudetribe.ncertkitabadmin;

public class NotesAdapter {
    String chapterName, chapterNo, url;

    public NotesAdapter() {
    }

    public NotesAdapter(String chapterName, String chapterNo, String url) {
        this.chapterName = chapterName;
        this.chapterNo = chapterNo;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }
}
