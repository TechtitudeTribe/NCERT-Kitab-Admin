package com.techtitudetribe.ncertkitabadmin;

public class BookAdapter {
    String chapter_name, chapter_no;

    public BookAdapter() {
    }

    public BookAdapter(String chapter_name, String chapter_no) {
        this.chapter_name = chapter_name;
        this.chapter_no = chapter_no;
    }

    public String getChapter_name() {
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
