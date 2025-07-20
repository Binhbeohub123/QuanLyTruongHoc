/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tool;

import Dao.InAnBaoCaoDAO;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import Dao.InAnBaoCaoDAO;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class QuanLyInAnBaoCao_View extends javax.swing.JPanel {
    private InAnBaoCaoDAO dao = new InAnBaoCaoDAO();
    private Map<String, String> dsLop = new java.util.LinkedHashMap<>();

    public QuanLyInAnBaoCao_View() {
        setupTheme();
        initComponents();
        initializeForm();
        loadKhoi();
        jTextArea2.putClientProperty("JTextArea.placeholderText", "Tất cả mọi thứ xem trước sẽ xuất hiện ở đây ....");
        String[] columnNames = {"Mã HS", "Họ Tên", "Ngày Sinh", "Giới Tính", "Trạng Thái"};
        khoaTable(jTable1);
        jTable1.setDefaultEditor(Object.class, null);
    }
     private void setupTheme() {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 16);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Button.arc", 16);
        UIManager.put("Panel.arc", 16);
        UIManager.put("TextArea.arc", 20);
        UIManager.put("ScrollPane.arc", 20);
    }
     private void khoaTable(JTable table) {
    // Khóa chỉnh sửa cell
    if (table.getModel() instanceof DefaultTableModel) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        table.setModel(new DefaultTableModel(model.getDataVector(), getColumnNames(model)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit cell
            }
        });
    }

    // Khóa di chuyển và resize cột
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setResizingAllowed(false);
}
     public List<String> getDanhSachMaHocSinhTrenTable() {
    List<String> dsMaHocSinh = new ArrayList<>();

    for (int i = 0; i < jTable1.getRowCount(); i++) {
        String maHS = (String) jTable1.getValueAt(i, 0); // Cột 0 là Mã HS
        dsMaHocSinh.add(maHS);
    }

    return dsMaHocSinh;
}
     private Vector<String> getColumnNames(DefaultTableModel model) {
    Vector<String> columnNames = new Vector<>();
    for (int i = 0; i < model.getColumnCount(); i++) {
        columnNames.add(model.getColumnName(i));
    }
    return columnNames;
}
    public String getHocKyDaChon() {
    String hocKyCombo = (String) jComboBox15.getSelectedItem();
    if (hocKyCombo.equals("Chọn Học Kỳ")) {
        return null; 
    }
    return convertHocKy(hocKyCombo);
}
     public String convertHocKy(String hocKyCombo) {
    switch (hocKyCombo) {
        case "Học Kỳ I":
            return "HK1";
        case "Học Kỳ II":
            return "HK2";
        default:
            return null;
    }
}
     public void xemTruocInAn() {
    if (!jCheckBox2.isSelected()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn 'In Bảng Điểm Cả Lớp' để xem trước!");
        return;
    }

    String maLop = getSelectedMaLop();
    String hocKy = getHocKyDaChon();
    List<String> dsMaHocSinh = getDanhSachMaHocSinhTrenTable();

    if (maLop == null || hocKy == null || dsMaHocSinh.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp, học kỳ và danh sách học sinh đầy đủ!");
        return;
    }

    List<Object[]> dsBangDiem = dao.loadBangDiemTheoDanhSachHocSinh(dsMaHocSinh, maLop, hocKy);

    if (dsBangDiem.isEmpty()) {
        jTextArea2.setText("Không tìm thấy bảng điểm cho danh sách đã chọn.");
        return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("BẢNG ĐIỂM LỚP ").append(maLop).append(" - ").append(hocKy).append("\n");
    sb.append("---------------------------------------------------\n");

    for (Object[] row : dsBangDiem) {
        String maHS = (String) row[0];
        String maMon = (String) row[1];
        float diem = (Float) row[2];
        int heSo = (Integer) row[3];
        float d45p = (Float) row[4];
        float dGK = (Float) row[5];
        float dCK = (Float) row[6];
        float dtb = (Float) row[7];
        String hocLuc = (String) row[8];
        String moTa = (String) row[9];

        sb.append("HS: ").append(maHS)
          .append(" | Môn: ").append(maMon)
          .append(" | Điểm: ").append(diem)
          .append(" | Hệ Số: ").append(heSo)
          .append(" | 45P: ").append(d45p)
          .append(" | GK: ").append(dGK)
          .append(" | CK: ").append(dCK)
          .append(" | TB: ").append(String.format("%.2f", dtb))
          .append(" | Học Lực: ").append(hocLuc)
          .append(" | Mô Tả: ").append(moTa)
          .append("\n");
    }

    jTextArea2.setText(sb.toString());
}
    
    private void initializeForm() { 
        setupPanelStyling();
    }
    private void setupPanelStyling() {
        String arcStyle = "arc: 20";
        jPanel1.putClientProperty("FlatLaf.style", arcStyle);
        jPanel2.putClientProperty("FlatLaf.style", arcStyle);
      }
    private void loadKhoi() {
    List<Integer> dsKhoi = dao.loadKhoi();
    DefaultComboBoxModel<String> modelKhoi = new DefaultComboBoxModel<>();

    modelKhoi.addElement("Chọn Khối"); // Placeholder giữ nguyên

    for (Integer khoi : dsKhoi) {
        modelKhoi.addElement(String.valueOf(khoi));
    }

    jComboBox13.setModel(modelKhoi);

    // Sự kiện chọn khối
    jComboBox13.addActionListener(e -> {
        String khoiChon = (String) jComboBox13.getSelectedItem();
        if (khoiChon == null || khoiChon.equals("Chọn Khối")) {
            // Không làm gì nếu chọn placeholder
            jComboBox14.setModel(new DefaultComboBoxModel<>(new String[]{"Chọn Lớp"}));
            return;
        }

        loadLopTheoKhoi(Integer.parseInt(khoiChon));
        resetTable();
    });
    } // Lưu ánh xạ TenLop -> MaLop

private void loadLopTheoKhoi(int khoi) {
    dsLop = dao.loadLopTheoKhoi(khoi);
    DefaultComboBoxModel<String> modelLop = new DefaultComboBoxModel<>();

    modelLop.addElement("Chọn Lớp"); // Placeholder giữ nguyên

    for (String tenLop : dsLop.keySet()) {
        modelLop.addElement(tenLop);
    }

    jComboBox14.setModel(modelLop);
}
private String getSelectedMaLop() {
    String tenLopChon = (String) jComboBox14.getSelectedItem();
    if (tenLopChon == null || tenLopChon.equals("Chọn Lớp")) {
        return null; // Không hợp lệ
    }

    return dsLop.get(tenLopChon);
}
private void loadHocSinhLenTable(String maLop) {
    List<Object[]> dsHocSinh = dao.loadHocSinhTheoLop(maLop);

    String[] columnNames = {"Mã HS", "Họ Tên", "Ngày Sinh", "Giới Tính", "Trạng Thái"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    for (Object[] row : dsHocSinh) {
        model.addRow(row);
    }

    jTable1.setModel(model);
}
private void resetTable() {
    String[] columnNames = {"Mã HS", "Họ Tên", "Ngày Sinh", "Giới Tính", "Trạng Thái"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0); // 0 dòng trống
    jTable1.setModel(model);
}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jComboBox15 = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1330, 785));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(227, 234, 243));
        jPanel2.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(java.awt.Color.darkGray);
        jLabel1.setText("Báo Cáo In Ấn");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox1.setForeground(java.awt.Color.darkGray);
        jCheckBox1.setText("In Sĩ Số Lớp");
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 180, -1));

        jCheckBox2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox2.setForeground(java.awt.Color.darkGray);
        jCheckBox2.setText("In Bảng Điểm Cả Lớp");
        jPanel2.add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 180, -1));

        jCheckBox3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox3.setForeground(java.awt.Color.darkGray);
        jCheckBox3.setText("Danh Sách Cơ Sở Vật Chất");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 210, -1));

        jCheckBox4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox4.setForeground(java.awt.Color.darkGray);
        jCheckBox4.setText("In Bảng Điểm Cá Nhân");
        jPanel2.add(jCheckBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 180, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 370, 640));

        jButton12.setBackground(new java.awt.Color(37, 99, 235));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Xem Trước");
        jButton12.setPreferredSize(new java.awt.Dimension(92, 31));
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton12MouseClicked(evt);
            }
        });
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 630, 130, 50));

        jButton13.setBackground(java.awt.Color.darkGray);
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("In Ngay");
        jButton13.setPreferredSize(new java.awt.Dimension(92, 31));
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton13MouseClicked(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 630, 130, 50));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, 770, 250));

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea2.setForeground(java.awt.Color.darkGray);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 350, 770, 270));

        jComboBox13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox13.setForeground(java.awt.Color.darkGray);
        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Khối" }));
        jPanel1.add(jComboBox13, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 250, 50));

        jComboBox14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox14.setForeground(java.awt.Color.darkGray);
        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Lớp" }));
        jComboBox14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox14MouseClicked(evt);
            }
        });
        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox14, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, 250, 50));

        jComboBox15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox15.setForeground(java.awt.Color.darkGray);
        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Học Kỳ", "Học Kỳ I", "Học Kỳ II" }));
        jPanel1.add(jComboBox15, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, 250, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton12MouseClicked

    private void jButton13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13MouseClicked

    private void jComboBox14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox14MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14MouseClicked

    private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
        // TODO add your handling code here:
        String tenLop = (String) jComboBox14.getSelectedItem();
    if (tenLop != null && !tenLop.equals("Chọn Lớp")) {
        String maLop = dsLop.get(tenLop);
        loadHocSinhLenTable(maLop);
    }
    }//GEN-LAST:event_jComboBox14ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        xemTruocInAn();
    }//GEN-LAST:event_jButton12ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
