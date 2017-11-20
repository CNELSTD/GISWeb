<%@ page language="java" contentType="application/json; "
    pageEncoding="ISO-8859-1" import="dao.GISWebDAO"%>
<%
	HttpSession sesion=request.getSession();
	try{
		String fechaTransacion = request.getParameter("fecTransaccion").toString();
		String numAsignacion = request.getParameter("numAsig").toString();
		String grupo = request.getParameter("grupo").toString();
		String subestacion = request.getParameter("subesta").toString();
		String alimentador = request.getParameter("alimenta").toString();
		String postesTotales = request.getParameter("posteTotales").toString();
		String postesNormales = request.getParameter("posteNormales").toString();
		String diferencia = request.getParameter("diferencia").toString();
		GISWebDAO ins = new GISWebDAO();
		out.print(ins.corregirDiferencias(fechaTransacion, numAsignacion, grupo, subestacion, alimentador, postesTotales, postesNormales, diferencia));
	}catch(NullPointerException e){
		out.print("{\"status\":\"fail\", \"msg\":\""+e.getMessage()+"\"");
	}
%>