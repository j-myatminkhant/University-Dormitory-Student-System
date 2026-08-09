/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author User
 */

public class EmployeeDetails {
    // model class holding getters, setters and properties
    private StringProperty id;
    private StringProperty name;
    private StringProperty empId;
    private StringProperty email;
    private StringProperty nic;
    private StringProperty tel;
    private StringProperty address;
    private StringProperty department;
    private StringProperty position;
    private StringProperty emgTel;
    private StringProperty deletedDate;
    
    public EmployeeDetails(String id, String name, String empId, String email, String nic, String tel, String address, String department, String position, String emgTel) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.empId = new SimpleStringProperty(empId);
        this.email = new SimpleStringProperty(email);
        this.nic = new SimpleStringProperty(nic);
        this.tel = new SimpleStringProperty(tel);
        this.address = new SimpleStringProperty(address);
        this.department = new SimpleStringProperty(department);
        this.position = new SimpleStringProperty(position);
        this.emgTel = new SimpleStringProperty(emgTel);
    }
    
     // Getters
    public String getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }

    public String getEmpId() {
        return empId.get();
    }
    
    public String getEmail() {
        return email.get();
    }

    public String getNic() {
        return nic.get();
    }

    public String getTel() {
        return tel.get();
    }
    
    public String getAddress() {
        return address.get();
    }
    
    public String getDepartment() {
        return department.get();
    }
    
    public String getPosition() {
        return position.get();
    }
    
    public String getEmgTel() {
        return emgTel.get();
    }
    
    // Setters
    public void setId(String value) {
        id.set(value);
    }
    
    public void setName(String value) {
        name.set(value);
    }
    
    public void setEmpId(String value) {
        empId.set(value);
    }
    
    public void setEmail(String value) {
        email.set(value);
    }

    public void setNic(String value) {
        nic.set(value);
    }

    public void setTel(String value) {
        tel.set(value);
    }
    
    public void setAddress(String value) {
        address.set(value);
    }
    
    public void setDepartment(String value) {
        department.set(value);
    }
    
    public void setPosition(String value) {
        position.set(value);
    }
    
    public void setEmgTel(String value) {
        emgTel.set(value);
    }
    
    // Property values
    
    public StringProperty idProperty() { return id; }
    
    public StringProperty nameProperty() { return name; }
    
    public StringProperty empIdProperty() { return empId; }
    
    public StringProperty emailProperty() { return email; }
    
    public StringProperty nicProperty() { return nic; }
    
    public StringProperty telProperty() { return tel; }
    
    public StringProperty addressProperty() { return address; }
    
    public StringProperty departmentProperty() { return department; }
    
    public StringProperty positionProperty() { return position; }
    
    public StringProperty emgTelProperty() { return emgTel; }
    
    public String getDeletedDate() {
        return deletedDate != null ? deletedDate.get() : null;
    }
    
    public void setDeletedDate(String deletedDate) {
        if (this.deletedDate == null) {
            this.deletedDate = new SimpleStringProperty();
        }
        this.deletedDate.set(deletedDate);
    }
    
    public StringProperty deletedDateProperty() { return deletedDate; }

}
