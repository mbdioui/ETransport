package Beans;

/**
 * Created by mohamed salah on 18/10/2016.
 */

public class User {
    private int id;
    private String f_name;
    private String l_name;
    private String mail;
    private String password;
    private String phone;
    private int active;
    private String confirmation_code;
    private Role user_role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public void setConfirmation_code(String confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public Role getUser_role() {
        return user_role;
    }

    public void setUser_role(Role user_role) {
        this.user_role = user_role;
    }
    public User(){}
    public User(int id, String f_name, String l_name, String mail, String password, String phone, int active, String confirmation_code, Role user_role) {
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.mail = mail;
        this.password = password;
        this.phone = phone;
        this.active = active;
        this.confirmation_code = confirmation_code;
        this.user_role = user_role;
    }
}
