/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import Panel.panelQLLH;
import Entity.User;
import Login.Login;
import Panel.QuanLyHocSinh_View;
import Panel.QuanLyInAnBaoCao_View;
import Panel.QuanLyNguoiDung_View;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author mphuc
 */
public class Main_Form extends JFrame {

    /**
     * Creates new form Main_Form
     */
    public enum UserRole {
        GVCN("GVCN", "Giáo Viên Chủ Nhiệm"),
        GVBM("GV", "Giáo Viên Bộ Môn"),
        BGH("BGH", "Ban Giám Hiệu"),
        QTV("ADMIN", "Quản Trị Viên");

        private final String code;
        private final String displayName;

        UserRole(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public String getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static UserRole fromCode(String code) {
            for (UserRole role : values()) {
                if (role.code.equals(code)) {
                    return role;
                }
            }
            return GVBM; // Default role
        }
    }

    private User currentUser;
    private UserRole currentRole;

    public Main_Form() {
        initComponents();
        setupButtonStyles();
        disableAllFunctions();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            this.currentRole = UserRole.fromCode(user.getMaVaiTro());
            updateUserInfo();
            setPermissions();
        } else {
            disableAllFunctions();
        }
    }

    /**
     * Cập nhật thông tin user trên giao diện
     */
    private void updateUserInfo() {
        if (currentUser != null) {
            jLabel3.setText(currentUser.getTenNguoiDung());
            jLabel4.setText(currentRole.getDisplayName());

            // Cập nhật title của form
            this.setTitle("Hệ Thống Quản Lý - " + currentUser.getTenNguoiDung() + " (" + currentRole.getDisplayName() + ")");
        }
    }

    /**
     * Disable tất cả chức năng (làm mờ)
     */
    private void disableAllFunctions() {
        jButton1.setEnabled(false);  // Trang Chủ
        jButton4.setEnabled(false);  // Quản Lý Học Sinh
        jButton5.setEnabled(false);  // Quản Lý Giáo Viên
        jButton6.setEnabled(false);  // Quản Lý Môn Học
        jButton7.setEnabled(false);  // Quản Lý Điểm & Hạnh Kiểm
        jButton9.setEnabled(false);  // Thời Khoá Biểu
        jButton10.setEnabled(false); // Quản Lý Lớp Học
        jButton11.setEnabled(false); // Hướng Dẫn Sử Dụng
        jButton12.setEnabled(false); // Báo Cáo & In Ấn
        jButton13.setEnabled(false); // Đổi Mật Khẩu
        jButton15.setEnabled(false); // Quản Lý Cơ Sở Vật Chất

        // Đăng xuất luôn enable
        jButton3.setEnabled(true);

        // Thêm tooltip để giải thích tại sao bị disable
        setDisabledTooltips();
    }

    /**
     * Set tooltip cho các button bị disable
     */
    private void setDisabledTooltips() {
        String noPermissionText = "Bạn không có quyền truy cập chức năng này";
        jButton1.setToolTipText(jButton1.isEnabled() ? null : noPermissionText);
        jButton4.setToolTipText(jButton4.isEnabled() ? null : noPermissionText);
        jButton5.setToolTipText(jButton5.isEnabled() ? null : noPermissionText);
        jButton6.setToolTipText(jButton6.isEnabled() ? null : noPermissionText);
        jButton7.setToolTipText(jButton7.isEnabled() ? null : noPermissionText);
        jButton9.setToolTipText(jButton9.isEnabled() ? null : noPermissionText);
        jButton10.setToolTipText(jButton10.isEnabled() ? null : noPermissionText);
        jButton11.setToolTipText(jButton11.isEnabled() ? null : noPermissionText);
        jButton12.setToolTipText(jButton12.isEnabled() ? null : noPermissionText);
        jButton13.setToolTipText(jButton13.isEnabled() ? null : noPermissionText);
        jButton15.setToolTipText(jButton15.isEnabled() ? null : noPermissionText);
    }

