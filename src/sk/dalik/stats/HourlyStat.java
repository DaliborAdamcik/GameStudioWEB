package sk.dalik.stats;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.services.jdbc.CommonServices;

/**
 * Servlet implementation class HourlyStat
 */
@WebServlet("/HourlyStat")
public class HourlyStat extends HttpServlet {
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
		try (CommonServices common = new sk.tsystems.gamestudio.services.jdbc.CommonServices();) {
			Map<String, List<ScoreEntity>> hs = common.topScoresHourly();
			//System.out.println(hs);
			request.setAttribute("hourlyreport", hs);
			request.getRequestDispatcher("/WEB-INF/jsp/hourscore.jsp").forward(request, response);
		}
	}

}