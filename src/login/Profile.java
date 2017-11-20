package login;

public class Profile {
	private String user;
    private String password;
    private Perfil perfil;
    
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getProfile(int id, int app){
    	perfil = new Perfil(this.user, this.password);
    	return perfil.getProfile(id,app);
    }
    
    public String updateProfile(int id, String name, String email){
    	perfil = new Perfil(this.user, this.password);
    	return perfil.updateProfile(id, name, email);
    }
    
    public String listarUsuarios(){
    	perfil = new Perfil(this.user, this.password);
    	return perfil.listarUsuarios();
    }
    
    public String listarRoles(String user_id){
    	perfil = new Perfil(this.user, this.password);
    	return perfil.listarRoles(user_id);
    }
    
    public String updateAccess(String user_id, String rol){
    	perfil = new Perfil(this.user, this.password);
    	return perfil.updateAccess(user_id, rol);
    }

}