    /**
     * Thiết lập style cho buttons
     */
    private void setupButtonStyles() {
        // Thiết lập style mặc định cho tất cả buttons
        javax.swing.JButton[] buttons = {
            jButton1, jButton3, jButton4, jButton5, jButton6,
            jButton7, jButton9, jButton10, jButton11, jButton12,
            jButton13, jButton14, jButton15
        };

        for (javax.swing.JButton button : buttons) {
            // Lưu màu gốc
            final java.awt.Color originalColor = button.getForeground();

            // Thiết lập opacity cho trạng thái enabled/disabled
            button.addPropertyChangeListener("enabled", evt -> {
                javax.swing.JButton source = (javax.swing.JButton) evt.getSource();
                if (source.isEnabled()) {
                    // ✅ ENABLED: Màu gốc, opacity đầy đủ
                    source.setForeground(originalColor);

                    // Đặt lại icon opacity nếu có
                    if (source.getIcon() != null) {
                        // Icon sáng bình thường
                    }
                } else {
                    // ❌ DISABLED: Cùng màu nhưng mờ hơn (alpha = 0.3)
                    java.awt.Color fadedColor = new java.awt.Color(
                            originalColor.getRed(),
                            originalColor.getGreen(),
                            originalColor.getBlue(),
                            77 // Alpha 77/255 ≈ 30% opacity (mờ hơn)
                    );
                    source.setForeground(fadedColor);
                }
            });

            // Set opacity ban đầu dựa trên trạng thái hiện tại
            if (!button.isEnabled()) {
                java.awt.Color fadedColor = new java.awt.Color(
                        originalColor.getRed(),
                        originalColor.getGreen(),
                        originalColor.getBlue(),
                        77 // 30% opacity
                );
                button.setForeground(fadedColor);
            }
        }
    }

    /**
     * Set quyền theo vai trò
     */
    private void setPermissions() {
        // Disable tất cả trước
        disableAllFunctions();

        switch (currentRole) {
            case GVCN:
                setGVCNPermissions();
                break;
            case GVBM:
                setGVBMPermissions();
                break;
            case BGH:
            case QTV:
                setFullPermissions();
                break;
            default:
                setGVBMPermissions(); // Default to GVBM permissions
                break;
        }

        // Đăng xuất luôn enable cho tất cả
        jButton3.setEnabled(true);

        // Cập nhật tooltips
        setDisabledTooltips();

        // Refresh layout
        revalidate();
        repaint();
    }

    /**
     * Quyền cho Giáo viên chủ nhiệm Quản Lý Cơ Sở Vật Chất, Báo Cáo & In Ấn,
     * Trang Chủ, Quản Lý Học Sinh, Thời Khoá Biểu, Đổi Mật Khẩu, Hướng dẫn sử
     * dụng, Quản Lý điểm & hạnh kiểm
     */
    private void setGVCNPermissions() {
        jButton1.setEnabled(true);   // Trang Chủ
        jButton4.setEnabled(true);   // Quản Lý Học Sinh
        jButton7.setEnabled(true);   // Quản Lý Điểm & Hạnh Kiểm
        jButton9.setEnabled(true);   // Thời Khoá Biểu
        jButton11.setEnabled(true);  // Hướng Dẫn Sử Dụng
        jButton12.setEnabled(true);  // Báo Cáo & In Ấn
        jButton13.setEnabled(true);  // Đổi Mật Khẩu
        jButton15.setEnabled(true);  // Quản Lý Cơ Sở Vật Chất
    }

    /**
     * Quyền cho Giáo viên bộ môn Trang Chủ, Thời Khoá Biểu, Đổi mật Khẩu, Hướng
     * dẫn sử dụng, Quản Lý điểm & hạnh kiểm
     */
    private void setGVBMPermissions() {
        jButton1.setEnabled(true);   // Trang Chủ
        jButton7.setEnabled(true);   // Quản Lý Điểm & Hạnh Kiểm
        jButton9.setEnabled(true);   // Thời Khoá Biểu
        jButton11.setEnabled(true);  // Hướng Dẫn Sử Dụng
        jButton13.setEnabled(true);  // Đổi Mật Khẩu
    }

