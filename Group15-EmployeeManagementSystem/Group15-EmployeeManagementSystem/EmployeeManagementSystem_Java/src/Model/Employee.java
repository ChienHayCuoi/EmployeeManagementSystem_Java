package Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ADMIN
 */
public class Employee {

    private int employeeID; // ma nhan vien
    private String employeeName; // ten nhan vien
    private String dateOfBirth; // ngay sinh
    private String gender;  // gioi tinh
    private String email;   // email
    private String phoneNumber; // so dien thoai
    private String hometown;    // que quan
    private String address; // dia chi lien he
    private String hireDate;    // ngay bat dau lam viec
    private String imagePath;   // anh profile
    private String department;
    private String position;
    private double salary; // luong

    public Employee(int employeeID, String employeeName, String dateOfBirth, String gender, String hometown, String phoneNumber, String email, String address, String hireDate, double salary, String department, String position, String imagePath) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.hometown = hometown;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.hireDate = hireDate;
        this.salary = salary;
        this.position = position;
        this.department = department;
        this.imagePath = imagePath;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
