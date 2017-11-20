<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8" import="dao.GISWebDAO"%><%
    HttpSession sesion=request.getSession();
    try{
		String jsoncallback="";
		try{
			if (request.getParameter("jsoncallback")!=null)
				if (!request.getParameter("jsoncallback").equals("?"))
					jsoncallback = request.getParameter("jsoncallback");
		}catch(NullPointerException e){}
		GISWebDAO g = new GISWebDAO();
		out.print(jsoncallback+"("+g.getEstadisticas()+")");
	}catch(NullPointerException e){}
%>