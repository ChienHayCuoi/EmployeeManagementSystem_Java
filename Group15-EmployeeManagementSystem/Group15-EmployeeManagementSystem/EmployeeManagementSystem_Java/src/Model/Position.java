/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */
public class Position {

    private int idChucVu;   // id chuc vu
    private String tenChucVu;   // ten chuc vu
    private String ghiChu;  // ghi chu

    public Position(int idChucVu, String tenChucVu, String ghiChu) {
        this.idChucVu = idChucVu;
        this.tenChucVu = tenChucVu;
        this.ghiChu = ghiChu;
    }

    public int getIdChucVu() {
        return idChucVu;
    }

    public void setIdChucVu(int idChucVu) {
        this.idChucVu = idChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public String[] dataRows(){
        return new String[]{String.valueOf(this.idChucVu), this.tenChucVu, this.ghiChu};
    }
    
    public String data(){
        return this.idChucVu + "," + this.tenChucVu + "," + this.ghiChu;
    }
}
