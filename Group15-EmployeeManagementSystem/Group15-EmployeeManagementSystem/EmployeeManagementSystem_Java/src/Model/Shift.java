/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */

// lop luu tru thong tin ve ca lam viec
public class Shift {
    private String shiftID; 
    private String timeStart;   // thoi gian bat dau
    private String timeEnd; // thoi gian ket thuc

    public Shift(String shiftID, String timeStart, String timeEnd) {
        this.shiftID = shiftID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
    
    
}
