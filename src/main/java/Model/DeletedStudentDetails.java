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
public class DeletedStudentDetails {

    // model class holding getters, setters and properties for deleted students
    private StringProperty id;
    private StringProperty originalId;
    private StringProperty name;
    private StringProperty nsbmid;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty nic;
    private StringProperty address;
    private StringProperty guardName;
    private StringProperty guardTel;
    private StringProperty room;
    private StringProperty deletedDate;
    private StringProperty deletedBy;

    public DeletedStudentDetails(String id, String originalId, String name, String nsbmid, String email, 
                               String phoneNumber, String nic, String address, String guardName, 
                               String guardTel, String room, String deletedDate, String deletedBy) {
        this.id = new SimpleStringProperty(id);
        this.originalId = new SimpleStringProperty(originalId);
        this.name = new SimpleStringProperty(name);
        this.nsbmid = new SimpleStringProperty(nsbmid);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.nic = new SimpleStringProperty(nic);
        this.address = new SimpleStringProperty(address);
        this.guardName = new SimpleStringProperty(guardName);
        this.guardTel = new SimpleStringProperty(guardTel);
        this.room = new SimpleStringProperty(room);
        this.deletedDate = new SimpleStringProperty(deletedDate);
        this.deletedBy = new SimpleStringProperty(deletedBy);
    }

    // Getters
    public String getId() {
        return id.get();
    }
    
    public String getOriginalId() {
        return originalId.get();
    }
    
    public String getName() {
        return name.get();
    }

    public String getNsbmId() {
        return nsbmid.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getNic() {
        return nic.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getGuardName() {
        return guardName.get();
    }

    public String getGuardTel() {
        return guardTel.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    public String getDeletedDate() {
        return deletedDate.get();
    }
    
    public String getDeletedBy() {
        return deletedBy.get();
    }

    // Setters
    public void setId(String value) {
        id.set(value);
    }
    
    public void setOriginalId(String value) {
        originalId.set(value);
    }
    
    public void setName(String value) {
        name.set(value);
    }

    public void setNsbmid(String value) {
        nsbmid.set(value);
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    public void setNic(String value) {
        nic.set(value);
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public void setGuardName(String value) {
        guardName.set(value);
    }

    public void setGuardTel(String value) {
        guardTel.set(value);
    }

    public void setRoom(String value) {
        room.set(value);
    }
    
    public void setDeletedDate(String value) {
        deletedDate.set(value);
    }
    
    public void setDeletedBy(String value) {
        deletedBy.set(value);
    }
    
    // Property values
    public StringProperty idProperty() { return id; }
    public StringProperty originalIdProperty() { return originalId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty nsbmIdProperty() { return nsbmid; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty nicProperty() { return nic; }
    public StringProperty addressProperty() { return address; }
    public StringProperty guardNameProperty() { return guardName; }
    public StringProperty guardTelProperty() { return guardTel; }
    public StringProperty roomProperty() { return room; }
    public StringProperty deletedDateProperty() { return deletedDate; }
    public StringProperty deletedByProperty() { return deletedBy; }

    public String getNIC() {
        return nic.get();
    }
}
