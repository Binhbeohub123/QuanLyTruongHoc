
package tool;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.text.MaskFormatter;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author acchi
 */
public class HoKhau_NhanKhau extends javax.swing.JPanel {
    private boolean isDatePickerVisible = false;
    private JDateChooser currentDateChooser = null;

    public HoKhau_NhanKhau() {
        setupTheme();
        initComponents();
        initializeForm();
    }
    private void setupTheme() {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 16);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Button.arc", 16);
        UIManager.put("Panel.arc", 16);
        UIManager.put("Panel.border", new FlatRoundBorder());
        UIManager.put("ScrollPane.arc",    12);
        UIManager.put("ScrollPane.border", new FlatRoundBorder());
    }
    private void initializeForm() {
        setupDateField();
        setupPanelStyling();
    }
    
    
    private void setupPanelStyling() {
        String arcStyle = "arc: 20";
        jPanel2.putClientProperty("FlatLaf.style", arcStyle);
        jPanel1.putClientProperty("FlatLaf.style", arcStyle);
        jPanel3.putClientProperty("FlatLaf.style", arcStyle);
    }
    private void setupDateField() {
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            dateFormatter.setAllowsInvalid(false);
            
            JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
            dateField.setFont(new java.awt.Font("Segoe UI", 0, 14));
            dateField.setText("");
            dateField.setToolTipText("Nhấp đúp để chọn ngày hoặc nhập trực tiếp (dd/MM/yyyy)");
            
            // Replace old field
            jPanel3.remove(jTextField3);
            jPanel3.add(dateField, new AbsoluteConstraints(590, 280, 400, 50));
            jTextField3 = dateField;
            
            showDatePicker();
            
            jPanel3.revalidate();
            jPanel3.repaint();
            
        } catch (ParseException e) {
            System.err.println("Error creating date formatter: " + e.getMessage());
            setupBasicDateField();
        }
    }
    

    
    private void setupBasicDateField() {
        jTextField3.setText("");
        jTextField3.setToolTipText("Nhập ngày theo định dạng dd/MM/yyyy");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateDateFormat();
            }
        });
    }
    
    private void showDatePicker() {
    if (isDatePickerVisible) return;
    
    try {
        isDatePickerVisible = true;
        
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(560, 50)); // Cập nhật kích thước
        dateChooser.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        Date currentDate = getCurrentDateFromField();
        dateChooser.setDate(currentDate != null ? currentDate : new Date());
        
        jTextField3.setVisible(false);
        jPanel3.add(dateChooser, new AbsoluteConstraints(20, 170, 560, 50)); // Vị trí đúng
        currentDateChooser = dateChooser;
        
        dateChooser.addPropertyChangeListener("date", evt -> {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                jTextField3.setText(sdf.format(selectedDate));
            }
        });
        
        jPanel3.revalidate();
        jPanel3.repaint();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            dateChooser.requestFocus();
        });
        
    } catch (Exception e) {
        System.err.println("Error showing date picker: " + e.getMessage());
        isDatePickerVisible = false;
    }
}
    
    private Date getCurrentDateFromField() {
        try {
            String currentText = jTextField3.getText();
            if (currentText != null && !currentText.trim().isEmpty() && 
                !currentText.equals("mm/dd/yyyy") && !currentText.equals("__/__/____")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                return sdf.parse(currentText);
            }
        } catch (ParseException e) {
            System.out.println("Could not parse current date: " + jTextField3.getText());
        }
        return null;
    }
    
    private void validateDateFormat() {
        String text = jTextField3.getText().trim();
        if (!text.isEmpty() && !text.equals("__/__/____")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date date = sdf.parse(text);
                jTextField3.setText(sdf.format(date));
            } catch (ParseException e) {
                showError("Định dạng ngày không hợp lệ!\nVui lòng nhập theo định dạng: dd/MM/yyyy\n\nVí dụ: 25/12/2023");
                jTextField3.requestFocus();
            }
        }
    }
    
    private void showError(String message) {
        javax.swing.JOptionPane.showMessageDialog(
            this, message, "Lỗi định dạng ngày", javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Public methods for external use
    public Date getSelectedDate() {
        return getCurrentDateFromField();
    }
    
    public void setSelectedDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            jTextField3.setText(sdf.format(date));
        } else {
            jTextField3.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();

        jPanel1.setBackground(new java.awt.Color(234, 232, 232));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(30, 30, 30));
        jTextField1.setText("Tìm Kiếm Nhanh...");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Xóa");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jPanel3.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 560, 50));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField3.setText("dd/MM/YYYY");
        jPanel3.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 560, 50));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Ngày Sinh");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 20));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Họ và Tên");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trộm Cắp", "Giết Người", "Buôn Lậu" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 560, 50));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("CCCD");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, -1, 20));

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jPanel3.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 560, 50));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Quan Hệ");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, -1, 20));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Xóa Trống");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 260, 110, 40));

        jButton2.setBackground(new java.awt.Color(0, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Lưu Nhân Khẩu");
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, 160, 40));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "#", "Họ Và Tên", "CCCD", "Ngay Sinh", "Quan Hệ", "Hành Động"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 255));
        jLabel2.setText("Quản Lý Nhân Khẩu");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jTextField1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel1))
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1195, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản Lý Nhân Khẩu", jPanel1);

        jPanel2.setBackground(new java.awt.Color(234, 232, 232));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Xóa");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jPanel4.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 560, 50));

        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField6.setText("dd/MM/YYYY");
        jPanel4.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 560, 50));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Ngày Cấp");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 20));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Số Hộ Khẩu");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Địa Chỉ");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, -1, 20));

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jPanel4.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 560, 50));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setText("Xóa Trống");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 260, 110, 40));

        jButton4.setBackground(new java.awt.Color(0, 255, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Lưu Hộ Khẩu");
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, 160, 40));

        jTable2.setBackground(new java.awt.Color(255, 255, 255));
        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTable2.setForeground(new java.awt.Color(255, 255, 255));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "#", "Số Hộ Khẩu", "Địa chỉ", "Ngày Cấp", "Hành Động"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 255, 0));
        jLabel12.setText("Quản Lý Hộ Khẩu");

        jTextField8.setBackground(new java.awt.Color(255, 255, 255));
        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(30, 30, 30));
        jTextField8.setText("Tìm Kiếm Nhanh...");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jTextField8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel7))
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1195, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(22, 22, 22)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản Lý Hộ Khẩu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
