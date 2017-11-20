<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8" import="dao.GISWebDAO"%><%
	HttpSession sesion=request.getSession();
	try{
		if (request.getParameter("subcode")!=null && !request.getParameter("subcode").equals("") && request.getParameter("alicode")!=null && !request.getParameter("alicode").equals("")  && request.getParameter("grupo")!=null && !request.getParameter("grupo").equals("")){
			String subcode = request.getParameter("subcode").toString();
			String alicode = request.getParameter("alicode").toString();
			String subdesc = request.getParameter("subdesc").toString();
			String alidesc = request.getParameter("alidesc").toString();
			String grupo = request.getParameter("grupo").toString();
			GISWebDAO g = new GISWebDAO();
			if (g.asignarTrabajo(subcode, alicode, grupo, subdesc, alidesc)){
				out.print("{\"status\":\"OK\"}");
			}else out.print("{\"status\":\"fail\", \"msg\":\"Error\"");
		}else out.print("{\"status\":\"fail\", \"msg\":\"Error\"");
	}catch(NullPointerException e){out.print("{\"status\":\"fail\", \"msg\":\"Error\"");}
%>