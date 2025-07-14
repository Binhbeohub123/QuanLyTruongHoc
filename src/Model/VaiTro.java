package Model;

public class VaiTro {
    private String maVaiTro;

    public VaiTro() {}

    public VaiTro(String maVaiTro, String tenVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    @Override
    public String toString() {
    return maVaiTro; // hoặc getTenVaiTro() nếu bạn dùng getter
}
}