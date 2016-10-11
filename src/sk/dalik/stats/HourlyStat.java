package sk.dalik.stats;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.services.jdbc.CommonServices;

/**
 * Servlet implementation class HourlyStat
 */
@WebServlet("/HourlyStat")
public class HourlyStat extends StatSuper {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HourlyStat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonServices common = getSvcs(request);
			
		GameEntity game = null;
		try {
			Integer gameid = Integer.parseInt(request.getParameter("gameid"));
			game = common.getGame(gameid);
		} catch(Exception e) {
			game = null;
		}
		
		Map<String, List<ScoreEntity>> hs = common.topScoresHourly(game);
		request.setAttribute("games", common.listGames());
		request.setAttribute("hourlyreport", hs);
		request.getRequestDispatcher("/WEB-INF/jsp/hourscore.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
