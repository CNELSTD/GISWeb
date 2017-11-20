package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import dao.GISWebDAO;

/**
 * Servlet implementation class EliminarFile
 */
@WebServlet("/EliminarFile")
public class EliminarFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY_POSTE = "\\\\172.18.115.10\\sig\\FOTOS\\POSTE\\";
	private final String UPLOAD_DIRECTORY_POSTE_RESPALDO = "\\\\172.18.115.94\\gisweb_fotos\\POSTE\\";
	private final String UPLOAD_DIRECTORY_TRAFO = "\\\\172.18.115.10\\sig\\FOTOS\\TRAFO\\";
	private final String UPLOAD_DIRECTORY_TRAFO_RESPALDO = "\\\\172.18.115.94\\gisweb_fotos\\TRAFO\\";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarFile() {
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
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String fileName = request.getParameter("file").toString().trim();
		String tipo = request.getParameter("tipo").toString().trim();
		String posteOI = request.getParameter("posteOI").toString().trim();
		String posteGI = request.getParameter("posteGI").toString().trim();
		String numPoste = request.getParameter("numPoste").toString().trim();
		String fecRevision = request.getParameter("fecRevision").toString().trim();
		String grupo = request.getParameter("grupo").toString().trim();

		if (tipo.equals("POSTE")) {
			File origen = new File(UPLOAD_DIRECTORY_POSTE + File.separator + fileName.toUpperCase());
			File respaldo = new File(UPLOAD_DIRECTORY_POSTE_RESPALDO + File.separator +fileName.toUpperCase());
			try{
			if(origen.exists() && respaldo.exists()) {
				if (origen.delete() && respaldo.delete()) {
					GISWebDAO gDAO = new GISWebDAO();
					out.print(gDAO.deleteFoto(posteOI, posteGI, numPoste, tipo, fileName, grupo, fecRevision));
				}
			}
			}catch (Exception e) {
				out.print("{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}");
				e.printStackTrace();
			}
		}else {
			File origen = new File(UPLOAD_DIRECTORY_TRAFO + File.separator + fileName.toUpperCase());
			File respaldo = new File(UPLOAD_DIRECTORY_TRAFO_RESPALDO + File.separator +fileName.toUpperCase());
			try{
			if(origen.exists() && respaldo.exists()) {
				if (origen.delete() && respaldo.delete()) {
					GISWebDAO gDAO = new GISWebDAO();
					out.print(gDAO.deleteFoto(posteOI, posteGI, numPoste, tipo, fileName, grupo, fecRevision));
				}
			}
			}catch (Exception e) {
				out.print("{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}");
				e.printStackTrace();
			}

		}
		
	}

}
