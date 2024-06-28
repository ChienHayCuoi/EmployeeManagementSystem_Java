/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */
public class Department {

    private int idPhongBan; // id phong ban
    private String tenPhongBan; // ten phong ban
    private String diaChi;  // dia chi
    private String sdtPhong;    // so dien thoai phong
    private String ghiChu;  // ghi chu

    public Department(int idPhongBan, String tenPhongBan, String diaChi, String sdtPhong, String ghiChu) {
        this.idPhongBan = idPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.diaChi = diaChi;
        this.sdtPhong = sdtPhong;
        this.ghiChu = ghiChu;
    }

    public int getIdPhongBan() {
        return idPhongBan;
    }

    public void setIdPhongBan(int idPhongBan) {
        this.idPhongBan = idPhongBan;
    }
    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdtPhong() {
        return sdtPhong;
    }

    public void setSdtPhong(String sdtPhong) {
        this.sdtPhong = sdtPhong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
