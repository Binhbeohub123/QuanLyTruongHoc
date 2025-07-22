/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Controller.InAnBaoCaoController;
import DAO.InAnBaoCaoDAO;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import java.awt.event.ItemEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
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
    private InAnBaoCaoController controller = new InAnBaoCaoController();
    
    public QuanLyInAnBaoCao_View() {
        setupTheme();
        initComponents();
        initializeForm();
        loadKhoi();
        setupSingleSelection(jCheckBox1, jCheckBox2, jCheckBox3, jCheckBox4, jCheckBox5);
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
     private void setupSingleSelection(JCheckBox... boxes) {
        for (JCheckBox cb : boxes) {
            cb.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // khi cb mới được tick, bỏ tick tất cả các box còn lại
                    for (JCheckBox other : boxes) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }
    }
     private boolean isAnyReportSelected() {
    return jCheckBox1.isSelected() || 
           jCheckBox2.isSelected() || 
           jCheckBox3.isSelected() || 
           jCheckBox4.isSelected() || 
           jCheckBox5.isSelected();
}
    public void inBaoCao(boolean chiXemTruoc) {
    String maLop = getSelectedMaLop();
    String hocKy = getHocKyDaChon();
    List<String> dsMaHocSinh = getDanhSachMaHocSinhTrenTable();
    List<String> dsMaHocSinhChon = getDanhSachHocSinhDuocChon();

    if (!isAnyReportSelected()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 loại báo cáo!");
        return;
    }

    // Kiểm tra riêng cho từng loại báo cáo (không bắt buộc lớp cho Cơ Sở Vật Chất)
    if ((jCheckBox1.isSelected() || jCheckBox2.isSelected() || jCheckBox4.isSelected()) && maLop == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp!");
        return;
    }

    if ((jCheckBox2.isSelected() || jCheckBox4.isSelected()) && hocKy == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn học kỳ!");
        return;
    }

    if (jCheckBox2.isSelected() && dsMaHocSinh.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Danh sách học sinh đang trống!");
        return;
    }

    if (jCheckBox4.isSelected() && dsMaHocSinhChon.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh trên bảng để in bảng điểm cá nhân!");
        return;
    }

    StringBuilder sb = new StringBuilder();

    // In Sĩ số lớp
    if (jCheckBox1.isSelected()) {
        sb.append("DANH SÁCH SĨ SỐ LỚP ").append(maLop).append("\n");
        sb.append("---------------------------------------------------\n");
        List<Object[]> dsSiSo = controller.loadSiSoLop(maLop);
        for (Object[] row : dsSiSo) {
            sb.append("Mã HS: ").append(row[0])
              .append(" | Họ Tên: ").append(row[1])
              .append(" | Ngày Sinh: ").append(row[2])
              .append(" | Giới Tính: ").append(row[3])
              .append(" | Trạng Thái: ").append(row[4]).append("\n");
        }
        sb.append("\n");
    }

    // In Bảng điểm lớp
    if (jCheckBox2.isSelected()) {
        sb.append("BẢNG ĐIỂM LỚP ").append(maLop).append(" - ").append(hocKy).append("\n");
        sb.append("---------------------------------------------------\n");
        List<Object[]> dsBangDiem = controller.loadBangDiemTheoDanhSachHocSinh(dsMaHocSinh, maLop, hocKy);
        if (dsBangDiem.isEmpty()) {
            sb.append("Không tìm thấy bảng điểm cho danh sách đã chọn.\n");
        } else {
            for (Object[] row : dsBangDiem) {
                sb.append("HS: ").append(row[0])
                  .append(" | Môn: ").append(row[1])
                  .append(" | 45P: ").append(row[2])
                  .append(" | Giữa Kỳ: ").append(row[3])
                  .append(" | Cuối Kỳ: ").append(row[4])
                  .append(" | TB: ").append(String.format("%.2f", row[5]))
                  .append(" | Học Lực: ").append(row[6])
                  .append(" | Mô Tả: ").append(row[7]).append("\n");
            }
        }
        sb.append("\n");
    }

    // In Bảng điểm cá nhân
    if (jCheckBox4.isSelected()) {
        sb.append("BẢNG ĐIỂM CÁ NHÂN\n");
        sb.append("---------------------------------------------------\n");
        for (String maHS : dsMaHocSinhChon) {
            sb.append("Mã HS: ").append(maHS).append("\n");
            List<Object[]> dsDiemCaNhan = controller.loadBangDiemCaNhan(maHS, hocKy);
            if (dsDiemCaNhan.isEmpty()) {
                sb.append("Không có dữ liệu.\n");
            } else {
                for (Object[] row : dsDiemCaNhan) {
                    sb.append("Môn: ").append(row[0])
                      .append(" | 45P: ").append(row[1])
                      .append(" | Giữa Kỳ: ").append(row[2])
                      .append(" | Cuối Kỳ: ").append(row[3])
                      .append(" | TB: ").append(String.format("%.2f", row[4]))
                      .append(" | Học Lực: ").append(row[5])
                      .append(" | Mô Tả: ").append(row[6]).append("\n");
                }
            }
            sb.append("\n");
        }
    }

    // In Cơ sở vật chất
    if (jCheckBox3.isSelected() || jCheckBox5.isSelected()) {
    sb.append("DANH SÁCH CƠ SỞ VẬT CHẤT");

    List<Object[]> dsTS = null;

    if (jCheckBox5.isSelected()) {
        maLop = getSelectedMaLop();
        if (maLop == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp để in theo lớp!");
            return;
        }

                String nienKhoa = (String) jComboBox17.getSelectedItem();
        if (nienKhoa == null || nienKhoa.equals("Chọn Niên Khóa")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn niên khóa!");
            return;
        }

        String maPhong = controller.getMaPhongHocTheoLopVaNienKhoa(maLop, nienKhoa);
        if (maPhong == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng học cho lớp: " + maLop);
            return;
        }

        sb.append(" - Theo Lớp ").append(maLop).append(" (Phòng: ").append(maPhong).append(")");
        dsTS = controller.getCSVCTheoPhong(maPhong);

    } else if (jCheckBox3.isSelected()) {
        sb.append(" - Toàn Trường");
        dsTS = controller.loadTatCaCoSoVatChat();
    }

    sb.append("\n---------------------------------------------------\n");

    if (dsTS == null || dsTS.isEmpty()) {
        sb.append("Không có dữ liệu tài sản.\n");
    } else {
        for (Object[] row : dsTS) {
            sb.append("Mã TS: ").append(row[0])
              .append(" | Tên TS: ").append(row[1])
              .append(" | Loại: ").append(row[2])
              .append(" | Tình Trạng: ").append(row[3])
              .append(" | Ngày Nhập: ").append(row[4]);

            // Với toàn trường (checkBox3), cần kiểm tra có cột Phòng không
            if (row.length > 5 && row[5] != null) {
                sb.append(" | Phòng: ").append(row[5]);
            }

            sb.append("\n");
        }
    }

    sb.append("\n");
}

    // Hiển thị hoặc xuất file
    if (chiXemTruoc) {
        jTextArea2.setText(sb.toString());
    } else {
        exportToFile(sb.toString());
    }
}
     public void xemTruocBaoCao() {
    String maLop = getSelectedMaLop();
    String hocKy = getHocKyDaChon();
    List<String> dsMaHocSinh = getDanhSachMaHocSinhTrenTable();
    List<String> dsMaHocSinhChon = getDanhSachHocSinhDuocChon();

    if (maLop == null || hocKy == null || dsMaHocSinh.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp, học kỳ và danh sách học sinh đầy đủ!");
        return;
    }

    if (!jCheckBox1.isSelected() && !jCheckBox2.isSelected() && !jCheckBox4.isSelected()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 loại báo cáo để xem trước!");
        return;
    }

    StringBuilder sb = new StringBuilder();

    // In Sĩ Số Lớp
    if (jCheckBox1.isSelected()) {
        sb.append("DANH SÁCH SĨ SỐ LỚP ").append(maLop).append("\n");
        sb.append("---------------------------------------------------\n");

        List<Object[]> dsSiSo = controller.loadSiSoLop(maLop);
        for (Object[] row : dsSiSo) {
            sb.append("Mã HS: ").append(row[0])
              .append(" | Họ Tên: ").append(row[1])
              .append(" | Ngày Sinh: ").append(row[2])
              .append(" | Giới Tính: ").append(row[3])
              .append(" | Trạng Thái: ").append(row[4])
              .append("\n");
        }
        sb.append("\n");
    }

    // In Bảng Điểm Cả Lớp
    if (jCheckBox2.isSelected()) {
        sb.append("BẢNG ĐIỂM LỚP ").append(maLop).append(" - ").append(hocKy).append("\n");
        sb.append("---------------------------------------------------\n");

        List<Object[]> dsBangDiem = controller.loadBangDiemTheoDanhSachHocSinh(dsMaHocSinh, maLop, hocKy);
        if (dsBangDiem.isEmpty()) {
            sb.append("Không tìm thấy bảng điểm cho danh sách đã chọn.\n");
        } else {
            for (Object[] row : dsBangDiem) {
                sb.append("HS: ").append(row[0])
                  .append(" | Môn: ").append(row[1])
                  .append(" | 45P: ").append(row[2])
                  .append(" | Giữa Kỳ: ").append(row[3])
                  .append(" | Cuối Kỳ: ").append(row[4])
                  .append(" | TB: ").append(String.format("%.2f", row[5]))
                  .append(" | Học Lực: ").append(row[6])
                  .append(" | Mô Tả: ").append(row[7])
                  .append("\n");
            }
        }
        sb.append("\n");
    }

    // In Bảng Điểm Cá Nhân
    if (jCheckBox4.isSelected()) {
        if (dsMaHocSinhChon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh trên bảng để in bảng điểm cá nhân!");
            return;
        }

        sb.append("BẢNG ĐIỂM CÁ NHÂN\n");
        sb.append("---------------------------------------------------\n");

        for (String maHS : dsMaHocSinhChon) {
            List<Object[]> dsDiemCaNhan = controller.loadBangDiemCaNhan(maHS, hocKy);
            sb.append("Mã HS: ").append(maHS).append("\n");

            if (dsDiemCaNhan.isEmpty()) {
                sb.append("Không có dữ liệu.\n");
            } else {
                for (Object[] row : dsDiemCaNhan) {
                    sb.append("Môn: ").append(row[0])
                      .append(" | 45P: ").append(row[1])
                      .append(" | Giữa Kỳ: ").append(row[2])
                      .append(" | Cuối Kỳ: ").append(row[3])
                      .append(" | TB: ").append(String.format("%.2f", row[4]))
                      .append(" | Học Lực: ").append(row[5])
                      .append(" | Mô Tả: ").append(row[6])
                      .append("\n");
                }
            }
            sb.append("\n");
        }
    }
if (jCheckBox5.isSelected()) {
    maLop = getSelectedMaLop();

    if (maLop == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp để in cơ sở vật chất theo lớp!");
        return;
    }

        String nienKhoa = (String) jComboBox17.getSelectedItem();
    if (nienKhoa == null || nienKhoa.equals("Chọn Niên Khóa")) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn niên khóa để in cơ sở vật chất theo lớp!");
        return;
    }

    String maPhong = controller.getMaPhongHocTheoLopVaNienKhoa(maLop, nienKhoa);
    if (maPhong == null) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy phòng học cho lớp: " + maLop);
        return;
    }

    sb.append("DANH SÁCH CƠ SỞ VẬT CHẤT CỦA PHÒNG ").append(maPhong).append("\n");
    sb.append("---------------------------------------------------\n");

    List<Object[]> dsTS = controller.getCSVCTheoPhong(maPhong);

    if (dsTS.isEmpty()) {
        sb.append("Không có dữ liệu tài sản.\n");
    } else {
        for (Object[] row : dsTS) {
            sb.append("Mã TS: ").append(row[0])
              .append(" | Tên TS: ").append(row[1])
              .append(" | Loại: ").append(row[2])
              .append(" | Tình Trạng: ").append(row[3])
              .append(" | Ngày Nhập: ").append(row[4])
              .append(" | Phòng: ").append(row[5])
              .append("\n");
        }
    }

    sb.append("\n");
}
    if (jCheckBox3.isSelected()) {
    sb.append("DANH SÁCH CƠ SỞ VẬT CHẤT TOÀN TRƯỜNG\n");
    sb.append("---------------------------------------------------\n");

    List<Object[]> dsTS = controller.loadTatCaCoSoVatChat();

    if (dsTS.isEmpty()) {
        sb.append("Không có dữ liệu tài sản.\n");
    } else {
        for (Object[] row : dsTS) {
            sb.append("Mã TS: ").append(row[0])
              .append(" | Tên TS: ").append(row[1])
              .append(" | Loại: ").append(row[2])
              .append(" | Tình Trạng: ").append(row[3])
              .append(" | Ngày Nhập: ").append(row[4])
              .append("\n");
        }
    }
    sb.append("\n");
}
}
    
     public void exportToFile(String content) {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Chọn thư mục để lưu file");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        File folder = chooser.getSelectedFile();
        String fileName = "BaoCao_" + LocalDate.now() + ".csv";
        File file = new File(folder, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
            fos.write(bom);
            try (OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter bw = new BufferedWriter(osw)) {
                bw.write(content);
            }
            JOptionPane.showMessageDialog(this, "Đã in thành công tại: \n" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in file!");
        }
    }
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

    modelKhoi.addElement("Chọn Khối");

    for (Integer khoi : dsKhoi) {
        modelKhoi.addElement(String.valueOf(khoi));
    }

    jComboBox13.setModel(modelKhoi);

    // Sự kiện chọn khối
    jComboBox13.addActionListener(e -> {
        String khoiChon = (String) jComboBox13.getSelectedItem();
        if (khoiChon == null || khoiChon.equals("Chọn Khối")) {
            jComboBox17.setModel(new DefaultComboBoxModel<>(new String[]{"Chọn Niên Khóa"}));
            jComboBox14.setModel(new DefaultComboBoxModel<>(new String[]{"Chọn Lớp"}));
            return;
        }

        loadNienKhoaTheoKhoi(khoiChon);
    });
} // Lưu ánh xạ TenLop -> MaLop

