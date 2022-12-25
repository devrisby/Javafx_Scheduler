package org.devrisby.c195.models;

public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private FirstLvlDivision division;

    public Customer(int customerID, String customerName, String address, String postalCode, String phone, FirstLvlDivision division) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    public Customer(String customerName, String address, String postalCode, String phone, FirstLvlDivision division) {
        this(-1, customerName, address, postalCode, phone, division);
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public FirstLvlDivision getDivision() {
        return division;
    }

    public void setDivision(FirstLvlDivision division) {
        this.division = division;
    }
}
