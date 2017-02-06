package com.epam.player.model;

/**
 * Created by Yaroslav_Strontsitsk on 2/1/2016.
 */
public class AuthorizationResult {

    public AuthorizationResult(boolean authorized, User user) {
        this.authorized = authorized;
        this.user = user;
    }

    private boolean authorized;
    private User user;

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
