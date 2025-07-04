package tool;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.text.MaskFormatter;
import randomcodemavuan.CaseUtils;
import utils.VnAddressSelectorService;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

public class Lamlai extends javax.swing.JPanel {
    private JDateChooser currentDateChooser = null;
    private boolean isDatePickerVisible = false;
    
    public Lamlai() {
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
        UIManager.put("TextArea.arc", 20);
        UIManager.put("ScrollPane.arc", 20);
    }
    
    private void initializeForm() {
        setupPanelStyling();
        setupFormFields();
        setupDateField();
    }
    
    private void setupPanelStyling() {
        String arcStyle = "arc: 20";
        jPanel2.putClientProperty("FlatLaf.style", arcStyle);
        jPanel3.putClientProperty("FlatLaf.style", arcStyle);
        jPanel4.putClientProperty("FlatLaf.style", arcStyle);
        jPanel5.putClientProperty("FlatLaf.style", arcStyle);
        jPanel6.putClientProperty("FlatLaf.style", arcStyle);
        jPanel7.putClientProperty("FlatLaf.style", arcStyle);
    }
    
    private void setupFormFields() {
        // Setup case code generation
        CaseUtils.assignCaseCodeToField(jTextField1);
        jTextField1.setEditable(false);
        
        // Setup address selector service
        VnAddressSelectorService addrSvc = new VnAddressSelectorService();
        addrSvc.bindAddressCombos(jComboBox5, jComboBox2, jComboBox4);
        
        // Setup text fields
        jTextArea1.setText("");
        jTextArea1.setToolTipText("Nhập mô tả chi tiết về vụ án...");
        
        jTextField9.setEditable(true);
        jTextField9.setText("");
        jTextField9.setToolTipText("Nhập tên điều tra viên");
    }
    
