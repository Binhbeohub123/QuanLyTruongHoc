package utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.SwingUtilities;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Hỗ trợ tích hợp chọn Tỉnh/Thành Phố, Huyện, Xã vào các JComboBox đã có sẵn.
 * Sử dụng API mở tại https://provinces.open-api.vn/
 */
public class VnAddressSelectorService {
    private static final String BASE_URL = "https://provinces.open-api.vn/api/";
    private final HttpClient http;
    private final ObjectMapper mapper;

    public VnAddressSelectorService() {
        this.http = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.mapper = new ObjectMapper();
    }

    /**
     * Khởi tạo và gắn listener cho 3 JComboBox: tỉnh, huyện, xã.
     * Sử dụng raw JComboBox để dễ tích hợp với UI builder.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void bindAddressCombos(JComboBox provinceCbo,
                                  JComboBox districtCbo,
                                  JComboBox wardCbo) {
        JComboBox<Province> prov = provinceCbo;
        JComboBox<District> dist = districtCbo;
        JComboBox<Ward> ward = wardCbo;

        // Clear và disable ban đầu
        prov.removeAllItems();
        dist.removeAllItems();
        ward.removeAllItems();
        prov.setEnabled(false);
        dist.setEnabled(false);
        ward.setEnabled(false);

        // Khi chọn tỉnh
        prov.addActionListener(e -> {
            int idx = prov.getSelectedIndex();
            if (idx <= 0) {
                resetCombo(dist, "-- Chọn huyện --");
                resetCombo(ward, "-- Chọn xã --");
                return;
            }
            Province selected = prov.getItemAt(idx);
            refreshDistricts(selected.code, dist, ward);
        });

        // Khi chọn huyện
        dist.addActionListener(e -> {
            int idx = dist.getSelectedIndex();
            if (idx <= 0) {
                resetCombo(ward, "-- Chọn xã --");
                return;
            }
            District selected = dist.getItemAt(idx);
            refreshWards(selected.code, ward);
        });

        // Bắt đầu tải provinces
        refreshProvinces(prov, dist, ward);
    }

    private <T> void resetCombo(JComboBox<T> combo, String placeholder) {
        combo.removeAllItems();
        //noinspection unchecked
        combo.addItem((T) placeholder);
        combo.setEnabled(false);
    }

    private void refreshProvinces(JComboBox<Province> provinceCbo,
                                  JComboBox<District> districtCbo,
                                  JComboBox<Ward> wardCbo) {
        // placeholder cho child combo
        districtCbo.removeAllItems();
        districtCbo.addItem(new District("-- Chọn huyện --", -1));
        wardCbo.removeAllItems();
        wardCbo.addItem(new Ward("-- Chọn xã --", -1));

        // loading provinces
        provinceCbo.removeAllItems();
        provinceCbo.addItem(new Province("(Đang tải…)", -1));

        new SwingWorker<List<Province>, Void>() {
            @Override
            protected List<Province> doInBackground() throws Exception {
                String uri = BASE_URL + "?depth=1";
                HttpResponse<String> resp = http.send(
                        HttpRequest.newBuilder(new URI(uri)).GET().build(),
                        HttpResponse.BodyHandlers.ofString());
                return mapper.readValue(resp.body(), new TypeReference<List<Province>>() {});
            }
            @Override
            protected void done() {
                try {
                    List<Province> list = get();
                    SwingUtilities.invokeLater(() -> {
                        provinceCbo.removeAllItems();
                        provinceCbo.addItem(new Province("-- Chọn tỉnh/thành phố --", -1));
                        list.forEach(provinceCbo::addItem);
                        provinceCbo.setEnabled(true);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Lỗi tải danh sách tỉnh: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        provinceCbo.removeAllItems();
                        provinceCbo.addItem(new Province("-- Lỗi tải --", -1));
                        provinceCbo.setEnabled(true);
                    });
                }
            }
        }.execute();
    }

    private void refreshDistricts(int provinceCode,
                                  JComboBox<District> districtCbo,
                                  JComboBox<Ward> wardCbo) {
        // reset ward
        wardCbo.removeAllItems();
        wardCbo.addItem(new Ward("-- Chọn xã --", -1));
        wardCbo.setEnabled(false);

        // loading districts
        districtCbo.removeAllItems();
        districtCbo.addItem(new District("(Đang tải…)", -1));

        new SwingWorker<List<District>, Void>() {
            @Override
            protected List<District> doInBackground() throws Exception {
                String uri = BASE_URL + "p/" + provinceCode + "?depth=2";
                HttpResponse<String> resp = http.send(
                        HttpRequest.newBuilder(new URI(uri)).GET().build(),
                        HttpResponse.BodyHandlers.ofString());
                Province p = mapper.readValue(resp.body(), Province.class);
                return p.districts != null ? p.districts : List.of();
            }
            @Override
            protected void done() {
                try {
                    List<District> list = get();
                    SwingUtilities.invokeLater(() -> {
                        districtCbo.removeAllItems();
                        districtCbo.addItem(new District("-- Chọn huyện --", -1));
                        list.forEach(districtCbo::addItem);
                        districtCbo.setEnabled(true);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Lỗi tải danh sách huyện: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        districtCbo.removeAllItems();
                        districtCbo.addItem(new District("-- Lỗi tải --", -1));
                        districtCbo.setEnabled(true);
                    });
                }
            }
        }.execute();
    }

    private void refreshWards(int districtCode,
                              JComboBox<Ward> wardCbo) {
        wardCbo.removeAllItems();
        wardCbo.addItem(new Ward("(Đang tải…)", -1));

        new SwingWorker<List<Ward>, Void>() {
            @Override
            protected List<Ward> doInBackground() throws Exception {
                String uri = BASE_URL + "d/" + districtCode + "?depth=2";
                HttpResponse<String> resp = http.send(
                        HttpRequest.newBuilder(new URI(uri)).GET().build(),
                        HttpResponse.BodyHandlers.ofString());
                District d = mapper.readValue(resp.body(), District.class);
                return d.wards != null ? d.wards : List.of();
            }
            @Override
            protected void done() {
                try {
                    List<Ward> list = get();
                    SwingUtilities.invokeLater(() -> {
                        wardCbo.removeAllItems();
                        wardCbo.addItem(new Ward("-- Chọn xã/phường --", -1));
                        list.forEach(wardCbo::addItem);
                        wardCbo.setEnabled(true);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Lỗi tải danh sách xã: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        wardCbo.removeAllItems();
                        wardCbo.addItem(new Ward("-- Lỗi tải --", -1));
                        wardCbo.setEnabled(true);
                    });
                }
            }
        }.execute();
    }

    // --- DTO Classes ---
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Province {
        public String name;
        public int code;
        public List<District> districts = new ArrayList<>();
        public Province() {}
        public Province(String name, int code) { this.name = name; this.code = code; }
        @Override public String toString() { return name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class District {
        public String name;
        public int code;
        public List<Ward> wards = new ArrayList<>();
        public District() {}
        public District(String name, int code) { this.name = name; this.code = code; }
        @Override public String toString() { return name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ward {
        public String name;
        public int code;
        public Ward() {}
        public Ward(String name, int code) { this.name = name; this.code = code; }
        @Override public String toString() { return name; }
    }
}
