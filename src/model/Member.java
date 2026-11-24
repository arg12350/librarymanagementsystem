package model;

import java.io.Serializable;

public class Member extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String department;
    private String phone;
    private String email;

    public Member(String id, String name, String department, String phone, String email) {
        super(id, name);
        this.department = department;
        this.phone = phone;
        this.email = email;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getDisplayRole() { return "Library Member"; }
}
