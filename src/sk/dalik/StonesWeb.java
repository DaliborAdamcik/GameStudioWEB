package sk.dalik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.game.stones.core.*;
import sk.tsystems.gamestudio.services.GameService;

import org.json.JSONObject;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(description = "Ajco", urlPatterns = { "/stones" })
public class StonesWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StonesWeb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/NewStones.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//JSONObject json = new JSONObject();
		JSONObject json = (JSONObject) request.getAttribute("json");
		
		json.put("game", "stones");
		
		try
		{
			String action = request.getParameter("action");
			if(action == null )
				action = "none";
			
			HttpSession session = request.getSession();
			Field field = (Field) session.getAttribute("stones");

			if (field == null || action.compareTo("new")==0) {
				int row;
				int col;
				try
				{
					row= Integer.parseInt(request.getParameter("row"));
					col= Integer.parseInt(request.getParameter("col"));
					if(row<2 || col<2 || row>10 || col>10 )
						throw new NumberFormatException();
				}
				catch (NullPointerException | NumberFormatException e)
				{
					row = 4;
					col = 4;
				}
				GameService gamSvc = (GameService) request.getAttribute("commonsvc");
				GameEntity gamEnt = (GameEntity) request.getAttribute("commonsvcgam");
				
				row = gamSvc.gameSetting(gamEnt, "stones_rows", Integer.class, 4);
				col = gamSvc.gameSetting(gamEnt, "stones_cols", Integer.class, 4);

				field = new Field(row, col);
				session.setAttribute("stones", field);
				session.setAttribute("stones-time", System.currentTimeMillis());
				json.put("newgame", true);
				json.put("rows", row);
				json.put("cols", col);
			}

			try {
				int value = Integer.parseInt(request.getParameter("move"));
				field.move(value);
			} catch (NumberFormatException | NullPointerException e) {
			}
			
			json.put("solved", field.isSolved());
			
			json.put("won", field.isSolved());
			if(field.isSolved())
			{
				/*int myscore = (int) (System.currentTimeMillis() - (long) session.getAttribute("stones-time")) / 1000;
				myscore = (100000 / myscore)*field.getRowCount()*field.getColumnCount();
				myscore = (int) (System.currentTimeMillis() - (long) session.getAttribute("stones-time"));
				*/
				ScoreEntity myscore = new ScoreEntity((int) (System.currentTimeMillis() - (long) session.getAttribute("stones-time")),
						field.getRowCount()*field.getColumnCount()); 
				
				request.setAttribute("score", myscore);
				json.put("score", myscore.getScore());
			}
			
	
			List<JSONObject> jsons = new ArrayList<>();
			for (int row = 0; row < field.getRowCount(); row++) {
				for (int col = 0; col < field.getColumnCount(); col++) {
					JSONObject son = new JSONObject();
					son.put("row", row);
					son.put("col", col);
					
					int value = field.getValueAt(row, col);
					if (value == Field.EMPTY_CELL) 
						son.put("val", 0);
					 else
						son.put("val", value);
					
					jsons.add(son);
				}
			}
			
			json.put("field", jsons);
		}
		finally {
			/*response.setContentType("application/json");
			response.getWriter().print(json);*/
		}
	}
	
/*	private Field newgame(HttpSession session)
	{
		Field stones = new Field(3,3);
		session.setAttribute("stones", stones);
		return stones;
	}
*/
}
