package login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Signin
 */
@WebServlet("/Signin")
public class Signin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signin() {
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
		HttpSession sesion = request.getSession( true );
		 try{
	    	 Perfil user = new Perfil();
			 user.username = request.getParameter("username").trim();
			 user.pass = request.getParameter("pass").trim();
			 user.myapp = request.getParameter("myapp");
			 if (user.Login()){
				 sesion.setAttribute("username",user.username);
	 			 sesion.setAttribute("pass",user.pass);
	 			 sesion.setAttribute("myapp",user.myapp);
	 			 sesion.setAttribute("usr_codi",user.pecodi);
	 			 sesion.setAttribute("usr_name",user.penomb);
	    		 sesion.setAttribute("usr_felo",user.pefelo);
	    		 sesion.setAttribute("usr_hora",user.pehora);
	    		 sesion.setAttribute("usr_zimbramail",user.pezimbramail);
	    		 sesion.setAttribute("usr_zimbrapass",user.pezimbrapass);
	    		 sesion.setAttribute("usr_type",user.petype);
				 sesion.setAttribute("usr_tipo",user.petipo);
				 sesion.setAttribute("usr_direccion",user.pedire);
				 sesion.setAttribute("usr_area",user.pececo);
				 sesion.setAttribute("usr_cargo",user.peocup);
				 sesion.setAttribute("usr_secure",user.secure);
				 sesion.setAttribute("msg_txt",user.msg);
				 sesion.setMaxInactiveInterval(24 * 60 * 60); // 24 hours
	    	 }else{
	    		 sesion.setAttribute("msg_txt",user.msg);
	    	 }
	     }catch(Exception e){
	    	 sesion.setAttribute("msg_txt",e.getMessage().substring(e.getMessage().indexOf("(")+1, e.getMessage().indexOf(")")));
	     }
	     response.sendRedirect("./");
	}

}
