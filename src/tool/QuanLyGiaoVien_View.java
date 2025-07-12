/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tool;

import Model.GiaoVien;
import Model.VaiTro;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import Controller.GiaoVienController;
import Dao.GiaoVienDAO;
import Dao.VaiTroDAO;
import Model.VaiTro;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author acchi
 */
public class QuanLyGiaoVien_View extends javax.swing.JPanel {
    private boolean isDatePickerVisible = false;
    private JDateChooser currentDateChooser = null;
    private GiaoVienController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private final String PLACEHOLDER = "Tìm kiếm giáo viên ...";
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.BLACK;
    private List<GiaoVien> danhSachCache;
    private String selectedImageBase64 = null;
    private boolean isEditing = false;
    private GiaoVienDAO dao = new GiaoVienDAO(null);
    
    public QuanLyGiaoVien_View() {
        setupTheme();
        initComponents();
        initializeForm();
        controller = new GiaoVienController();
        String[] columnNames = {
            "Mã GV", "Tên", "Email", "Ngày sinh", "Giới tính", "Trạng thái",
            "Địa chỉ", "SĐT","Vai Trò", "Bộ môn"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        jTable1.setModel(tableModel);
        loadTableData();
        jButton9.setVisible(false);
        VaiTroDAO vaiTroDAO = new VaiTroDAO();
        List<VaiTro> listVaiTro = vaiTroDAO.getAllVaiTro();
        jComboBox2.removeAllItems(); // Xóa cũ nếu có
        for (VaiTro vt : listVaiTro) {
        jComboBox2.addItem(vt.getMaVaiTro()); // ComboBox sẽ hiển thị Ten_vai_tro do toString() override
    }
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
    
    private void initializeForm() {
        setupPanelStyling();
        setupDateField();
    }
    private void loadTableData() {
    danhSachCache = controller.hienThiTatCaGiaoVien();
    tableModel.setRowCount(0); // clear bảng cũ

    for (GiaoVien gv : danhSachCache) {
        tableModel.addRow(new Object[] {
            gv.getMaNguoiDung(),
            gv.getTenNguoiDung(),
            gv.getEmail(),
            gv.getNgaySinh(),
            gv.isGioiTinh() ? "Nam" : "Nữ",
            gv.getTrangThai(),
            gv.getDiaChi(),
            gv.getSoDienThoai(),
            gv.getVaitro(),
            gv.getBoMon(),
        });
    }
}
    private void timKiemGiaoVien() {
    String keyword = jTextField19.getText().trim();
    List<GiaoVien> ketQua = controller.timKiemGiaoVien(keyword);
    tableModel.setRowCount(0);
    
    for (GiaoVien gv : ketQua) {
        tableModel.addRow(new Object[]{
            gv.getMaNguoiDung(),
            gv.getTenNguoiDung(),
            gv.getEmail(),
            gv.getNgaySinh(),
            gv.isGioiTinh() ? "Nam" : "Nữ",
            gv.getTrangThai(),
            gv.getDiaChi(),
            gv.getSoDienThoai(),
            gv.getBoMon(),
        });
    }
}
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        String maGV = tableModel.getValueAt(selectedRow, 0).toString();

        // Tìm đối tượng GiaoVien từ danhSachCache
        GiaoVien gv = danhSachCache.stream()
            .filter(g -> g.getMaNguoiDung().equals(maGV))
            .findFirst()
            .orElse(null);

        if (gv != null) {
            // Gán dữ liệu vào form
            jTextField14.setText(gv.getMaNguoiDung());
            jTextField20.setText(gv.getTenNguoiDung());
            jTextField16.setText(gv.getEmail());
            jTextField15.setText(gv.getSoDienThoai());
            jTextArea3.setText(gv.getDiaChi());
            jPasswordField1.setEnabled(false);
            jComboBox1.setSelectedItem(gv.isGioiTinh() ? "Nam" : "Nữ");
            jComboBox3.setSelectedItem(gv.getBoMon());
            jComboBox4.setSelectedItem(gv.getTrangThai());

            // Gán ngày sinh - SỬA LỖI TẠI ĐÂY
               try {
                Date date = gv.getNgaySinh(); // gv.getNgaySinh() đã là Date
                setSelectedDate(date);
            } catch (Exception e) {
                setSelectedDate(null);
            }

            // Gán ảnh - SỬA LỖI TẠI ĐÂY
          String base64 = gv.getAnhNguoiDung();
          if (base64 != null && !base64.isEmpty()) {
              try {
                  byte[] imageBytes = Base64.getDecoder().decode(base64);
                  ImageIcon icon = new ImageIcon(imageBytes);
                  Image img = icon.getImage().getScaledInstance(399, 360, Image.SCALE_SMOOTH);
                  jLabel22.setIcon(new ImageIcon(img));
                  jLabel23.setText("Đã có ảnh");
              } catch (Exception e) {
                  e.printStackTrace();
                  jLabel22.setIcon(null);
                  jLabel23.setText("Lỗi ảnh");
              }
          } else {
              jLabel22.setIcon(null);
              jLabel23.setText("Chưa có ảnh");
          }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu giáo viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    private void xoaGiaoVien() {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow >= 0) {
        String maGV = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa giáo viên có mã: " + maGV + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean thanhCong = controller.xoa(maGV);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTableData();  // Cập nhật lại bảng
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
}
    private void Save(){
    String maGV = jTextField14.getText().trim();
    String ten = jTextField20.getText().trim();
    String email = jTextField16.getText().trim();
    String sdt = jTextField15.getText().trim();
    String diaChi = jTextArea3.getText().trim();
    String gioiTinh = jComboBox1.getSelectedItem().toString();
    String trangThai = jComboBox4.getSelectedItem().toString();
    String boMon = jComboBox3.getSelectedItem().toString();
    String VaiTro = jComboBox2.getSelectedItem().toString().trim();
    
    String ngaySinhStr = jTextField17.getText().trim();
    Date ngaySinh = null;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        ngaySinh = sdf.parse(ngaySinhStr);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ. Vui lòng nhập đúng định dạng dd/MM/yyyy");
        return;
    }

    String ngaySinhDB = new SimpleDateFormat("yyyy-MM-dd").format(ngaySinh);

    GiaoVien gv = new GiaoVien(
    maGV,
    ten,
    email,
    ngaySinh, // đúng kiểu Date
    gioiTinh.equals("Nam"),
    trangThai,
    diaChi,
    sdt,
    boMon,
    new String(jPasswordField1.getPassword()), // mật khẩu từ jPasswordField
    VaiTro,
    selectedImageBase64 // giả sử bạn có lưu ảnh dạng base64 string
);
    if (isEditing) {
        controller.capNhat(gv);
        JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
    } else {
        controller.themMoi(gv);
        JOptionPane.showMessageDialog(this, "Thêm mới giáo viên thành công!");
    }

    loadTableData();
    lamMoi();
    }
        
    private void lamMoi() {
        if (currentDateChooser != null) {
    jPanel5.remove(currentDateChooser);
    currentDateChooser = null;
    isDatePickerVisible = false;
}
    jLabel21.setText("Thêm Người Dùng Mới");
    jPasswordField1.setEnabled(true);
    setupDateField();
    jTextField14.setText(dao.sinhMaNguoiDungMoi()); // Gen mã mới
    jTextField14.setEditable(false);
    jTextField20.setText("");
    jTextField16.setText("");
    jTextField15.setText("");
    jTextField17.setText("");
    jTextArea3.setText("");
    jPasswordField1.setText("");
    jComboBox1.setSelectedIndex(0);
    jComboBox3.setSelectedIndex(0);
    jComboBox4.setSelectedIndex(0);
    selectedImageBase64 = null;
    jLabel22.setIcon(null);
    jLabel23.setText("Chưa có ảnh");
    setSelectedDate(null);
    jButton9.setVisible(false);
}
    
    private void setupPanelStyling() {
        String arcStyle = "arc: 20";
        jPanel5.putClientProperty("FlatLaf.style", arcStyle);
        jPanel4.putClientProperty("FlatLaf.style", arcStyle);
        jTextField14.setText(dao.sinhMaNguoiDungMoi());
        jTextField14.setEditable(false);
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
            jPanel5.remove(jTextField17);
            jPanel5.add(dateField, new AbsoluteConstraints(470, 300, 400, 50));
            jTextField17 = dateField;
            
            showDatePicker();
            
            jPanel5.revalidate();
            jPanel5.repaint();
            
        } catch (ParseException e) {
            System.err.println("Error creating date formatter: " + e.getMessage());
            setupBasicDateField();
        }
    }
    

    
    private void setupBasicDateField() {
        jTextField17.setText("");
        jTextField17.setToolTipText("Nhập ngày theo định dạng dd/MM/yyyy");
        jTextField17.addFocusListener(new java.awt.event.FocusAdapter() {
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
        dateChooser.setPreferredSize(new Dimension(400, 50)); // Cập nhật kích thước
        dateChooser.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        Date currentDate = getCurrentDateFromField();
        dateChooser.setDate(currentDate != null ? currentDate : new Date());
        
        jTextField17.setVisible(false);
        jPanel5.add(dateChooser, new AbsoluteConstraints(470, 300, 400, 50)); // Vị trí đúng
        currentDateChooser = dateChooser;
        
        dateChooser.addPropertyChangeListener("date", evt -> {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                jTextField17.setText(sdf.format(selectedDate));
            }
        });
        
        jPanel5.revalidate();
        jPanel5.repaint();
        
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
            String currentText = jTextField17.getText();
            if (currentText != null && !currentText.trim().isEmpty() && 
                !currentText.equals("mm/dd/yyyy") && !currentText.equals("__/__/____")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                return sdf.parse(currentText);
            }
        } catch (ParseException e) {
            System.out.println("Could not parse current date: " + jTextField17.getText());
        }
        return null;
    }
    
    private void validateDateFormat() {
        String text = jTextField17.getText().trim();
        if (!text.isEmpty() && !text.equals("__/__/____")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date date = sdf.parse(text);
                jTextField17.setText(sdf.format(date));
            } catch (ParseException e) {
                showError("Định dạng ngày không hợp lệ!\nVui lòng nhập theo định dạng: dd/MM/yyyy\n\nVí dụ: 25/12/2023");
                jTextField17.requestFocus();
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
    if (currentDateChooser != null) {
        currentDateChooser.setDate(date);
    }

    if (jTextField17 instanceof JFormattedTextField) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            jTextField17.setText(sdf.format(date));
        } else {
            // Reset về mask ban đầu: __/__/____
            try {
                MaskFormatter formatter = new MaskFormatter("##/##/####");
                formatter.setPlaceholderCharacter('_');
                formatter.setValueContainsLiteralCharacters(true);
                formatter.install((JFormattedTextField) jTextField17);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            jTextField17.setText("");  // đảm bảo không mất định dạng
        }
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
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField19 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel29 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jTextField20 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        jTabbedPane1.setForeground(new java.awt.Color(128, 128, 128));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(221, 221, 221));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1310, 710));

