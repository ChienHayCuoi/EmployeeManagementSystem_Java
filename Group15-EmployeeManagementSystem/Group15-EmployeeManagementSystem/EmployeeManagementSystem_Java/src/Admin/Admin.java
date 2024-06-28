package Admin;

import Model.Account;
import Model.DayStatus;
import Model.ESalary;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import Model.Department;
import View.Login;
import Model.Employee;
import Model.Position;
import java.util.List;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import javax.annotation.processing.FilerException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Admin extends javax.swing.JFrame {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String DATE_PATTERN = "^\\d{2}/\\d{2}/\\d{4}$";
    private boolean checkImg;
    private String getFileName;
    private List<Employee> employeesList = new ArrayList<>();
    private List<Department> departmentList = new ArrayList<>();
    private List<Position> positionList = new ArrayList<>();
    private List<ESalary> esList = new ArrayList<>();
    private List<Account> accountList = new ArrayList<>();
    private DefaultTableModel employeeModel;
    private DefaultTableModel departmentModel;
    private DefaultTableModel positionModel;
    private DefaultTableModel SEModel;
    private DefaultTableModel accountModel;
    private int srIndex;
    private DefaultTableModel model;

    public Admin() {
        initComponents();
        insertCbxDay();
        insertCbxMonth();
        insertCbxYear();
        initEmployeeList();
        initDepartmentList();
        initPositionList();
        initAccountList();
        initESalary();
        updateSETable(esList);
        updateEmployeeTable(employeesList);
        updateDepartmentTable(departmentList);
        updatePositionTable(positionList);
        updateAccountTable(accountList);
        insertCbxDepartmentFromEmployeeTable();
        txtFindEmployee.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtFindEmployee.getText().isEmpty()) {
                    updateEmployeeTable(employeesList);
                }
            }
        });
        txtFindDepartment.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtFindDepartment.getText().isEmpty()) {
                    updateDepartmentTable(departmentList);
                }
            }
        });
        txtFindPosition.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtFindPosition.getText().isEmpty()) {
                    updatePositionTable(positionList);
                }
            }
        });
        tableES.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                upload();
            }
        });
        insertCbxDepartmentFromEmployeeTable();
    }
    private void initESalary(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("data\\timeKeeping.txt"));
            String line;
            while((line = reader.readLine()) != null){
                List<DayStatus> dayStatus = new ArrayList<>();
                String[] parts = line.split("-");
                String[] wDates = parts[2].split(";");
                for(int i = 0; i < wDates.length; i++){
                    String[] string1 = wDates[i].split("_");
                    String[] string2 = string1[0].split(",");
                    LocalDate localDate = LocalDate.of(Integer.parseInt(string2[0]), 
                            Integer.parseInt(string2[1]), Integer.parseInt(string2[2]));
                    dayStatus.add(new DayStatus(localDate, string1[1]));
                }
                
                esList.add(new ESalary(Integer.parseInt(parts[0]), parts[1], dayStatus));
            }
        }catch(IOException e){
            System.out.println("Admin.Admin.initESalary()");
        }
    }


    private void upload() {
        int sr = tableES.getSelectedRow();
        List<DayStatus> x = new ArrayList<>();
        if(sr != -1){
        if (sr != -1) {
            int id = Integer.parseInt(tableES.getValueAt(sr, 0).toString());
            for (ESalary e : esList) {
                if (e.getId() == id) {
                    x = e.getWorkDays();
                }
            }
        }
        calendarPanel1.setWorkingDays(x);
    }
    }
    private void updateSETable(List<ESalary> x) { 
        SEModel = (DefaultTableModel) tableES.getModel();
        SEModel.setRowCount(0);
        for (ESalary e : x) {
            String[] dataRow = {String.valueOf(e.getId()), e.getName()};
            SEModel.addRow(dataRow);
        }
    }

    //EMPLOYEE====================================================================================================================
    private void initEmployeeList() { //Khởi tạo list từ file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\employee.txt"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                Employee e = new Employee(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], Double.parseDouble(parts[9]), parts[10], parts[11], parts[12]);
                employeesList.add(e);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addEmployeeList() { //Thêm nhân viên vào list
        Employee e = new Employee(Integer.parseInt(txtMaNV.getText()), txtHoTen.getText(), birthDay.getSelectedItem() + "/" + birthMonth.getSelectedItem() + "/" + birthYear.getSelectedItem(), txtGender.getText(), txtHomeTown.getText(), txtPhoneNum.getText(), txtEmail.getText(), txtContactAddress.getText(), hireDate.getText(), Double.parseDouble(salary.getText()), CbxDepartmentFromET.getSelectedItem().toString(), txtPosition.getText(), "ImgEmployee\\\\" + getFileName);
        employeesList.add(e);
    }

    private void removeEmployeeFromTable() { //Xóa nhân viên khỏi list và bảng
        int sr = tableEmployee.getSelectedRow();
        List<Employee> removeList = new ArrayList<>();
        if (sr != -1) {
            int id = Integer.parseInt(tableEmployee.getValueAt(sr, 0).toString());
            for (Employee e : employeesList) {
                if (e.getEmployeeID() == id) {
                    removeList.add(e);
                }
            }
            for (Employee e : removeList) {
                employeesList.remove(e);
            }
            updateEmployeeTable(employeesList);
            JOptionPane.showMessageDialog(null, "Đã xóa một nhân viên thành công!!");
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 đối tượng!");
        }
    }

    private void writeEmployeeListToFile() { // Lưu dữ liệu vào file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\employee.txt", false));
            for (Employee e : employeesList) {
                String data = String.valueOf(e.getEmployeeID()) + "," + e.getEmployeeName() + ","
                        + e.getDateOfBirth() + "," + e.getGender() + "," + e.getHometown() + "," + e.getPhoneNumber() + ","
                        + e.getEmail() + "," + e.getAddress() + "," + e.getHireDate() + "," + String.valueOf(e.getSalary()) + "," + e.getDepartment() + "," + e.getPosition() + "," + e.getImagePath();
                writer.write(data);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Lưu thành công!");
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lưu không thành công!");
        }
    }

    private void updateEmployeeTable(List<Employee> x) { // Cập nhật bảng sau khi chỉnh sửa
        employeeModel = (DefaultTableModel) tableEmployee.getModel();
        employeeModel.setRowCount(0);
        for (Employee e : x) {
            String[] dataRow = {String.valueOf(e.getEmployeeID()), e.getEmployeeName(), e.getDateOfBirth(), e.getGender(), e.getHometown(), e.getPhoneNumber(), e.getEmail(), e.getAddress(), e.getHireDate(), String.valueOf(e.getSalary()), e.getDepartment(), e.getPosition(), e.getImagePath()};
            employeeModel.addRow(dataRow);
        }
    }

    public void insertCbxDay() {
        birthDay.removeAllItems();
        for (int i = 1; i <= 31; i++) {
            birthDay.addItem(String.valueOf(i));
        }
        birthDay.setSelectedIndex(-1);
    }

    public void insertCbxMonth() {
        birthMonth.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            birthMonth.addItem(String.valueOf(i));
        }
        birthMonth.setSelectedIndex(-1);
    }

    public void insertCbxYear() {
        birthYear.removeAllItems();
        for (int i = 1900; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            birthYear.addItem(String.valueOf(i));
        }
        birthYear.setSelectedIndex(-1);
    }

    private void insertCbxDepartmentFromEmployeeTable() {
        CbxDepartmentFromET.removeAllItems();
        for (Department d : departmentList) {
            CbxDepartmentFromET.addItem(d.getTenPhongBan());
        }
        CbxDepartmentFromET.setSelectedItem(-1);
    }

    private boolean checkDepartmentIsSelected() {
        if (CbxDepartmentFromET.getSelectedIndex() == -1) {
            return false;
        }
        return true;
    }

    public boolean checkBirthdayIsSelected() {
        if (birthDay.getSelectedIndex() == -1 || birthMonth.getSelectedIndex() == -1 || birthYear.getSelectedIndex() == -1) {
            return false;
        }
        return true;
    }

    private void saveImage(File imageFile) throws IOException {
        try {
            String folderPath = "ImgEmployee";
            Path folder = Paths.get(folderPath);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            Path des = Paths.get(folderPath, imageFile.getName());
            getFileName = imageFile.getName();
            int count = 0;
            String fileName;
            while (Files.exists(des)) {
                fileName = "img" + count + ".jpg";
                getFileName = fileName;
                des = Paths.get(folderPath, fileName);
                count++;
            }
            Files.copy(imageFile.toPath(), des);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isCheckEmpID(int empID) {
        for (Employee emp : employeesList) {
            if (emp.getEmployeeID() == empID) {
                return true;
            }
        }
        return false;
    }

    //=============================================================================================================================
    //DEPARTMENT===================================================================================================================
    private void initDepartmentList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\department.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Department d = new Department(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4]);
                departmentList.add(d);

            }
            reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Khởi tạo dữ liệu Department không thành công!");
        }
    }

    private void addDepartmentList() {
        Department d = new Department(Integer.parseInt(txtDpmID.getText()), txtDepartmentName.getText(), txtDpmAddress.getText(), txtDpmPhoneNumber.getText(), txtDpmNote.getText());
        departmentList.add(d);
    }

    private void removeDepartment() {
        int sr = tableDepartment.getSelectedRow();
        List<Department> removeList = new ArrayList<>();
        if (sr != -1) {
            int id = Integer.parseInt(tableDepartment.getValueAt(sr, 0).toString());
            for (Department d : departmentList) {
                if (d.getIdPhongBan() == id) {
                    removeList.add(d);
                }
            }
            for (Department d : removeList) {
                departmentList.remove(d);
            }
            updateDepartmentTable(departmentList);
            JOptionPane.showMessageDialog(null, "Đã xoá phòng ban thành công!!");
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 đối tượng!");
        }
    }

    private void updateDepartmentTable(List<Department> x) {
        departmentModel = (DefaultTableModel) tableDepartment.getModel();
        departmentModel.setRowCount(0);
        for (Department d : x) {
            int countEmployee = 0;
            for (Employee e : employeesList) {
                if (e.getDepartment().equals(d.getTenPhongBan())) {
                    countEmployee++;
                }
            }

            String[] datarow = {String.valueOf(d.getIdPhongBan()), d.getTenPhongBan(), d.getDiaChi(), d.getSdtPhong(), String.valueOf(countEmployee), d.getGhiChu()};
            departmentModel.addRow(datarow);
        }
    }

    private void writeDepartmentToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\department.txt"));
            for (Department d : departmentList) {
                String data = d.getIdPhongBan() + "," + d.getTenPhongBan() + "," + d.getDiaChi() + "," + d.getSdtPhong() + "," + d.getGhiChu();
                writer.write(data);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lưu không thành công!");
        }
    }

    private boolean isCheckDepartID(int depID) {
        for (Department dep : departmentList) {
            if (dep.getIdPhongBan() == depID) {
                return true;
            }
        }
        return false;
    }

    //=============================================================================================================================
    //POSITION===================================================================================================================
    private void initPositionList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\position.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Position p = new Position(Integer.parseInt(parts[0]), parts[1], parts[2]);
                positionList.add(p);

            }
            reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Khởi tạo dữ liệu Position không thành công!");
        }
    }

    private void writePositiontToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\position.txt"));
            for (Position p : positionList) {
                String data = p.data();
                writer.write(data);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lưu không thành công!");
        }
    }

    private void removePositionFromTable() {
        int sr = tablePosition.getSelectedRow();
        List<Position> removeList = new ArrayList<>();
        if (sr != -1) {
            int id = Integer.parseInt(tablePosition.getValueAt(sr, 0).toString());
            for (Position p : positionList) {
                if (p.getIdChucVu() == id) {
                    removeList.add(p);
                }
            }
            for (Position p : removeList) {
                positionList.remove(p);
            }
            updatePositionTable(positionList);
            JOptionPane.showMessageDialog(null, "Đã xoá chức vụ thành công!!");
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 đối tượng!!");
        }
    }

    private void addPositionList() {
        Position p = new Position(Integer.parseInt(txtPosID.getText()), txtPositionName.getText(), txtPosNote.getText());
        positionList.add(p);
    }

    private boolean PosIsEmpty() {
        if (txtPosID.getText().isEmpty() || txtPositionName.getText().isEmpty() || txtPosNote.getText().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isCheckPosID(int posID) {
        for (Position pos : positionList) {
            if (pos.getIdChucVu() == posID) {
                return true;
            }
        }
        return false;
    }

    private void updatePositionTable(List<Position> x) {
        positionModel = (DefaultTableModel) tablePosition.getModel();
        positionModel.setRowCount(0);
        for (Position p : x) {
            positionModel.addRow(p.dataRows());
        }
    }

    // ============================================================================================================================
    // PHAN QUYEN TAI KHOAN =======================================================================================================
    private void initAccountList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\account.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Account acc = new Account(parts[0], parts[1], Integer.parseInt(parts[2]));
                accountList.add(acc);

            }
            reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Khởi tạo dữ liệu không thành công!");
        }
    }

    private void writeAccountToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\account.txt", false));
            for (Account acc : accountList) {
                String data = acc.getUsername() + "," + acc.getPassword() + "," + acc.getAccType();
                writer.write(data);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lưu không thành công!");
        }
    }

    private void updateAccountTable(List<Account> accountList) {
        accountModel = (DefaultTableModel) tblAccountDivide.getModel();
        accountModel.setRowCount(0);
        for (Account acc : accountList) {
            String[] dataRow = {acc.getUsername(), acc.getPassword(), String.valueOf(acc.getAccType())};
            accountModel.addRow(dataRow);
        }
    }

    private void removeAccount() {
        int sr = tblAccountDivide.getSelectedRow();
        List<Account> removeList = new ArrayList<>();
        if (sr != -1) {
            String username = jTextField1.getText();
            for (Account account : accountList) {
                if (account.getUsername().equals(username)) {
                    removeList.add(account);
                }
            }
            for (Account acc : removeList) {
                accountList.remove(acc);
            }
            updateAccountTable(accountList);
            JOptionPane.showMessageDialog(null, "Đã xoá tài khoản thành công!!");
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 đối tượng!!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnQuitClicked = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        txtFindEmployee = new javax.swing.JTextField();
        btnSearchEmp = new javax.swing.JButton();
        btnAddEmployee = new javax.swing.JButton();
        btnSaveEmployeeData = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnUpdateEmployee = new javax.swing.JButton();
        btnDeleteEmployee = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmployee = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtGender = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPhoneNum = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtHomeTown = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtContactAddress = new javax.swing.JTextField();
        hireDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        salary = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnUpload = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        labelImg = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        btnPrintEmpList = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        birthDay = new javax.swing.JComboBox<>();
        birthMonth = new javax.swing.JComboBox<>();
        birthYear = new javax.swing.JComboBox<>();
        CbxDepartmentFromET = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        txtFindDepartment = new javax.swing.JTextField();
        btnSearchDpm = new javax.swing.JButton();
        btnAddDepartment = new javax.swing.JButton();
        btnSaveDepartmentData = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        btnUpdateDepartment = new javax.swing.JButton();
        btnDeleteDpm = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDepartment = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        txtDpmID = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDepartmentName = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDpmPhoneNumber = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDpmAddress = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtDpmNote = new javax.swing.JTextArea();
        jLabel27 = new javax.swing.JLabel();
        btnPrintDpmList = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        txtFindPosition = new javax.swing.JTextField();
        btnSearchPos = new javax.swing.JButton();
        btnAddPosition = new javax.swing.JButton();
        btnSavePos = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        btnUpdatePosition = new javax.swing.JButton();
        btnDeletePosition = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePosition = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        txtPosID = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtPositionName = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtPosNote = new javax.swing.JTextArea();
        btnPrintPosList = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        calendarPanel1 = new JCalendar.CalendarPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableES = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblAccountDivide = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        btnAccout = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnDelAcc = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jLabel51 = new javax.swing.JLabel();
        jCheckBox16 = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin ");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN TRỊ");

        btnQuitClicked.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/quit.png"))); // NOI18N
        btnQuitClicked.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuitClicked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitClickedActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Thoát:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuitClicked, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuitClicked, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 204), 2, true));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jPanel11.setBackground(new java.awt.Color(102, 204, 255));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 153), 2, true));

        txtFindEmployee.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btnSearchEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/search.png"))); // NOI18N
        btnSearchEmp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchEmpActionPerformed(evt);
            }
        });

        btnAddEmployee.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnAddEmployee.setText("Thêm mới");
        btnAddEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployeeActionPerformed(evt);
            }
        });

        btnSaveEmployeeData.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnSaveEmployeeData.setText("Lưu");
        btnSaveEmployeeData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveEmployeeDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtFindEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchEmp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveEmployeeData, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveEmployeeData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSearchEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFindEmployee))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnAddEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(102, 204, 255));
        jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 153), 2, true));

        btnUpdateEmployee.setBackground(new java.awt.Color(0, 255, 51));
        btnUpdateEmployee.setText("Cập nhật");
        btnUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployeeActionPerformed(evt);
            }
        });

        btnDeleteEmployee.setBackground(new java.awt.Color(255, 0, 0));
        btnDeleteEmployee.setText("Xóa");
        btnDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmployeeActionPerformed(evt);
            }
        });

        tableEmployee.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        tableEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ tên", "Ngày sinh", "Giới tính", "Quê quán", "Số ĐT", "Email", "ĐC liên hệ", "Ngày làm việc", "Lương", "Phòng ban", "Chức vụ", "Ảnh"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableEmployee);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setText("Mã nhân viên :");

        txtMaNV.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setText("Họ tên :");

        txtHoTen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Ngày sinh :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setText("Giới tính :");

        txtGender.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setText("Email :");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setText("Số ĐT :");

        txtPhoneNum.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPhoneNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneNumKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setText("Quê quán :");

        txtHomeTown.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setText("ĐC liên hệ :");

        txtContactAddress.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        hireDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel11.setText("Ngày làm việc :");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel12.setText("Lương :");

        salary.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel13.setText("Ảnh :");

        btnUpload.setText("Upload...");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        jPanel27.setBackground(new java.awt.Color(204, 204, 204));
        jPanel27.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 0), 2, true));

        labelImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
        );

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel25.setText("Phòng ban :");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel26.setText("Chức vụ :");

        txtPosition.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btnPrintEmpList.setBackground(new java.awt.Color(255, 255, 51));
        btnPrintEmpList.setText("Xuất PDF");
        btnPrintEmpList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintEmpListActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Danh sách nhân viên:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .addComponent(txtMaNV)
                                    .addComponent(txtHomeTown)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(3, 3, 3))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(23, 23, 23)))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(hireDate)
                                    .addComponent(CbxDepartmentFromET, 0, 213, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPosition)
                            .addComponent(txtGender, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(txtHoTen)
                            .addComponent(txtContactAddress)
                            .addComponent(salary))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(btnUpload)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPhoneNum, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                    .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(88, 88, 88))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrintEmpList, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(txtHomeTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtEmail)
                                .addComponent(jLabel10)
                                .addComponent(txtContactAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addComponent(btnUpload))
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(hireDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(salary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26)
                                .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel25)
                                .addComponent(CbxDepartmentFromET, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btnDeleteEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrintEmpList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý nhân viên", jPanel2);

        jPanel13.setBackground(new java.awt.Color(102, 204, 255));
        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 153), 2, true));

        btnSearchDpm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/search.png"))); // NOI18N
        btnSearchDpm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchDpm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchDpmActionPerformed(evt);
            }
        });

        btnAddDepartment.setText("Thêm mới");
        btnAddDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDepartmentActionPerformed(evt);
            }
        });

        btnSaveDepartmentData.setText("Lưu");
        btnSaveDepartmentData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDepartmentDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtFindDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchDpm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveDepartmentData, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveDepartmentData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnSearchDpm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txtFindDepartment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(102, 204, 255));
        jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 153), 2, true));

        btnUpdateDepartment.setBackground(new java.awt.Color(51, 255, 51));
        btnUpdateDepartment.setText("Cập nhật");
        btnUpdateDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDepartmentActionPerformed(evt);
            }
        });

        btnDeleteDpm.setBackground(new java.awt.Color(255, 0, 51));
        btnDeleteDpm.setText("Xóa");
        btnDeleteDpm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDpmActionPerformed(evt);
            }
        });

        tableDepartment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Số phòng", "Tên phòng", "Địa chỉ", "Số điện thoại", "SL nhân viên", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDepartmentMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableDepartment);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel15.setText("ID Phòng:");

        txtDpmID.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel17.setText("Tên phòng:");

        txtDepartmentName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel18.setText("Số điện thoại:");

        txtDpmPhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtDpmPhoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDpmPhoneNumberKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel19.setText("Địa chỉ:");

        txtDpmAddress.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel20.setText("Ghi chú:");

        txtDpmNote.setColumns(20);
        txtDpmNote.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtDpmNote.setRows(5);
        jScrollPane9.setViewportView(txtDpmNote);

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Danh sách các phòng:");

        btnPrintDpmList.setBackground(new java.awt.Color(255, 255, 0));
        btnPrintDpmList.setText("Xuất PDF");
        btnPrintDpmList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDpmListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(btnUpdateDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteDpm, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintDpmList, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDpmID, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDpmPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel17))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(txtDepartmentName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(104, 104, 104)
                                        .addComponent(jLabel19)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDpmAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(txtDepartmentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(txtDpmAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(txtDpmID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(txtDpmPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPrintDpmList, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btnUpdateDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btnDeleteDpm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý phòng ban", jPanel3);

        jPanel15.setBackground(new java.awt.Color(102, 204, 255));
        jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 153), 2, true));

        btnSearchPos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/search.png"))); // NOI18N
        btnSearchPos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPosActionPerformed(evt);
            }
        });

        btnAddPosition.setText("Thêm mới");
        btnAddPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPositionActionPerformed(evt);
            }
        });

        btnSavePos.setText("Lưu");
        btnSavePos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtFindPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchPos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSavePos, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSavePos, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnAddPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnSearchPos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtFindPosition, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(102, 204, 255));
        jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 153), 2, true));

        btnUpdatePosition.setBackground(new java.awt.Color(0, 255, 51));
        btnUpdatePosition.setText("Cập nhật");
        btnUpdatePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePositionActionPerformed(evt);
            }
        });

        btnDeletePosition.setBackground(new java.awt.Color(255, 51, 0));
        btnDeletePosition.setText("Xóa");
        btnDeletePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePositionActionPerformed(evt);
            }
        });

        tablePosition.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã chức vụ", "Tên chức vụ", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePositionMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablePosition);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel21.setText("ID chức vụ:");

        txtPosID.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel23.setText("Tên chức vụ:");

        txtPositionName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel24.setText("Ghi chú:");

        txtPosNote.setColumns(20);
        txtPosNote.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPosNote.setRows(5);
        jScrollPane10.setViewportView(txtPosNote);

        btnPrintPosList.setBackground(new java.awt.Color(255, 255, 51));
        btnPrintPosList.setText("Xuất PDF");
        btnPrintPosList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPosListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(btnUpdatePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeletePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintPosList, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addComponent(txtPosID, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(110, 110, 110)
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPositionName, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 425, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtPosID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(txtPositionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdatePosition, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btnDeletePosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrintPosList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý chức vụ", jPanel4);

        calendarPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tableES.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Họ Tên"
            }
        ));
        jScrollPane4.setViewportView(tableES);

        jPanel7.setBackground(new java.awt.Color(255, 0, 0));
        jPanel7.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel17.setBackground(new java.awt.Color(0, 255, 0));
        jPanel17.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLabel52.setText("Nghỉ");

        jLabel53.setText("Đi làm đúng giờ");

        jPanel18.setBackground(new java.awt.Color(255, 255, 0));
        jPanel18.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLabel54.setText("Đi làm trễ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel53)
                        .addGap(37, 37, 37)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calendarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(calendarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Lương, chấm công", jPanel5);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel14.setText("TÀI KHOẢN VÀ PHÂN QUYỀN");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 1059, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        tblAccountDivide.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tài khoản", "Mật khẩu ", "Quyền "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAccountDivide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccountDivideMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblAccountDivide);

        jPanel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel22.setText("Chi tiết tài khoản:");

        jLabel29.setText("Tên tài khoản:");

        jLabel30.setText("Sửa quyền");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "employee", " " }));

        btnAccout.setText("Lưu");
        btnAccout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccoutActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 204, 204));

        btnDelAcc.setText("Xóa");
        btnDelAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelAccActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(btnAccout)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelAcc))
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccout)
                    .addComponent(btnDelAcc))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Quản lý nhân viên");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Quản lý phòng ban");

        jLabel33.setText("Thêm nhân viên");

        jLabel34.setText("Sửa, xóa nhân viên");

        jLabel35.setText("Thêm phòng ban");

        jLabel36.setText("Sửa, xóa phòng ban");

        jLabel37.setText("Xuất Excel");

        jLabel38.setText("Xuất Excel");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Quản lý chức vụ");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Quản lý lịch làm việc");

        jLabel39.setText("Xuất Excel");

        jLabel40.setText("Sửa, xóa chức vụ");

        jLabel43.setText("Thêm chức vụ");

        jLabel44.setText("Xuất Excel");

        jLabel45.setText("Sửa, xóa lịch làm việc");

        jLabel46.setText("Thêm lịch làm việc");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Quản lý lương, chấm công");

        jLabel48.setText("Xem danh sách chấm công");

        jLabel49.setText("Sửa, xóa bảng chấm công");

        jLabel50.setText("Xuất Excel");

        jLabel51.setText("Chấm công nhân viên");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addGap(97, 97, 97))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel43)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel47)
                        .addGap(57, 57, 57)))
                .addGap(12, 12, 12)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(135, 135, 135)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(98, 98, 98)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox3)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel33)
                                            .addComponent(jCheckBox1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel34)))
                                    .addComponent(jCheckBox2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel38)))
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jCheckBox6)
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel35)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel22Layout.createSequentialGroup()
                                                .addComponent(jCheckBox4)
                                                .addGap(28, 28, 28))
                                            .addComponent(jCheckBox5)))
                                    .addGap(28, 28, 28))))))
                .addGap(79, 79, 79)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox9)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel43)
                                    .addComponent(jCheckBox7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel40))
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel45)
                                .addComponent(jCheckBox8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel39))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jCheckBox12)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel46)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jCheckBox10)
                                        .addGap(28, 28, 28))
                                    .addComponent(jCheckBox11)))
                            .addGap(9, 9, 9)
                            .addComponent(jLabel44)
                            .addGap(3, 3, 3))))
                .addGap(52, 52, 52)
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jCheckBox13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox16)
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox15)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel49)
                            .addComponent(jCheckBox14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel50)))
                .addContainerGap())
        );

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Danh sách tài khoản");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jLabel16)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cấp quyền tài khoản", jPanel8);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitClickedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitClickedActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new Login().setVisible(true);
        } else {
            return;
        }
    }//GEN-LAST:event_btnQuitClickedActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnAccoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccoutActionPerformed
        srIndex = tblAccountDivide.getSelectedRow();
        int type = jComboBox2.getSelectedItem().equals("admin") ? 0 : 1;

        if (srIndex != -1) {
            String username = jTextField1.getText();
            boolean found = false;
            for (Account acc : accountList) {
                if (acc.getUsername().equals(username)) {
                    acc.setAccType(type);
                    tblAccountDivide.setValueAt(type, srIndex, 2);
                    JOptionPane.showMessageDialog(null, "Đã lưu thành công!");
                    found = true;
                }
            }
            if (found) {
                writeAccountToFile();
                JOptionPane.showMessageDialog(null, "Đã lưu thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Tài khoản không tồn tại!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Chưa chọn bản ghi!");
        }
    }//GEN-LAST:event_btnAccoutActionPerformed

    private void tblAccountDivideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccountDivideMouseClicked
        model = (DefaultTableModel) tblAccountDivide.getModel();
        srIndex = tblAccountDivide.getSelectedRow();
        jTextField1.setText(model.getValueAt(srIndex, 0).toString());
        String type = model.getValueAt(srIndex, 2).toString();
        int accType = Integer.parseInt(type);
        if (accType == 0) {
            jComboBox2.setSelectedItem("admin");
        } else {
            jComboBox2.setSelectedItem("employee");
        }
    }//GEN-LAST:event_tblAccountDivideMouseClicked

    private void btnPrintPosListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPosListActionPerformed
        try {
            MessageFormat header = new MessageFormat("Thông tin về chức vụ");
            MessageFormat footer = new MessageFormat("Page{0,number, integer}");
            tablePosition.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrintPosListActionPerformed

    private void tablePositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePositionMouseClicked
        model = (DefaultTableModel) tablePosition.getModel();
        srIndex = tablePosition.getSelectedRow();
        txtPosID.setText(model.getValueAt(srIndex, 0).toString());
        txtPosID.setEditable(false);
        txtPositionName.setText(model.getValueAt(srIndex, 1).toString());
        txtPosNote.setText(model.getValueAt(srIndex, 2).toString());
    }//GEN-LAST:event_tablePositionMouseClicked

    private void btnDeletePositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePositionActionPerformed
        removePositionFromTable();
    }//GEN-LAST:event_btnDeletePositionActionPerformed

    private void btnUpdatePositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePositionActionPerformed
        srIndex = tablePosition.getSelectedRow();
        if (srIndex != -1) {
            int id = Integer.parseInt(txtPosID.getText());
            for (Position p : positionList) {
                if (p.getIdChucVu() == id) {
                    p.setTenChucVu(txtPositionName.getText());
                    p.setGhiChu(txtPosNote.getText());

                    updatePositionTable(positionList);
                    clearPositionTextFields();
                    txtPosID.setEditable(true);
                    JOptionPane.showMessageDialog(null, "Đã sửa thông tin chức vụ!!");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Chưa chọn bản ghi!");
        }
    }//GEN-LAST:event_btnUpdatePositionActionPerformed

    private void btnSavePosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePosActionPerformed
        int op = JOptionPane.showConfirmDialog(null, "Bạn có muốn lưu dữ liệu không?");
        if (op == JOptionPane.YES_OPTION) {
            writePositiontToFile();
            JOptionPane.showMessageDialog(null, "Lưu thành công!!");
        }
    }//GEN-LAST:event_btnSavePosActionPerformed

    private void btnAddPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPositionActionPerformed
        if (PosIsEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!!");
        } else {
            if (isCheckPosID(Integer.parseInt(txtPosID.getText()))) {
                JOptionPane.showMessageDialog(null, "Mã chức vụ này đã tồn tại. Vui lòng nhập mã khác");
            } else {
                addPositionList();
                updatePositionTable(positionList);
                clearPositionTextFields();
                JOptionPane.showMessageDialog(null, "Thêm thành công!!");
            }
        }
    }//GEN-LAST:event_btnAddPositionActionPerformed

    private void btnSearchPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPosActionPerformed
        if (txtFindPosition.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên cần tìm!!");
        } else {
            List<Position> findList = new ArrayList<>();
            String searchText = txtFindPosition.getText().trim().toLowerCase();
            for (Position e : positionList) {
                if (e.getTenChucVu().toLowerCase().contains(searchText)) {
                    findList.add(e);
                }
            }
            updatePositionTable(findList);
        }
    }//GEN-LAST:event_btnSearchPosActionPerformed

    private void btnPrintDpmListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDpmListActionPerformed
        try {
            MessageFormat header = new MessageFormat("Thông tin về phòng ban");
            MessageFormat footer = new MessageFormat("Page{0,number, integer}");
            tableDepartment.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            Logger.getLogger(Department.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnPrintDpmListActionPerformed

    private void tableDepartmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDepartmentMouseClicked
        model = (DefaultTableModel) tableDepartment.getModel();
        srIndex = tableDepartment.getSelectedRow();
        txtDpmID.setText(model.getValueAt(srIndex, 0).toString());
        txtDpmID.setEditable(false);
        txtDepartmentName.setText(model.getValueAt(srIndex, 1).toString());
        txtDpmAddress.setText(model.getValueAt(srIndex, 2).toString());
        txtDpmPhoneNumber.setText(model.getValueAt(srIndex, 3).toString());
        txtDpmNote.setText(model.getValueAt(srIndex, 5).toString());
    }//GEN-LAST:event_tableDepartmentMouseClicked

    private void btnDeleteDpmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDpmActionPerformed
        removeDepartment();
        insertCbxDepartmentFromEmployeeTable();
    }//GEN-LAST:event_btnDeleteDpmActionPerformed

    private void btnUpdateDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDepartmentActionPerformed
        srIndex = tableDepartment.getSelectedRow();
        if (srIndex != -1) {
            int id = Integer.parseInt(txtDpmID.getText());
            for (Department d : departmentList) {
                if (d.getIdPhongBan() == id) {
                    d.setTenPhongBan(txtDepartmentName.getText());
                    d.setDiaChi(txtDpmAddress.getText());
                    d.setSdtPhong(txtDpmPhoneNumber.getText());
                    d.setGhiChu(txtDpmNote.getText());

                    updateDepartmentTable(departmentList);
                    clearDepartmentTextfields();
                    txtDpmID.setEditable(true);
                    JOptionPane.showMessageDialog(null, "Đã sửa thông tin phòng ban!!");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Chưa chọn bản ghi!");
        }
    }//GEN-LAST:event_btnUpdateDepartmentActionPerformed

    private void btnSaveDepartmentDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDepartmentDataActionPerformed
        int op = JOptionPane.showConfirmDialog(null, "Bạn có muốn lưu dữ liệu?");
        if (op == JOptionPane.YES_OPTION) {
            writeDepartmentToFile();
        } else {
            return;
        }
    }//GEN-LAST:event_btnSaveDepartmentDataActionPerformed

    private void btnAddDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDepartmentActionPerformed
        if (txtDpmID.getText().isEmpty() || txtDepartmentName.getText().isEmpty() || txtDpmAddress.getText().isEmpty() || txtDpmPhoneNumber.getText().isEmpty() || txtDpmNote.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
        } else {
            if (isCheckDepartID(Integer.parseInt(txtDpmID.getText()))) {
                JOptionPane.showMessageDialog(null, "Mã phòng ban này đã tồn tại. Vui lòng nhập mã khác");
            } else {
                addDepartmentList();
                insertCbxDepartmentFromEmployeeTable();
                updateDepartmentTable(departmentList);
                clearDepartmentTextfields();
                JOptionPane.showMessageDialog(null, "Thêm thành công!!");
            }
        }
    }//GEN-LAST:event_btnAddDepartmentActionPerformed

    private void btnSearchDpmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchDpmActionPerformed
        if (txtFindDepartment.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên cần tìm!!");
        } else {
            List<Department> findList = new ArrayList<>();
            String searchText = txtFindDepartment.getText().trim().toLowerCase();
            for (Department d : departmentList) {
                if (d.getTenPhongBan().toLowerCase().contains(searchText)) {
                    findList.add(d);
                }
            }
            updateDepartmentTable(findList);
        }
    }//GEN-LAST:event_btnSearchDpmActionPerformed

    private void btnPrintEmpListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintEmpListActionPerformed
        try {
            MessageFormat header = new MessageFormat("Danh sách nhân viên");
            MessageFormat footer = new MessageFormat("Page{0,number, integer}");
            tableEmployee.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnPrintEmpListActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image image = imageIcon.getImage().getScaledInstance(labelImg.getWidth(), labelImg.getHeight(), Image.SCALE_SMOOTH);
            labelImg.setIcon(new ImageIcon(image));
            try {
                saveImage(selectedFile);
                checkImg = true;
            } catch (IOException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            checkImg = false;
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    private void txtPhoneNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneNumKeyTyped

    private void tableEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeeMouseClicked
        model = (DefaultTableModel) tableEmployee.getModel();
        srIndex = tableEmployee.getSelectedRow();
        txtMaNV.setText(model.getValueAt(srIndex, 0).toString());
        txtMaNV.setEditable(false);
        txtHoTen.setText(model.getValueAt(srIndex, 1).toString());

        String birthTime = model.getValueAt(srIndex, 2).toString();
        String[] parts = birthTime.split("/");
        birthDay.setSelectedItem(parts[0]);
        birthMonth.setSelectedItem(parts[1]);
        birthYear.setSelectedItem(parts[2]);

        txtGender.setText(model.getValueAt(srIndex, 3).toString());
        txtHomeTown.setText(model.getValueAt(srIndex, 4).toString());
        txtPhoneNum.setText(model.getValueAt(srIndex, 5).toString());
        txtEmail.setText(model.getValueAt(srIndex, 6).toString());
        txtContactAddress.setText(model.getValueAt(srIndex, 7).toString());
        hireDate.setText(model.getValueAt(srIndex, 8).toString());
        salary.setText(model.getValueAt(srIndex, 9).toString());

        String department = model.getValueAt(srIndex, 10).toString();
        CbxDepartmentFromET.setSelectedItem(department);

        txtPosition.setText(model.getValueAt(srIndex, 11).toString());
        String image_path = model.getValueAt(srIndex, 12).toString();
        if (image_path != null && !image_path.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(image_path);
            Image originImage = imageIcon.getImage();

            Image resizedImage = originImage.getScaledInstance(labelImg.getWidth(), labelImg.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon resizeIcon = new ImageIcon(resizedImage);
            labelImg.setIcon(resizeIcon);
        } else {
            labelImg.setIcon(null);
        }
    }//GEN-LAST:event_tableEmployeeMouseClicked

    private void btnDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmployeeActionPerformed
        removeEmployeeFromTable();
    }//GEN-LAST:event_btnDeleteEmployeeActionPerformed

    private void btnUpdateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEmployeeActionPerformed
        srIndex = tableEmployee.getSelectedRow();
        if (srIndex != -1) {
            int id = Integer.parseInt(txtMaNV.getText());
            for (Employee e : employeesList) {
                if (e.getEmployeeID() == id) {
                    e.setEmployeeName(txtHoTen.getText());
                    e.setDateOfBirth(birthDay.getSelectedItem() + "/" + birthMonth.getSelectedItem() + "/"
                            + birthYear.getSelectedItem());
                    e.setGender(txtGender.getText());
                    e.setHometown(txtHomeTown.getText());
                    e.setPhoneNumber(txtPhoneNum.getText());
                    e.setEmail(txtEmail.getText());
                    e.setAddress(txtContactAddress.getText());
                    e.setHireDate(hireDate.getText());
                    e.setSalary(Double.parseDouble(salary.getText()));
                    e.setDepartment(CbxDepartmentFromET.getSelectedItem().toString());
                    e.setPosition(txtPosition.getText());
                    if (checkImg) {
                        e.setImagePath("ImgEmployee\\" + getFileName);
                    }
                    updateEmployeeTable(employeesList);
                    clearEmployeeTextFields();
                    txtMaNV.setEditable(true);
                    JOptionPane.showMessageDialog(null, "Đã sửa thông tin nhân viên!!");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Chưa chọn bản ghi!");
        }
    }//GEN-LAST:event_btnUpdateEmployeeActionPerformed

    private void btnSaveEmployeeDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveEmployeeDataActionPerformed
        int op = JOptionPane.showConfirmDialog(null, "Bạn có muốn lưu dữ liệu?");
        if (op == JOptionPane.YES_OPTION) {
            writeEmployeeListToFile();
        } else {
            return;
        }
    }//GEN-LAST:event_btnSaveEmployeeDataActionPerformed

    private void btnAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployeeActionPerformed
        if (txtMaNV.getText().isEmpty() || txtHoTen.getText().isEmpty()
                || txtHomeTown.getText().isEmpty() || txtGender.getText().isEmpty()
                || txtEmail.getText().isEmpty() || txtContactAddress.getText().isEmpty()
                || hireDate.getText().isEmpty() || salary.getText().isEmpty() || !checkDepartmentIsSelected()
                || txtPosition.getText().isEmpty() || txtPhoneNum.getText().isEmpty()
                || !checkBirthdayIsSelected() || !checkImg) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
        } else {
            if (isCheckEmpID(Integer.parseInt(txtMaNV.getText()))) {
                JOptionPane.showMessageDialog(null, "Mã nhân viên này đã tồn tại. "
                        + "Vui lòng nhập mã nhân viên khác");
            } else {
                addEmployeeList();
                updateEmployeeTable(employeesList);
                clearEmployeeTextFields();
                JOptionPane.showMessageDialog(null, "Thêm thành công!!");
            }
        }
    }//GEN-LAST:event_btnAddEmployeeActionPerformed

    private void btnSearchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEmpActionPerformed
        if (txtFindEmployee.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên cần tìm!!");
        } else {
            List<Employee> findList = new ArrayList<>();
            String searchText = txtFindEmployee.getText().trim().toLowerCase();
            for (Employee e : employeesList) {
                if (e.getEmployeeName().toLowerCase().contains(searchText)) {
                    findList.add(e);
                }
            }
            updateEmployeeTable(findList);
        }
    }//GEN-LAST:event_btnSearchEmpActionPerformed

    private void btnDelAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelAccActionPerformed
        removeAccount();
        writeAccountToFile();
    }//GEN-LAST:event_btnDelAccActionPerformed

    private void txtDpmPhoneNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDpmPhoneNumberKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDpmPhoneNumberKeyTyped

    private boolean checkPhoneNumber() {
        if (txtPhoneNum.getText().trim().length() < 10 || txtPhoneNum.getText().trim().length() > 12) {
            return false;
        }
        if (txtDpmPhoneNumber.getText().trim().length() < 10 || txtDpmPhoneNumber.getText().trim().length() > 12) {
            return false;
        }

        return true;
    }

    private boolean checkEmail() {
        if (!txtEmail.getText().trim().matches(EMAIL_PATTERN)) {
            return false;
        }
        return true;
    }
    
    private boolean checkHireDate(){
        if(hireDate.getText().trim().matches(DATE_PATTERN)){
            return false;
        }
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JComboBox<String> CbxDepartmentFromET;
    javax.swing.JComboBox<String> birthDay;
    javax.swing.JComboBox<String> birthMonth;
    javax.swing.JComboBox<String> birthYear;
    javax.swing.JButton btnAccout;
    javax.swing.JButton btnAddDepartment;
    javax.swing.JButton btnAddEmployee;
    javax.swing.JButton btnAddPosition;
    javax.swing.JButton btnDelAcc;
    javax.swing.JButton btnDeleteDpm;
    javax.swing.JButton btnDeleteEmployee;
    javax.swing.JButton btnDeletePosition;
    javax.swing.JButton btnPrintDpmList;
    javax.swing.JButton btnPrintEmpList;
    javax.swing.JButton btnPrintPosList;
    javax.swing.JButton btnQuitClicked;
    javax.swing.JButton btnSaveDepartmentData;
    javax.swing.JButton btnSaveEmployeeData;
    javax.swing.JButton btnSavePos;
    javax.swing.JButton btnSearchDpm;
    javax.swing.JButton btnSearchEmp;
    javax.swing.JButton btnSearchPos;
    javax.swing.JButton btnUpdateDepartment;
    javax.swing.JButton btnUpdateEmployee;
    javax.swing.JButton btnUpdatePosition;
    javax.swing.JButton btnUpload;
    JCalendar.CalendarPanel calendarPanel1;
    javax.swing.JTextField hireDate;
    javax.swing.JCheckBox jCheckBox1;
    javax.swing.JCheckBox jCheckBox10;
    javax.swing.JCheckBox jCheckBox11;
    javax.swing.JCheckBox jCheckBox12;
    javax.swing.JCheckBox jCheckBox13;
    javax.swing.JCheckBox jCheckBox14;
    javax.swing.JCheckBox jCheckBox15;
    javax.swing.JCheckBox jCheckBox16;
    javax.swing.JCheckBox jCheckBox2;
    javax.swing.JCheckBox jCheckBox3;
    javax.swing.JCheckBox jCheckBox4;
    javax.swing.JCheckBox jCheckBox5;
    javax.swing.JCheckBox jCheckBox6;
    javax.swing.JCheckBox jCheckBox7;
    javax.swing.JCheckBox jCheckBox8;
    javax.swing.JCheckBox jCheckBox9;
    javax.swing.JComboBox<String> jComboBox2;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel10;
    javax.swing.JLabel jLabel11;
    javax.swing.JLabel jLabel12;
    javax.swing.JLabel jLabel13;
    javax.swing.JLabel jLabel14;
    javax.swing.JLabel jLabel15;
    javax.swing.JLabel jLabel16;
    javax.swing.JLabel jLabel17;
    javax.swing.JLabel jLabel18;
    javax.swing.JLabel jLabel19;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel20;
    javax.swing.JLabel jLabel21;
    javax.swing.JLabel jLabel22;
    javax.swing.JLabel jLabel23;
    javax.swing.JLabel jLabel24;
    javax.swing.JLabel jLabel25;
    javax.swing.JLabel jLabel26;
    javax.swing.JLabel jLabel27;
    javax.swing.JLabel jLabel28;
    javax.swing.JLabel jLabel29;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabel30;
    javax.swing.JLabel jLabel31;
    javax.swing.JLabel jLabel32;
    javax.swing.JLabel jLabel33;
    javax.swing.JLabel jLabel34;
    javax.swing.JLabel jLabel35;
    javax.swing.JLabel jLabel36;
    javax.swing.JLabel jLabel37;
    javax.swing.JLabel jLabel38;
    javax.swing.JLabel jLabel39;
    javax.swing.JLabel jLabel4;
    javax.swing.JLabel jLabel40;
    javax.swing.JLabel jLabel41;
    javax.swing.JLabel jLabel42;
    javax.swing.JLabel jLabel43;
    javax.swing.JLabel jLabel44;
    javax.swing.JLabel jLabel45;
    javax.swing.JLabel jLabel46;
    javax.swing.JLabel jLabel47;
    javax.swing.JLabel jLabel48;
    javax.swing.JLabel jLabel49;
    javax.swing.JLabel jLabel5;
    javax.swing.JLabel jLabel50;
    javax.swing.JLabel jLabel51;
    javax.swing.JLabel jLabel52;
    javax.swing.JLabel jLabel53;
    javax.swing.JLabel jLabel54;
    javax.swing.JLabel jLabel6;
    javax.swing.JLabel jLabel7;
    javax.swing.JLabel jLabel8;
    javax.swing.JLabel jLabel9;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel10;
    javax.swing.JPanel jPanel11;
    javax.swing.JPanel jPanel12;
    javax.swing.JPanel jPanel13;
    javax.swing.JPanel jPanel14;
    javax.swing.JPanel jPanel15;
    javax.swing.JPanel jPanel16;
    javax.swing.JPanel jPanel17;
    javax.swing.JPanel jPanel18;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel21;
    javax.swing.JPanel jPanel22;
    javax.swing.JPanel jPanel27;
    javax.swing.JPanel jPanel3;
    javax.swing.JPanel jPanel4;
    javax.swing.JPanel jPanel5;
    javax.swing.JPanel jPanel6;
    javax.swing.JPanel jPanel7;
    javax.swing.JPanel jPanel8;
    javax.swing.JPanel jPanel9;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane10;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JScrollPane jScrollPane4;
    javax.swing.JScrollPane jScrollPane6;
    javax.swing.JScrollPane jScrollPane9;
    javax.swing.JTabbedPane jTabbedPane1;
    javax.swing.JTextField jTextField1;
    javax.swing.JLabel labelImg;
    javax.swing.JTextField salary;
    javax.swing.JTable tableDepartment;
    javax.swing.JTable tableES;
    javax.swing.JTable tableEmployee;
    javax.swing.JTable tablePosition;
    javax.swing.JTable tblAccountDivide;
    javax.swing.JTextField txtContactAddress;
    javax.swing.JTextField txtDepartmentName;
    javax.swing.JTextField txtDpmAddress;
    javax.swing.JTextField txtDpmID;
    javax.swing.JTextArea txtDpmNote;
    javax.swing.JTextField txtDpmPhoneNumber;
    javax.swing.JTextField txtEmail;
    javax.swing.JTextField txtFindDepartment;
    javax.swing.JTextField txtFindEmployee;
    javax.swing.JTextField txtFindPosition;
    javax.swing.JTextField txtGender;
    javax.swing.JTextField txtHoTen;
    javax.swing.JTextField txtHomeTown;
    javax.swing.JTextField txtMaNV;
    javax.swing.JTextField txtPhoneNum;
    javax.swing.JTextField txtPosID;
    javax.swing.JTextArea txtPosNote;
    javax.swing.JTextField txtPosition;
    javax.swing.JTextField txtPositionName;
    // End of variables declaration//GEN-END:variables

    private void clearEmployeeTextFields() {
        txtMaNV.setText(null);
        txtHoTen.setText(null);
        txtHomeTown.setText(null);
        txtGender.setText(null);
        txtEmail.setText(null);
        txtContactAddress.setText(null);
        hireDate.setText(null);
        salary.setText(null);
        txtPosition.setText(null);
        txtPhoneNum.setText(null);
        labelImg.setIcon(null);
        CbxDepartmentFromET.setSelectedIndex(0);
        birthDay.setSelectedIndex(-1);
        birthMonth.setSelectedIndex(-1);
        birthYear.setSelectedIndex(-1);
    }

    private void clearDepartmentTextfields() {
        txtDpmID.setText(null);
        txtDepartmentName.setText(null);
        txtDpmAddress.setText(null);
        txtDpmPhoneNumber.setText(null);
        txtDpmNote.setText(null);
    }

    private void clearPositionTextFields() {
        txtPosID.setText(null);
        txtPositionName.setText(null);
        txtPosNote.setText(null);
    }
}
