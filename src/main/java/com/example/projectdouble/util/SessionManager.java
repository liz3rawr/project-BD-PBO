package com.example.projectdouble.util;

import com.example.projectdouble.model.User;

public class SessionManager {

    private static User loggedInUser; // Objek User yang sedang login

    /**
     * Mengatur user yang sedang login.
     * @param user Objek User yang baru saja login.
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Mendapatkan user yang sedang login.
     * @return Objek User yang sedang login, atau null jika tidak ada.
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Menghapus sesi user (saat logout).
     */
    public static void clearSession() {
        loggedInUser = null;
    }
}
