package sk.dalik;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.game.guessnumber.core.GuessNumber;
import sk.tsystems.gamestudio.services.GameService;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(description = "Ajco", urlPatterns = { "/Guess" })
public class GuessWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GuessWeb() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/jsp/NewGuess.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//JSONObject json = new JSONObject();
		JSONObject json = (JSONObject) request.getAttribute("json");

		json.put("game", "guess");

		try{

			HttpSession session = request.getSession();
			GuessNumber guess = (GuessNumber) session.getAttribute("guess");
			String action = request.getParameter("action");
			if(action==null)
				action = "none";
				
			int size;
			try
			{
				size = Integer.parseInt(request.getParameter("max"));
				if(size<10 || size>100)
					throw new NumberFormatException();
			}
			catch(NumberFormatException | NullPointerException e)
			{
				size = 25;
			}
			
			if(guess== null || action.compareTo("new")==0)
			{
				GameService gamSvc = (GameService) request.getAttribute("commonsvc");
				GameEntity gamEnt = (GameEntity) request.getAttribute("commonsvcgam");
							
				guess = new GuessNumber(gamSvc.gameSetting(gamEnt, "guess_size", Integer.class, 100));
				
				session.setAttribute("guess", guess);
				session.setAttribute("guess-time", System.currentTimeMillis());

				json.put("newgame", true);
				json.put("size", size);
			}//
			
			try
			{
				int iguess = Integer.parseInt(request.getParameter("number"));
				json.put("number", iguess);
				
				int res = guess.tryHit(iguess);
				
				if(res<0)
					json.put("hit", "lower");
				if(res>0)
					json.put("hit", "higher");

				json.put("won", res==0);
				if(res==0)
				{
					ScoreEntity myscore = new ScoreEntity((int) (System.currentTimeMillis() - (long) session.getAttribute("guess-time")),
							guess.getBound()); 
					
					/*int myscore = (int) (System.currentTimeMillis() - (long) session.getAttribute("guess-time")) / 1000;
					myscore = (100000 / myscore)*guess.getBound();*/

					request.setAttribute("score", myscore);
					json.put("score", myscore.getScore());
				}
			}
			catch(NumberFormatException | NullPointerException e)
			{
				//json.put("error", e.getMessage());
			}
		}
		finally {
			/* response.setContentType("application/json");
			response.getWriter().print(json); */
		}
		
	}

}