private void loadLopTheoKhoiVaNienKhoa(String khoi, String nienKhoa) {
    dsLop = dao.loadLopTheoKhoiVaNienKhoa(khoi, nienKhoa);

    DefaultComboBoxModel<String> modelLop = new DefaultComboBoxModel<>();
    modelLop.addElement("Chọn Lớp");

    for (String tenLop : dsLop.keySet()) {
        modelLop.addElement(tenLop);
    }

    jComboBox14.setModel(modelLop);
}
private void loadNienKhoaTheoKhoi(String khoi) {
    List<String> dsNienKhoa = dao.loadNienKhoaTheoKhoi(khoi);

    DefaultComboBoxModel<String> modelNienKhoa = new DefaultComboBoxModel<>();
    modelNienKhoa.addElement("Chọn Niên Khóa");

    for (String nk : dsNienKhoa) {
        modelNienKhoa.addElement(nk);
    }

    jComboBox17.setModel(modelNienKhoa);

    // Sự kiện chọn niên khóa
    jComboBox17.addActionListener(e -> {
        String nienKhoaChon = (String) jComboBox17.getSelectedItem();
        if (nienKhoaChon == null || nienKhoaChon.equals("Chọn Niên Khóa")) {
            jComboBox14.setModel(new DefaultComboBoxModel<>(new String[]{"Chọn Lớp"}));
            return;
        }

        loadLopTheoKhoiVaNienKhoa(khoi, nienKhoaChon);
    });
}

