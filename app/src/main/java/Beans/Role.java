package Beans;

/**
 * Created by mohamed salah on 18/10/2016.
 */
public class Role {
    private int id;
    private String user_role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public Role(int id, String role)
    {this.id=id;
    this.user_role=role;}

    public Role(){}
}