    /**
     * Quyền đầy đủ cho BGH và QTV
     */
    private void setFullPermissions() {
        jButton1.setEnabled(true);   // Trang Chủ
        jButton4.setEnabled(true);   // Quản Lý Học Sinh
        jButton5.setEnabled(true);   // Quản Lý Giáo Viên
        jButton6.setEnabled(true);   // Quản Lý Môn Học
        jButton7.setEnabled(true);   // Quản Lý Điểm & Hạnh Kiểm
        jButton9.setEnabled(true);   // Thời Khoá Biểu
        jButton10.setEnabled(true);  // Quản Lý Lớp Học
        jButton11.setEnabled(true);  // Hướng Dẫn Sử Dụng
        jButton12.setEnabled(true);  // Báo Cáo & In Ấn
        jButton13.setEnabled(true);  // Đổi Mật Khẩu
        jButton15.setEnabled(true);  // Quản Lý Cơ Sở Vật Chất
    }

    /**
     * Kiểm tra quyền truy cập chức năng
     */
    private boolean hasPermission(String functionName) {
        if (currentRole == null) {
            return false;
        }

        switch (currentRole) {
            case BGH:
            case QTV:
                return true; // Full permissions
            case GVCN:
                return isGVCNFunction(functionName);
            case GVBM:
                return isGVBMFunction(functionName);
            default:
                return false;
        }
    }

    private boolean isGVCNFunction(String functionName) {
        switch (functionName) {
            case "HOME":
            case "STUDENT_MANAGEMENT":
            case "GRADE_MANAGEMENT":
            case "SCHEDULE":
            case "HELP":
            case "REPORT":
            case "CHANGE_PASSWORD":
            case "FACILITY_MANAGEMENT":
                return true;
            default:
                return false;
        }
    }

    private boolean isGVBMFunction(String functionName) {
        switch (functionName) {
            case "HOME":
            case "GRADE_MANAGEMENT":
            case "SCHEDULE":
            case "HELP":
            case "CHANGE_PASSWORD":
                return true;
            default:
                return false;
        }
    }

