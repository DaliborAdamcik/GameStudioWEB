package sk.dalik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(urlPatterns = { "/memorize" })
public class MemorizeWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemorizeWeb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/NewMemorize.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//JSONObject json = new JSONObject();
		JSONObject json = (JSONObject) request.getAttribute("json");
		
		json.put("game", "memorize");
		
		try
		{
			String action = request.getParameter("action");
			if(action == null )
				action = "none";
			
			HttpSession session = request.getSession();
			MemorizeLogic field = (MemorizeLogic) session.getAttribute("memorize");

			if (field == null || action.compareTo("new")==0) {
				field = new MemorizeLogic(3);
				
				session.setAttribute("memorize", field);
				session.setAttribute("memorize-time", System.currentTimeMillis());
				session.setAttribute("mem-first", null);

				json.put("newgame", true);
				json.put("size", field.getSize());
			}

			if (action.compareTo("open")==0) {
				try {
					int second = Integer.parseInt(request.getParameter("num"));

					Integer first;
					if((first = (Integer)session.getAttribute("mem-first"))==null)
						session.setAttribute("mem-first", second);
					else
					{	
						session.setAttribute("mem-first", null);
						json.put("docompare", true);
						json.put("first", first);
						json.put("second", second);
						json.put("equals", field.equal(first, second));
					}	
						
					json.put("valfor", second);
					json.put("value", field.valOf(second));
						
				} catch (NumberFormatException | NullPointerException e) {
				}
			}
			
			json.put("won", field.isSolved());
			if(field.isSolved())
			{
				int myscore = (int) (System.currentTimeMillis() - (long) session.getAttribute("memorize-time")) / 1000;
				myscore = (100000 / myscore)*field.getSize();

				request.setAttribute("score", myscore);
				json.put("score", myscore);
			}
		}
		finally {
			/*response.setContentType("application/json");
			response.getWriter().print(json);*/
		}
	}
	
	private class MemorizeLogic
	{
		private ArrayList<Pex> pexeso;

		MemorizeLogic(int items) {
			if(items<3)
				throw new RuntimeException("Memorize size must be 3 or moore");
			
			pexeso = new ArrayList<>();
			for(int i = 0;i<items;i++)
			{
				Pex pex = new Pex(i);
				pexeso.add(pex);
				pexeso.add(pex);
			}
			
			Collections.shuffle(pexeso);
		}

		public int getSize() {
			return pexeso.size();
		}
		
		public boolean isSolved()
		{
			boolean sol = true;
			for(Pex pe: pexeso)
				sol &= pe.isOpen();
			
			return sol;
		}
		
		public boolean equal(int first, int second)
		{
			if(!testeqp(first) && !testeqp(second))
				throw new RuntimeException("Out of bounds");
			
			if(pexeso.get(first).equals(pexeso.get(second)))
			{
				pexeso.get(first).setOpen();
				return true;
			}
			
			return false;
		}

		public int valOf(int index)
		{
			if(!testeqp(index))
				throw new RuntimeException("Out of bounds");
			
			return pexeso.get(index).getNum(); 
		}

		/*
		public List<Integer> listItems()
		{
			List<Integer> lis = new ArrayList<>();
			for(Pex pe: pexeso)
			{
				if (pe.isOpen())
					lis.add(pe.getNum());
				else
					lis.add(-1);
			}
			return lis;
		}*/
		
		private boolean testeqp(int i)
		{
			return i>=0 && i<pexeso.size();
		}
		
		private class Pex
		{
			private int num;
			private boolean open;
			Pex(int num) {
				super();
				this.num = num;
				this.open = false;
			}
			public int getNum() {
				return num;
			}
			public boolean isOpen() {
				return open;
			}
			public void setOpen() {
				this.open = true;
			}
		}
		
		
		
	}
}
