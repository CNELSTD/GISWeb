<%@ page language="java" contentType="application/json; "
    pageEncoding="ISO-8859-1" import="dao.GISWebDAO"%>
<%
	HttpSession sesion=request.getSession();
	try{
		String desde = request.getParameter("desde").toString();
		String hasta = request.getParameter("hasta").toString();
		String grupo = request.getParameter("grupo").toString();
		String estado = request.getParameter("estado").toString();
		String numPoste = request.getParameter("numPoste").toString();
		String soloFotos = request.getParameter("soloFotos").toString();
		GISWebDAO ins = new GISWebDAO();
		out.print(ins.getListaPostes(desde, hasta, grupo, numPoste, estado, soloFotos));
	}catch(NullPointerException e){}
%>