private String getSelectedMaLop() {
    String tenLopChon = (String) jComboBox14.getSelectedItem();
    if (tenLopChon == null || tenLopChon.equals("Chọn Lớp")) {
        return null; // Không hợp lệ
    }

    return dsLop.get(tenLopChon);
}
private void loadHocSinhLenTable(String maLop) {
    List<Object[]> dsHocSinh = controller.loadHocSinhTheoLop(maLop);

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
public List<String> getDanhSachHocSinhDuocChon() {
    List<String> dsMaHocSinh = new ArrayList<>();
    int[] selectedRows = jTable1.getSelectedRows();

    for (int row : selectedRows) {
        String maHS = (String) jTable1.getValueAt(row, 0); // Cột 0 là Mã HS
        dsMaHocSinh.add(maHS);
    }

    return dsMaHocSinh;
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
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jComboBox15 = new javax.swing.JComboBox<>();
        jComboBox17 = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1670, 940));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
        jCheckBox3.setText("Danh Sách Cơ Sở Vật Chất Toàn Bộ");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 290, -1));

        jCheckBox4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox4.setForeground(java.awt.Color.darkGray);
        jCheckBox4.setText("In Bảng Điểm Cá Nhân");
        jPanel2.add(jCheckBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 180, -1));

        jCheckBox5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox5.setForeground(java.awt.Color.darkGray);
        jCheckBox5.setText("Danh Sách Cơ Sở Vật Chất Theo Lớp");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 290, -1));

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
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

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

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea2.setForeground(java.awt.Color.darkGray);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jComboBox13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox13.setForeground(java.awt.Color.darkGray);
        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Khối" }));

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

        jComboBox15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox15.setForeground(java.awt.Color.darkGray);
        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Học Kỳ", "Học Kỳ I", "Học Kỳ II" }));

        jComboBox17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox17.setForeground(java.awt.Color.darkGray);
        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Niên Khóa" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 42, Short.MAX_VALUE)
                                .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
        inBaoCao(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        inBaoCao(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
