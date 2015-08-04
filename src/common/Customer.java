package common;

import java.io.Serializable;


public class Customer implements Serializable {

    private static int customerNumberCount = 0;
    private int customerReferenceNumber;
    private String name;
    private String street1;
    private String street2;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private static final long serialVersionUID = 1420672609912364060L;

    public Customer() {
        // Need for json deserialization
    }

    public Customer(String name, String street1, String street2, String city, String province, String postalCode, String country) {
        Customer.customerNumberCount++;
        customerReferenceNumber = customerNumberCount;
        this.name = name;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Customer(String name) {
        customerNumberCount++;
        customerReferenceNumber = customerNumberCount;
        this.name = name;
        street1 = null;
        street2 = null;
        city = null;
        province = null;
        postalCode = null;
        country = null;
    }

    public int getCustomerReferenceNumber() {
        return customerReferenceNumber;
    }

    public void setCustomerReferenceNumber(int customerReferenceNumber) {
        this.customerReferenceNumber = customerReferenceNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return customerReferenceNumber + ";;d" +name
                + street1 + street2
                + city + province
                + postalCode + country;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Customer)) {
            return false;
        }
        if(obj == this) {
            return true;
        }

        Customer customer = (Customer) obj;
        return customer.getCustomerReferenceNumber() == getCustomerReferenceNumber();
    }

    @Override
    public int hashCode() {
        return 3 * 50 + getCustomerReferenceNumber();
    }

}
