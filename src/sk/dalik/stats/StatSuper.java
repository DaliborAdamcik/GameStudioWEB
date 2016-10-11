package sk.dalik.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.jdbc.CommonServices;

public abstract class StatSuper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StatSuper() {
        super();
    }

	protected boolean Authentificated(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonServices com = getSvcs(request);
		
		request.setAttribute("authresult", "Need to be authentficated");
		
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		
		if(request.getParameter("logout")!=null)
			request.getSession().setAttribute("tsadminid", null);
		
		try {
			UserEntity usr = com.getUser(name);
			request.setAttribute("authresult", "Bad login");
			if(usr.getPassword().compareTo(pass)==0) {
				request.getSession().setAttribute("tsadminid", usr.getID());
			} else
				request.setAttribute("authresult", "Bad passowrd");

			
		} catch (NullPointerException e) {}
		
		Integer adminid= (Integer) request.getSession().getAttribute("tsadminid");
		if(adminid!=null) {
			UserEntity usr = com.getUser(adminid);
			String usrss = com.gameSetting(com.getGame(1), "valid_users", String.class, " ");
			List<String> usrs = new ArrayList<>(Arrays.asList(usrss.trim().split(",")));
			
			if(usr!= null && (usr.getID()==1 || usrs.indexOf(usr.getName())>=0)) {
				request.setAttribute("tsadmin", usr);
				return true;
			}
			request.setAttribute("authresult", "Pemissions denied");
		}

		request.getRequestDispatcher("/WEB-INF/jsp/StatLogin.jsp").forward(request, response);
		return false;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("windows-1250");
		request.setCharacterEncoding("UTF-8");

		try (CommonServices common = new sk.tsystems.gamestudio.services.jdbc.CommonServices();) {
			request.setAttribute("comsvc", common);
			if(Authentificated(request, response))
				super.service(request, response);
		}
	}
	
	protected CommonServices getSvcs(HttpServletRequest request) {
		return (CommonServices) request.getAttribute("comsvc");
	}
}
