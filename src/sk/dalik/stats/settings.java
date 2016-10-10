package sk.dalik.stats;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.services.jdbc.CommonServices;

/**
 * Servlet implementation class HourlyStat
 */
@WebServlet("/SetTings")
public class settings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public settings() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (CommonServices common = new sk.tsystems.gamestudio.services.jdbc.CommonServices();) {
			try {
				//TODO: protect me by sign in
				
				String sname = request.getParameter("sname");
				String value = request.getParameter("value");
				
				if(sname==null || value==null)
					throw new NullPointerException();

				Integer gameid = Integer.parseInt(request.getParameter("gameid"));
				GameEntity game = common.getGame(gameid);
				common.saveSetting(game, sname, value);
			} catch (NullPointerException | NumberFormatException e) {}
			
			
			request.setAttribute("settings", common.listSettings());
			request.setAttribute("games", common.listGames());
			
			request.getRequestDispatcher("/WEB-INF/jsp/Sets.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	

}
