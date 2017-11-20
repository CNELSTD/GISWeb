package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Perfil {
	private String driver = null;
	private String url = null;
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	public String username;
	public String pass;
	public String pecodi;
	public String penomb;
	public String pefelo;
	public String pehora="";
	public String pezimbramail;
	public String pezimbrapass;
	public String petype;
	public String petipo;
	public int pedire;
	public int pececo;
	public int peocup;
	public String myapp;
	public String secure;
	private String fecha;
	private String hora;
	public String msg;
	public Perfil(){
		this.driver="com.ibm.as400.access.AS400JDBCDriver";
		this.url = "jdbc:as400:172.30.1.167;prompt=false";
		Format date_format= new SimpleDateFormat("yyyyMMdd");
		Format hora_format1= new SimpleDateFormat("hhmm");
		Date date=new Date();
		this.fecha = date_format.format(date);
		this.hora = hora_format1.format(date);
	}
	
	public Perfil(String user, String pass){
		this.username = user;
		this.pass = pass;
		this.driver="com.ibm.as400.access.AS400JDBCDriver";
		this.url = "jdbc:as400:172.30.1.167;prompt=false";
	}
	
	public boolean Login(){
		boolean foo=false;
		try{
			TimeZone.setDefault(TimeZone.getTimeZone("GMT-05:00"));
			Locale.setDefault(new Locale("es","ES")); 
			Class.forName(driver).newInstance();
			String sql="";
			this.username = this.username.toUpperCase();
			this.con = DriverManager.getConnection(url,this.username,this.pass);
			this.stm = this.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			sql="SELECT * FROM EMELSAD.USUF01 AS us WHERE us.PEASUS='"+this.username+"' FETCH FIRST 1 ROWS ONLY";
			rs=stm.executeQuery(sql);
			String codigo = "";
			if( !rs.next() ){
				sql="INSERT INTO EMELSAD.USUF01 (PENOMB, PEASUS, PEFELO, PETIPO, PEESTA, PEHORA, PEDIRE, PEMAIL, PEPASS) VALUES ('"+this.username+"', '"+this.username+"', '"+this.fecha+"', 'user', '1', '"+this.hora+"',0, 'usuario@std.cnel.ec', 'password')";
				stm.clearBatch();
				stm.executeUpdate(sql);
				sql="SELECT PECODI FROM EMELSAD.USUF01 AS us WHERE us.PEASUS='"+this.username+"' FETCH FIRST 1 ROWS ONLY";
				rs=null;
				stm.clearBatch();
				rs=stm.executeQuery(sql);
				if( rs.next() ){
					codigo = rs.getString("PECODI");
					this.pecodi = codigo;
					this.penomb = this.username;
					this.petipo = "user";
					this.pezimbramail = "";
					this.pezimbrapass = "";
					this.pefelo = this.fecha;
					this.pehora = this.hora;
					this.pedire = 0;
					this.pececo = 0;
					this.peocup = 0;
				}
			}else{
				codigo = rs.getString("PECODI");
				this.pecodi = codigo;
				this.penomb = rs.getString("penomb");
				this.petipo = rs.getString("petipo");
				this.pezimbramail = rs.getString("pemail");
				this.pezimbrapass = rs.getString("pepass");
				this.pedire = rs.getInt("pedire");
				this.pececo = rs.getInt("pececo");
				this.peocup = rs.getInt("peocup");
				this.pefelo = rs.getString("pefelo").substring(0, 4)+"-"+rs.getString("pefelo").substring(4, 6)+"-"+rs.getString("pefelo").substring(6, 8);
				if (rs.getString("pehora") !=null)	this.pehora = (rs.getInt("pehora")<1000)? "0"+rs.getString("pehora").substring(0,1)+":"+rs.getString("pehora").substring(1,3) : rs.getString("pehora").substring(0,2)+":"+rs.getString("pehora").substring(2,4);
				else this.pehora = this.hora.substring(0,2)+":"+this.hora.substring(3,4);
				sql = "UPDATE EMELSAD.USUF01 SET PEFELO='"+this.fecha+"', PEHORA='"+this.hora+"', PEAPPS='"+this.myapp+"' WHERE PECODI='"+rs.getString("pecodi")+"'";
				stm.executeUpdate(sql);
			}
			rs=null;
			sql="SELECT * FROM EMELSAD.APLF02 AS ap WHERE ap.ASPECO='"+codigo+"' AND ap.ASAPCO='"+this.myapp+"' FETCH FIRST 1 ROWS ONLY";
			stm.clearBatch();
			rs=stm.executeQuery(sql);
			if( !rs.next() ){
				this.petype = "user";
				sql="INSERT INTO EMELSAD.APLF02 (ASAPCO, ASPECO, ASTYPE) VALUES ('"+this.myapp+"', '"+codigo+"', '"+this.petype+"')";
				stm.clearBatch();
				stm.executeUpdate(sql);
			}else{
				this.petype = rs.getString("astype");
			}
			foo=true;
			closeConnection();
		}
		catch(java.lang.ClassNotFoundException e){ 
			e.printStackTrace();
			this.msg = e.getMessage().substring(e.getMessage().indexOf("(")+1, e.getMessage().indexOf(")"));
		}catch(SQLException e){
			e.printStackTrace();
			this.msg = e.getMessage().substring(e.getMessage().indexOf("(")+1, e.getMessage().indexOf(")"));
		}catch(InstantiationException e){
			e.printStackTrace();
			this.msg = e.getMessage().substring(e.getMessage().indexOf("(")+1, e.getMessage().indexOf(")"));
		}catch (IllegalAccessException e){
			e.printStackTrace();
			this.msg = e.getMessage().substring(e.getMessage().indexOf("(")+1, e.getMessage().indexOf(")"));
		};
		return foo;
	}
	
	private void openConnection(){
    	TimeZone.setDefault(TimeZone.getTimeZone("GMT-05:00"));
    	try{
			Class.forName(driver).newInstance();
			this.con = DriverManager.getConnection(url,username,pass);
			this.stm = this.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}catch(java.lang.ClassNotFoundException e){ 
			e.printStackTrace();
		}catch(SQLException e){ 
			e.printStackTrace(); 
		}catch (InstantiationException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		};
	}
	
	public String getProfile(int id, int app){
		String respuesta = "";
    	openConnection();
    	String query = "SELECT PENOMB, ACNOMB, PEMAIL, PEASUS, PEFELO, PEHORA, ASAPCO FROM EMELSAD.USUF01 AS u "+
    		"INNER JOIN EMELSAD.APLF02 AS a ON (a.ASPECO = u.PECODI) "+
    		"INNER JOIN EMELSAD.APLF03 AS b ON (a.ASTYPE = b.ACVALU) "+
    		"WHERE PECODI="+id+" AND a.ASAPCO="+app;
    	try{
    		rs = stm.executeQuery(query);
        	respuesta+="{\"status\":\"OK\", ";
        	if (rs.next()){
        		respuesta +="\"name\":\""+rs.getString("PENOMB")+"\", " +
    			"\"email\":\""+rs.getString("PEMAIL")+"\", " +
    			"\"rol\":\""+rs.getString("ACNOMB")+"\", " +
    			"\"lastLogin\":\""+rs.getString("PEFELO").substring(0, 4)+"-"+rs.getString("PEFELO").substring(4, 6)+"-"+rs.getString("PEFELO").substring(6, 8)+" "+rs.getString("PEHORA").substring(0, 2)+":"+rs.getString("PEHORA").substring(2, 4)+"\"," +
    			"\"alias\":\""+rs.getString("PEASUS")+"\"";
        	}
        	respuesta+="}";
        }catch (Exception e){
            e.printStackTrace();
            respuesta += "{\"status\":\"fail\", \"msg\":\""+e.getMessage()+"\"}";
        }
        closeConnection();
        return respuesta;
	}
	
	
	public String updateProfile(int id, String name, String email){
    	String respuesta = "";
    	openConnection();
    	String query = "UPDATE EMELSAD.USUF01 SET PENOMB='"+name+"', PEMAIL='"+email+"' WHERE PECODI='"+id+"'";
    	try{
    		stm.executeUpdate(query);
        	respuesta += "{\"status\":\"OK\"}";
        }catch (Exception e){
            e.printStackTrace();
            respuesta += "{\"status\":\"fail\", \"msg\":\""+e.getMessage()+"\"}";
        }
        closeConnection();
        return respuesta;
    }
	
	public String listarUsuarios(){
		String respuesta = "";
    	openConnection();
    	String query = "SELECT PECODI, PENOMB, PEASUS, PETIPO FROM EMELSAD.USUF01 AS u WHERE EXISTS (SELECT * FROM EMELSAD.APLF02 AS a WHERE a.ASPECO = u.PECODI AND a.ASAPCO=6)";
    	try {
            rs = stm.executeQuery(query);
            respuesta="{\"status\":\"OK\", \"Users\":[ ";
            while(rs.next()){
                respuesta += "{"+
                	"\"id\":\""+rs.getInt("PECODI")+"\", " +
                	"\"name\":\"" + rs.getString("PENOMB") + "\""+
                "},";
            }
            respuesta = respuesta.substring(0, respuesta.length()-1);
			respuesta+="]}";
        }catch (Exception e){
            e.printStackTrace();
            respuesta = "{\"status\":\"fail\",\"msg\":\""+e.getMessage()+"\"}";
        }
        closeConnection();
        return respuesta;
	}
	
	public String listarRoles(String user_id){
		String respuesta = "";
    	openConnection();
    	String query = "SELECT ACNOMB, ACVALU, (SELECT 1 FROM EMELSAD.APLF02 AS a WHERE ASPECO="+user_id+" AND ASAPCO=6 AND a.ASTYPE = b.ACVALU) AS SEL FROM EMELSAD.APLF03 AS b";
    	try {
            rs = stm.executeQuery(query);
            respuesta="{\"status\":\"OK\", \"Roles\":[ ";
            while(rs.next()){
                respuesta += "{"+
                	"\"name\":\"" + rs.getString("ACNOMB") + "\", "+
                	"\"type\":\""+rs.getString("ACVALU")+"\", " +
                	"\"selected\":\""+rs.getString("SEL")+"\"" +
                "},";
            }
            respuesta = respuesta.substring(0, respuesta.length()-1);
			respuesta+="]}";
        }catch (Exception e){
            e.printStackTrace();
            respuesta = "{\"status\":\"fail\",\"msg\":\""+e.getMessage()+"\"}";
        }
        closeConnection();
        return respuesta;
	}
	
	public String updateAccess(String user_id, String rol){
		String respuesta = "{\"status\":\"OK\"}";
		openConnection();
		try{
			stm.executeUpdate("UPDATE EMELSAD.APLF02 SET ASTYPE='"+rol+"' WHERE ASPECO='"+user_id+"' AND ASAPCO='6'");
		}catch(Exception e){
			respuesta = "{\"status\":\"fail\",\"msg\":\""+e.getMessage()+"\"}";
		}
		closeConnection();
		return respuesta;
	}
	
	private void closeConnection(){
		try{
			if (this.rs!=null) this.rs.close();
			if (this.stm!=null) this.stm.close();
			if (this.con!=null) this.con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
