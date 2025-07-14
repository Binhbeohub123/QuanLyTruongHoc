package Dao;

import Model.GiaoVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connect.XJdbc;

public class GiaoVienDAO {
    private static Connection conn;

    public GiaoVienDAO(Connection conn) {
        this.conn = XJdbc.openConnection();
    }

    public List<GiaoVien> getAll() {
        List<GiaoVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NguoiDung";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GiaoVien gv = new GiaoVien();
                gv.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                gv.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                gv.setEmail(rs.getString("Email"));
                gv.setNgaySinh(rs.getDate("Ngay_thang_nam_sinh"));
                gv.setGioiTinh(rs.getBoolean("Gioi_tinh"));
                gv.setTrangThai(rs.getString("Trang_thai"));
                gv.setDiaChi(rs.getString("dia_chi"));
                gv.setBoMon(rs.getString("BoMon"));
                gv.setSoDienThoai(rs.getString("So_dien_thoai"));
                gv.setMatKhau(rs.getString("Mat_khau"));
                gv.setVaitro(rs.getString("Ma_vai_tro"));
                gv.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                list.add(gv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(GiaoVien gv) {
        String sql = "INSERT INTO NguoiDung (Ma_nguoi_dung, Ten_nguoi_dung, Email, Ngay_thang_nam_sinh, Gioi_tinh, Trang_thai, dia_chi, So_dien_thoai, BoMon, Ma_vai_tro, Mat_khau, Anh_nguoi_dung) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gv.getMaNguoiDung());
            ps.setString(2, gv.getTenNguoiDung());
            ps.setString(3, gv.getEmail());
            ps.setDate(4, new java.sql.Date(gv.getNgaySinh().getTime()));
            ps.setBoolean(5, gv.isGioiTinh());
            ps.setString(6, gv.getTrangThai());
            ps.setString(7, gv.getDiaChi());
            ps.setString(8, gv.getSoDienThoai());
            ps.setString(9, gv.getBoMon());
            ps.setString(10, gv.getVaitro());
            ps.setString(11, gv.getMatKhau());
            ps.setString(12, gv.getAnhNguoiDung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(GiaoVien gv) {
        String sql = "UPDATE NguoiDung SET Ten_nguoi_dung = ?, Email = ?, Ngay_thang_nam_sinh = ?, Gioi_tinh = ?, Trang_thai = ?, dia_chi = ?, So_dien_thoai = ?, BoMon = ?, Ma_vai_tro = ?, Mat_khau = ?, Anh_nguoi_dung = ? " +
             "WHERE Ma_nguoi_dung = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gv.getTenNguoiDung());
            ps.setString(2, gv.getEmail());
            ps.setDate(3, new java.sql.Date(gv.getNgaySinh().getTime()));
            ps.setBoolean(4, gv.isGioiTinh());
            ps.setString(5, gv.getTrangThai());
            ps.setString(6, gv.getDiaChi());
            ps.setString(7, gv.getSoDienThoai());
            ps.setString(8, gv.getBoMon());
            ps.setString(9, gv.getVaitro());
            ps.setString(10, gv.getMatKhau());
            ps.setString(11, gv.getAnhNguoiDung());
            ps.setString(12, gv.getMaNguoiDung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maNguoiDung) {
        String sql = "DELETE FROM NguoiDung WHERE Ma_nguoi_dung = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNguoiDung);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<GiaoVien> search(String keyword) {
    List<GiaoVien> list = new ArrayList<>();
    String sql = "SELECT * FROM NguoiDung WHERE " +
             "(Ma_nguoi_dung LIKE ? OR Ten_nguoi_dung LIKE ? OR Email LIKE ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        String search = "%" + keyword + "%";
        ps.setString(1, search);
        ps.setString(2, search);
        ps.setString(3, search);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GiaoVien gv = new GiaoVien();
            gv.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
            gv.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
            gv.setEmail(rs.getString("Email"));
            gv.setNgaySinh(rs.getDate("Ngay_thang_nam_sinh"));
            gv.setGioiTinh(rs.getBoolean("Gioi_tinh"));
            gv.setTrangThai(rs.getString("Trang_thai"));
            gv.setDiaChi(rs.getString("dia_chi"));
            gv.setBoMon(rs.getString("BoMon"));
            gv.setVaitro(rs.getString("Ma_Vai_Tro"));
            gv.setSoDienThoai(rs.getString("So_dien_thoai"));
            gv.setMatKhau(rs.getString("Mat_khau"));
            gv.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
            list.add(gv);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
public boolean capNhatMatKhau(String maNguoiDung, String matKhauMoi) {
    String sql = "UPDATE NguoiDung SET Mat_khau = ? WHERE Ma_nguoi_dung = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, matKhauMoi);
        stmt.setString(2, maNguoiDung);
        
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public String sinhMaNguoiDungMoi() {
    String prefix = "ND";
    String sql = "SELECT TOP 1 Ma_Nguoi_Dung FROM NguoiDung "
               + "WHERE Ma_Nguoi_Dung LIKE ? "
               + "ORDER BY CONVERT(INT, SUBSTRING(Ma_Nguoi_Dung, 3, LEN(Ma_Nguoi_Dung))) DESC";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, prefix + "%");
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString("Ma_Nguoi_Dung");
                int num = Integer.parseInt(last.substring(prefix.length()));
                return prefix + String.format("%03d", num + 1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return prefix + "001";
}
public String layMatKhauTheoMa(String maGV) {
    String sql = "SELECT Mat_Khau FROM NguoiDung WHERE Ma_Nguoi_Dung = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maGV);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("Mat_Khau");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
public String layAnhNguoiDung(String maNguoiDung) {
    String sql = "SELECT Anh_Nguoi_Dung FROM NguoiDung WHERE Ma_Nguoi_Dung = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maNguoiDung);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("Anh_Nguoi_Dung");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public List<String> getAllMaMonHoc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaMonHoc FROM MonHoc";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                list.add(rs.getString("MaMonHoc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
public boolean xoaGiaoVienVaCapNhatLopHoc(String maGiaoVien) {
    String sqlUpdateLopHoc = "UPDATE LopHoc SET Ma_Nguoi_Dung = NULL WHERE Ma_Nguoi_Dung = ?";
    String sqlDeleteGiaoVien = "DELETE FROM NguoiDung WHERE Ma_Nguoi_Dung = ?";

    Connection conn = null;
    PreparedStatement stmtLop = null;
    PreparedStatement stmtGV = null;

    try {
        conn.setAutoCommit(false); // Bắt đầu transaction

        // Cập nhật LopHoc: chuyển về chưa có giáo viên
        stmtLop = conn.prepareStatement(sqlUpdateLopHoc);
        stmtLop.setString(1, maGiaoVien);
        stmtLop.executeUpdate();

        // Xóa GiaoVien
        stmtGV = conn.prepareStatement(sqlDeleteGiaoVien);
        stmtGV.setString(1, maGiaoVien);
        stmtGV.executeUpdate();

        conn.commit(); // Thành công
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        try {
            if (conn != null) conn.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    } finally {
        try {
            if (stmtLop != null) stmtLop.close();
            if (stmtGV != null) stmtGV.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
public void giaLapChuaCoGiaoVien(String maGiaoVien) {
    String sql = "UPDATE LopHoc SET Ma_Nguoi_Dung = NULL WHERE Ma_Nguoi_Dung = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, maGiaoVien);
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public List<String> layDanhSachLopChuNhiem(String maGiaoVien) {
    List<String> danhSachLop = new ArrayList<>();
    String sql = "SELECT MaLop FROM LopHoc WHERE Ma_Nguoi_Dung = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maGiaoVien);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            danhSachLop.add(rs.getString("MaLop"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return danhSachLop;
}
}