    private void setupDateField() {
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            dateFormatter.setAllowsInvalid(false);
            
            JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
            dateField.setFont(new java.awt.Font("Segoe UI", 0, 15));
            dateField.setText("");
            dateField.setToolTipText("Nhấp đúp để chọn ngày hoặc nhập trực tiếp (dd/MM/yyyy)");
            
            // Replace old field
            jPanel4.remove(jTextField8);
            jPanel4.add(dateField, new AbsoluteConstraints(590, 280, 490, 50));
            jTextField8 = dateField;
            
            // Add event listeners
            showDatePicker();
            
            jPanel4.revalidate();
            jPanel4.repaint();
            
        } catch (ParseException e) {
            System.err.println("Error creating date formatter: " + e.getMessage());
            setupBasicDateField();
        }
    }
    

    
    private void setupBasicDateField() {
        jTextField8.setText("");
        jTextField8.setToolTipText("Nhập ngày theo định dạng dd/MM/yyyy");
        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
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
            dateChooser.setPreferredSize(new Dimension(490, 50));
            dateChooser.setFont(new java.awt.Font("Segoe UI", 0, 15));
            
            Date currentDate = getCurrentDateFromField();
            dateChooser.setDate(currentDate != null ? currentDate : new Date());
            
            jTextField8.setVisible(false);
            jPanel4.add(dateChooser, new AbsoluteConstraints(590, 280, 490, 50));
            currentDateChooser = dateChooser;
            
            dateChooser.addPropertyChangeListener("date", evt -> {
                Date selectedDate = dateChooser.getDate();
                if (selectedDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    jTextField8.setText(sdf.format(selectedDate));
                }
            });
            
            jPanel4.revalidate();
            jPanel4.repaint();
            
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
            String currentText = jTextField8.getText();
            if (currentText != null && !currentText.trim().isEmpty() && 
                !currentText.equals("mm/dd/yyyy") && !currentText.equals("__/__/____")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                return sdf.parse(currentText);
            }
        } catch (ParseException e) {
            System.out.println("Could not parse current date: " + jTextField8.getText());
        }
        return null;
    }
    
    private void validateDateFormat() {
        String text = jTextField8.getText().trim();
        if (!text.isEmpty() && !text.equals("__/__/____")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date date = sdf.parse(text);
                jTextField8.setText(sdf.format(date));
            } catch (ParseException e) {
                showError("Định dạng ngày không hợp lệ!\nVui lòng nhập theo định dạng: dd/MM/yyyy\n\nVí dụ: 25/12/2023");
                jTextField8.requestFocus();
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
            jTextField8.setText(sdf.format(date));
        } else {
            jTextField8.setText("");
        }
    }
    
    public boolean validateForm() {
        String[] requiredFields = {
            jTextField1.getText().trim().isEmpty() ? "Mã vụ án không được để trống!" : null,
            jComboBox5.getSelectedIndex() == -1 ? "Vui lòng chọn Tỉnh/Thành phố!" : null,
            jComboBox2.getSelectedIndex() == -1 ? "Vui lòng chọn Quận/Huyện!" : null,
            jComboBox4.getSelectedIndex() == -1 ? "Vui lòng chọn Xã/Phường!" : null,
            (jTextField8.getText().trim().isEmpty() || jTextField8.getText().equals("__/__/____")) ? "Vui lòng nhập ngày xảy ra!" : null,
            getCurrentDateFromField() == null ? "Định dạng ngày không hợp lệ!" : null,
            jTextField9.getText().trim().isEmpty() ? "Vui lòng nhập tên điều tra viên!" : null,
            jTextArea1.getText().trim().isEmpty() ? "Vui lòng nhập mô tả vụ án!" : null
        };
        
        for (String error : requiredFields) {
            if (error != null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this, error, "Lỗi xác thực dữ liệu", javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return false;
            }
        }
        return true;
    }
    
    public void clearForm() {
        jTextField1.setText("");
        jComboBox5.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox4.setSelectedIndex(-1);
        jComboBox1.setSelectedIndex(0);
        jTextField8.setText("");
        jComboBox3.setSelectedIndex(0);
        jTextField9.setText("");
        jTextArea1.setText("");
        
        CaseUtils.assignCaseCodeToField(jTextField1);
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();

        jToolBar1.setRollover(true);

        setPreferredSize(new java.awt.Dimension(1280, 1035));

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1270, 1035));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(51, 51, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo/servers.png"))); // NOI18N
        jLabel1.setText(" Hệ Thống Quản Lý Tội Phạm Hình Sự");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1250, 70));

        jPanel3.setBackground(new java.awt.Color(234, 232, 232));
        jPanel3.setPreferredSize(new java.awt.Dimension(160, 121));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("1");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Chờ Xử Lý");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 110, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 250, 160));

        jPanel5.setBackground(new java.awt.Color(234, 232, 232));
        jPanel5.setPreferredSize(new java.awt.Dimension(160, 121));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 153, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("1");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Đang Điều Tra");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 110, -1));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 250, 160));

        jPanel6.setBackground(new java.awt.Color(234, 232, 232));
        jPanel6.setPreferredSize(new java.awt.Dimension(160, 121));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 255, 153));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("1");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Đã Giải Quyết");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 110, -1));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 150, 250, 160));

        jPanel7.setBackground(new java.awt.Color(234, 232, 232));
        jPanel7.setPreferredSize(new java.awt.Dimension(160, 121));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("1");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tổng Số Vụ");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 110, -1));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 150, 250, 160));

        jPanel4.setBackground(new java.awt.Color(234, 232, 232));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(30, 30, 30));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo/pencil.png"))); // NOI18N
        jLabel11.setText("Thêm vụ án mới");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 50));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Mã vụ án:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 70, -1));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField1.setText("AutoGeneration By ???");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 490, 50));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Tỉnh / Thành Phố:");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 130, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Xã / Phường:");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 100, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Mức Độ Ưu Tiên :");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 130, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Mô Tả:");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 130, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Loại tội danh:");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 100, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("Quận / Huyện:");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 110, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Ngày xảy ra:");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 90, -1));

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField8.setText("mm/dd/yyyy");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 280, 490, 50));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("Điều tra viên:");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, 100, -1));

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextField9.setText("AutoGeneration By ???");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 370, 490, 50));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trộm Cắp", "Giết Người", "Buôn Lậu" }));
        jPanel4.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, 490, 50));

        jButton1.setBackground(new java.awt.Color(255, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo/delete.png"))); // NOI18N
        jButton1.setText("XÓA TẤT CẢ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 600, 530, 40));

        jButton2.setBackground(new java.awt.Color(102, 153, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo/sign.png"))); // NOI18N
        jButton2.setText("THÊM VỤ ÁN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 520, 40));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jPanel4.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, 490, 50));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Mô tả chi tiết về vụ án ...");
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 1060, 130));

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cao", "Trung Bình", "Thấp" }));
        jPanel4.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 490, 50));

        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox4MouseClicked(evt);
            }
        });
        jPanel4.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 490, 50));

        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jComboBox5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox5MouseClicked(evt);
            }
        });
        jPanel4.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 490, 50));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 1100, 640));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Công an thành phố Dĩ An - Tỉnh Bình Dương");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1250, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1250, 1000));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5MouseClicked

    private void jComboBox4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    Object selProv = jComboBox5.getSelectedItem();
    String province = selProv != null ? selProv.toString() : "";

    // 2. District
    Object selDist = jComboBox2.getSelectedItem();
    String district = selDist != null ? selDist.toString() : "";

    // 3. Ward
    Object selWard = jComboBox4.getSelectedItem();
    String ward = selWard != null ? selWard.toString() : "";

    System.out.println("Tỉnh: "    + province);
    System.out.println("Huyện: "   + district);
    System.out.println("Xã/Phường: " + ward);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
