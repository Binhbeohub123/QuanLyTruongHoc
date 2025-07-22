package Util;

import javax.swing.JOptionPane;

public class XDialog {
    public static void alert(String message) {
        JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean confirm(String message) {
        int result = JOptionPane.showConfirmDialog(null, message, "Xác nhận", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}