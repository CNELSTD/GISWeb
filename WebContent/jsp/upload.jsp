<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dao.GISWebDAO"%>
<%@ page import="java.io.*,dao.GISWebDAO" %>
<% HttpSession sesion=request.getSession(); %>
<html>
<head>
<title>Pocket PC</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../css/sync.css" media="screen" />
</head>
<body>
<div id="page">
	<div id="header">
		<a href="./"><img alt="" src="../images/gis_logo.png"
				style="width: 10%; height: 10%; top: 30%;"> GIS WEB</a>
	</div>
	<div id="formulario">
<%
	String user="";
	String pass="";
	try{
		user = sesion.getAttribute("username").toString();
		pass = sesion.getAttribute("pass").toString();
	}catch (Exception e){}
	//to get the content type information from JSP Request Header
	String contentType = request.getContentType();
	//here we are checking the content type is not equal to Null and as well as the passed data from mulitpart/form-data is greater than or equal to 0
	if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
		DataInputStream in = new DataInputStream(request.getInputStream());
		//we are taking the length of Content type data
		int formDataLength = request.getContentLength();
		byte dataBytes[] = new byte[formDataLength];
		int byteRead = 0;
		int totalBytesRead = 0;
		//this loop converting the uploaded file into byte code
		while (totalBytesRead < formDataLength) {
			byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
			totalBytesRead += byteRead;
			}

		String file = new String(dataBytes);
		//for saving the file name
		String saveFile = file.substring(file.indexOf("filename=\"") + 10);
		saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
		saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));
		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1,contentType.length());
		int pos;
		//extracting the index of file 
		pos = file.indexOf("filename=\"");
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		int boundaryLocation = file.indexOf(boundary, pos) - 4;
		int startPos = ((file.substring(0, pos)).getBytes()).length;
		int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

		// creating a new file with the same name and writing the content in new file
		FileOutputStream fileOut = new FileOutputStream(application.getRealPath("/")+"\\uploads\\"+saveFile);
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();
		out.print("Archivo subido con &eacute;xito, ejecutando script...<br/><br/>");
		GISWebDAO pd = new GISWebDAO();
		String result = pd.uploadScript(application.getRealPath("/")+"\\uploads\\"+saveFile);
		if (result.equals("OK")){
			out.println("<strong>Finalizado correctamente</strong>");
			out.println("<script type=\"text/javascript\">");
			out.println("setTimeout(\"window.location='../'\", 1500);");
			out.println("</script>");
		}else if(result.equals("R")){
			out.println("<strong>Este script no se ha subido, porque ya fue subido anteriormente</strong>");
			out.println("<script type=\"text/javascript\">");
			out.println("setTimeout(\"window.location='../'\", 5000);");
			out.println("</script>");
		}else if(result.equals("Corrupt")){
			out.print("<span style=\"color:red;font-weight:bold;\">El archivo no se subi&oacute; de manera correcta debido a un problema con su conexi&oacute;n de internet, int&eacute;ntelo de nuevo por favor.");
		}else if(result.equals("")){
			out.print("<span style=\"color:red;font-weight:bold;\">Usted ha perdido sesi&oacute;n de usuario, por favor actualize la p&aacute;gina y vuelva a ingresar con su usuario y contraseña.");
		}else{
			out.println("<span style=\"color:red;font-weight:bold;\">Ha ocurrido un error:");
			out.println("<br/><br/>" + result);
			out.println("<br/><br/>Por favor comun&iacute;quese con el Centro de Computo para que reciba asistencia.<br/><br/>No intente subir de nuevo el script ya que podr&iacute;a resultar en informaci&oacute;n duplicada!!!.<br/><br/>Copie la informaci&oacute;n del error que se presenta en esta ventana para que se facilite su asistencia.<br/><br/>Gracias por su comprensi&oacute;n.</span>");
		}
	}
%>
	</div>
</div>
</body>
</html>
