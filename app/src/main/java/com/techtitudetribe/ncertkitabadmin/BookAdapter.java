package com.techtitudetribe.ncertkitabadmin;

public class BookAdapter {
    String chapter_name, chapter_no, booksUrl;

    public BookAdapter() {
    }

    public BookAdapter(String chapter_name, String chapter_no, String booksUrl) {
        this.chapter_name = chapter_name;
        this.chapter_no = chapter_no;
        this.booksUrl = booksUrl;
    }

    public String getBooksUrl() {
        return booksUrl;
    }

    public void setBooksUrl(String booksUrl) {
        this.booksUrl = booksUrl;
    }

    public  String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapter_no() {
        return chapter_no;
    }

    public void setChapter_no(String chapter_no) {
        this.chapter_no = chapter_no;
    }
}
