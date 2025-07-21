/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tool;

import Dao.HocSinhDAO;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import javax.swing.UIManager;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import Controller.HocSinhController;
import Model.GiaoVien;
import Model.HocSinh;
import java.awt.Image;
import java.io.File;
import java.util.Base64;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author acchi
 */
public class QuanLyHocSinh_View extends javax.swing.JPanel {
    
    private boolean isDatePickerVisible = false;
    private JDateChooser currentDateChooser = null;
    private HocSinhDAO lopDAO = new HocSinhDAO();
    private HocSinhController controller = new HocSinhController();
    private List<HocSinh> danhSachCache;
    private DefaultTableModel tableModel;
    private boolean isEditing = false;
    private String selectedImageBase64 = null;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.BLACK;
    /**
     * Creates new form QuanLyHocSinh_View
     */
    public QuanLyHocSinh_View() {
        setupTheme();
        initComponents();
        initializeForm();
        String[] columnNames = {
    "Mã HS",
    "Họ và Tên",
    "Ngày Sinh",
    "Giới Tính",
    "Trạng Thái",
    "Tên Cha",
    "SĐT Cha",
    "Tên Mẹ",
    "SĐT Mẹ",
    "Mã Lớp",
};
        tableModel = new DefaultTableModel(columnNames, 0);
        jTable1.setModel(tableModel);
        loadTable();
        currentDateChooser.setDate(new Date());
        khoaTable(jTable1);
        jTextField19.putClientProperty("JTextField.placeholderText", "Tìm Kiếm Học Sinh ....");
        jTextField19.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        timKiemHocSinh();
    }
    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        timKiemHocSinh();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        timKiemHocSinh();
    }
});
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

// Hàm lấy tên cột từ DefaultTableModel

