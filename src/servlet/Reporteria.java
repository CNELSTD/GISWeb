package servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GISWebDAO;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * Servlet implementation class Reporteria
 */
@WebServlet("/Reporteria")
public class Reporteria extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Reporteria() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = null;
		try{
			String desde = "";
			String hasta = "";
			String fecTransaccion = "";
			String numAsignacion = "";
			String grupo = request.getParameter("grupo").toString();
			String opcion = request.getParameter("opcion").toString();
			response.setHeader("Pragma","no-cache");
			response.setDateHeader ("Expires", 0);
			response.setHeader("Cache-Control","no-store");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=ReporteGISWeb_"+grupo+".xls");
			WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
			//WritableSheet s = w.createSheet("FISCALIZACIONES", 0);
			GISWebDAO g = new GISWebDAO();
			if (opcion.equals("reporte1")){
				desde = request.getParameter("desde").toString();
				hasta = request.getParameter("hasta").toString();
				grupo = request.getParameter("grupo").toString();
				w = g.getSheetRevisionesParciales(response.getOutputStream(), grupo, desde, hasta);				
			}else if (opcion.equals("reporte2")){
				desde = request.getParameter("desde").toString();
				hasta = request.getParameter("hasta").toString();
				grupo = request.getParameter("grupo").toString();
				w = g.getSheetRevisionesTotales(response.getOutputStream(), grupo, desde, hasta);
			}else if (opcion.equals("reporte3")){
				fecTransaccion = request.getParameter("fect").toString();
				grupo = request.getParameter("grupo").toString();
				numAsignacion = request.getParameter("numa").toString();
				w = g.getSheetDiferencias(response.getOutputStream(), fecTransaccion, grupo, numAsignacion);
			}
			w.write();
			w.close();
		}catch (Exception ex){
			throw new ServletException("Exception in Excel Sample Servlet", ex);
		}finally{
			if (out != null) out.close();
		}
	}

}
