<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<% HttpSession sesion=request.getSession();
try{
	if (!sesion.getAttribute("myapp").toString().equals("76")){
		sesion.removeAttribute("username");
		sesion.removeAttribute("pass");
		sesion.removeAttribute("msg_txt");
		sesion.removeAttribute("myapp");
		sesion.removeAttribute("usr_type");
	}
}catch(NullPointerException e){}
try{
	if (request.getParameter("p").equalsIgnoreCase("logout")){
		sesion.removeAttribute("username");
		sesion.removeAttribute("pass");
		sesion.removeAttribute("msg_txt");
		sesion.removeAttribute("myapp");
		sesion.removeAttribute("usr_type");
	}
}catch(NullPointerException e){}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GIS WEB</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
<link rel="stylesheet prefetch"
	href="font-awesome/css/font-awesome.min.css">
<script src="js/jquery.min.js"></script>
<% if (sesion.getAttribute("username") != null){ %>
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" media="screen" />
<link rel="stylesheet" type="text/css" href="jquery-ui-1.11.4/jquery-ui.min.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/jquery-confirm.min.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/jquery.accordion.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css" media="screen" />
<script src="jquery-ui-1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/funciones.js"></script>
<script type="text/javascript" src="js/jquery-confirm.min.js"></script>
<script type="text/javascript" src="js/jquery.accordion.js"></script>
<script type="text/javascript" src="js/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script type="text/javascript" src="js/extra.js"></script>
<%}else{ %>
<link rel="stylesheet" type="text/css" href="css/login.css" media="screen" />
<%} %>
</head>
<body>

	<% if (sesion.getAttribute("username") == null){ %>
	<jsp:include page="files/login.jsp" />
	<%}else{ 
	String username = sesion.getAttribute("username").toString();
	String password = sesion.getAttribute("pass").toString();
%>
	<div class="container-fluid">
		<% if (sesion.getAttribute("usr_type").toString().equals("sa") || sesion.getAttribute("usr_type").toString().equals("admin")){ %>
		<jsp:include page="files/admin.jsp" />
		<% } else if (sesion.getAttribute("usr_type").toString().equals("user")){ %>
		<jsp:include page="files/user.jsp" />
		<%}else{
	    	sesion.removeAttribute("username");
			sesion.removeAttribute("pass");
			sesion.removeAttribute("msg_txt");
			sesion.removeAttribute("myapp");
			sesion.removeAttribute("usr_type");
	   	 %>
		<jsp:include page="index.jsp" />
		<%} %>
	</div>
	<% } %>

</body>
</html>