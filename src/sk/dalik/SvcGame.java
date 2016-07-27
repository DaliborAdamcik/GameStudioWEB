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
import sk.tsystems.gamestudio.entity.UserEntity;
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
			if(request.getParameter("checknick")!=null)
			{
				String nick = request.getParameter("checknick").trim();
				// TODO check with regex
				json.put("usernonexists", user.getUser(nick)==null);
				json.put("nick", nick);
				return ;
			}
			
			if(request.getParameter("accact")!=null&&"register".equals(request.getParameter("accact")))
			{
				json.put("regmsg", "initialization failed:");
				try
				{
					String nick = request.getParameter("name");
					String pass = request.getParameter("pass");
					
					if(nick.length()<3)
						json.put("regmsg", "Name is too short");
					else
					if(pass.length()<3)
						json.put("regmsg", "Password is too short");
					else
					{ 
						UserEntity usrik = user.addUser(nick); 
						usrik.setPassword(pass);
						if(!user.updateUser(usrik))
							json.put("error", "error setting password");
							
						json.put("add", usrik!=null);
						json.put("regmsg", usrik!=null?"ok":"fail");
						request.getSession().setAttribute("userID", usrik.getID());
					}
				}
				catch(NullPointerException e)
				{
					json.put("error", e.getMessage());
					return;
				}
			}

			if(request.getParameter("accact")!=null&&"signin".equals(request.getParameter("accact")))
			{
				String userName = request.getParameter("username");
				String password = request.getParameter("pass");
				if(!user.auth(userName, password))
					json.put("auth", false);
				else
				{
					json.put("auth", true);
					request.getSession().setAttribute("userID", user.me().getID());
				}
			}
			
			if(request.getParameter("accact")!=null&&"signout".equals(request.getParameter("accact"))) // logout
			{
				request.getSession().setAttribute("userID", null);
				json.put("signout", true);
			}
			
			// restore logon from session
			Integer currUID = null;
			if( (currUID = (Integer) request.getSession().getAttribute("userID"))!=null)
			{
				UserEntity currUsr = user.getUser(currUID);
				if(currUsr!=null)
				{
					user.setCurrUser(currUsr);
					json.put("username", currUsr.getName());
				}
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
				int gameid = Integer.parseInt(request.getParameter("gameID"));
				gam = game.getGame(gameid);
				request.setAttribute("GameIDi", gameid);
			}
			catch(NumberFormatException | NullPointerException e)
			{ return; }

			switch(request.getParameter("option"))
			{
			case "commentandscore":
				request.setAttribute("scores", score.topScores(gam));
				request.setAttribute("comments", comme.commentsFor(gam));
				request.getRequestDispatcher("/WEB-INF/jsp/comscore.jsp").include(request, response);
				purecode = true;
				break;
			case "addcomment":
				String comm = request.getParameter("comment");
				if(comm==null || comm.length()==0 || user.me()==null)
					return ;
				JSONObject jsn = new JSONObject(comm);
				CommentEntity comment = new CommentEntity(gam, user.me(), jsn.getString("comm"));
				comme.addComment(comment);
				//response.getWriter().println("OK "+comment.getID());
				break;
			case "play":
				request.getRequestDispatcher(gam.getServletPath()).include(request, response);
				Integer scr = (Integer) request.getAttribute("score");   
				if(scr!=null && user.me()!=null)
				{
					ScoreEntity scoo = new ScoreEntity(gam, user.me(), scr);
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
			json.put("error", "Error during servlet execurion:"+e.getMessage());
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
