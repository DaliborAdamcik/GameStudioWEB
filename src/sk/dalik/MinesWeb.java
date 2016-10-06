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
import org.json.*;

import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.game.minesweeper.core.*;

/**
 * Servlet implementation class MinesWeb
 */
@WebServlet("/MinesWeb")
public class MinesWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MinesWeb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/NewMines.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//JSONObject json = new JSONObject();
		JSONObject json = (JSONObject) request.getAttribute("json");
		
		json.put("client", "none");
		json.put("game", "mines");
		
		try
		{
			String command = request.getParameter("action");
			
			if(command==null)
				command = "empty";
			
			json.put("comamnd", command);
			
			HttpSession session = request.getSession();
			Field field = (Field) session.getAttribute("mines");
			 
			if (command.compareTo("new")==0) {
				int row = tryParseInt(request.getParameter("row"));
				int col = tryParseInt(request.getParameter("col"));
				int mines = tryParseInt(request.getParameter("mine"));
				json.put("row", row);
				json.put("col", col);
				
				if(row>1 && col>1 && row<11 && col<11 && mines>0)
				{
					field = new Field(row, col, mines);
					session.setAttribute("mines", field);
					session.setAttribute("mines-time", System.currentTimeMillis());
					
					json.put("client", "field");
					return;
				}
				else
				{
					json.put("error", "Invalid parameters for new game");
					return;
				}
			 }
			
			if(field == null)
			{
				json.put("error", "Please, start new game.");
				return;
			}
			
			
			try
			{
				int row = Integer.parseInt(request.getParameter("row"));
				int col = Integer.parseInt(request.getParameter("col"));
			
				switch (command) {
					case "open":
						field.openTile(row, col);
						json.put("client", "open");
						
						jsonField(field, json);
					break;
				
				case "mark":
						field.markTile(row, col);
						json.put("client", "mark");
						jsonField(field, json);
						break;
				}
			}
			catch(NumberFormatException e){} 
			
			if(GameState.SOLVED.equals(field.getState()))
			{
				json.put("won", true);
				ScoreEntity myscore = new ScoreEntity((int) (System.currentTimeMillis() - (long) session.getAttribute("mines-time")),
						field.getRowCount()*field.getColumnCount()*field.getMineCount()); 
						
				/*		(int) (System.currentTimeMillis() - (long) session.getAttribute("mines-time")) / 1000;
				myscore = (100000 / myscore)*field.getRowCount()*field.getColumnCount();*/

				request.setAttribute("score", myscore.getScore());
				json.put("score", myscore);
			}
			
			json.put("state", field.getState().toString());
			json.put("nummines", field.getRemainingMineCount());
		}
		finally {
			/*response.setContentType("application/json");
			response.getWriter().print(json);*/
		}
	}
	
	private int tryParseInt(String s)
	{
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		}
		
	}
	
	
	private void jsonField(Field field, JSONObject jsn)
	{
		List<JSONObject> sons = new ArrayList<>();
		
		for(int row = 0; row<field.getRowCount();row++)
        {
   			for(int col =0;col<field.getColumnCount();col++)
        	{
        		Tile tile = field.getTile(row, col);
        		JSONObject son = new JSONObject();
				son.put("row", row);
				son.put("col", col);
				son.put("state", "close");

        		
        		switch(tile.getState())// OPEN CLOSED MARKED
        		{
					case MARKED:
							son.put("state", "mark");
						break;
					case CLOSED:
		        		if(field.getState()!=GameState.FAILED)
		        		{
		        			break;
		        		}
					default:
						if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Mine)
							son.put("state", "bomb");
							else
							if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Clue)
							{
								son.put("state", "open");
								son.put("value", ((sk.tsystems.gamestudio.game.minesweeper.core.Clue)tile).getValue() );
							}
							else
								son.put("state", "null");
						break;

        		}
        		
        		sons.add(son);
        		
        	}
        } 
		
		jsn.put("clues", sons);
	}
}