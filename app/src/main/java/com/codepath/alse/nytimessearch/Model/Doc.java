package com.codepath.alse.nytimessearch.Model;

import java.util.ArrayList;
import java.util.List;


public class Doc {

    private String webUrl;
    private String snippet;
    private String leadParagraph;


    private List<Object> multimedia = new ArrayList<Object>();
    private Headline headline;



    public Doc(String webUrl, String snippet, String leadParagraph, List<Object> multimedia, Headline headline ){
        this.webUrl = webUrl;
        this.snippet = snippet;
        this.leadParagraph = leadParagraph;
        this.multimedia = multimedia;
        this.headline = headline;
    }

    /**
     * @return The webUrl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * @param webUrl The web_url
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * @return The snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @param snippet The snippet
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     * @return The leadParagraph
     */
    public String getLeadParagraph() {
        return leadParagraph;
    }

    /**
     * @param leadParagraph The lead_paragraph
     */
    public void setLeadParagraph(String leadParagraph) {
        this.leadParagraph = leadParagraph;
    }


    /**
     * @return The multimedia
     */
    public List<Object> getMultimedia() {
        return multimedia;
    }

    /**
     * @param multimedia The multimedia
     */
    public void setMultimedia(List<Object> multimedia) {
        this.multimedia = multimedia;
    }

    /**
     * @return The headline
     */
    public Headline getHeadline() {
        return headline;
    }

    /**
     * @param headline The headline
     */
    public void setHeadline(Headline headline) {
        this.headline = headline;
    }





}
