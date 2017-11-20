package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import dao.GISWebDAO;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY_POSTE = "\\\\172.18.115.10\\sig\\FOTOS\\POSTE\\";
	private final String UPLOAD_DIRECTORY_POSTE_RESPALDO = "\\\\172.18.115.94\\gisweb_fotos\\POSTE\\";
	private final String UPLOAD_DIRECTORY_TRAFO = "\\\\172.18.115.10\\sig\\FOTOS\\TRAFO\\";
	private final String UPLOAD_DIRECTORY_TRAFO_RESPALDO = "\\\\172.18.115.94\\gisweb_fotos\\TRAFO\\";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
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
		// TODO Auto-generated method stub
				response.setContentType("application/json");
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				PrintWriter out = response.getWriter();
				String posteOI = "", posteGI = "", numPoste = "", tipo = "", fileName = "", grupo="", fecRevision = "";
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					try {
						List<FileItem> multiparts = upload.parseRequest(request);
						for (FileItem item : multiparts) {
							if (item.isFormField()) {
								String fieldName = item.getFieldName();
								//String fieldValue = item.getString();
								if (fieldName.equals("objectID")) {
									posteOI = item.getString();
								}else if (fieldName.equals("globalID")) {
									posteGI = item.getString();
								}else if (fieldName.equals("numPoste")) {
									numPoste = item.getString();
								}else if (fieldName.equals("fecRevision")) {
									fecRevision = item.getString();
								}else if (fieldName.equals("grupo")) {
									grupo = item.getString();
								}else if (fieldName.equals("tipo")) {
									tipo = item.getString();
								}
							}else {
								String name = item.getName();
								String partes [] = name.split(".jpg");
								String nameRevisado = "";
								if (partes[0].trim().length() > 10) {
									nameRevisado = partes[0].trim().substring(0, 10) + ".jpg";
								}else {
									nameRevisado = name;
								}
								fileName = nameRevisado;
								if (tipo.equals("POSTE")) {
									item.write(new File(UPLOAD_DIRECTORY_POSTE + File.separator + nameRevisado));
									File origen = new File(UPLOAD_DIRECTORY_POSTE + File.separator + nameRevisado);
									File destiny = new File(UPLOAD_DIRECTORY_POSTE_RESPALDO + File.separator + nameRevisado);
									FileUtils.copyFile(origen, destiny);
								}else {
									item.write(new File(UPLOAD_DIRECTORY_TRAFO + File.separator + nameRevisado));
									File origen = new File(UPLOAD_DIRECTORY_TRAFO + File.separator + nameRevisado);
									File destiny = new File(UPLOAD_DIRECTORY_TRAFO_RESPALDO + File.separator + nameRevisado);
									FileUtils.copyFile(origen, destiny);
								}

							}
						}
						GISWebDAO gDAO = new GISWebDAO();
						out.print(gDAO.saveFoto(posteOI, posteGI, numPoste, tipo, fileName, grupo, fecRevision));
					} 
					catch (FileUploadException e) {
						out.print("{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}");
						e.printStackTrace();
					} catch (Exception e) {
						out.print("{\"status\":\"FAIL\", \"msg\":\""+e.getMessage()+"\"}");
						e.printStackTrace();
					}
				}
	}

}
