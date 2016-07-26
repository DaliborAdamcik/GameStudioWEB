package sk.dalik;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import sk.tsystems.gamestudio.entity.CommentEntity;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.RatingEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.services.CommentService;
import sk.tsystems.gamestudio.services.GameService;
import sk.tsystems.gamestudio.services.RatingService;
import sk.tsystems.gamestudio.services.ScoreService;
import sk.tsystems.gamestudio.services.UserService;

/**
 * Servlet implementation class SvcGameStud
 */
@WebServlet(description = "Game studio services", urlPatterns = { "/SvcGame" })
public class SvcGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SvcGame() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		request.setAttribute("json", json);
		boolean purecode = false;
		try
		(
				GameService game = new sk.tsystems.gamestudio.services.jdbc.GameSvc();
				UserService user = new sk.tsystems.gamestudio.services.jdbc.UserSvc();
				CommentService comme = new sk.tsystems.gamestudio.services.jdbc.CommentSvc();
				ScoreService score =  new sk.tsystems.gamestudio.services.jdbc.ScoreSvc();
				RatingService rating = new sk.tsystems.gamestudio.services.jdbc.RatingSvc();
				/*GameService game = new sk.tsystems.gamestudio.services.jpa.GameSvc();
				UserService user = new sk.tsystems.gamestudio.services.jpa.UserSvc();
				CommentService comme = new sk.tsystems.gamestudio.services.jpa.CommentSvc();
				ScoreService score =  new sk.tsystems.gamestudio.services.jpa.ScoreSvc();
				RatingService rating = new sk.tsystems.gamestudio.services.jpa.RatingSvc();*/
		)
		{
			String userName = request.getParameter("username");
			if(userName!=null)
				request.getSession().setAttribute("username", userName);
			else
				userName = (String) request.getSession().getAttribute("username");
			
			if(userName!= null)
			{
				user.auth(userName);
				json.put("username", userName);
			}
			
			GameEntity gam;
			String reqOption = request.getParameter("option");
			
			if("gamelist".equals(reqOption))
			{
				json.put("gamelist", game.listGames());
				return;
			}
			
			try
			{
				gam = game.getGame(Integer.parseInt(request.getParameter("gameID")));
			}
			catch(NumberFormatException | NullPointerException e)
			{ return; }

			switch(request.getParameter("option"))
			{
			case "comment":
				request.setAttribute("comments", comme.commentsFor(gam));
				request.getRequestDispatcher("/WEB-INF/jsp/comments.jsp").forward(request, response);
				purecode = true;
				break;
			case "addcomment":
				String comm = request.getParameter("comment");
				if(comm==null || comm.length()==0 || userName==null)
					return ;
				JSONObject jsn = new JSONObject(comm);
				CommentEntity comment = new CommentEntity(gam, user.me(), jsn.getString("comm"));
				comme.addComment(comment);
				//response.getWriter().println("OK "+comment.getID());
				break;
			case "score":
				request.setAttribute("scores", score.topScores(gam));
				request.getRequestDispatcher("/WEB-INF/jsp/scores.jsp").include(request, response);
				purecode = true;
				break;
			case "play":
				request.getRequestDispatcher(gam.getServletPath()).include(request, response);
				Integer scr = (Integer) request.getAttribute("score");   
				if(scr!=null && user.me()!=null)
				{
					ScoreEntity scoo = new ScoreEntity(gam, null, scr);
					score.addScore(scoo);
				}
				break;

			case "addrate":
				try {
					int ratt = Integer.parseInt(request.getParameter("rate"));
					if(user.me()!=null)
						rating.addRating(new RatingEntity(gam, null, ratt));
				} catch (NumberFormatException | NullPointerException e) {}
			case "getrate":
				json.put("gamerating", rating.gameRating(gam));
				RatingEntity rat;
				if(user.me()!=null && (rat = rating.myRating(gam, user.me()))!=null )
					json.put("myrating",rat.getRating());
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(!purecode)
			{
				response.setContentType("application/json");
				response.getWriter().print(json);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
