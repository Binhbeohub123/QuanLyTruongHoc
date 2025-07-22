/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Controller.QLLH_Controller;
import Entity.QLLH;
import Login.Login;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class panelQLLH extends javax.swing.JPanel {

    private QLLH_Controller controller = new QLLH_Controller();
    private DefaultTableModel tableModel;

    public panelQLLH() {
        initComponents();
        loadTable();
        loadGVCN();
        loadNamHoc();
        loadPhongHoc();
        updateMaVaTenLopTuDong();
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiemTatCa();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiemTatCa();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiemTatCa();
            }
        });

        cbbKhoiLop.addActionListener(e -> updateMaVaTenLopTuDong());
        cbbNamhoc.addActionListener(e -> updateMaVaTenLopTuDong());
    }
    private boolean isEditMode = false;

    private void timKiemTatCa() {
        String keyword = txtTimKiem.getText().trim();
        if (tableModel == null) {
            tableModel = (DefaultTableModel) tblData.getModel(); // fallback
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblData.setRowSorter(sorter);

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                String escaped = Pattern.quote(keyword); // escape ký tự đặc biệt
                List<RowFilter<Object, Object>> filters = new ArrayList<>();
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    filters.add(RowFilter.regexFilter("(?i)" + escaped, i)); // tìm ở tất cả cột
                }
                sorter.setRowFilter(RowFilter.orFilter(filters));
            } catch (PatternSyntaxException e) {
                sorter.setRowFilter(null);
                System.err.println("Regex lỗi: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Từ khóa chứa ký tự không hợp lệ!");
            }
        }
    }

    private void updateMaVaTenLopTuDong() {
        if (isEditMode) {
            return; // ✅ nếu đang chỉnh sửa thì bỏ qua
        }
        String khoi = cbbKhoiLop.getSelectedItem().toString();
        String namHoc = cbbNamhoc.getSelectedItem().toString();

        String tenLopMoi = controller.generateTenLop(khoi, namHoc);

        txtMaLop.setText(""); // ❌ không sinh mã lớp tự động
        txtMaLop.setEditable(true); // ✅ để người dùng tự nhập

        txtTenLop.setText(tenLopMoi != null ? tenLopMoi : "Hết slot tên lớp!");
    }

    private void loadTable() {
        // Hủy sorter tạm thời để tránh lỗi khi load lại
        tblData.setRowSorter(null);

        tableModel = getTableModel();
        tblData.setModel(tableModel);
        tblData.getTableHeader().setReorderingAllowed(false);

        for (QLLH lh : controller.getDanhSachLop()) {
            tableModel.addRow(new Object[]{
                lh.getMaLop(),
                lh.getTenLop(),
                lh.getKhoi(),
                lh.getNamHoc(),
                lh.getSiSo(),
                lh.getMaNguoiDung(),
                lh.getMaPhongHoc()
            });
        }

        // Gán lại sorter nếu cần
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblData.setRowSorter(sorter);
    }

    private void timKiemLop() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        DefaultTableModel model = getTableModel();
        tblData.setModel(model);

        for (QLLH lh : controller.getDanhSachLop()) {
            if (lh.getMaLop().toLowerCase().contains(keyword)
                    || lh.getTenLop().toLowerCase().contains(keyword)
                    || lh.getKhoi().toLowerCase().contains(keyword)
                    || lh.getNamHoc().toLowerCase().contains(keyword)
                    || String.valueOf(lh.getSiSo()).contains(keyword)
                    || lh.getMaNguoiDung().toLowerCase().contains(keyword)
                    || lh.getMaPhongHoc().toLowerCase().contains(keyword)) {

                model.addRow(new Object[]{
                    lh.getMaLop(),
                    lh.getTenLop(),
                    lh.getKhoi(),
                    lh.getNamHoc(),
                    lh.getSiSo(),
                    lh.getMaNguoiDung(),
                    lh.getMaPhongHoc()
                });
            }
        }
    }

    private DefaultTableModel getTableModel() {
        return new DefaultTableModel(
                new String[]{"Mã lớp", "Tên lớp", "Khối lớp", "Năm học", "Sĩ số lớp", "GVCN", "Tên phòng học"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void resetForm() {
        isEditMode = false;
        txtMaLop.setText("");
        txtTenLop.setText("");
        txtSiSo.setText("");
        txtTimKiem.setText("");
        cbbKhoiLop.setSelectedIndex(0);
        cbbNamhoc.setSelectedIndex(0);
        cbbGVCN.setSelectedIndex(0);
        cbbPhong.setSelectedIndex(0);
    }

    private void loadNamHoc() {
        List<String> list = controller.getAllNamHoc();
        cbbNamhoc.removeAllItems();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Bảng Năm học đang trống!");
            return;
        }
        for (String item : list) {
            cbbNamhoc.addItem(item);
        }
        cbbNamhoc.setSelectedIndex(0);
    }

    private void loadGVCN() {
        List<String> list = controller.getAllMaGVCN();
        cbbGVCN.removeAllItems();
        for (String item : list) {
            cbbGVCN.addItem(item);
        }
    }

    private void loadPhongHoc() {
        List<String> list = controller.getAllTenPhongHoc();
        cbbPhong.removeAllItems();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Bảng Phòng học đang trống!");
            return;
        }
        for (String item : list) {
            cbbPhong.addItem(item);
        }
        cbbPhong.setSelectedIndex(0);
    }

    private String getValueAtSafe(int row, int col) {
        Object val = tblData.getValueAt(row, col);
        return val != null ? val.toString() : "";
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
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnChinhsua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnlammoi = new javax.swing.JButton();
        btnthem = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMaLop = new javax.swing.JTextField();
        cbbKhoiLop = new javax.swing.JComboBox<>();
        txtTenLop = new javax.swing.JTextField();
        txtSiSo = new javax.swing.JTextField();
        txtCapNhat = new javax.swing.JButton();
        cbbGVCN = new javax.swing.JComboBox<>();
        btnXemDanhSach = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cbbPhong = new javax.swing.JComboBox<>();
        cbbNamhoc = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(1670, 930));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("DANH SÁCH THÔNG TIN LỚP HỌC");

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã lớp", "Tên lớp", "Khối lớp", "Năm học", "Sĩ số lớp", "GVCN", "Mã phòng học"
            }
        ));
        jScrollPane1.setViewportView(tblData);

        btnChinhsua.setBackground(new java.awt.Color(0, 102, 255));
        btnChinhsua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChinhsua.setForeground(new java.awt.Color(255, 255, 255));
        btnChinhsua.setText("Chỉnh sửa ");
        btnChinhsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChinhsuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 102, 255));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Tìm kiếm:");

        btnlammoi.setBackground(new java.awt.Color(0, 102, 255));
        btnlammoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnlammoi.setForeground(new java.awt.Color(255, 255, 255));
        btnlammoi.setText("Làm mới");
        btnlammoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlammoiActionPerformed(evt);
            }
        });

        btnthem.setBackground(new java.awt.Color(0, 102, 255));
        btnthem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnthem.setForeground(new java.awt.Color(255, 255, 255));
        btnthem.setText("Thêm");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 914, Short.MAX_VALUE)
                        .addComponent(btnthem)
                        .addGap(18, 18, 18)
                        .addComponent(btnChinhsua)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa)
                        .addGap(18, 18, 18)
                        .addComponent(btnlammoi))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnChinhsua, btnXoa, btnlammoi, btnthem});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChinhsua)
                    .addComponent(btnXoa)
                    .addComponent(btnlammoi)
                    .addComponent(btnthem))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnChinhsua, btnXoa, btnlammoi, btnthem});

        jTabbedPane1.addTab("Danh Sách Thông tin lớp học", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("CẬP NHẬT THÔNG TIN LỚP HỌC");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Mã lớp:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Tên lớp:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Sĩ số:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Khối lớp:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Năm học:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("GVCN:");

        cbbKhoiLop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "11", "12" }));
        cbbKhoiLop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbKhoiLopActionPerformed(evt);
            }
        });

        txtTenLop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenLopActionPerformed(evt);
            }
        });

        txtCapNhat.setBackground(new java.awt.Color(0, 102, 255));
        txtCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCapNhat.setForeground(new java.awt.Color(255, 255, 255));
        txtCapNhat.setText("Cập nhật ");
        txtCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapNhatActionPerformed(evt);
            }
        });

        cbbGVCN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbGVCNActionPerformed(evt);
            }
        });

        btnXemDanhSach.setBackground(new java.awt.Color(0, 102, 255));
        btnXemDanhSach.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXemDanhSach.setForeground(new java.awt.Color(255, 255, 255));
        btnXemDanhSach.setText("Xem danh sách");
        btnXemDanhSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemDanhSachActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(0, 102, 255));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Mã phòng:");

        cbbPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbPhongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(43, 43, 43)
                                .addComponent(cbbNamhoc, 0, 202, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(34, 34, 34)
                                .addComponent(cbbPhong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7))
                                .addGap(48, 48, 48)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSiSo, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                    .addComponent(txtTenLop, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                    .addComponent(txtMaLop)
                                    .addComponent(cbbGVCN, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbbKhoiLop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXemDanhSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCapNhat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(1179, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTenLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCapNhat))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbbKhoiLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXemDanhSach))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSiSo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbbGVCN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbbNamhoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbbPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(529, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cập nhật thông tin lớp học", jPanel2);

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

    private void btnChinhsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChinhsuaActionPerformed
        int row = tblData.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần chỉnh sửa!");
            return;
        }

        try {
            isEditMode = true; // ✅ Gán cờ chỉnh sửa

            String maLop = getValueAtSafe(row, 0);
            String tenLop = getValueAtSafe(row, 1);
            String khoi = getValueAtSafe(row, 2);
            String namHoc = getValueAtSafe(row, 3);
            String siSo = getValueAtSafe(row, 4);
            String gvcn = getValueAtSafe(row, 5);
            String phong = getValueAtSafe(row, 6);

            txtMaLop.setText(maLop);
            txtTenLop.setText(tenLop);
            cbbKhoiLop.setSelectedItem(khoi);
            cbbNamhoc.setSelectedItem(namHoc);
            txtSiSo.setText(siSo);
            cbbGVCN.setSelectedItem(gvcn);
            String tenPhongHoc = controller.getTenPhongHocByMa(phong); 
            cbbPhong.setSelectedItem(tenPhongHoc);

            txtTenLop.setEditable(true);
            txtMaLop.setEditable(true);

            jTabbedPane1.setSelectedIndex(1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi chọn lớp: " + e.getMessage());
        }
        isEditMode = true;

    }//GEN-LAST:event_btnChinhsuaActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTenLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenLopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenLopActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            if (cbbNamhoc.getSelectedItem() == null
                    || cbbGVCN.getSelectedItem() == null
                    || cbbPhong.getSelectedItem() == null
                    || cbbKhoiLop.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ thông tin (năm học, GVCN, phòng học, khối lớp)!");
                return;
            }

            String maLop = txtMaLop.getText().trim();
            String tenLop = txtTenLop.getText().trim();
            if (maLop.isEmpty() || tenLop.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được để trống!");
                return;
            }

            if (!maLop.matches("^[a-zA-Z0-9]+$") || !tenLop.matches("^[a-zA-Z0-9]+$")) {
                JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được chứa ký tự đặc biệt!");
                return;
            }

            int siSo = Integer.parseInt(txtSiSo.getText().trim());
            String khoi = cbbKhoiLop.getSelectedItem().toString();
            String namHoc = cbbNamhoc.getSelectedItem().toString();
            String maNguoiDung = cbbGVCN.getSelectedItem().toString();
            String tenPhongHoc = cbbPhong.getSelectedItem().toString();
            String maPhongHoc = controller.getMaPhongHocByTen(tenPhongHoc); // Chuyển đổi TenPhongHoc -> Ma_Phong_hoc
            QLLH lop = new QLLH(maLop, tenLop, siSo, khoi, maNguoiDung, namHoc, maPhongHoc);
            String message = controller.addLopHocWithMessage(lop); // ⚠️ phải khai báo xong trước khi dùng

            JOptionPane.showMessageDialog(this, message);
            if (message.startsWith("✅")) {
                loadTable();    // ✅ Load lại bảng khi thành công
                resetForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sĩ số phải là số nguyên dương!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

    }//GEN-LAST:event_btnThemActionPerformed

    private void cbbKhoiLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbKhoiLopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbKhoiLopActionPerformed

    private void cbbPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbPhongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbPhongActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        btnThem.setEnabled(false);
        int row = tblData.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần xóa!");
            return;
        }

        String maLop = tblData.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa lớp " + maLop + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.deleteLopHoc(maLop);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Xóa lớp thành công!");
                loadTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!");
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnXemDanhSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemDanhSachActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnXemDanhSachActionPerformed

    private void txtCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapNhatActionPerformed
        btnThem.setVisible(false);
        try {
            if (cbbNamhoc.getSelectedItem() == null || cbbGVCN.getSelectedItem() == null || cbbPhong.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn đủ thông tin (năm học, GVCN, phòng học)!");
                return;
            }

            String maLop = txtMaLop.getText().trim();
            String tenLop = txtTenLop.getText().trim();
            if (maLop.isEmpty() || tenLop.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được để trống!");
                return;
            }
            if (!maLop.matches("^[a-zA-Z0-9]+$") || !tenLop.matches("^[a-zA-Z0-9]+$")) {
                JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được chứa ký tự đặc biệt!");
                return;
            }

            String khoi = cbbKhoiLop.getSelectedItem().toString();
            String namHoc = cbbNamhoc.getSelectedItem().toString();
            int siSo = Integer.parseInt(txtSiSo.getText().trim());
            String maNguoiDung = cbbGVCN.getSelectedItem().toString();
            String tenPhongHoc = cbbPhong.getSelectedItem().toString();
            String maPhongHoc = controller.getMaPhongHocByTen(tenPhongHoc);
            QLLH lop = new QLLH(maLop, tenLop, siSo, khoi, maNguoiDung, namHoc, maPhongHoc);
            String message = controller.updateLopHocWithMessage(lop);

            JOptionPane.showMessageDialog(this, message);
            if (message.startsWith("✅")) {
                loadTable();
                resetForm();
                jTabbedPane1.setSelectedIndex(0);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠ Sĩ số phải là số nguyên!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_txtCapNhatActionPerformed

    private void cbbGVCNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbGVCNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbGVCNActionPerformed

    private void btnlammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlammoiActionPerformed
        loadTable();
        resetForm();
    }//GEN-LAST:event_btnlammoiActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        isEditMode = false; // ✅ đây là thao tác thêm mới
        resetForm();
        updateMaVaTenLopTuDong(); // chỉ gọi khi thêm
        txtTenLop.setEditable(true);
        txtMaLop.setEditable(true);
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnthemActionPerformed
    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("PasswordField.showCapsLock", true);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 10);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login dialog = new Login(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChinhsua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXemDanhSach;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnlammoi;
    private javax.swing.JButton btnthem;
    private javax.swing.JComboBox<String> cbbGVCN;
    private javax.swing.JComboBox<String> cbbKhoiLop;
    private javax.swing.JComboBox<String> cbbNamhoc;
    private javax.swing.JComboBox<String> cbbPhong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblData;
    private javax.swing.JButton txtCapNhat;
    private javax.swing.JTextField txtMaLop;
    private javax.swing.JTextField txtSiSo;
    private javax.swing.JTextField txtTenLop;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
