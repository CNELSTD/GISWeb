package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class GISWebDAO {

	private Statement stmt;
	private Statement stmtDB2 = null;
	private ResultSet rs;
	private ResultSet rsDB2 = null;
	private Connection conOra;
	private Connection conDb2 = null;
	private String userORA;
	private String userDB2;
	private String passwordDB2;
	private String passwordORA;
	private boolean estaconectado = false;
	private String query = "";

	public GISWebDAO (){
		try{
			/*this.userORA = "sigelec";
			this.passwordORA = "sigelec";*/
			this.userORA = "igc";
			this.passwordORA = "igc";
			this.userDB2 = "EMESIS14";
			this.passwordDB2 = "EMESIS14";
			openConnection();
			conOra.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private boolean openConnection(){
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=172.18.115.10)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=BDDSIG)))"; /*PRODUCCION*/
		java.util.Locale.setDefault(new java.util.Locale("es","ES"));
		try{
			Class.forName(driver).newInstance();
			conOra = DriverManager.getConnection(url,this.userORA, this.passwordORA);
			stmt = this.conOra.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		catch(Exception e){
			System.out.println("Error en conexión");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean openConnectionDB2(){
		String driver = "com.ibm.as400.access.AS400JDBCDriver";
		String url = "jdbc:as400:172.30.1.167;prompt=false";
		//String url = "jdbc:as400:172.18.113.2;prompt=false";
		try{
			Class.forName(driver).newInstance();
			conDb2 = DriverManager.getConnection(url,this.userDB2, this.passwordDB2);
			stmtDB2 = this.conDb2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		catch(Exception e){
			System.out.println("Error en conexión");
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public String downloadScript(String grupo, String asignacion){
		ArrayList<String> globalIDPostes = new ArrayList<String>();
		ArrayList<Integer> objectIDPostes = new ArrayList<Integer>();
		String alimencode = "";
		//String fecasig = "";
		String respuesta = "DELETE FROM ASIGNACION"+System.getProperty("line.separator")+ 
				"DELETE FROM TRANSACCION"+System.getProperty("line.separator")+ 
				"DELETE FROM POSTE"+System.getProperty("line.separator")+
				"DELETE FROM POSTE_PROPIEDAD"+System.getProperty("line.separator")+
				"DELETE FROM POSTE_TIPOCIMIENTO"+System.getProperty("line.separator")+
				"DELETE FROM POSTE_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM ESTRUCTURA_EN_POSTE"+System.getProperty("line.separator")+
				"DELETE FROM MESTRO_ESTRUCTURAS"+System.getProperty("line.separator")+
				"DELETE FROM TRANSFORMADOR"+System.getProperty("line.separator")+
				"DELETE FROM TRAFOS_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM FASES_CONEXION"+System.getProperty("line.separator")+
				"DELETE FROM MARCA_TRANSFORMADOR"+System.getProperty("line.separator")+
				"DELETE FROM SECCIONADOR"+System.getProperty("line.separator")+
				"DELETE FROM SECCIONADOR_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM SECCIONADOR_TIPO"+System.getProperty("line.separator")+
				"DELETE FROM SECCIONADOR_POSICIONES"+System.getProperty("line.separator")+
				"DELETE FROM CAPACITOR"+System.getProperty("line.separator")+
				"DELETE FROM CAPACITOR_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM TENSOR"+System.getProperty("line.separator")+
				"DELETE FROM TENSOR_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM MEDIDOR"+System.getProperty("line.separator")+
				"DELETE FROM MEDIDOR_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_MEDIA"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_MEDIA_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_BAJA"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_BAJA_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_FASES_CONEXION"+System.getProperty("line.separator")+
				"DELETE FROM LUMINARIA"+System.getProperty("line.separator")+
				"DELETE FROM LUMINARIA_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_NEUTRO"+System.getProperty("line.separator")+
				"DELETE FROM CONDUCTOR_NEUTRO_SUBTIPO"+System.getProperty("line.separator")+
				"DELETE FROM POSTE_REPETIDO"+System.getProperty("line.separator");
		estaconectado = openConnection();
		if (estaconectado){
			try {
				query = "SELECT ID, SUBESTACODE, ALIMENCODE, FECASIG FROM GISWEB_ASIGNACIONES WHERE ESTAASIG = 'ASG' AND GRUPO = '"+grupo+"' AND ID = " + asignacion;
				stmt = conOra.createStatement();
				rs = stmt.executeQuery(query);
				if(rs.next()){
					alimencode = rs.getString(3);
					//fecasig = rs.getString(4);
					respuesta += "INSERT INTO ASIGNACION (NUMASIGNACION, ALIMENTADOR,FECHAASIG,GRUPO) VALUES ("+rs.getInt(1)+",'"+rs.getString(3)+"', '"+rs.getString(4)+"', '"+grupo+"')"+System.getProperty("line.separator");
				}
				rs = null;
				stmt.clearBatch();

				/*query = "SELECT CODIGOELEMENTO AS NUM_POSTE, GLOBALID AS CODIGO, nvl(CODIGOESTRUCTURA,' ') AS ESTRUCTURA, COORD_X, COORD_Y, nvl(PROPIEDAD,' '), nvl(TIPOCIMIENTO,' '), nvl(ATERRAMIENTO,' '), nvl(PUESTOATIERRA,2), SUBTIPO "+ 
						"FROM ESTRUCTURASOPORTE WHERE ROWNUM <= 20000";*/
				query = "SELECT CODIGOELEMENTO AS NUM_POSTE, GLOBALID AS CODIGO, nvl(CODIGOESTRUCTURA,' ') AS ESTRUCTURA, COORD_X, COORD_Y, nvl(PROPIEDAD,' '), nvl(TIPOCIMIENTO,' '), nvl(ATERRAMIENTO,' '), nvl(PUESTOATIERRA,2), SUBTIPO, OBJECTID "+ 
						"FROM SIGELEC.ESTRUCTURASOPORTE WHERE ALIMENTADOR = '"+alimencode+"'";
				stmt = conOra.createStatement();
				rs = stmt.executeQuery(query);
				int cont = 1;
				while(rs.next()){
					globalIDPostes.add(rs.getString(2).trim());
					objectIDPostes.add(rs.getInt(11));
					respuesta += "INSERT INTO POSTE (NUMPOSTE,GLOBALID,ESTRUCTURA,COORDX,COORDY,PROPIEDAD,TIPOCIMIENTO,ATERRAMIENTO,PUESTOTIERRA,SUBTIPO, OBJECTID, REVISADO,FECHAREVISION, HORAREVISION, OBSREVISION, TIPOREVISION, DESCARGADO) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"', '"+rs.getString(3).trim()+"', '"+rs.getString(4).trim()+"', '"+rs.getString(5).trim()+"', '"+rs.getString(6).trim()+"', '"+rs.getString(7).trim()+"', '"+rs.getString(8).trim()+"', "+rs.getInt(9)+", "+rs.getInt(10)+","+rs.getInt(11)+",'','','','','','')"+System.getProperty("line.separator");
					System.out.println("postes: " + cont);
					cont++;
				}

				System.out.println("total objectIds: " + objectIDPostes.size());
				rs = null;
				stmt.clearBatch();
				query = "SELECT * FROM POSTE_PROPIEDAD";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					respuesta += "INSERT INTO POSTE_PROPIEDAD (CODIGO, DESCRIPCION) VALUES ('"+rs.getString(1).trim()+"', '"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID AS ID_CODESTRUCTURA, T1.GLOBALID, nvl(T1.CODIGOESTRUCTURA,' '), nvl(T1.CANTIDAD,0), T1.ESTRUCTURASOPORTEGLOBALID, T2.CODIGOELEMENTO "+
							"FROM SIGELEC.ESTRUCTURAENPOSTE T1 "+
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2  ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) "+
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					while(rs.next()){
						respuesta += "INSERT INTO ESTRUCTURA_EN_POSTE (IDESTRUCTURA, GLOBALID, ESTRUCTURA, CANTIDAD, IDPOSTE, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"', '"+rs.getString(3).trim()+"', "+rs.getInt(4)+", '"+rs.getString(5).trim()+"',"+rs.getInt(6)+",'','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT * FROM IGC.POSTE_TIPOCIMIENTO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					respuesta += "INSERT INTO POSTE_TIPOCIMIENTO (CODIGO, DESCRIPCION) VALUES ('"+rs.getString(1).trim()+"', '"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT * FROM IGC.POSTE_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					respuesta += "INSERT INTO POSTE_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT nvl(CODIGOESTRUCTURA,' '), nvl(DESCRIPCIONCORTA,' '), nvl(DESCRIPCIONLARGA,' ') FROM SIGELEC.CATALOGOESTRUCTURA";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					respuesta += "INSERT INTO MESTRO_ESTRUCTURAS (CODIGOESTRUCTURA, DESCRIPCIONCORTA, DESCRIPCIONLARGA) VALUES ('"+rs.getString(1).trim()+"', '"+rs.getString(2).trim()+"','"+cleanString(rs.getString(3)).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID, T1.GLOBALID, nvl(T1.TRAFO,'0'), T1.SUBTIPO, nvl(T1.FASECONEXION,0), nvl(T1.CODIGOESTRUCTURA,' '), nvl(T1.PROPIEDAD,' '), nvl(T1.ESTRUCTURASOPORTEGLOBALID,' '), T2.CODIGOELEMENTO "+
							"FROM  SIGELEC.PUESTOTRANSFDISTRIBUCION T1 "+ 
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2  ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) "+
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					System.out.println(query);
					while(rs.next()){
						System.out.println("transformador");
						respuesta += "INSERT INTO TRANSFORMADOR (OBJECTID, GLOBALID, NUMTRAFO, SUBTIPO, FASECONEXION, CODIGOESTRUCTURA, PROPIEDAD, POSTEGI, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"', '"+rs.getString(3).trim()+"', "+rs.getInt(4)+", "+rs.getInt(5)+",'"+rs.getString(6).trim()+"','"+rs.getString(7).trim()+"','"+rs.getString(8).trim()+"',"+rs.getInt(9)+",'','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT * FROM IGC.TRAFOS_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a trafos subtipo");
					respuesta += "INSERT INTO TRAFOS_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT * FROM IGC.FASES_CONEXION";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a fases de conexion");
					respuesta += "INSERT INTO FASES_CONEXION (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT distinct(MARCA) FROM SIGELEC.UNIDADTRANSFDISTRIBUCION WHERE MARCA IS NOT NULL";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a marca transformador");
					respuesta += "INSERT INTO MARCA_TRANSFORMADOR (DESCRIPCION) VALUES ('"+rs.getString(1).trim()+"')"+System.getProperty("line.separator");
				}

				/*SECCIONADOR*/
				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID, T1.GLOBALID, T1.TIPO, T1.SUBTIPO, T1.POSICIONACTUAL_A, T1.POSICIONACTUAL_B, T1.POSICIONACTUAL_C,  nvl(T1.CODIGOESTRUCTURA,' '), T1.ESTRUCTURASOPORTEGLOBALID, T2.CODIGOELEMENTO "+
							"FROM SIGELEC.PUESTOSECCIONADORFUSIBLE T1 "+ 
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2 ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) "+
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					System.out.println(query);
					while(rs.next()){
						System.out.println("transformador");
						respuesta += "INSERT INTO SECCIONADOR (OBJECTID, GLOBALID, TIPO, SUBTIPO, POSICIONACTUAL_A, POSICIONACTUAL_B, POSICIONACTUAL_C, CODIGOESTRUCTURA, POSTEGI, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"', "+rs.getInt(3)+", "+rs.getInt(4)+", "+rs.getInt(5)+","+rs.getInt(6)+","+rs.getInt(7)+",'"+rs.getString(8).trim()+"','"+rs.getString(9).trim()+"',"+rs.getInt(10)+",'','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM SECCIONADOR_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a SECCIONADOR_SUBTIPO");
					respuesta += "INSERT INTO SECCIONADOR_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM SECCIONADOR_TIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a SECCIONADOR_TIPO");
					respuesta += "INSERT INTO SECCIONADOR_TIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM SECCIONADOR_POSICIONES";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a SECCIONADOR_POSICIONES");
					respuesta += "INSERT INTO SECCIONADOR_POSICIONES (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				/*CAPACITOR*/
				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID, T1.GLOBALID, T1.SUBTIPO, T1.POTENCIAKVA, nvl(T1.CODIGOESTRUCTURA, ' '), T1.ESTRUCTURASOPORTEGLOBALID, T2.CODIGOELEMENTO  "+
							"FROM SIGELEC.PUESTOCORRECTORFACTORPOTENCIA T1 "+ 
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2 ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) "+
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					System.out.println(query);
					while(rs.next()){
						System.out.println("CAPACITOR");
						respuesta += "INSERT INTO CAPACITOR (OBJECTID, GLOBALID, SUBTIPO, POTENCIAKVA, CODIGOESTRUCTURA, POSTEGI, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"', "+rs.getInt(3)+", "+rs.getInt(4)+", '"+rs.getString(5).trim()+"','"+rs.getString(6).trim()+"',"+rs.getInt(7)+",'','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM CAPACITOR_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a CAPACITOR_SUBTIPO");
					respuesta += "INSERT INTO CAPACITOR_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				/*TENSOR*/
				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID, T1.GLOBALID, T1.SUBTIPO, nvl(T1.CODIGOESTRUCTURA,' '), T1.ESTRUCTURASOPORTEGLOBALID, T2.CODIGOELEMENTO " + 
							"FROM SIGELEC.TENSOR T1 " + 
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2 ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) " + 
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					System.out.println(query);
					while(rs.next()){
						System.out.println("TENSOR");
						respuesta += "INSERT INTO TENSOR (OBJECTID, GLOBALID, SUBTIPO, CODIGOESTRUCTURA, POSTEGI, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"', "+rs.getInt(3)+", '"+rs.getString(4).trim()+"','"+rs.getString(5).trim()+"',"+rs.getInt(6)+", '','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM TENSOR_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a TENSOR_SUBTIPO");
					respuesta += "INSERT INTO TENSOR_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}


				/*MEDIDORES*/
				rs = null;
				stmt.clearBatch();
				for(int i =0; i < objectIDPostes.size(); i++){
					query = "SELECT pc.OBJECTID, nvl(pc.GLOBALID,''), pc.SUBTIPO, nvl(cc.MDENUMFAB,'0'), nvl(cc.MDENUMEMP,'0'), pc.COORD_X, pc.COORD_Y, 120 AS VOLTAJE, es.OBJECTID, nvl(es.GLOBALID,''), es.CODIGOELEMENTO " + 
							"FROM SIGELEC.PUNTOCARGA pc "+ 
							"LEFT OUTER JOIN SIGELEC.CONEXIONCONSUMIDOR cc ON (pc.GLOBALID = cc.PUNTOCARGAGLOBALID) "+ 
							"LEFT OUTER JOIN SIGELEC.ESTRUCTURASOPORTE es ON (pc.MIOID = es.OBJECTID) "+
							"WHERE pc.MIOID = "+objectIDPostes.get(i);
					rs = stmt.executeQuery(query);
					System.out.println(query);
					while(rs.next()){
						System.out.println("Medidor");
						respuesta += "INSERT INTO MEDIDOR (OBJECTID, GLOBALID, SUBTIPO, SERIE, NUMEMP, COORDX, COORDY, VOLTAJE, POSTEOBJID, POSTEGI, NUMPOSTE, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"', "+rs.getInt(3)+", '"+rs.getString(4).trim()+"', '"+rs.getString(5).trim()+"','"+rs.getString(6)+"','"+rs.getString(7)+"',"+rs.getInt(8)+","+rs.getInt(9)+",'"+rs.getString(10)+"',"+rs.getInt(11)+",'','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM MEDIDOR_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a MEDIDOR_SUBTIPO");
					respuesta += "INSERT INTO MEDIDOR_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				/*CONDUCTORES*/
				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM CONDUCTOR_MEDIA_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a CONDUCTOR_MEDIA_SUBTIPO");
					respuesta += "INSERT INTO CONDUCTOR_MEDIA_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM CONDUCTOR_BAJA_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a CONDUCTOR_BAJA_SUBTIPO");
					respuesta += "INSERT INTO CONDUCTOR_BAJA_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM CONDUCTOR_FASES_CONEXION";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a CONDUCTOR_FASES_CONEXION");
					respuesta += "INSERT INTO CONDUCTOR_FASES_CONEXION (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				/*LUMINARIAS*/
				rs = null;
				stmt.clearBatch();
				for(int i =0; i < globalIDPostes.size(); i++){
					query = "SELECT T1.OBJECTID, nvl(T1.GLOBALID,' '), T1.SUBTIPO, nvl(T1.CODIGOESTRUCTURA,' '), nvl(T1.ESTRUCTURASOPORTEGLOBALID,' '), T2.CODIGOELEMENTO,  nvl(T1.PUESTOTRANSFDISTGLOBALID,' '), nvl(T3.TRAFO,'0') "+
							"FROM SIGELEC.LUMINARIA T1 "+
							"INNER JOIN SIGELEC.ESTRUCTURASOPORTE T2  ON (T1.ESTRUCTURASOPORTEGLOBALID = T2.GLOBALID) "+
							"LEFT OUTER JOIN SIGELEC.PUESTOTRANSFDISTRIBUCION T3 ON (T1.PUESTOTRANSFDISTGLOBALID = T3.GLOBALID) "+
							"WHERE T1.ESTRUCTURASOPORTEGLOBALID = '"+globalIDPostes.get(i)+"'";
					rs = stmt.executeQuery(query);
					while(rs.next()){
						respuesta += "INSERT INTO LUMINARIA (OBJECTID, GLOBALID, SUBTIPO, CODIGOESTRUCTURA, POSTEGI, NUMPOSTE, TRAFOGI, NUMTRAFO, ESTADO, FECREVISION) VALUES ("+rs.getInt(1)+", '"+rs.getString(2).trim()+"', "+rs.getInt(3)+", '"+rs.getString(4).trim()+"', '"+rs.getString(5).trim()+"',"+rs.getInt(6)+",'"+rs.getString(7).trim()+"','"+rs.getString(8).trim()+"','','')"+System.getProperty("line.separator");
					}
					rs = null;
					stmt.clearBatch();
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM LUMINARIA_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a LUMINARIA_SUBTIPO");
					respuesta += "INSERT INTO LUMINARIA_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGO, DESCRIPCION FROM CONDUCTOR_NEUTRO_SUBTIPO";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a CONDUCTOR_NEUTRO_SUBTIPO");
					respuesta += "INSERT INTO CONDUCTOR_NEUTRO_SUBTIPO (CODIGO, DESCRIPCION) VALUES ("+rs.getInt(1)+",'"+rs.getString(2).trim()+"')"+System.getProperty("line.separator");
				}

				rs = null;
				stmt.clearBatch();
				query = "SELECT CODIGOELEMENTO, COUNT(*) FROM SIGELEC.ESTRUCTURASOPORTE GROUP BY CODIGOELEMENTO HAVING COUNT(*)>1";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					System.out.println("Entra a POSTE_REPETIDO");
					respuesta += "INSERT INTO POSTE_REPETIDO (NUMPOSTE) VALUES ("+rs.getInt(1)+")"+System.getProperty("line.separator");
				}

				closeConnection();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return respuesta;
	}

	public String uploadScript(String path){
		String respuesta="", last="", line="";
		//int k = -1;
		int k = 0;
		int i = 0;
		estaconectado = openConnection();
		if (estaconectado){
			try {
				FileReader fr = new FileReader(path);
				FileReader fr1 = new FileReader(path);
				BufferedReader in = new BufferedReader(fr);
				BufferedReader entrada = new BufferedReader(fr1);
				while ((line = in.readLine()) != null) {
					if (line != null) {
						last = line;
					}
				}
				stmt = conOra.createStatement();
				if (last.equals("115162077941606023@N00")){
					while((query = entrada.readLine()) != null){
						if (!query.equals("")){
							i++;
							if (k == 0) {
								if (query.substring(0, 6).equals("UPDATE") || query.substring(0, 6).equals("DELETE")){
									if (!query.contains(" WHERE ")){
										respuesta = "Linea " + String.valueOf(i) + " :<br/>" + query;
										break;
									}else{
										stmt.clearBatch();
										stmt.executeUpdate(query);
									}
								}else if(query.equals("115162077941606023@N00")){
									break;
								}else{
									stmt.clearBatch();
									stmt.executeUpdate(query);
								}                    				    
							}else if (i == 1){
								k = 0;
								/*VERIFICA QUE LA ASIGNACION QUE SE HIZO AL INICIO YA HAYA TERMINADO*/
								//rs = stmt.executeQuery("SELECT CAFINA FROM SICOD.INSF01 WHERE CACODI = "+ query);
								//while(rs.next()){
								//k = rs.getInt(1);
								//}
								if (k == 1) break;
							} 
						}
					}
				}else{
					respuesta = "Corrupt";
				}

				/*stmt.clearBatch();
				String query = "UPDATE GISMOVIL_TRANSACCIONES SET FECTRANSACCION="+fecha+", HORTRANSACCION='"+hora+"' WHERE  NUMTRANSACCION = ";*/

				stmt.close();
				conOra.close();
				if (k == 0) {
					respuesta = "OK";
				}else if (k == 1) {
					respuesta = "R";
				}

			}
			catch (Exception e){
				e.printStackTrace();
				respuesta = "Linea " + String.valueOf(i) + " :<br/>" + query;
			}
		}
		closeConnection();
		return respuesta;
	}

	public String getOpciones() {
		String respuesta = "";
		ResultSet rs2 = null;
		Statement stmt2 = null;
		estaconectado = openConnection();
		if (estaconectado){
			try {																					//LACONCORDIA,ELCENTENARIO	
				String sql = "SELECT SED_CODIGO, SED_NOMBRE FROM igc.isig_subestacion WHERE SED_CODIGO IN ('SE03','SE08', 'SE13','SE14','SE15')";
				System.out.println(sql);
				stmt = conOra.createStatement();
				stmt2 = conOra.createStatement();
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"Subestaciones\": [ ";
				while (rs.next()){
					respuesta += "{\"cod\":\""+rs.getString(1).trim()+"\",\"nom\":\""+rs.getString(2).trim()+"\", \"Alimentadores\": [";

					sql = "select a.ALI_CODANT,a.ali_nombre "+
							"from igc.isig_alimentador a, igc.isig_subestacion s "+
							"where s.SED_CODIGO='"+rs.getString(1).trim()+"' and a.sed_codigo=s.sed_codigo";
					rs2 = stmt2.executeQuery(sql);
					while (rs2.next()) {
						respuesta += "{\"cod\":\""+rs2.getString(1).trim()+"\",\"nom\":\""+rs2.getString(2).trim()+"\"},";
					}
					respuesta = respuesta.substring(0, respuesta.length() - 1);
					respuesta += "]},";
				}
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}

		closeConnection();
		return respuesta;
	}
	
	public String getEstadisticas() {
		String respuesta = "";
		estaconectado = openConnection();
		if (estaconectado){
			try {			
				//String sql = "select TIPREVISION AS ESTADO,count(DISTINCT NUMPOSTE) AS CANTIDAD from GISMOVIL_POSTES group by TIPREVISION order by TIPREVISION";
				String sql = "select TIPREVISION AS ESTADO,count(*) AS CANTIDAD from GISMOVIL_POSTES group by TIPREVISION order by TIPREVISION";
				stmt = conOra.createStatement();
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"estadisticas1\": [ ";
				while (rs.next()){
					respuesta += "{\"y\":"+rs.getInt(2)+",\"label\":\""+rs.getString(1).trim()+"\"},";
				}
				
				rs = null;
				stmt.clearBatch();
				/*sql = "SELECT DISTINCT(SUBSTR(T1.FLAG, 20, 3)) AS GRUPO," + 
						"(SELECT COUNT(DISTINCT NUMPOSTE) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'ACTUALIZACION') AS ACTUALIZACION," + 
						"(SELECT COUNT(DISTINCT NUMPOSTE) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'ACTUALIZA-SIG') AS ACTUALIZACION_SIG," + 
						"(SELECT COUNT(DISTINCT NUMPOSTE) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'SIN NOVEDAD') AS SIN_NOVEDAD," + 
						"(SELECT COUNT(DISTINCT NUMPOSTE) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'INGRESO') AS NUEVOS " + 
						"FROM GISMOVIL_POSTES T1 ORDER BY GRUPO";*/
				sql = "SELECT DISTINCT(SUBSTR(T1.FLAG, 20, 3)) AS GRUPO," + 
						"(SELECT COUNT(*) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'ACTUALIZACION') AS ACTUALIZACION," + 
						"(SELECT COUNT(*) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'ACTUALIZA-SIG') AS ACTUALIZACION_SIG," + 
						"(SELECT COUNT(*) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'SIN NOVEDAD') AS SIN_NOVEDAD," + 
						"(SELECT COUNT(*) FROM GISMOVIL_POSTES WHERE FLAG = T1.FLAG AND TIPREVISION = 'INGRESO') AS NUEVOS " + 
						"FROM GISMOVIL_POSTES T1 ORDER BY GRUPO";
						
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "], \"estadisticas2\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					respuesta += "{\"grp\":\""+rs.getString(1)+"\",\"est1\":"+rs.getInt(2)+",\"est2\":"+rs.getInt(3)+",\"est3\":"+rs.getInt(4)+",\"est4\":"+rs.getInt(5)+"},";
					
				}
				
				rs = null;
				stmt.clearBatch();
				//sql = "SELECT COUNT(DISTINCT NUMPOSTE) FROM GISMOVIL_POSTES";		
				sql = "SELECT COUNT(*) FROM GISMOVIL_POSTES";
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "], \"estadisticas3\": [ ";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					respuesta += "{\"tot\":"+rs.getInt(1)+"}";
				}
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}
		closeConnection();
		return respuesta;
	}

	public String getGrupos() {
		String respuesta = "";

		estaconectado = openConnection();
		if (estaconectado){
			try {
				String sql = "SELECT T1.NOMBRE, T1.DESCRIPCION FROM GISMOVIL_GRUPOS T1 WHERE T1.ESTADO = 'ACT'";
				System.out.println(sql);
				stmt = conOra.createStatement();
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"Grupos\": [ ";
				while (rs.next()){
					respuesta += "{\"cod\":\""+rs.getString(1).trim()+"\",\"desc\":\""+rs.getString(2).trim()+"\"},";
				}
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}

		return respuesta;
	}

	public String getAllGrupos() {
		String respuesta = "";

		estaconectado = openConnection();
		if (estaconectado){
			try {
				String sql = "SELECT NOMBRE, DESCRIPCION, PROYECTO, FECHAINGRESO, ESTADO FROM GISMOVIL_GRUPOS";
				System.out.println(sql);
				stmt = conOra.createStatement();
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"Grupos\": [ ";
				while (rs.next()){
					respuesta += "{\"cod\":\""+rs.getString(1).trim()+"\",\"desc\":\""+rs.getString(2).trim()+"\", \"pro\":\""+rs.getString(3)+"\",\"fec\":\""+rs.getString(4)+"\",\"est\":\""+rs.getString(5)+"\"},";
				}
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}

		return respuesta;
	}

	public String getAsignaciones() {
		String respuesta = "";
		ResultSet rs2 = null;
		Statement stmt2 = null;
		estaconectado = openConnection();
		if (estaconectado){
			try {
				String sql = "SELECT T1.NOMBRE, T1.DESCRIPCION FROM GISMOVIL_GRUPOS T1 WHERE (SELECT COUNT(*) FROM GISWEB_ASIGNACIONES WHERE GRUPO = T1.NOMBRE AND ESTAASIG = 'ASG') > 0 AND T1.ESTADO = 'ACT'";
				System.out.println(sql);
				stmt = conOra.createStatement();
				stmt2 = conOra.createStatement();
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"Grupos\": [ ";
				while (rs.next()){
					respuesta += "{\"cod\":\""+rs.getString(1).trim()+"\",\"desc\":\""+rs.getString(2).trim()+"\", \"Asignaciones\": [";

					sql = "SELECT ID, FECASIG, HORASIG, SUBESTADESC, ALIMENDESC "+
							"FROM GISWEB_ASIGNACIONES "+
							"WHERE GRUPO = '"+rs.getString(1).trim()+"' AND ESTAASIG = 'ASG'";
					rs2 = stmt2.executeQuery(sql);
					while (rs2.next()) {
						String fecha = rs2.getString(2).trim().substring(0, 4) + "/" + rs2.getString(2).trim().substring(4, 6) + "/" + rs2.getString(2).trim().substring(6,8);
						respuesta += "{\"cod\":\""+rs2.getString(1).trim()+"\",\"fec\":\""+fecha+" - "+ rs2.getString(3).trim()+"\", \"desc\":\""+rs2.getString(4).trim()+" - " + rs2.getString(5).trim()+"\"},";
					}
					respuesta = respuesta.substring(0, respuesta.length() - 1);
					respuesta += "]},";
				}
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}

		return respuesta;
	}

	public boolean asignarTrabajo(String subcode, String alicode, String grupo, String subdesc, String alidesc){
		boolean confirmar = false;
		estaconectado = openConnection();
		if(estaconectado){
			try{
				rs = null;
				stmt = conOra.createStatement();
				String sql = "SELECT TO_CHAR(SYSDATE, 'YYYYMMDD'), TO_CHAR(SYSDATE, 'HH:MM:SS') FROM dual";
				String fecha = "";
				String hora = "";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					fecha = rs.getString(1);
					hora = rs.getString(2);
				}

				stmt.clearBatch();
				sql = "INSERT INTO GISWEB_ASIGNACIONES (SUBESTACODE, ALIMENCODE, FECASIG, ESTAASIG, GRUPO, HORASIG, SUBESTADESC, ALIMENDESC) VALUES ('"+subcode+"', '"+alicode+"', '"+fecha+"' , 'ASG', '"+grupo+"','"+hora+"', '"+subdesc+"', '"+alidesc+"')";
				stmt.executeUpdate(sql);
				stmt.close();
				conOra.close();
				confirmar=true;
			}
			catch (Exception e){
				e.printStackTrace();
				confirmar=false;
			}
		}
		return confirmar;
	}


	/*SECCION DE REPORTERIA*/
	public WritableWorkbook getSheetRevisionesParciales(ServletOutputStream stream, String grupo, String desde, String hasta) throws Exception{
		WritableWorkbook w = Workbook.createWorkbook(stream);
		String condicion = "";
		openConnection();
		try{
			if (!grupo.equals("TODOS")) condicion = "AND (SUBSTR(P.FLAG,LENGTH(P.FLAG) - 1,2)) = '"+grupo+"'";

			WritableSheet s10 = w.createSheet("LUMINARIA", 0);
			//String q10 = "SELECT * FROM GISMOVIL_LUMINARIA WHERE NUMPOSTE IN ("+numpostes+")";
			String q10 = "SELECT L.* FROM GISMOVIL_LUMINARIA L, GISMOVIL_POSTES P WHERE P.NUMPOSTE = L.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q10, s10);

			WritableSheet s9 = w.createSheet("MEDIDOR", 0);
			//String q9 = "SELECT * FROM GISMOVIL_MEDIDOR WHERE NUMPOSTE IN ("+numpostes+")";
			String q9 = "SELECT M.* FROM GISMOVIL_MEDIDOR M, GISMOVIL_POSTES P WHERE P.NUMPOSTE = M.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q9, s9);

			WritableSheet s8 = w.createSheet("SECCIONADOR", 0);
			//String q8 = "SELECT * FROM GISMOVIL_SECCIONADOR WHERE NUMPOSTE IN ("+numpostes+")";
			String q8 = "SELECT S.* FROM GISMOVIL_SECCIONADOR S, GISMOVIL_POSTES P WHERE P.NUMPOSTE = S.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q8, s8);

			WritableSheet s7 = w.createSheet("CAPACITOR", 0);
			//String q7 = "SELECT * FROM GISMOVIL_CAPACITOR WHERE NUMPOSTE IN ("+numpostes+")";
			String q7 = "SELECT C.* FROM GISMOVIL_CAPACITOR C, GISMOVIL_POSTES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q7, s7);

			WritableSheet s6 = w.createSheet("TENSOR", 0);
			//String q6 = "SELECT * FROM GISMOVIL_TENSOR WHERE NUMPOSTE IN ("+numpostes+")";
			String q6 = "SELECT T.* FROM GISMOVIL_TENSOR T, GISMOVIL_POSTES P WHERE P.NUMPOSTE = T.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q6, s6);

			WritableSheet s5 = w.createSheet("TRANSFORMADOR", 0);
			//String q5 = "SELECT * FROM GISMOVIL_TRANSFORMADOR WHERE NUMPOSTE IN ("+numpostes+")";
			String q5 = "SELECT T.* FROM GISMOVIL_TRANSFORMADOR T, GISMOVIL_POSTES P WHERE P.NUMPOSTE = T.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q5, s5);

			WritableSheet s4 = w.createSheet("CONDUCTOR_NEUTRO", 0);
			//String q4 = "SELECT * FROM GISMOVIL_CONDUCTOR_NEUTRO WHERE NUMPOSTE IN ("+numpostes+")";
			String q4 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_NEUTRO C, GISMOVIL_POSTES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q4, s4);

			WritableSheet s3 = w.createSheet("CONDUCTOR_BAJA", 0);
			//String q3 = "SELECT * FROM GISMOVIL_CONDUCTOR_BAJA WHERE NUMPOSTE IN ("+numpostes+")";
			String q3 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_BAJA C, GISMOVIL_POSTES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q3, s3);

			WritableSheet s2 = w.createSheet("CONDUCTOR_MEDIA", 0);
			//String q2 = "SELECT * FROM GISMOVIL_CONDUCTOR_MEDIA WHERE NUMPOSTE IN ("+numpostes+")";
			String q2 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_MEDIA C, GISMOVIL_POSTES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q2, s2);

			WritableSheet s1 = w.createSheet("ESTRUCTURA_EN_POSTE", 0);
			//String query1 = "SELECT * FROM GISMOVIL_ESTRUCTURA_EN_POSTE WHERE NUMPOSTE IN ("+numpostes+")";
			String q1 = "SELECT E.* FROM GISMOVIL_ESTRUCTURA_EN_POSTE E, GISMOVIL_POSTES P WHERE P.NUMPOSTE = E.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q1, s1);

			WritableSheet s = w.createSheet("POSTES", 0);
			//String query = "SELECT * FROM GISMOVIL_POSTES WHERE NUMPOSTE IN ("+numpostes+")";
			String query = "SELECT A.SUBESTADESC AS SUBESTACION, A.ALIMENDESC AS ALIMNETADOR, P.* FROM GISMOVIL_POSTES P, GISWEB_ASIGNACIONES A "+
					"WHERE A.ID = P.NUMASIGNACION AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, query, s);

		}catch(Exception e){
			e.printStackTrace();
		}

		closeConnection();
		return w;
	}

	public WritableWorkbook getSheetRevisionesTotales(ServletOutputStream stream, String grupo, String desde, String hasta) throws Exception{
		WritableWorkbook w = Workbook.createWorkbook(stream);
		String condicion = "";
		openConnection();
		try{
			if (!grupo.equals("TODOS")) condicion = "AND (SUBSTR(P.FLAG,LENGTH(P.FLAG) - 1,2)) = '"+grupo+"'";

			WritableSheet s10 = w.createSheet("LUMINARIA_TOTALES", 0);
			//String q10 = "SELECT * FROM GISMOVIL_LUMINARIA_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q10 = "SELECT L.* FROM GISMOVIL_LUMINARIA_TOTALES L, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = L.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q10, s10);

			WritableSheet s9 = w.createSheet("MEDIDOR_TOTALES", 0);
			//String q9 = "SELECT * FROM GISMOVIL_MEDIDOR_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q9 = "SELECT M.* FROM GISMOVIL_MEDIDOR_TOTALES M, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = M.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q9, s9);

			WritableSheet s8 = w.createSheet("SECCIONADOR_TOTALES", 0);
			//String q8 = "SELECT * FROM GISMOVIL_SECCIONADOR_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q8 = "SELECT S.* FROM GISMOVIL_SECCIONADOR_TOTALES S, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = S.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q8, s8);

			WritableSheet s7 = w.createSheet("CAPACITOR_TOTALES", 0);
			//String q7 = "SELECT * FROM GISMOVIL_CAPACITOR_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q7 = "SELECT C.* FROM GISMOVIL_CAPACITOR_TOTALES C, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q7, s7);

			WritableSheet s6 = w.createSheet("TENSOR_TOTALES", 0);
			//String q6 = "SELECT * FROM GISMOVIL_TENSOR_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q6 = "SELECT T.* FROM GISMOVIL_TENSOR_TOTALES T, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = T.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q6, s6);

			WritableSheet s5 = w.createSheet("TRANSFORMADOR_TOTALES", 0);
			//String q5 = "SELECT * FROM GISMOVIL_TRANSFORMADOR_TOTALES WHERE NUMPOSTE IN ("+numpostes+")";
			String q5 = "SELECT T.* FROM GISMOVIL_TRANSFORMADOR_TOTALES T, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = T.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q5, s5);

			WritableSheet s4 = w.createSheet("CONDUCTOR_NEUTRO_TOTALES", 0);
			//String q4 = "SELECT * FROM GISMOVIL_CONDUCTOR_NEUTRO_TOT WHERE NUMPOSTE IN ("+numpostes+")";
			String q4 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_NEUTRO_TOT C, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q4, s4);

			WritableSheet s3 = w.createSheet("CONDUCTOR_BAJA_TOTALES", 0);
			//String q3 = "SELECT * FROM GISMOVIL_CONDUCTOR_BAJA_TOT WHERE NUMPOSTE IN ("+numpostes+")";
			String q3 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_BAJA_TOT C, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q3, s3);

			WritableSheet s2 = w.createSheet("CONDUCTOR_MEDIA_TOTALES", 0);
			//String q2 = "SELECT * FROM GISMOVIL_CONDUCTOR_MEDIA_TOT WHERE NUMPOSTE IN ("+numpostes+")";
			String q2 = "SELECT C.* FROM GISMOVIL_CONDUCTOR_MEDIA_TOT C, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = C.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q2, s2);

			WritableSheet s1 = w.createSheet("ESTRUCTURA_EN_POSTE_TOTALES", 0);
			//String query1 = "SELECT * FROM GISMOVIL_ESTRUCTURA_EN_POSTE_T WHERE NUMPOSTE IN ("+numpostes+")";
			String q1 = "SELECT E.* FROM GISMOVIL_ESTRUCTURA_EN_POSTE_T E, GISMOVIL_POSTES_TOTALES P WHERE P.NUMPOSTE = E.NUMPOSTE AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, q1, s1);

			WritableSheet s = w.createSheet("POSTES_TOTALES", 0);
			String query = "SELECT A.SUBESTADESC AS SUBESTACION, A.ALIMENDESC AS ALIMENTADOR, P.* FROM GISMOVIL_POSTES_TOTALES P, GISWEB_ASIGNACIONES A "+
					"WHERE A.ID = P.NUMASIGNACION AND P.FECREVISION BETWEEN "+desde+" AND "+hasta+" "+condicion+" ORDER BY P.FECREVISION ASC";
			getSheet(stream, query, s);

		}catch(Exception e){
			e.printStackTrace();
		}
		closeConnection();
		return w;
	}

	public WritableWorkbook getSheetDiferencias(ServletOutputStream stream, String fecTransaccion, String grupo, String numAsignacion) throws Exception{
		WritableWorkbook w = Workbook.createWorkbook(stream);

		openConnection();
		try{

			stmt = conOra.createStatement();
			String sql = "SELECT DISTINCT(FECREVISION) " + 
					"FROM GISMOVIL_TRANSACCIONES_TOTALES  WHERE GRUPO = '"+grupo+"' AND FECTRANSACCION = "+fecTransaccion+" AND NUMASIGNACION = " + numAsignacion;
			rs = stmt.executeQuery(sql);
			String fechas = "";
			while (rs.next()){
				fechas += rs.getInt(1)+","; 
			}

			if (fechas.length() > 0) {
				fechas = fechas.substring(0, fechas.length() - 1);				
			}else {
				fechas = "0";
			}

			rs = null;
			stmt.clearBatch();
			closeConnection();

			WritableSheet s10 = w.createSheet("LUMINARIA", 0);
			String q10 = "SELECT L.* FROM GISMOVIL_LUMINARIA_TOTALES L " +
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (L.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE P.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND P.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND P.FECREVISION IN ("+fechas+")";
			getSheet(stream, q10, s10);

			WritableSheet s9 = w.createSheet("MEDIDOR", 0);
			String q9 = "SELECT M.* FROM GISMOVIL_MEDIDOR_TOTALES M "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (M.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE M.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND M.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND M.FECREVISION IN ("+fechas+")";
			getSheet(stream, q9, s9);

			WritableSheet s8 = w.createSheet("SECCIONADOR", 0);
			String q8 = "SELECT S.* FROM GISMOVIL_SECCIONADOR_TOTALES S "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (S.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE S.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND S.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND S.FECREVISION IN ("+fechas+")";
			getSheet(stream, q8, s8);

			WritableSheet s7 = w.createSheet("CAPACITOR", 0);
			String q7 = "SELECT C.* FROM GISMOVIL_CAPACITOR_TOTALES C "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (C.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE C.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND C.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND C.FECREVISION IN ("+fechas+")";
			getSheet(stream, q7, s7);

			WritableSheet s6 = w.createSheet("TENSOR", 0);
			String q6 = "SELECT T.* FROM GISMOVIL_TENSOR_TOTALES T "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (T.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE T.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND T.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND T.FECREVISION IN ("+fechas+")";
			getSheet(stream, q6, s6);

			WritableSheet s5 = w.createSheet("TRANSFORMADOR", 0);
			String q5 = "SELECT TR.* FROM GISMOVIL_TRANSFORMADOR_TOTALES TR "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (TR.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE TR.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND TR.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND TR.FECREVISION IN ("+fechas+")";
			getSheet(stream, q5, s5);

			WritableSheet s4 = w.createSheet("CONDUCTOR_NEUTRO", 0);
			String q4 = "SELECT CN.* FROM GISMOVIL_CONDUCTOR_NEUTRO_TOT CN "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (CN.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE CN.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CN.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CN.FECREVISION IN ("+fechas+")";
			getSheet(stream, q4, s4);

			WritableSheet s3 = w.createSheet("CONDUCTOR_BAJA", 0);
			String q3 = "SELECT CB.* FROM GISMOVIL_CONDUCTOR_BAJA_TOT CB "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (CB.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE CB.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CB.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CB.FECREVISION IN ("+fechas+")";
			getSheet(stream, q3, s3);

			WritableSheet s2 = w.createSheet("CONDUCTOR_MEDIA", 0);
			String q2 = "SELECT CM.* FROM GISMOVIL_CONDUCTOR_MEDIA_TOT CM "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (CM.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE CM.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CM.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND CM.FECREVISION IN ("+fechas+")";
			getSheet(stream, q2, s2);

			WritableSheet s1 = w.createSheet("ESTRUCTURA_EN_POSTE", 0);
			String query1 = "SELECT ET.* FROM GISMOVIL_ESTRUCTURA_EN_POSTE_T ET "+
					"INNER JOIN GISMOVIL_POSTES_TOTALES P ON (ET.NUMPOSTE = P.NUMPOSTE AND P.EXISTE = 'NO') "+
					//"WHERE ET.NUMPOSTE IN ("+numpostes+") AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND ET.FECREVISION IN ("+fechas+")";
					"WHERE P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND ET.FECREVISION IN ("+fechas+")";
			getSheet(stream, query1, s1);

			WritableSheet s = w.createSheet("POSTES", 0);
			//String query = "SELECT * FROM GISMOVIL_POSTES_TOTALES WHERE NUMPOSTE IN ("+numpostes+") AND NUMASIGNACION = "+numAsignacion+" AND FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND FECREVISION IN ("+fechas+") AND EXISTE = 'NO'";
			String query = "SELECT A.SUBESTADESC AS SUBESTACION, A.ALIMENDESC AS ALIMENTADOR, P.* FROM GISMOVIL_POSTES_TOTALES P, GISWEB_ASIGNACIONES A "+
					"WHERE A.ID = P.NUMASIGNACION AND P.NUMASIGNACION = "+numAsignacion+" AND P.FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND P.FECREVISION IN ("+fechas+") AND P.EXISTE = 'NO'";
			getSheet(stream, query, s);

		}catch(Exception e){
			e.printStackTrace();
		}
		closeConnection();
		return w;
	}


	private void getSheet(ServletOutputStream stream, String query, WritableSheet s)throws Exception{
		openConnection();
		WritableCellFormat bcell = new WritableCellFormat();
		bcell.setBorder(Border.ALL, BorderLineStyle.THIN);

		WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD, false);
		font.setColour(Colour.WHITE);
		WritableCellFormat estadoACell = new WritableCellFormat(font);
		estadoACell.setBorder(Border.ALL, BorderLineStyle.THIN);
		estadoACell.setBackground(Colour.LIGHT_ORANGE);
		WritableCellFormat estadoNCell = new WritableCellFormat(font);
		estadoNCell.setBorder(Border.ALL, BorderLineStyle.THIN);
		estadoNCell.setBackground(Colour.GREEN);
		WritableCellFormat estadoSCell = new WritableCellFormat(font);
		estadoSCell.setBorder(Border.ALL, BorderLineStyle.THIN);
		estadoSCell.setBackground(Colour.LIGHT_BLUE);

		WritableCellFormat dp2cell = new WritableCellFormat(new NumberFormat("0.00"));
		dp2cell.setBorder(Border.ALL, BorderLineStyle.THIN);
		dp2cell.setAlignment(Alignment.CENTRE);

		WritableCellFormat dp0cell = new WritableCellFormat(new NumberFormat("0"));
		dp0cell.setBorder(Border.ALL, BorderLineStyle.THIN);
		dp0cell.setAlignment(Alignment.CENTRE);

		try {
			rs = stmt.executeQuery(query);
			if( this.rs.next() ){
				for (int i=1;i<=rs.getMetaData().getColumnCount(); i++){
					s.addCell(new Label(i-1, 0, rs.getMetaData().getColumnName(i), bcell));
				}
			}
			rs.beforeFirst();
			int c=0;
			while( this.rs.next() ){
				c++;
				for (int i=1;i<=rs.getMetaData().getColumnCount(); i++){
					if (rs.getString(i)!=null){
						try{
							//System.out.println("1;  " + rs.getString(i));
							if (rs.getString(i).indexOf(".")!=-1){
								//System.out.println("entra");
								s.addCell(new Number(i-1,c,rs.getDouble(i), dp2cell));								
							}
							else
								s.addCell(new Number(i-1,c,rs.getInt(i), dp0cell));
						}catch(Exception e){
							if (rs.getString(i).trim().equals("INGRESO")) {
								s.addCell(new Label(i-1,c,rs.getString(i).trim(), estadoNCell));
							}else if(rs.getString(i).trim().equals("ACTUALIZACION")) {
								s.addCell(new Label(i-1,c,rs.getString(i).trim(), estadoACell));
							}else if(rs.getString(i).trim().equals("SIN NOVEDAD")) {
								s.addCell(new Label(i-1,c,rs.getString(i).trim(), estadoSCell));
							}else {
								s.addCell(new Label(i-1,c,rs.getString(i).trim(), bcell));
							}
						}
					}else{
						s.addCell(new Label(i-1,c,"", bcell));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeConnection();
	}

	/*DATA PARA ACTUALIZAR EN SIG*/
	public String getDataActualizarSIG() {
		String respuesta = "";
		estaconectado = openConnection();
		if(estaconectado){
			try{
				rs = null;
				stmt = conOra.createStatement();
				String sql = "SELECT OBJECTID, GLOBALID, NUMPOSTE, SUBSTR(FLAG, 20, 3) AS GRUPO, FECREVISION, HORREVISION, NUMTRANSACCION FROM GISMOVIL_POSTES WHERE TIPREVISION = 'ACTUALIZACION' AND GLOBALID IS NOT NULL " + 
						"ORDER BY GRUPO ASC";
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"postes\": [ ";
				boolean entra = false;
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"npt\":"+rs.getInt(3)+",\"grp\":\""+rs.getString(4).trim()+"\", \"fec\":\""+rs.getString(5).trim()+"\",\"hor\":\""+rs.getString(6).trim()+"\", \"tra\":"+rs.getInt(7)+"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
				}
				respuesta += "]}";
				System.out.println(respuesta);
				stmt.close();
				conOra.close();
			}
			catch (Exception e){
				respuesta = "{\"status\": \"OK\",\"msg\": \"Error: "+e.getMessage()+"\"}";
				e.printStackTrace();
			}
		}

		closeConnection();
		return respuesta;
	}

	/*LISTADO DE POSTES QUE TIENE ESTADO ACTUALIZADO DE LA APP MOVIL PARA SUBIR FOTO DE POSTE Y TRAFO*/
	public String getListaPostes(String desde, String hasta, String grupo, String numPoste, String estado, String soloFotos) {
		String respuesta = "";
		String condicion = "";
		String sql = "";
		if (numPoste != "") {
			condicion = "T1.NUMPOSTE = "+numPoste+" AND ROWNUM = 1";
		}else {
			if (estado.equals("TODOS") && grupo.equals("TODOS")) {
				condicion = "T1.FECREVISION BETWEEN "+desde+" AND "+hasta+" AND T1.TIPREVISION IN ('ACTUALIZACION','ACTUALIZA-SIG','INGRESO')";
			}else if (estado.equals("TODOS") && !grupo.equals("TODOS") ) {
				condicion = "T1.FECREVISION BETWEEN "+desde+" AND "+hasta+" AND SUBSTR(T1.FLAG, 20, 3) = '"+grupo+"'";
			}else if (!estado.equals("TODOS") && grupo.equals("TODOS") ) {
				condicion = "T1.FECREVISION BETWEEN "+desde+" AND "+hasta+" AND T1.TIPREVISION = '"+estado+"'";
			}else {
				condicion = "T1.FECREVISION BETWEEN "+desde+" AND "+hasta+" AND SUBSTR(T1.FLAG, 20, 3) = '"+grupo+"' AND T1.TIPREVISION = '"+estado+"'";
			}
		}
		estaconectado = openConnection();
		if(estaconectado){
			try{
				rs = null;
				stmt = conOra.createStatement();
				if (soloFotos.equals("SI")) {
					sql = "SELECT DISTINCT T1.OBJECTID, NVL(T1.GLOBALID,' '), T1.NUMPOSTE, SUBSTR(T1.FLAG, 20, 3) AS GRUPO, T1.FECREVISION, T1.HORREVISION, T1.NUMASIGNACION, T1.NUMTRANSACCION, T1.TIPREVISION," + 
							"NVL((SELECT NOMFOTO FROM GISWEB_FOTOS WHERE POSTEOI = T1.OBJECTID AND nvl(POSTEGI,' ') = nvl(T1.GLOBALID,' ') AND NUMPOSTE = T1.NUMPOSTE AND TIPOFOTO = 'POSTE'),' ') AS FOTOP," + 
							"NVL((SELECT NOMFOTO FROM GISWEB_FOTOS WHERE POSTEOI = T1.OBJECTID AND nvl(POSTEGI,' ') = nvl(T1.GLOBALID,' ') AND NUMPOSTE = T1.NUMPOSTE AND TIPOFOTO = 'TRAFO'), ' ') AS FOTOT,T2.ALIMENDESC " + 
							"FROM GISMOVIL_POSTES T1, GISWEB_ASIGNACIONES T2, GISWEB_FOTOS T3 " + 
							"WHERE " + condicion + " AND T1.NUMASIGNACION = T2.ID AND " + 
							"T3.POSTEOI = T1.OBJECTID AND nvl(T3.POSTEGI,' ') = nvl(T1.GLOBALID,' ') " + 
							"ORDER BY T1.FECREVISION";
				}else {
					sql = "SELECT T1.OBJECTID, NVL(T1.GLOBALID,' '), T1.NUMPOSTE, SUBSTR(T1.FLAG, 20, 3) AS GRUPO, T1.FECREVISION, T1.HORREVISION, T1.NUMASIGNACION, T1.NUMTRANSACCION, T1.TIPREVISION,"+
							"NVL((SELECT NOMFOTO FROM GISWEB_FOTOS WHERE POSTEOI = T1.OBJECTID AND nvl(POSTEGI,' ') = nvl(T1.GLOBALID,' ') AND NUMPOSTE = T1.NUMPOSTE AND TIPOFOTO = 'POSTE'),' ') AS FOTOP,"+
							"NVL((SELECT NOMFOTO FROM GISWEB_FOTOS WHERE POSTEOI = T1.OBJECTID AND nvl(POSTEGI,' ') = nvl(T1.GLOBALID,' ') AND NUMPOSTE = T1.NUMPOSTE AND TIPOFOTO = 'TRAFO'), ' ') AS FOTOT,"+
							"T2.ALIMENDESC "+
							"FROM GISMOVIL_POSTES T1, GISWEB_ASIGNACIONES T2 "+
							"WHERE " + condicion + " AND T1.NUMASIGNACION = T2.ID "+ 
							"ORDER BY T1.FECREVISION";					
				}
				rs = stmt.executeQuery(sql);
				System.out.println(sql);
				respuesta = "{\"status\": \"OK\",\"postes\": [ ";
				boolean entra = false;
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"npt\":"+rs.getInt(3)+",\"grp\":\""+rs.getString(4).trim()+"\", \"fec\":\""+rs.getString(5).trim()+"\",\"hor\":\""+rs.getString(6).trim()+"\","+
							"\"numa\":"+rs.getInt(7)+",\"numt\":"+rs.getInt(8)+",\"fotp\":\""+rs.getString(10).trim()+"\",\"fott\":\""+rs.getString(11).trim()+"\",\"est\":\""+rs.getString(9).trim()+"\", \"alim\":\""+rs.getString(12).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
				}
				
				rs = null;
				stmt.clearBatch();
				sql = "SELECT COUNT(DISTINCT NOMFOTO) FROM GISWEB_FOTOS WHERE TIPOFOTO = 'POSTE'";
				respuesta += "], \"totalFotosP\": [ ";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					respuesta += "{\"totP\":"+rs.getInt(1)+"}";
				}
				
				rs = null;
				stmt.clearBatch();
				sql = "SELECT COUNT(DISTINCT NOMFOTO) FROM GISWEB_FOTOS WHERE TIPOFOTO = 'TRAFO'";
				respuesta += "], \"totalFotosT\": [ ";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					respuesta += "{\"totT\":"+rs.getInt(1)+"}";
				}
				respuesta += "]}";
				System.out.println(respuesta);
				stmt.close();
				conOra.close();
			}
			catch (Exception e){
				respuesta = "{\"status\": \"FAIL\",\"msg\": \"Error: "+e.getMessage()+"\"}";
				e.printStackTrace();
			}
		}

		closeConnection();
		return respuesta;
	}

	public boolean actualizarDataSIG(String numPostes) {
		boolean confirmar = false;
		System.out.println(numPostes);
		estaconectado = openConnection();
		if(estaconectado){
			try {
				String numPostesArray [] = numPostes.split(",");
				String dataSP [] = null;
				stmt = conOra.createStatement();
				for (String numPoste : numPostesArray) {
					dataSP= numPoste.split(":");
					System.out.println(dataSP[0] + " - " + dataSP[1]);
					String sql = "CALL sp_act_sigmovil_poste_u2 ("+Integer.parseInt(dataSP[0])+","+Integer.parseInt(dataSP[1])+")";
					stmt.executeUpdate(sql);
					stmt.clearBatch();
				}
				confirmar = true;
			} catch (Exception e) {
				confirmar = false;
				e.printStackTrace();
				System.out.println(e.getMessage());
			}			
		}
		closeConnection();
		return confirmar;
	}

	public String corregirDiferencias(String fechaTransaccion, String numAsignacion, String grupo, String subestacion, String alimentador, String postesTotales, String postesNormales, String diferencia) {
		String respuesta = "";
		estaconectado = openConnection();
		openConnectionDB2();
		if(estaconectado){
			try {
				stmt = conOra.createStatement();
				stmtDB2 = conDb2.createStatement();

				String sqlDB2 = "SELECT SESSION_USER FROM sysibm.sysdummy1";
				rsDB2 = stmtDB2.executeQuery(sqlDB2);
				String usuario = "";
				while(rsDB2.next()){
					usuario = rsDB2.getString(1);
				}
				String sql ="INSERT INTO GISWEB_TRANSACCION_DIFERENCIAS VALUES('"+fechaTransaccion+"', '"+grupo+"', "+numAsignacion+", '"+subestacion+"', '"+alimentador+"', "+postesTotales+","+postesNormales+","+diferencia+",'"+usuario+"', (SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') FROM dual), (SELECT TO_CHAR(SYSDATE, 'HH:MM:SS') FROM dual))";
				stmt.executeUpdate(sql);

				
				stmt.clearBatch();
				sql = "SELECT DISTINCT(FECREVISION) " + 
						"FROM GISMOVIL_TRANSACCIONES_TOTALES  WHERE GRUPO = '"+grupo+"' AND FECTRANSACCION = "+fechaTransaccion+" AND NUMASIGNACION = " + numAsignacion;
				rs = stmt.executeQuery(sql);
				String fechas = "";
				while (rs.next()){
					fechas += rs.getInt(1)+","; 
				}

				if (fechas.length() > 0) {
					fechas = fechas.substring(0, fechas.length() - 1);				
				}else {
					fechas = "0";
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT NUMPOSTE, nvl(GLOBALID,' '), OBJECTID, FECREVISION FROM GISMOVIL_POSTES_TOTALES WHERE FLAG='031-DT-2017-VROJAS-"+grupo+"' AND NUMASIGNACION = " +numAsignacion+ " AND FECREVISION IN("+fechas+") AND EXISTE = 'NO'";
				rs = stmt.executeQuery(sql);
				Statement stmt2 = conOra.createStatement();
				while(rs.next()) {
					String posteGI = rs.getString(2).trim();
					String condicion = " AND GLOBALID ='"+posteGI+"' " ;
					String condicion2 = " AND POSTEGI ='"+posteGI+"' " ;
					if (posteGI.equals("")) {
						condicion = " AND GLOBALID IS NULL " ;
						condicion2 = " AND POSTEGI IS NULL " ;
					}
					
					String query = "INSERT INTO GISMOVIL_POSTES (OBJECTID, GLOBALID, NUMPOSTE, ESTRUCTURA, COORDX, COORDY, TIPOCIMIENTO, ATERRAMIENTO, PUESTOTIERRA, SUBTIPO, PROPIEDAD, FLAG, FECREVISION, HORREVISION, OBSERVACION, TIPREVISION, NUMASIGNACION, NUMTRANSACCION) "+
							"SELECT OBJECTID, GLOBALID, NUMPOSTE, ESTRUCTURA, COORDX, COORDY, TIPOCIMIENTO, ATERRAMIENTO, PUESTOTIERRA, SUBTIPO, PROPIEDAD, FLAG, FECREVISION, HORREVISION, OBSERVACION, TIPREVISION, NUMASIGNACION, NUMTRANSACCION "+
							"FROM GISMOVIL_POSTES_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND OBJECTID = "+rs.getString(3)+" AND FECREVISION = "+rs.getInt(4)+" AND EXISTE = 'NO'"+condicion;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_CAPACITOR (OBJECTID,GLOBALID,ESTRUCTURA,SUBTIPO,POTENCIAKVA,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_CAPACITOR_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_CONDUCTOR_BAJA (OBJECTID,GLOBALID,ESTRUCTURA,SUBTIPO,FASECONEXION,VOLTAJE,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_CONDUCTOR_BAJA_TOT WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_CONDUCTOR_MEDIA (OBJECTID,GLOBALID,ESTRUCTURA,SUBTIPO,FASECONEXION,VOLTAJE,SECFASE,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_CONDUCTOR_MEDIA_TOT WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_CONDUCTOR_NEUTRO (OBJECTID,GLOBALID,ESTRUCTURA,SUBTIPO,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+ 
							"SELECT * FROM GISMOVIL_CONDUCTOR_NEUTRO_TOT WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_ESTRUCTURA_EN_POSTE (OBJECTID,GLOBALID,ESTRUCTURA,CANTIDAD,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_ESTRUCTURA_EN_POSTE_T WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_LUMINARIA (OBJECTID,GLOBALID,SUBTIPO,ESTRUCTURA,POSTEGI,NUMPOSTE,TRAFOGI,NUMTRAFO,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_LUMINARIA_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_MEDIDOR (OBJECTID,GLOBALID,SUBTIPO,SERIE,NUMEMPRESA,COORDX,COORDY,VOLTAJE,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_MEDIDOR_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_SECCIONADOR (OBJECTID,GLOBALID,ESTRUCTURA,TIPO,SUBTIPO,POSICION_ACTUAL_A,POSICION_ACTUAL_B,POSICION_ACTUAL_C,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_SECCIONADOR_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_TENSOR (OBJECTID,GLOBALID,ESTRUCTURA,SUBTIPO,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_TENSOR_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "INSERT INTO GISMOVIL_TRANSFORMADOR (OBJECTID,GLOBALID,ESTRUCTURA,NUMTRAFO,SUBTIPO,FASECONEXION,PROPIEDAD,POSTEGI,NUMPOSTE,ESTADO,FECREVISION) "+
							"SELECT * FROM GISMOVIL_TRANSFORMADOR_TOTALES WHERE NUMPOSTE = "+rs.getInt(1)+" AND FECREVISION = "+rs.getInt(4)+condicion2;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
					query = "UPDATE GISMOVIL_POSTES_TOTALES SET EXISTE = 'SI' WHERE NUMPOSTE = "+rs.getInt(1)+" AND OBJECTID = "+rs.getString(3)+" AND FECREVISION = "+rs.getInt(4)+" AND EXISTE='NO'"+condicion;
					System.out.println(query);
					stmt2.executeUpdate(query);
					stmt2.clearBatch();
					
				}
				respuesta = "{\"status\": \"OK\",\"msg\": \"Proceso Ejecutado con Exito\"}";
			} catch (SQLException e) {
				respuesta = "{\"status\": \"FAIL\",\"msg\": \"Error: "+e.getMessage()+"\"}";
				e.printStackTrace();
			}
			
			closeConnection();

		}

		return respuesta;
	}

	public String saveFoto(String posteOI, String posteGI, String numPoste, String tipo, String fileName, String grupo, String fecRevision) {

		String respuesta = "";
		estaconectado = openConnection();
		openConnectionDB2();
		if( estaconectado) {
			try {
				stmt = conOra.createStatement();
				stmtDB2 = conDb2.createStatement();

				String sqlDB2 = "SELECT SESSION_USER FROM sysibm.sysdummy1";
				rsDB2 = stmtDB2.executeQuery(sqlDB2);
				String usuario = "";
				while(rsDB2.next()){
					usuario = rsDB2.getString(1);
				}
				String sql ="INSERT INTO GISWEB_FOTOS VALUES('"+posteOI+"', '"+posteGI+"', '"+numPoste+"', '"+tipo+"', '"+fileName+"', (SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') FROM dual), '"+fecRevision+"','"+grupo+"', '"+usuario+"')";
				stmt.executeUpdate(sql);
				respuesta = "{\"status\":\"OK\", \"msg\":\"FOTO SUBIDA CON EXITO\"}";
				System.out.println(respuesta);
			} catch (Exception e) {
				respuesta = "{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}";
			}
			closeConnection();
		}

		return respuesta;

	}

	public String deleteFoto(String posteOI, String posteGI, String numPoste, String tipo, String fileName, String grupo, String fecRevision) {
		String respuesta = "";
		estaconectado = openConnection();
		openConnectionDB2();
		if( estaconectado) {
			try {
				stmt = conOra.createStatement();
				stmtDB2 = conDb2.createStatement();

				String sqlDB2 = "SELECT SESSION_USER FROM sysibm.sysdummy1";
				rsDB2 = stmtDB2.executeQuery(sqlDB2);
				String usuario = "";
				while(rsDB2.next()){
					usuario = rsDB2.getString(1);
				}

				String sql ="INSERT INTO GISWEB_FOTOS_DELETE VALUES ("+posteOI+", '"+posteGI+"', "+numPoste+", '"+tipo+"', '"+fileName+"', (SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') FROM dual), '"+fecRevision+"','"+grupo+"','"+usuario+"')";
				stmt.executeUpdate(sql);

				stmt.clearBatch();
				sql = "DELETE FROM GISWEB_FOTOS WHERE POSTEOI="+posteOI+" AND POSTEGI='"+posteGI+"' AND NUMPOSTE="+numPoste+" AND TIPOFOTO='"+tipo+"' AND NOMFOTO='"+fileName+"' AND GRUPO='"+grupo+"'";
				stmt.executeUpdate(sql);

				respuesta = "{\"status\":\"OK\", \"msg\":\"FOTO ELIMINADA CON EXITO\"}";
				System.out.println(respuesta);
			} catch (Exception e) {
				respuesta = "{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}";
			}

			closeConnection();
		}
		return respuesta;

	}

	public String getElementosByPoste(String posteGI, String numPoste, String fecRevision, String posteOI, String numAsign, String grupo) {
		String respuesta = "";
		estaconectado = openConnection();
		boolean entra = false;
		if(estaconectado){
			try{
				rs = null;
				stmt = conOra.createStatement();
				String sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, SUBTIPO, POTENCIAKVA, ESTADO, FECREVISION FROM GISMOVIL_CAPACITOR " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta = "{\"status\": \"OK\",\"capacitores\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\", \"subt\":\""+rs.getString(4).trim()+"\",\"pote\":\""+rs.getString(5)+"\",\"esta\":\""+rs.getString(6).trim()+"\", \"fecr\":\""+rs.getString(7).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID, ' '), ESTRUCTURA, SUBTIPO, FASECONEXION, VOLTAJE, ESTADO, FECREVISION FROM GISMOVIL_CONDUCTOR_BAJA " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"conductoresB\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\", \"subt\":\""+rs.getString(4).trim()+"\",\"fase\":\""+rs.getString(5)+"\",\"volt\":\""+rs.getString(6)+"\",\"esta\":\""+rs.getString(7).trim()+"\", \"fecr\":\""+rs.getString(8).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, SUBTIPO, FASECONEXION, VOLTAJE, SECFASE, ESTADO, FECREVISION FROM GISMOVIL_CONDUCTOR_MEDIA " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"conductoresM\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\", \"subt\":\""+rs.getString(4).trim()+"\",\"fase\":\""+rs.getString(5)+"\",\"volt\":\""+rs.getString(6)+"\",\"secu\":\""+rs.getString(7).trim()+"\", \"esta\":\""+rs.getString(8).trim()+"\", \"fecr\":\""+rs.getString(9).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, SUBTIPO, ESTADO, FECREVISION FROM GISMOVIL_CONDUCTOR_NEUTRO " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"conductoresN\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\", \"subt\":\""+rs.getString(4).trim()+"\",\"esta\":\""+rs.getString(5).trim()+"\", \"fecr\":\""+rs.getString(6).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, CANTIDAD, ESTADO, FECREVISION FROM GISMOVIL_ESTRUCTURA_EN_POSTE " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"estructuras\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\",\"cant\":"+rs.getInt(4)+",\"esta\":\""+rs.getString(5).trim()+"\", \"fecr\":\""+rs.getString(6).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), SUBTIPO, ESTRUCTURA, TRAFOGI, NUMTRAFO, ESTADO, FECREVISION FROM GISMOVIL_LUMINARIA " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"luminarias\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"subt\":\""+rs.getString(3).trim()+"\",\"estr\":\""+rs.getString(4).trim()+"\", \"trgi\":\""+rs.getString(5).trim()+"\", \"nutr\":"+rs.getInt(6)+",\"esta\":\""+rs.getString(7).trim()+"\", \"fecr\":\""+rs.getString(8).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), SUBTIPO, SERIE, NUMEMPRESA, COORDX, COORDY, VOLTAJE, ESTADO, FECREVISION FROM GISMOVIL_MEDIDOR " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"medidores\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"subt\":\""+rs.getString(3).trim()+"\",\"seri\":\""+rs.getString(4).trim()+"\", \"nuem\":\""+rs.getString(5)+"\",\"corx\":\""+rs.getString(6)+"\",\"cory\":\""+rs.getString(7)+"\",\"volt\":\""+rs.getString(8)+"\",\"esta\":\""+rs.getString(9).trim()+"\", \"fecr\":\""+rs.getString(10).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, TIPO, SUBTIPO, POSICION_ACTUAL_A, POSICION_ACTUAL_B, POSICION_ACTUAL_C, ESTADO, FECREVISION FROM GISMOVIL_SECCIONADOR " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"seccionadores\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\",\"tipo\":\""+rs.getString(4).trim()+"\", \"subt\":\""+rs.getString(5).trim()+"\", \"posa\":\""+rs.getString(6)+"\",\"posb\":\""+rs.getString(7)+"\",\"posc\":\""+rs.getString(8)+"\",\"esta\":\""+rs.getString(9).trim()+"\", \"fecr\":\""+rs.getString(10).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, SUBTIPO, ESTADO, FECREVISION FROM GISMOVIL_TENSOR " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"tensores\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\",\"subt\":\""+rs.getString(4).trim()+"\", \"esta\":\""+rs.getString(5).trim()+"\", \"fecr\":\""+rs.getString(6).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBJECTID, nvl(GLOBALID,' '), ESTRUCTURA, NUMTRAFO, SUBTIPO, FASECONEXION, PROPIEDAD, ESTADO, FECREVISION FROM GISMOVIL_TRANSFORMADOR " + 
						"WHERE POSTEGI = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"'";
				respuesta += "],\"transformadores\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"oid\":"+rs.getInt(1)+",\"gid\":\""+rs.getString(2).trim()+"\",\"estr\":\""+rs.getString(3).trim()+"\", \"nutr\":\""+rs.getString(4).trim()+"\",\"subt\":\""+rs.getString(5).trim()+"\", \"fase\":\""+rs.getString(6).trim()+"\", \"prop\":\""+rs.getString(7).trim()+"\", \"esta\":\""+rs.getString(8).trim()+"\", \"fecr\":\""+rs.getString(9).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}

				rs = null;
				stmt.clearBatch();
				sql = "SELECT OBSERVACION FROM GISMOVIL_POSTES " + 
						"WHERE GLOBALID = '"+posteGI+"' AND NUMPOSTE = "+numPoste+" AND FECREVISION = '"+fecRevision+"' AND OBJECTID = "+posteOI+" AND FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND NUMASIGNACION =" + numAsign;
				respuesta += "],\"observaciones\": [ ";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					entra = true;
					respuesta += "{\"obs\":\""+rs.getString(1).trim()+"\"},";
				}
				if (entra) {
					respuesta = respuesta.substring(0, respuesta.length()-1);
					entra = false;
				}


				respuesta += "]}";
				System.out.println(respuesta);
				stmt.close();
				conOra.close();
			}
			catch (Exception e){
				respuesta = "{\"status\": \"OK\",\"msg\": \"Error: "+e.getMessage()+"\"}";
				e.printStackTrace();
			}
		}

		closeConnection();
		return respuesta;
	}

	public String getRevisiones() {
		String respuesta = "";
		Statement stmt2 = null;
		Statement stmt3 = null;

		estaconectado = openConnection();
		if (estaconectado){
			try {
				stmt = conOra.createStatement();
				stmt2 = conOra.createStatement();
				stmt3 = conOra.createStatement();

				String sql = "SELECT DISTINCT(T1.FECTRANSACCION), T1.GRUPO, T1.NUMASIGNACION, ASI.SUBESTADESC, ASI.ALIMENDESC "+
						"FROM GISMOVIL_TRANSACCIONES_TOTALES T1 "+
						"INNER JOIN GISWEB_ASIGNACIONES ASI ON (ASI.ID = T1.NUMASIGNACION AND ASI.GRUPO = T1.GRUPO) "+
						"ORDER BY T1.FECTRANSACCION ASC";
				String fechaTransaccion = "";
				String grupo = "";
				String numAsignacion = "";
				String subestacion = "";
				String alimentador = "";
				rs = stmt.executeQuery(sql);
				respuesta = "{\"status\": \"OK\",\"Revisiones\": [ ";
				while (rs.next()) {
					ResultSet rs2 = null;
					fechaTransaccion = rs.getString(1).trim();
					grupo = rs.getString(2).trim();
					numAsignacion = rs.getString(3).trim();
					subestacion = rs.getString(4).trim();
					alimentador = rs.getString(5).trim();

					sql = "SELECT DISTINCT(FECREVISION) FROM GISMOVIL_TRANSACCIONES_TOTALES WHERE FECTRANSACCION = "+fechaTransaccion+" AND GRUPO = '"+grupo+"' AND NUMASIGNACION = " + numAsignacion; 
					rs2 = stmt2.executeQuery(sql);
					String fechasRevision = "";
					while (rs2.next()) {
						fechasRevision += rs2.getString(1).trim() + ",";
					}

					fechasRevision = fechasRevision.substring(0, fechasRevision.length() - 1);

					sql = "SELECT OBJECTID, NUMPOSTE FROM GISMOVIL_POSTES_TOTALES WHERE FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND NUMASIGNACION = " + numAsignacion + " AND FECREVISION IN ("+fechasRevision+") AND EXISTE IS NULL";
					rs2 = null;
					stmt2.clearBatch();
					ResultSet rs3 = null;
					rs2 = stmt2.executeQuery(sql);
					while(rs2.next()) {
						sql = "SELECT COUNT(*) FROM GISMOVIL_POSTES WHERE OBJECTID = "+rs2.getString(1)+" AND NUMPOSTE = "+rs2.getInt(2)+" AND FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND NUMASIGNACION = " + numAsignacion + " AND FECREVISION IN ("+fechasRevision+")";
						stmt3.clearBatch();
						rs3 = stmt3.executeQuery(sql);
						String existe = "";
						if (rs3.next()) {
							if(rs3.getInt(1) > 0) {
								existe = "SI";
							}else {
								existe = "NO";
							}
						}

						rs3 = null;
						stmt3.clearBatch();

						sql = "UPDATE GISMOVIL_POSTES_TOTALES SET EXISTE ='"+existe+"' WHERE OBJECTID = "+rs2.getString(1)+" AND NUMPOSTE = "+rs2.getInt(2)+" AND FLAG = '031-DT-2017-VROJAS-"+grupo+"' AND NUMASIGNACION = " + numAsignacion + " AND FECREVISION IN ("+fechasRevision+")";
						stmt3.executeUpdate(sql);
						stmt3.clearBatch();
					}

					String query = "SELECT DISTINCT(T1.FECTRANSACCION), T1.GRUPO, T1.FECREVISION, "+
							"(SELECT COUNT(*) FROM GISMOVIL_POSTES_TOTALES WHERE NUMASIGNACION = T1.NUMASIGNACION AND FLAG = '031-DT-2017-VROJAS-'||T1.GRUPO AND FECREVISION = T1.FECREVISION ) AS PTOTALES, "+ 
							"(SELECT COUNT(*) FROM GISMOVIL_POSTES_TOTALES WHERE NUMASIGNACION = T1.NUMASIGNACION AND FLAG = '031-DT-2017-VROJAS-'||T1.GRUPO AND FECREVISION = T1.FECREVISION AND EXISTE = 'SI') AS PPARCIALES "+
							"FROM GISMOVIL_TRANSACCIONES_TOTALES T1 "+
							"WHERE T1.GRUPO = '"+grupo+"' AND T1.FECTRANSACCION = "+fechaTransaccion+" AND T1.NUMASIGNACION ="+numAsignacion;

					rs2 = null;
					stmt2.clearBatch();
					rs2 = stmt2.executeQuery(query);
					int countPostesT = 0;
					int countPostesN = 0;
					while (rs2.next()) {
						countPostesT = countPostesT + rs2.getInt(4);
						countPostesN = countPostesN + rs2.getInt(5);
					}
					System.out.println(countPostesT);
					System.out.println(countPostesN);
					respuesta += "{\"fect\":"+fechaTransaccion+",\"grup\":\""+grupo+"\",\"numa\":"+numAsignacion+",\"sube\":\""+subestacion+"\", \"alim\":\""+alimentador+"\",\"poto\":"+countPostesT+",\"pono\":"+countPostesN+",\"dife\":"+(countPostesT - countPostesN)+"},";
				}
				respuesta = respuesta.substring(0, respuesta.length() - 1);
				respuesta += "]}";
				System.out.println(respuesta);
			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}

			try {
				if (stmt2!=null) stmt2.close();
				if (stmt3!=null) stmt3.close();				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			closeConnection();

		}

		return respuesta;
	}


	public String getEstadoBDD() {
		String respuesta = "";
		String estado = "DISPONIBLE";
		estaconectado = openConnection();
		if (estaconectado){
			try {
				stmt = conOra.createStatement();
				String sql = "select table_name, to_number(extractvalue(xmltype(dbms_xmlgen.getxml('select count(*) c from '||owner||'.'||table_name)),'/ROWSET/ROW/C')) as count " + 
						"from all_tables where owner = 'SIGELEC' and table_name like 'A%' and table_name not in ('ACTUALIZADAPOSTE','ACTUALIZADATOTRAFO','ATRIBUTOSCONSUMIDOR','ACTUALIZADAUNIDADTOTRAFO','ACTUALIZADATOLUMINARIA') " + 
						"group by table_name,to_number(extractvalue(xmltype(dbms_xmlgen.getxml('select count(*) c from '||owner||'.'||table_name)),'/ROWSET/ROW/C')) " + 
						"having to_number(extractvalue(xmltype(dbms_xmlgen.getxml('select count(*) c from '||owner||'.'||table_name)),'/ROWSET/ROW/C')) >0";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					estado = "NO DISPONIBLE";
				}

				respuesta = "{\"status\": \"OK\",\"estado\":\""+estado+"\"}";

			}catch (Exception e) {
				e.printStackTrace();
				respuesta = "{\"status\": \"FAIL\",\"msg\": \"Error: "+e.getMessage()+"\"}";
				System.out.println(e.getMessage());
			}
		}

		closeConnection();
		return respuesta;
	}

	public static String cleanString(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return texto;
	}

	private void closeConnection(){
		try{
			if (rs!=null) rs.close();
			if (rsDB2!=null) rsDB2.close();
			if (stmt!=null) stmt.close();
			if (stmtDB2!=null) stmtDB2.close();
			if (conOra!=null) conOra.close();
			if (conDb2!=null) conDb2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}




}
