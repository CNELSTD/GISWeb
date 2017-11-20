<%@ page language="java" contentType="application/json; "
    pageEncoding="ISO-8859-1" import="dao.GISWebDAO"%>
<%
	HttpSession sesion=request.getSession();
	try{
		GISWebDAO ins = new GISWebDAO();
		out.print(ins.getDataActualizarSIG());
	}catch(NullPointerException e){}
%>