/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author whyia
 */
public class ESalary {
    private int id;
    private String name;
    private List<DayStatus> workDays;

    public ESalary(int id, String name, List<DayStatus> date) {
        this.id = id;
        this.name = name;
        this.workDays = date;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DayStatus> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<DayStatus> workDays) {
        this.workDays = workDays;
    }

   
   
}