private Vector<String> getColumnNames(DefaultTableModel model) {
    Vector<String> columnNames = new Vector<>();
    for (int i = 0; i < model.getColumnCount(); i++) {
        columnNames.add(model.getColumnName(i));
    }
    return columnNames;
}
     private int tinhTuoi(Date ngaySinh) {
    Calendar birth = Calendar.getInstance();
    birth.setTime(ngaySinh);

    Calendar today = Calendar.getInstance();

    int tuoi = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

    // Nếu tháng sinh chưa đến hoặc ngày sinh chưa đến trong năm nay thì trừ đi 1
    if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
        tuoi--;
    }

    return tuoi;
}
    private void setupTheme() {
        try {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 16);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("Button.arc", 16);
            UIManager.put("Panel.arc", 16);
            UIManager.put("TextArea.arc", 20);
            UIManager.put("ScrollPane.arc", 20);
        } catch (Exception e) {
            System.err.println("Error setting up theme: " + e.getMessage());
        }
    }
    
    private void initializeForm() {
        setupDateField();
        setupPanelStyling();
        loadCombobox();
        jTextField16.setText(controller.sinhMaHocSinhMoi());
        jTextField16.setEditable(false);
    }
    private void loadCombobox() {
    jComboBox2.removeAllItems();
    List<String> dsLop = lopDAO.selectAllMaLop();
    for (String ma : dsLop) {
        jComboBox2.addItem(ma);
    }
}
    public void loadTable() {
    danhSachCache = controller.hienThiTatCaHocSinh();
    tableModel.setRowCount(0);
    for (HocSinh hs : danhSachCache) {
        tableModel.addRow(new Object[]{
            hs.getMaHocSinh(),
            hs.getHoTen(),
            hs.getNgaySinh(),
            hs.isGioiTinh() ? "Nam" : "Nữ",
            hs.getTrangThai(),
            hs.getTenCha(),
            hs.getSdtCha(),
            hs.getTenMe(),
            hs.getSdtMe(),
            hs.getMaLop()
        });
    }
}

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        // 1. Lấy mã HS từ tableModel
        String maHS = tableModel.getValueAt(selectedRow, 0).toString();

        // 2. Tìm đối tượng HocSinh trong danhSachCache
        HocSinh hs = danhSachCache.stream()
            .filter(h -> h.getMaHocSinh().equals(maHS))
            .findFirst()
            .orElse(null);

        if (hs != null) {
            // 3. Đổ dữ liệu lên form
            jTextField16.setText(hs.getMaHocSinh());
            jTextField15.setText(hs.getHoTen());

            // Ngày sinh
            try {
                Date date = hs.getNgaySinh(); // gv.getNgaySinh() đã là Date
                setSelectedDate(date);
            } catch (Exception e) {
                setSelectedDate(null);
            }

            // Giới tính, Trạng thái, Mã lớp, Thông tin PH
            jComboBox1.setSelectedItem(hs.isGioiTinh() ? "Nam" : "Nữ");
            jComboBox3.setSelectedItem(hs.getTrangThai());
            jComboBox2.setSelectedItem(hs.getMaLop());
            jTextField18.setText(hs.getTenCha());
            jTextField21.setText(hs.getSdtCha());
            jTextField20.setText(hs.getTenMe());
            jTextField22.setText(hs.getSdtMe());

            String base64 = hs.getAnhnguoidung();
            if (base64 != null && !base64.isEmpty()) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(base64);
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(399, 360, Image.SCALE_SMOOTH);
                    jLabel22.setIcon(new ImageIcon(img));
                    jLabel23.setText("Đã có ảnh");

                    // Giữ ảnh cũ để khi không chọn ảnh mới thì vẫn lưu lại
                    selectedImageBase64 = base64;

                } catch (Exception e) {
                    e.printStackTrace();
                    jLabel22.setIcon(null);
                    jLabel23.setText("Lỗi ảnh");
                    selectedImageBase64 = null; // Tránh bị nullPointer khi ảnh lỗi
                }
            } else {
                jLabel22.setIcon(null);
                jLabel23.setText("Chưa có ảnh");
                selectedImageBase64 = null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu học sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void setupPanelStyling() {
        String arcStyle = "arc: 20";
        jPanel5.putClientProperty("FlatLaf.style", arcStyle);
        jPanel4.putClientProperty("FlatLaf.style", arcStyle);
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
            String formattedDate = sdf.format(date);
            jTextField17.setText(formattedDate);
        } else {
            jTextField17.setText("");
        }
    }
}
    private void timKiemHocSinh() {
    String keyword = jTextField19.getText().trim();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    jTable1.setRowSorter(sorter);

    if (keyword.isEmpty() || keyword.equals("Tìm kiếm học sinh ....")) {
        sorter.setRowFilter(null); // Xóa filter => Hiện tất cả
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }
}
    private void Save() {
    String maHS = jTextField16.getText().trim();
    String ten = jTextField15.getText().trim();
    String gioiTinh = jComboBox1.getSelectedItem().toString();
    String trangThai = jComboBox3.getSelectedItem().toString();
    String tencha = jTextField18.getText().trim();
    String sdtcha = jTextField21.getText().trim();
    String tenme = jTextField20.getText().trim();
    String sdtme = jTextField22.getText().trim();
    String lopHoc = jComboBox2.getSelectedItem().toString();

    
    Date ngaySinh = getCurrentDateFromField();
    if (ngaySinh == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày sinh hợp lệ", "Lỗi", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int tuoi = tinhTuoi(ngaySinh);
    if (tuoi < 15 || tuoi > 50) {
        JOptionPane.showMessageDialog(this, "Tuổi học sinh phải từ 15 đến 50", "Lỗi", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Kiểm tra tên học sinh có rỗng không
    if (ten.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập tên học sinh", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validate phụ huynh: ít nhất 1 tên cha/mẹ và 1 số điện thoại cha/mẹ
    boolean coTenPhuHuynh = !tencha.isEmpty() || !tenme.isEmpty();
    boolean coSDTPhuHuynh = !sdtcha.isEmpty() || !sdtme.isEmpty();

    if (!coTenPhuHuynh || !coSDTPhuHuynh) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất 1 tên phụ huynh và 1 số điện thoại phụ huynh!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Kiểm tra tên có chứa ký tự đặc biệt
    if (!ten.matches("^[\\p{L}\\s\\-\\.]+$")) {
        JOptionPane.showMessageDialog(this, "Tên học sinh không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!tencha.isEmpty() && !tencha.matches("^[\\p{L}\\s\\-\\.]+$")) {
    JOptionPane.showMessageDialog(this, "Tên cha không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.WARNING_MESSAGE);
    return;
}

if (!tenme.isEmpty() && !tenme.matches("^[\\p{L}\\s\\-\\.]+$")) {
    JOptionPane.showMessageDialog(this, "Tên mẹ không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.WARNING_MESSAGE);
    return;
}
if (!sdtcha.isEmpty() && !sdtcha.matches("^0[0-9]{9}$")) {
    JOptionPane.showMessageDialog(this, "Số điện thoại cha phải đủ 10 số và bắt đầu bằng 0!", "Lỗi", JOptionPane.WARNING_MESSAGE);
    return;
}

if (!sdtme.isEmpty() && !sdtme.matches("^0[0-9]{9}$")) {
    JOptionPane.showMessageDialog(this, "Số điện thoại mẹ phải đủ 10 số và bắt đầu bằng 0!", "Lỗi", JOptionPane.WARNING_MESSAGE);
    return;
}
if (sdtcha != null && sdtcha.equals(sdtme)) {
    JOptionPane.showMessageDialog(
        this,
        "Số điện thoại cha và mẹ không được trùng với nhau",
        "Lỗi",
        JOptionPane.WARNING_MESSAGE
    );
    return;
}

    // Tạo đối tượng Học Sinh
    HocSinh hs = new HocSinh(maHS, ten, ngaySinh, gioiTinh.equals("Nam"), trangThai, lopHoc, selectedImageBase64,tencha, sdtcha, tenme, sdtme);

 
    if (isEditing) {
    controller.capNhat(hs);
    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
    
} else {
    controller.Themmoi(hs);
JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
    }
    lamMoi();
    loadTable();
    
    jTable1.setModel(tableModel);   // đảm bảo jTable1 đang xài đúng tableModel vừa load
    khoaTable(jTable1);  
}
    

private void lamMoi() {
    isEditing = false; 
    // Reset date picker
    if (currentDateChooser != null) {
        jPanel5.remove(currentDateChooser);
        currentDateChooser = null;
        isDatePickerVisible = false;
    }

    // Gán tiêu đề
    jLabel21.setText("Thêm Học Sinh Mới");
    setupDateField();
    jTextField16.setText(controller.sinhMaHocSinhMoi());
    jTextField16.setEditable(false);

    // Clear các trường thông tin học sinh
    jTextField15.setText(""); // Tên học sinh
    jTextField17.setText(""); // Nếu là ô khác ví dụ địa chỉ (tuỳ cấu trúc của bạn)

    jComboBox1.setSelectedIndex(0);
    jComboBox2.setSelectedIndex(0);
    jComboBox3.setSelectedIndex(0);
     // Lớp học

    // Clear thông tin phụ huynh
    jTextField18.setText(""); // Tên cha
    jTextField21.setText(""); // SĐT cha
    jTextField20.setText(""); // Tên mẹ
    jTextField22.setText(""); // SĐT mẹ
    
    if (jTable1.getRowSorter() != null) {
        jTable1.setRowSorter(null);
    }
    // Reset ảnh
    selectedImageBase64 = null;
    jLabel22.setIcon(null);
    jLabel23.setText("Chưa có ảnh");

    // Reset ngày sinh
    setSelectedDate(null);

    // Reset trạng thái
}
private void xoaHocSinh() {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow >= 0) {
        String maHS = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa học sinh có mã: " + maHS + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean thanhCong = controller.xoa(maHS); // hoặc controller.xoaHocSinh(maHS)
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTable();  // hoặc loadTableData(), tuỳ tên method của bạn
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(
            this,
            "Vui lòng chọn một học sinh để xóa!",
            "Thông báo",
            JOptionPane.WARNING_MESSAGE
        );
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
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();

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

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel31.setForeground(java.awt.Color.darkGray);
        jLabel31.setText("Danh sách học sinh");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1318, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jLabel21.setText("Thêm Học Sinh Mới");
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

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(128, 128, 128));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Mã số học sinh");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(473, 92, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(128, 128, 128));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Tên học sinh");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 210, 400, 50));

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

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(128, 128, 128));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Tên Lớp");
        jPanel5.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 360, -1, -1));

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

        jButton9.setBackground(new java.awt.Color(37, 99, 235));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Lưu thông tin");
        jButton9.setPreferredSize(new java.awt.Dimension(92, 31));
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jPanel5.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 40, 130, 50));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
        jPanel5.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 300, 400, 50));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Load từ Bảng LopHoc" }));
        jPanel5.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 820, 50));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang Học", "Đình Chỉ", "Chuyển Lớp", "Kết Thúc" }));
        jPanel5.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 210, 400, 50));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(128, 128, 128));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Trạng Thái");
        jPanel5.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 180, -1, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(java.awt.Color.gray);
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Đã có ảnh / Chưa có ảnh !");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 550, 200, -1));

        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 818, 50));

        jTextField18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, 400, 50));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(128, 128, 128));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Tên Cha");
        jPanel5.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 450, -1, -1));

        jTextField20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 480, 400, 50));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(128, 128, 128));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Tên Mẹ");
        jPanel5.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 450, -1, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(128, 128, 128));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("SĐT Cha");
        jPanel5.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, -1, -1));

        jTextField21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 570, 400, 50));

        jTextField22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 570, 400, 50));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(128, 128, 128));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("SĐT Mẹ");
        jPanel5.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 540, -1, -1));

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
        timKiemHocSinh();
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        // gọi lại hàm đã có sẵn để đổ dữ liệu
        jTable1MouseClicked(null); // hoặc gọi extractFillFormFromRow(selectedRow);
        isEditing = true;
        jButton9.setVisible(true);
        jLabel21.setText("Chỉnh Sửa Hồ Sơ Học Sinh");
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
        Save();
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        // TODO add your handling code here:
        lamMoi();
    }//GEN-LAST:event_jButton8MouseClicked

    private void jTextField19FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19FocusGained

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19FocusLost

    private void jButton11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseClicked
        // TODO add your handling code here:
        xoaHocSinh();
    }//GEN-LAST:event_jButton11MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    // End of variables declaration//GEN-END:variables
}
