package sk.dalik;

import java.io.IOException;
import java.util.regex.Pattern;

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
import sk.tsystems.gamestudio.services.jdbc.StatisticsDTO;

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
        System.out.println("*****************************************************************************************");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("stats")!=null && "show".equals(request.getParameter("stats"))) // get DBO stat
		{
			StatisticsDTO stats;
			try {
				stats = new StatisticsDTO();
				request.setAttribute("stats", stats);
				request.getRequestDispatcher("/WEB-INF/jsp/Stats.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return;
		}
		
		JSONObject json = new JSONObject();
		request.setAttribute("json", json); // global json share for servlets
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
			if(game.listGames().isEmpty()) // we need to create games 
			{
				GameEntity ga = new GameEntity(0, "Minesweeper");
				ga.setRunnable(sk.tsystems.gamestudio.game.minesweeper.Minesweeper.class);
				ga.setServletPath("MinesWeb");
				game.addGame(ga);
				
				ga = new GameEntity(0, "Stones");
				ga.setRunnable(sk.tsystems.gamestudio.game.stones.ui.StonesUI.class);
				ga.setServletPath("stones");
				game.addGame(ga);

				ga = new GameEntity(0, "Guess the number");
				ga.setRunnable(sk.tsystems.gamestudio.game.guessnumber.ui.GuesNumRun.class);
				ga.setServletPath("Guess");
				game.addGame(ga);

				ga = new GameEntity(0, "Memorize");
				//ga.setRunnable(sk.tsystems.gamestudio.game.guessnumber.ui.GuesNumRun.class);
				ga.setServletPath("memorize");
				game.addGame(ga);
				
			}
			
			if(request.getParameter("checknick")!=null)
			{
				String nick = request.getParameter("checknick").trim();
				String mail = request.getParameter("mail");
				if(mail!=null) {
					json.put("mailnotexists", user.getUser(mail)==null);
					json.put("mailok", chekMailFmt(mail));
					json.put("mail", mail);
				}
				
				// TODO check with regex
				json.put("usernonexists", user.getUser(nick)==null);
				json.put("nick", nick);
				return ;
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
			
			if(request.getParameter("accact")!=null)
			{
				accounting(request, json, user);
				return;
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
				JSONObject child = new JSONObject(); // game must use separate json
				request.setAttribute("json", child);

				request.getRequestDispatcher(gam.getServletPath()).include(request, response);
				ScoreEntity scr = (ScoreEntity) request.getAttribute("score");   
				if(scr!=null && user.me()!=null)
				{
					scr.setUser(user.me());
					scr.setGame(gam);
					
					score.addScore(scr);
				}
				json.put("gameout", child);
				request.setAttribute("json", json); // restore main json
				break;

			case "addrate":
				try {
					int ratt = Integer.parseInt(request.getParameter("rate"));
					if(user.me()!=null)
						rating.addRating(new RatingEntity(gam, user.me(), ratt));
				} catch (NumberFormatException | NullPointerException e) {}
			case "getrate":
				json.put("rating", getRating(gam, user.me(), rating));
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
	
	private void accounting(HttpServletRequest request, JSONObject jsonm, UserService user)
	{
		JSONObject jsona = new JSONObject();
		String action = request.getParameter("accact"); 
		if(action==null)
			action = "unknown";
		
		try
		{
			jsona.put("result", false); // globally is not OK
			jsona.put("message", "empty");
			jsona.put("action", action);

			String name = request.getParameter("username");
			String pass = request.getParameter("pass");
			String newpass = request.getParameter("new");
			String mail = request.getParameter("mail");
			
			if("signout".equals(action)) // logout
			{
				request.getSession().setAttribute("userID", null);
				user.setCurrUser(null);
				jsona.put("result", true);
				return;
			}
			
			if("register".equals(action))
			{
				jsona.put("message", "Registration failed");
				try
				{
					mail= mail.toLowerCase();
					if(name.length()<3)
						jsona.put("message", "Name is too short");
					else
					if(pass.length()<3)
						jsona.put("message", "Password is too short");
					else
					if(!chekMailFmt(mail))
						jsona.put("message", "Invalid email address");
					else
					{ 
						UserEntity usrik = user.addUser(name); 
						usrik.setPassword(pass);
						usrik.setMail(mail);
						if(!user.updateUser(usrik))
							jsona.put("message", "Error updating account"); // TODO what do do??
							
						jsona.put("message", usrik!=null?"ok":"fail");
						request.getSession().setAttribute("userID", usrik.getID());
						user.setCurrUser(usrik);
						jsona.put("result", usrik!=null);
					}
				}
				catch(NullPointerException e)
				{
					jsona.put("message", "Registration failed. Required fields is no set.");
					e.printStackTrace();
				}
				return;
			}

			if("signin".equals(request.getParameter("accact")))
			{
				if(!user.auth(name, pass))
					jsona.put("message", "Bad name or password");
				else
				{
					request.getSession().setAttribute("userID", user.me().getID());
					jsona.put("result", true);
				}
				return;
			}
			else
			if("newpass".equals(action))
			{
				if(user.me()==null) // we must be signed in
				{
					jsona.put("message", "To change password, you must be signed in first");
					return;
				}
				
				if(user.me().getPassword().compareTo(pass)!=0)
				{
					jsona.put("message", "Bad old password.");
					return;
				}

				if(newpass.length()<3)
				{
					jsona.put("message", "New password is too short.");
					return;
				}
					
				user.me().setPassword(newpass);
				jsona.put("message", "error setting password");
				if(user.updateUser(user.me()))
				{
					jsona.put("message", "error setting password");
					jsona.put("result", true);
				}
				return;
			}
		}
		finally {
			if(user.me()!=null)
				jsona.put("username", user.me().getName());
				
			jsona.put("signed", user.me()!=null);
			
			jsonm.put("auth", jsona);
		}
		
	}
	
	private JSONObject getRating(GameEntity gam, UserEntity me, RatingService rating)
	{
		JSONObject json = new JSONObject();
		json.put("game", rating.gameRating(gam));
		
		RatingEntity rat;
		if(me!=null && (rat = rating.myRating(gam, me))!=null)
			json.put("user",rat.getRating());
		
		return json;
	}
	
	private Boolean chekMailFmt(String mail) {
		Pattern mailCHekc = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		return mailCHekc.matcher(mail).matches();
	}
}
