<%@ page language="java" contentType="application/json; "
    pageEncoding="ISO-8859-1" import="dao.GISWebDAO"%>
<%
	HttpSession sesion=request.getSession();
	try{
		String posteGI = request.getParameter("posteGI").toString();
		String numPoste = request.getParameter("numPoste").toString();
		String fecRevision = request.getParameter("fecRevision").toString();
		String posteOI = request.getParameter("posteOI").toString();
		String numAsig = request.getParameter("numAsig").toString();
		String grupo = request.getParameter("grupo").toString();
		GISWebDAO ins = new GISWebDAO();
		out.print(ins.getElementosByPoste(posteGI, numPoste, fecRevision, posteOI, numAsig, grupo));
	}catch(NullPointerException e){}
%>