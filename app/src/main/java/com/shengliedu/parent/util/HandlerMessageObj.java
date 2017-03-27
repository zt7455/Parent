package com.shengliedu.parent.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zt10 on 2017/2/13.
 */

public class HandlerMessageObj {
    private Context context;
    private String type;
    private String json;
    private String link;
    private String anwser;
    private String wrong;
    private String path;
    private int content_id;
    private int bookId;
    private ImageView imageView;

    private TextView textView;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getWrong() {
        return wrong;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
