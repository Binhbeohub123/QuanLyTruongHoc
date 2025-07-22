package Util;

import Entity.User;

/**
 */
public class XAuth {
    public static User user = null; 
    
    public static boolean isLoggedIn() {
        return user != null;
    }
    
    public static boolean isManager() {
        return user != null && user.isManager();
    }
    
    public static boolean isEmployee() {
        return user != null && !user.isManager();
    }
    
    public static String getCurrentUsername() {
        return user != null ? user.getUsername() : "Guest";
    }
    
    public static String getCurrentUserRole() {
        return user != null ? user.getRole() : "Unknown";
    }
    
    public static void logout() {
        user = null;
    }
}