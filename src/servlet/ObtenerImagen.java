package servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ObtenerImagen
 */
@WebServlet("/ObtenerImagen")
public class ObtenerImagen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerImagen() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpeg");
		String fileName = request.getParameter("fileName").toString().trim();
		String tipo = request.getParameter("tipo").toString().trim();
		File f = null;
		if (tipo.equals("POSTE")) {
			f = new File("\\\\172.18.115.10\\sig\\FOTOS\\POSTE\\"+fileName.toUpperCase());			
		}else {
			f = new File("\\\\172.18.115.10\\sig\\FOTOS\\TRAFO\\"+fileName.toUpperCase());

		}
		BufferedImage bi = ImageIO.read(f);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
