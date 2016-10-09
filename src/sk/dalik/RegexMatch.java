package sk.dalik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.DocFlavor.STRING;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import sk.tsystems.gamestudio.entity.ScoreEntity;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(urlPatterns = { "/regmatch" })
public class RegexMatch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegexMatch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/NewRegMatch.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = (JSONObject) request.getAttribute("json");
		
		json.put("game", "regmatch");
		
		try
		{
			String action = request.getParameter("action");
			if(action == null )
				action = "none";
			
			HttpSession session = request.getSession();
			RegxLogic field = (RegxLogic) session.getAttribute("regexmatch");

			if (field == null || action.compareTo("new")==0) {
				field = new RegxLogic();
				
				session.setAttribute("regexmatch", field);
				session.setAttribute("regexmatch-time", System.currentTimeMillis());

				json.put("newgame", true);
				json.put("words", field.getWordList());
				//json.put("size", field.getSize());
				//regexmatch
			}

			if (action.compareTo("tryhit")==0) {
				String word = request.getParameter("word");
				if(word == null)
					word = "";

				boolean hit = field.tryHit(word);
				json.put("hit", hit);
			}

			json.put("regex", field.getRegex());

			/*if (action.compareTo("open")==0) {
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
			
			/*json.put("won", field.isSolved());
			if(field.isSolved())
			{
				ScoreEntity myscore = new ScoreEntity((int) (System.currentTimeMillis() - (long) session.getAttribute("memorize-time")),
						field.getSize()); 
				
				request.setAttribute("score", myscore);
				json.put("score", myscore.getScore());
			}*/
		}
		finally {
		}
	}
	
	private class RegxLogic
	{
		private ArrayList<Regexo> levels;

		RegxLogic() {
			levels = new ArrayList<>();
			levels.add(new Regexo("this is a \"sample\" text to play regex match", "t\\w*t|t\\w*s|p\\w*y"));
			Collections.shuffle(levels);
		}
		
		public List<String> getWordList() {
			return levels.get(0).getWords();
		}

		public String getRegex() {
			return levels.get(0).getRegexs();
		}

		public boolean tryHit(String word) {
			return levels.get(0).tryHit(word);
		}
		/*public int getSize() {
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
		
		/*private boolean testeqp(int i)
		{
			return i>=0 && i<pexeso.size();
		}*/
		
		private class Regexo
		{
			@SuppressWarnings("serial")
			private class NoHitException extends Exception {public NoHitException(String message) {super(message);}};
			private String text;
			private List<String> regexs;
			private List<String> words;
			private int tryCnt;
			private int pos;
			private int hitCount;
			private Pattern test;
			private String currTest;
			private String target; 
			
			public Regexo(String text, String rexes) {
				this.text = text;
				words = new ArrayList<>(Arrays.asList(text.split(" ")));
				regexs = new ArrayList<>(Arrays.asList(rexes.split("\\|")));
				tryCnt = 0;
				pos = -1;
				hitCount = 0;
				nextPat();
			}

			public String getRegexs() {
				return currTest;
			}

			public List<String> getWords() {
				return words;
			}
			
			private boolean nextPat() {
				pos++;
				
				if(pos>= regexs.size())
					return false;
				
				currTest = regexs.get(pos);
				test = Pattern.compile("("+currTest+")");
				
				Matcher mat = test.matcher(text);
				if(!mat.find())
					throw new RuntimeException("no match");
				
				target = mat.group(1);
				
				System.out.println(target);
				
				return true;
			}
			
			public boolean tryHit(String word) {
				try {
					if(word.indexOf(word) < 0)
						throw new NoHitException("No word in list");
					
					if(word.compareTo(target)!=0)
						throw new NoHitException("Bad word");
					
					nextPat();
					
					return true;
					
				} catch (NoHitException e) {
					e.printStackTrace();
					if(tryCnt++==3)
						nextPat();
					
					return false;
				}
			}
			
		}
		
		
		
	}
}