    /**
     * Hiển thị thông báo không có quyền
     */
    private void showNoPermissionMessage() {
        JOptionPane.showMessageDialog(this,
                "Bạn không có quyền truy cập chức năng này!",
                "Không có quyền",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("A");

        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Cấp Hàm");

        jButton14.setText("jButton14");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(1444, Short.MAX_VALUE)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addContainerGap(18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 1690, 80));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(9, 86, 164));
        jPanel1.setForeground(new java.awt.Color(46, 129, 253));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("HỆ THỐNG QUẢN LÝ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(24, 24, 24))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 153, 153));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Home.png"))); // NOI18N
        jButton1.setText("  Trang Chủ");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 153, 153));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Open Pane.png"))); // NOI18N
        jButton3.setText("Đăng Xuất");
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(153, 153, 153));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Man Reading a Book.png"))); // NOI18N
        jButton4.setText("Quản Lý Học Sinh");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(153, 153, 153));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Teacher.png"))); // NOI18N
        jButton5.setText("Quản Lý Giáo Viên");
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(153, 153, 153));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Note.png"))); // NOI18N
        jButton6.setText("Quản Lý Môn Học");
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(153, 153, 153));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Popular Man.png"))); // NOI18N
        jButton7.setText("Quản Lý Điểm & Hạnh Kiểm");
        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(153, 153, 153));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Calendar.png"))); // NOI18N
        jButton9.setText("Thời Khoá Biểu");
        jButton9.setBorder(null);
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(153, 153, 153));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Class.png"))); // NOI18N
        jButton10.setText("Quản Lý Lớp Học");
        jButton10.setBorder(null);
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(153, 153, 153));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/User Manual.png"))); // NOI18N
        jButton11.setText("Hướng Dẫn Sử Dụng");
        jButton11.setBorder(null);
        jButton11.setBorderPainted(false);
        jButton11.setContentAreaFilled(false);
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(153, 153, 153));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Tick Box.png"))); // NOI18N
        jButton12.setText("Báo Cáo & In Ấn");
        jButton12.setBorder(null);
        jButton12.setBorderPainted(false);
        jButton12.setContentAreaFilled(false);
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(153, 153, 153));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Privacy.png"))); // NOI18N
        jButton13.setText("Đổi Mật Khẩu");
        jButton13.setBorder(null);
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(153, 153, 153));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Desk.png"))); // NOI18N
        jButton15.setText("Quản Lý Cơ Sở Vật Chất");
        jButton15.setBorder(null);
        jButton15.setBorderPainted(false);
        jButton15.setContentAreaFilled(false);
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1670, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, 940));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jButton1.isEnabled() && hasPermission("HOME")) {
            // Mở trang chủ
            JOptionPane.showMessageDialog(this, "Mở Trang Chủ");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            Login login = new Login(this, true);
            login.setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
      if (jButton5.isEnabled() && hasPermission("CLASS_MANAGEMENT")) {

        // Xóa hết component cũ
        jPanel3.removeAll();

        // Tạo và thêm panel QLLH
        QuanLyNguoiDung_View p1 = new QuanLyNguoiDung_View();
        jPanel3.setLayout(new java.awt.BorderLayout()); // Đảm bảo layout đúng
        jPanel3.add(p1, java.awt.BorderLayout.CENTER);

        // Cập nhật lại giao diện
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
      if (jButton4.isEnabled() && hasPermission("CLASS_MANAGEMENT")) {

        // Xóa hết component cũ
        jPanel3.removeAll();

        // Tạo và thêm panel QLLH
        QuanLyHocSinh_View p1 = new QuanLyHocSinh_View();
        jPanel3.setLayout(new java.awt.BorderLayout()); // Đảm bảo layout đúng
        jPanel3.add(p1, java.awt.BorderLayout.CENTER);

        // Cập nhật lại giao diện
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (jButton6.isEnabled() && hasPermission("SUBJECT_MANAGEMENT")) {
            // Mở quản lý môn học
            JOptionPane.showMessageDialog(this, "Mở Quản Lý Môn Học");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (jButton7.isEnabled() && hasPermission("GRADE_MANAGEMENT")) {
            // Mở quản lý điểm & hạnh kiểm
            JOptionPane.showMessageDialog(this, "Mở Quản Lý Điểm & Hạnh Kiểm");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        if (jButton9.isEnabled() && hasPermission("SCHEDULE")) {
            // Mở thời khoá biểu
            JOptionPane.showMessageDialog(this, "Mở Thời Khoá Biểu");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
      if (jButton12.isEnabled() && hasPermission("CLASS_MANAGEMENT")) {

        // Xóa hết component cũ
        jPanel3.removeAll();

        // Tạo và thêm panel QLLH
        QuanLyInAnBaoCao_View p1 = new QuanLyInAnBaoCao_View();
        jPanel3.setLayout(new java.awt.BorderLayout()); // Đảm bảo layout đúng
        jPanel3.add(p1, java.awt.BorderLayout.CENTER);

        // Cập nhật lại giao diện
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
       if (jButton10.isEnabled() && hasPermission("CLASS_MANAGEMENT")) {

        // Xóa hết component cũ
        jPanel3.removeAll();

        // Tạo và thêm panel QLLH
        panelQLLH p1 = new panelQLLH();
        jPanel3.setLayout(new java.awt.BorderLayout()); // Đảm bảo layout đúng
        jPanel3.add(p1, java.awt.BorderLayout.CENTER);

        // Cập nhật lại giao diện
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        if (jButton15.isEnabled() && hasPermission("FACILITY_MANAGEMENT")) {
            // Mở quản lý cơ sở vật chất
            JOptionPane.showMessageDialog(this, "Mở Quản Lý Cơ Sở Vật Chất");
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        if (jButton13.isEnabled() && hasPermission("CHANGE_PASSWORD")) {
            // Mở đổi mật khẩu
            JOptionPane.showMessageDialog(this, "Mở Form Đổi Mật Khẩu");
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        if (jButton11.isEnabled() && hasPermission("HELP")) {
            // Mở hướng dẫn sử dụng
            JOptionPane.showMessageDialog(this, "Mở Hướng Dẫn Sử Dụng");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        if (currentUser != null) {
            String info = String.format(
                    "Thông tin tài khoản:\n"
                    + "Mã người dùng: %s\n"
                    + "Tên: %s\n"
                    + "Email: %s\n"
                    + "Vai trò: %s\n"
                    + "Bộ môn: %s\n"
                    + "Trạng thái: %s",
                    currentUser.getMaNguoiDung(),
                    currentUser.getTenNguoiDung(),
                    currentUser.getEmail(),
                    currentRole.getDisplayName(),
                    currentUser.getBoMon(),
                    currentUser.getStatusDisplay()
            );
            JOptionPane.showMessageDialog(this, info, "Thông tin tài khoản", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
