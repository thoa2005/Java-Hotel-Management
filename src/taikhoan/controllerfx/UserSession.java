package taikhoan.controllerfx;

public class UserSession {
	private static int idUser;
    public static void setIdUser(int id) { idUser = id; }
    public static int getIdUser() { return idUser; }
    public static void deleteUser() {idUser=0;};
}
