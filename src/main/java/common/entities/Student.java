package main.java.common.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {
    private Integer number;
    private String fiscalCode;
    private String firstName;
    private String lastName;

    @Id
    @Column(name = "number", nullable = false)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Basic
    @Column(name = "fiscal_code", nullable = false, length = 16)
    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
       if(o == this) return true;
       if(! (o instanceof Student)) return false;
       Student s = (Student) o;
       return s.number == number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        return number + " - " + firstName + " " + lastName;
    }

}
