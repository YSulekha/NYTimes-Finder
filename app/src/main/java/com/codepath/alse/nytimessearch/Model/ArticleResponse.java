package com.codepath.alse.nytimessearch.Model;

import java.util.ArrayList;
import java.util.List;



public class ArticleResponse {


    private List<Doc> docs = new ArrayList<Doc>();


    /**
     *
     * @return
     * The docs
     */
    public List<Doc> getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     * The docs
     */
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }




}