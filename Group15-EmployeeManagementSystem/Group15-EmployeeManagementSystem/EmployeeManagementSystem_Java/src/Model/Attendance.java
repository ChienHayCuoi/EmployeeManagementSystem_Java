/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */

// Lop luu tru thong tin ve cham cong
public class Attendance {
    private int id;
    private String in_time; // thoi gian vao lam
    private String in_status;   // trang thai vao lam
    private String out_time;    // thoi gian tan lam
    private String out_status;  // trang thai tan lam
    private String notes;   // ghi chu
    private String lack_of; // ly do vang, phep,...

    public Attendance(int id, String in_time, String in_status, String out_time, String out_status, String notes, String lack_of) {
        this.id = id;
        this.in_time = in_time;
        this.in_status = in_status;
        this.out_time = out_time;
        this.out_status = out_status;
        this.notes = notes;
        this.lack_of = lack_of;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getIn_status() {
        return in_status;
    }

    public void setIn_status(String in_status) {
        this.in_status = in_status;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getOut_status() {
        return out_status;
    }

    public void setOut_status(String out_status) {
        this.out_status = out_status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLack_of() {
        return lack_of;
    }

    public void setLack_of(String lack_of) {
        this.lack_of = lack_of;
    }
    
}
