package Dao;

import Model.HocSinh;
import connect.XJdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HocSinhDAO {
    private static Connection conn;
     
    public HocSinhDAO(Connection conn) {
        this.conn = XJdbc.openConnection();
    }

    public List<HocSinh> selectAll() {
        List<HocSinh> list = new ArrayList<>();
        String sql = "SELECT * FROM HocSinh";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new HocSinh(
                    rs.getString("MaHocSinh"),
                    rs.getString("HoTen"),
                    rs.getDate("NgaySinh"),
                    rs.getBoolean("GioiTinh"),
                    rs.getString("Trang_thai"),
                    rs.getString("ThongTinPhuHuynh"),
                    rs.getString("MaLop"),
                    rs.getString("Anh")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(HocSinh hs) {
        String sql = "INSERT INTO HocSinh "
            + "(MaHocSinh, HoTen, NgaySinh, GioiTinh, Trang_thai, ThongTinPhuHuynh, MaLop) "
            + "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hs.getMaHocSinh());
            ps.setString(2, hs.getHoTen());
            ps.setDate(3, new java.sql.Date(hs.getNgaySinh().getTime()));
            ps.setBoolean(4, hs.isGioiTinh());
            ps.setString(5, hs.getTrangThai());
            ps.setString(6, hs.getThongTinPhuHuynh());
            ps.setString(7, hs.getMaLop());
            ps.setString(8, hs.getAnhnguoidung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(HocSinh hs) {
    // 1. Thêm Anh=? vào SET, tổng cộng 7 dấu ? ở SET + 1 ở WHERE = 8
    String sql = "UPDATE HocSinh SET "
        + "HoTen = ?, "
        + "NgaySinh = ?, "
        + "GioiTinh = ?, "
        + "Trang_thai = ?, "
        + "ThongTinPhuHuynh = ?, "
        + "MaLop = ?, "
        + "Anh = ? "
        + "WHERE MaHocSinh = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        // 2. Gán tham số đúng thứ tự
        ps.setString(1, hs.getHoTen());
        ps.setDate(2, new java.sql.Date(hs.getNgaySinh().getTime()));
        ps.setBoolean(3, hs.isGioiTinh());
        ps.setString(4, hs.getTrangThai());
        ps.setString(5, hs.getThongTinPhuHuynh());
        ps.setString(6, hs.getMaLop());
        // Nếu getAnhnguoidung() trả về byte[], dùng setBytes; 
        // nếu trả về Base64 string, dùng setString.
        ps.setString(7, hs.getAnhnguoidung());  
        ps.setString(8, hs.getMaHocSinh());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean delete(String maHocSinh) {
    String sqlDeleteScores = "DELETE FROM DiemSo WHERE MaHocSinh = ?";
    String sqlDeleteHS     = "DELETE FROM HocSinh WHERE MaHocSinh = ?";

    try {
        conn.setAutoCommit(false);  // bắt đầu transaction

        // 1. Xóa điểm liên quan
        try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteScores)) {
            ps1.setString(1, maHocSinh);
            ps1.executeUpdate();
        }

        // 2. Xóa bản ghi HocSinh
        int affected;
        try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteHS)) {
            ps2.setString(1, maHocSinh);
            affected = ps2.executeUpdate();
        }

        conn.commit();  // nếu cả hai bước thành công thì commit
        return affected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        try {
            conn.rollback();  // rollback khi có lỗi
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ignored) {}
    }
}
    public List<HocSinh> search(String keyword) {
        List<HocSinh> list = new ArrayList<>();
        String sql = "SELECT * FROM HocSinh "
                   + "WHERE MaHocSinh LIKE ? "
                   + "   OR HoTen      LIKE ? "
                   + "   OR MaLop      LIKE ?";
        String like = "%" + keyword + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new HocSinh(
                        rs.getString("MaHocSinh"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh"),
                        rs.getBoolean("GioiTinh"),
                        rs.getString("Trang_thai"),
                        rs.getString("ThongTinPhuHuynh"),
                        rs.getString("MaLop"),
                        rs.getString("Anh")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> selectAllMaLop() {
        List<String> ds = new ArrayList<>();
        String sql = "SELECT MaLop FROM LopHoc";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("MaLop"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    public String generateRandomMaHocSinh() {
        // Lấy về tất cả mã HS hiện có để kiểm tra trùng
        Set<String> existing = new HashSet<>();
        String sql = "SELECT MaHocSinh FROM HocSinh";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                existing.add(rs.getString("MaHocSinh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sinh cho đến khi không trùng
        Random rnd = new Random();
        String candidate;
        do {
            int num = rnd.nextInt(10_000);        // 0..9999
            candidate = String.format("HS%04d", num);
        } while (existing.contains(candidate));
        return candidate;
    }
}

