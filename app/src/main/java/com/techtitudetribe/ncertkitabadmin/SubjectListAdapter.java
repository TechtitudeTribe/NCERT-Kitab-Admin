package com.techtitudetribe.ncertkitabadmin;

public class SubjectListAdapter {
    String  name;
    long count;

    public SubjectListAdapter() {

    }

    public SubjectListAdapter(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
