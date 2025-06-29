package com.example.projectdouble.util;

import com.example.projectdouble.model.User;

public class SessionManager {

    private static User loggedInUser; // Objek User yang sedang login

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}
