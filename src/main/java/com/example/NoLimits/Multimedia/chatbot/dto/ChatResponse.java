package com.example.NoLimits.Multimedia.chatbot.dto;

import java.util.List;

public class ChatResponse {

    private String reply;
    private List<String> actions;
    private String route;
    private boolean externalWarning;
    private String source;

    public ChatResponse() {
    }

    public ChatResponse(String reply, List<String> actions, String route, boolean externalWarning, String source) {
        this.reply = reply;
        this.actions = actions;
        this.route = route;
        this.externalWarning = externalWarning;
        this.source = source;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isExternalWarning() {
        return externalWarning;
    }

    public void setExternalWarning(boolean externalWarning) {
        this.externalWarning = externalWarning;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}