package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GISWebDAO;

/**
 * Servlet implementation class DownloadScript
 */
@WebServlet("/DownloadScript")
public class DownloadScript extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadScript() {
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
		try{		
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=scriptGIS.sql");
			PrintWriter out = response.getWriter();
			GISWebDAO pd = new GISWebDAO();
			out.print(pd.downloadScript(request.getParameter("grupo"), request.getParameter("asignacion")));
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
