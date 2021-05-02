package app.dmmunoz.mifamilia;

public class Usuario {

    private String UserID;
    private String Name;
    private String Email;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public Usuario()
    {

    }

    public Usuario(String uid, String name, String email) {
        UserID = uid;
        Name = name;
        Email = email;

    }

}