        jTextField19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField19.setForeground(new java.awt.Color(128, 128, 128));
        jTextField19.setText("Tìm kiếm giáo viên ....");
        jTextField19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField19FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField19FocusLost(evt);
            }
        });
        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(5, 150, 105));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Chỉnh Sửa");
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(255, 51, 51));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Xóa");
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton11MouseClicked(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel31.setForeground(java.awt.Color.darkGray);
        jLabel31.setText("Danh sách giáo viên");

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
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1330, 720));

        jTabbedPane1.addTab("Xem Danh Sách", jPanel2);

        jPanel1.setBackground(new java.awt.Color(221, 221, 221));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(1310, 710));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setForeground(java.awt.Color.darkGray);
        jLabel21.setText("Thêm Người Dùng Mới");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 54, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray, 2));
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 92, 399, 360));

        jButton7.setBackground(new java.awt.Color(37, 99, 235));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Chọn Ảnh");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 493, 115, 49));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(java.awt.Color.gray);
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Đã có ảnh / Chưa có ảnh !");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 550, 200, -1));

        jTextField14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField14.setText("Genaration");
        jPanel5.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 400, 50));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(128, 128, 128));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Mã Số Người Dùng");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(473, 92, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(128, 128, 128));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Tên người dùng");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 400, 50));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(128, 128, 128));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Giới tính");
        jPanel5.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 270, -1, -1));

        jTextField17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField17.setText("dd/MM/yyyy");
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 400, 50));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(128, 128, 128));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Ngày Sinh");
        jPanel5.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 270, -1, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(128, 128, 128));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Bộ môn");
        jPanel5.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 360, -1, -1));

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jPanel5.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 570, 820, -1));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(128, 128, 128));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Địa chỉ");
        jPanel5.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, -1, -1));

        jButton8.setBackground(java.awt.Color.darkGray);
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Làm mới");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jPanel5.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 40, 115, 49));

        jTextField20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 210, 400, 50));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", "Khác" }));
        jPanel5.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 300, 400, 50));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(128, 128, 128));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Số Điện Thoại");
        jPanel5.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 360, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GVCN", "BGH" }));
        jPanel5.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 480, 400, 50));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(128, 128, 128));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Email");
        jPanel5.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 450, -1, -1));

        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, 400, 50));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(128, 128, 128));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Vai trò");
        jPanel5.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 450, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Load từ Bảng Môn Học" }));
        jPanel5.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 390, 400, 50));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(128, 128, 128));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Mật Khẩu");
        jPanel5.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 180, -1, -1));

        jPasswordField1.setText("jPasswordField1");
        jPanel5.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 210, 400, 50));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hoạt Động", "Bị Khóa", "Bị Chặn" }));
        jPanel5.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 120, 400, 50));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(128, 128, 128));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Trạng Thái");
        jPanel5.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, -1, -1));

        jButton12.setBackground(new java.awt.Color(37, 99, 235));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Lưu thông tin");
        jButton12.setPreferredSize(new java.awt.Dimension(92, 31));
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton12MouseClicked(evt);
            }
        });
        jPanel5.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 40, 130, 50));

        jButton9.setBackground(new java.awt.Color(37, 99, 235));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Đổi Mật Khẩu");
        jButton9.setPreferredSize(new java.awt.Dimension(92, 31));
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jPanel5.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 590, 140, 50));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1330, 720));

        jTabbedPane1.addTab("Thêm / Chỉnh Sửa", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        int selectedIndex = jTabbedPane1.getSelectedIndex();

    for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
        if (i == selectedIndex) {
            jTabbedPane1.setForegroundAt(i, new Color(37, 99, 235)); // đỏ tươi
        } else {
            jTabbedPane1.setForegroundAt(i, new Color(128, 128, 128)); // xám trung tính
        }
    }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
        timKiemGiaoVien();
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField19FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusGained
        // TODO add your handling code here:
        if (jTextField19.getText().trim().equals(PLACEHOLDER)) {
        jTextField19.setText("");
        jTextField19.setForeground(TEXT_COLOR);
    }
    }//GEN-LAST:event_jTextField19FocusGained

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusLost
        // TODO add your handling code here:
        if (jTextField19.getText().trim().isEmpty()) {
        jTextField19.setText(PLACEHOLDER);
        jTextField19.setForeground(PLACEHOLDER_COLOR);
    }
    }//GEN-LAST:event_jTextField19FocusLost

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        // TODO add your handling code here:
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        // gọi lại hàm đã có sẵn để đổ dữ liệu
        jTable1MouseClicked(null); // hoặc gọi extractFillFormFromRow(selectedRow);
        isEditing = true;
        jButton9.setVisible(true);
        jLabel21.setText("Chỉnh Sửa Người Dùng");
        jTabbedPane1.setSelectedIndex(1); // tab thứ 2 (index bắt đầu từ 0)
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_jButton10MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
    JFileChooser fileChooser = new JFileChooser();
    int returnValue = fileChooser.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
            byte[] imageBytes = java.nio.file.Files.readAllBytes(file.toPath());
            selectedImageBase64 = java.util.Base64.getEncoder().encodeToString(imageBytes);

            // Hiển thị ảnh đã chọn lên jLabel22
            ImageIcon icon = new ImageIcon(imageBytes);
            Image img = icon.getImage().getScaledInstance(399, 360, Image.SCALE_SMOOTH);
            jLabel22.setIcon(new ImageIcon(img));
            jLabel23.setText("Đã có ảnh");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đọc ảnh!");
        }
    }
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        // TODO add your handling code here:
        jPasswordField1.setEnabled(true);
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseClicked
        // TODO add your handling code here:
        xoaGiaoVien();
    }//GEN-LAST:event_jButton11MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        // TODO add your handling code here:
        lamMoi();
        isEditing = false;
    }//GEN-LAST:event_jButton8MouseClicked

    private void jButton12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseClicked
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_jButton12MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    // End of variables declaration//GEN-END:variables
}
