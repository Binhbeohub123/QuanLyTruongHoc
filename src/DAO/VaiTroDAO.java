package DAO;

import Model.VaiTro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static javax.management.remote.JMXConnectorFactory.connect;
import util.XJdbc;

public class VaiTroDAO {
    private Connection conn;

    public VaiTroDAO() {
        this.conn = XJdbc.openConnection();
    }

    public List<VaiTro> getAllVaiTro() {
        List<VaiTro> list = new ArrayList<>();
        String sql = "SELECT Ma_vai_tro FROM VaiTro";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getString("Ma_vai_tro").trim());
                list.add(vt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}