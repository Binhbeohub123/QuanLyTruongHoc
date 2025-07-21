package tool;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class to display any JPanel in its own JFrame,
 * sizing the window based on the panel's preferred size or a given aspect ratio.
 */
public class PanelLauncher {
    /**
     * Shows the given JPanel in a new JFrame window,
     * packing to the panel's preferred size.
     * @param panel JPanel instance to display
     * @param title Frame title
     */
    public static void showPanel(JPanel panel, String title) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(panel);
            frame.pack();                       // size to preferred sizes
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Shows the JPanel maintaining a specific width-to-height ratio.
     * @param panel JPanel instance to display
     * @param title Frame title
     * @param ratio aspect ratio (width / height)
     * @param baseHeight desired base height
     */
    public static void showPanelWithRatio(JPanel panel, String title, double ratio, int baseHeight) {
        EventQueue.invokeLater(() -> {
            int width = (int) (baseHeight * ratio);
            int height = baseHeight;
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            panel.setPreferredSize(new Dimension(width, height));
            frame.setContentPane(panel);
            frame.pack();                       // respects preferred size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    

    public static void main(String[] args) {
    // Ví dụ: khởi chạy form Quản Lý Giáo Viên
    javax.swing.SwingUtilities.invokeLater(() -> {
        // Tạo instance của panel bạn muốn hiển thị
        //QuanLyInAnBaoCao_View panel = new QuanLyInAnBaoCao_View();
        QuanLyNguoiDung_View panel = new QuanLyNguoiDung_View();
        //QuanLyHocSinh_View panel = new QuanLyHocSinh_View();
        // Hiển thị với tiêu đề
        showPanel(panel, "Quản Lý Học Sinh");
    });
}
    
}