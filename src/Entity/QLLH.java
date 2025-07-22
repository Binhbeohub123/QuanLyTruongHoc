package Entity;

public class QLLH {
    private String maLop;              
    private String tenLop;             
    private String khoi;               
    private String namHoc;            
    private int siSo;                  
    private String maNguoiDung;        
    private String TenPhonghoc;         

    
    public QLLH() {}

   
    public QLLH(String maLop, String tenLop, int siSo, String khoi, String maNguoiDung, String namHoc, String TenPhonghoc) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.siSo = siSo;
        this.khoi = khoi;
        this.maNguoiDung = maNguoiDung;
        this.namHoc = namHoc;
        this.TenPhonghoc = TenPhonghoc;
    }

    

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoi() {
        return khoi;
    }

    public void setKhoi(String khoi) {
        this.khoi = khoi;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public int getSiSo() {
        return siSo;
    }

    public void setSiSo(int siSo) {
        this.siSo = siSo;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaPhongHoc() {
        return TenPhonghoc;
    }

    public void setMaPhongHoc(String maPhongHoc) {
        this.TenPhonghoc = TenPhonghoc;
    }
}