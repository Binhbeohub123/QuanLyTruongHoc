/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import connect.XJdbc;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class InAnBaoCaoDAO {
    
    private Connection conn;

    public InAnBaoCaoDAO() {
        this.conn = XJdbc.openConnection();
    }
    
    public List<Integer> loadKhoi() {
    List<Integer> dsKhoi = new ArrayList<>();

    try {
        String sql = "SELECT DISTINCT Khoi FROM LopHoc ORDER BY Khoi";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            dsKhoi.add(rs.getInt("Khoi"));
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load khối!");
    }

    return dsKhoi;
}
    public Map<String, String> loadLopTheoKhoi(int khoi) {
    Map<String, String> dsLop = new LinkedHashMap<>();

    try {
        String sql = "SELECT MaLop, TenLop FROM LopHoc WHERE Khoi = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, khoi);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            dsLop.put(rs.getString("TenLop"), rs.getString("MaLop"));
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load lớp theo khối!");
    }

    return dsLop;
}
    public List<Object[]> loadHocSinhTheoLop(String maLop) {
    List<Object[]> dsHocSinh = new ArrayList<>();

    try {
        String sql = "SELECT MaHocSinh, HoTen, NgaySinh, GioiTinh, Trang_thai FROM HocSinh WHERE MaLop = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maLop);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[5];
            row[0] = rs.getString("MaHocSinh");
            row[1] = rs.getString("HoTen");
            row[2] = rs.getDate("NgaySinh");
            row[3] = rs.getInt("GioiTinh") == 1 ? "Nam" : "Nữ";
            row[4] = rs.getString("Trang_thai");

            dsHocSinh.add(row);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load học sinh!");
    }

    return dsHocSinh;
}
    public List<Object[]> loadBangDiemTheoDanhSachHocSinh(List<String> dsMaHocSinh, String maLop, String hocKy) {
    List<Object[]> dsDiem = new ArrayList<>();

    if (dsMaHocSinh == null || dsMaHocSinh.isEmpty()) {
        return dsDiem; // Tránh lỗi nếu danh sách rỗng
    }

    try {
        // Xây dựng câu SQL động với số lượng dấu hỏi tương ứng
        String placeholders = String.join(",", java.util.Collections.nCopies(dsMaHocSinh.size(), "?"));
        String sql = "SELECT MaHocSinh, MaMonHoc, diem45Phut, diemGiuaKy, diemCuoiKy, diemTrungBinh, hocLuc, MoTa "
                   + "FROM DiemSo "
                   + "WHERE MaHocSinh IN (" + placeholders + ") "
                   + "AND MaHocSinh IN (SELECT MaHocSinh FROM HocSinh WHERE MaLop = ?) "
                   + "AND MaHocKy = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        int index = 1;
        for (String maHS : dsMaHocSinh) {
            ps.setString(index++, maHS);
        }

        ps.setString(index++, maLop);
        ps.setString(index, hocKy);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[10];
            row[0] = rs.getString("MaHocSinh");
            row[1] = rs.getString("MaMonHoc");
            row[2] = rs.getFloat("diem45Phut");
            row[3] = rs.getFloat("diemGiuaKy");
            row[4] = rs.getFloat("diemCuoiKy");
            row[5] = rs.getFloat("diemTrungBinh");
            row[6] = rs.getString("hocLuc");
            row[7] = rs.getString("MoTa");

            dsDiem.add(row);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load bảng điểm!");
    }

    return dsDiem;
}
    public List<Object[]> loadBangDiemCaNhan(String maHocSinh, String hocKy) {
    List<Object[]> dsDiem = new ArrayList<>();

    try {
        String sql = "SELECT MaMonHoc, diem45Phut, diemGiuaKy, diemCuoiKy, diemTrungBinh, hocLuc, MoTa " +
                     "FROM DiemSo " +
                     "WHERE MaHocSinh = ? AND MaHocKy = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHocSinh);
        ps.setString(2, hocKy);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[7];
            row[0] = rs.getString("MaMonHoc");
            row[1] = rs.getFloat("diem45Phut");
            row[2] = rs.getFloat("diemGiuaKy");
            row[3] = rs.getFloat("diemCuoiKy");
            row[4] = rs.getFloat("diemTrungBinh");
            row[5] = rs.getString("hocLuc");
            row[6] = rs.getString("MoTa");

            dsDiem.add(row);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load bảng điểm cá nhân!");
    }

    return dsDiem;
}
public List<Object[]> loadSiSoLop(String maLop) {
    List<Object[]> dsHocSinh = new ArrayList<>();

    try {
        String sql = "SELECT MaHocSinh, HoTen, NgaySinh, GioiTinh, Trang_thai " +
                     "FROM HocSinh " +
                     "WHERE MaLop = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maLop);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[5];
            row[0] = rs.getString("MaHocSinh");
            row[1] = rs.getString("HoTen");
            row[2] = rs.getDate("NgaySinh");
            row[3] = rs.getInt("GioiTinh") == 1 ? "Nam" : "Nữ";
            row[4] = rs.getString("Trang_thai");

            dsHocSinh.add(row);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load sĩ số lớp!");
    }

    return dsHocSinh;
}
public List<Object[]> loadTatCaCoSoVatChat() {
    List<Object[]> list = new ArrayList<>();
    try {
        String sql = "SELECT * FROM CoSoVatChat";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[8];
            row[0] = rs.getString("MaTaiSan");
            row[1] = rs.getString("TenTaiSan");
            row[2] = rs.getString("LoaiTaiSan");
            row[3] = rs.getString("TinhTrang");
            row[4] = rs.getDate("NgayNhap");
            row[5] = rs.getString("Ma_Phong_hoc");
            row[6] = rs.getInt("ID");
            row[7] = rs.getString("MaLoaiTS");

            list.add(row);
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public List<Object[]> loadCoSoVatChatTheoLop(String maPhongHoc) {
    List<Object[]> list = new ArrayList<>();
    try {
        String sql = "SELECT * FROM CoSoVatChat WHERE Ma_Phong_hoc = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maPhongHoc);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[8];
            row[0] = rs.getString("MaTaiSan");
            row[1] = rs.getString("TenTaiSan");
            row[2] = rs.getString("LoaiTaiSan");
            row[3] = rs.getString("TinhTrang");
            row[4] = rs.getDate("NgayNhap");
            row[5] = rs.getString("Ma_Phong_hoc");
            row[6] = rs.getInt("ID");
            row[7] = rs.getString("MaLoaiTS");

            list.add(row);
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
}
