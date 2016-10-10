package sk.dalik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.services.GameService;

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
		
		String action = request.getParameter("action");
		if(action == null )
			action = "none";
		
		HttpSession session = request.getSession();
		Regexo field = (Regexo) session.getAttribute("regexmatch");

		if (field == null || action.compareTo("new")==0) {
			GameService gamSvc = (GameService) request.getAttribute("commonsvc");
			GameEntity gamEnt = (GameEntity) request.getAttribute("commonsvcgam");
			
			int i = 0;
			String txt;
			String reg;
			ArrayList<Regexo> levels = new ArrayList<>();
			do {
				reg = gamSvc.gameSetting(gamEnt, "rgxr_"+i, String.class, null);
				txt = gamSvc.gameSetting(gamEnt, "rgxs_"+i, String.class, null);
				i++;
				if(reg!=null&& txt!=null)
					levels.add(new Regexo(txt, reg));
				
			} while (reg!=null && txt!=null);
			
			if(levels.size()==0)
				throw new RuntimeException("no levels found in config.");
			
			Collections.shuffle(levels);
			
			field = levels.get(0);
			
			session.setAttribute("regexmatch", field);
			session.setAttribute("regexmatch-time", System.currentTimeMillis());

			json.put("newgame", true);
			json.put("words", field.getWords());
			//regexmatch
		}
		
		if(request.getParameter("retid")!=null) 
			json.put("retid", request.getParameter("retid"));

		if (action.compareTo("tryhit")==0) {
			String word = request.getParameter("word");
			if(word == null)
				word = "";
			
			boolean hit = field.tryHit(word);
			json.put("hit", hit);
		}

		json.put("regex", field.getRegex());
		json.put("tryc", field.tryCount());
		json.put("hitc", field.hitCount());
		json.put("size", field.regexCount());


		json.put("won", field.isWin());
		if(field.isWin())
		{
			ScoreEntity myscore = new ScoreEntity((int) (System.currentTimeMillis() - (long) session.getAttribute("regexmatch-time")),
					0); 
			
			request.setAttribute("score", myscore);
			json.put("score", myscore.getScore());
		}
	}
	
	private class Regexo {
		@SuppressWarnings("serial")
		private class NoHitException extends Exception {
			public NoHitException(String message) {
				super(message);
			}
		};

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
			Collections.shuffle(regexs);

			tryCnt = 0;
			pos = -1;
			hitCount = 0;
			nextPat();
		}

		public String getRegex() {
			return currTest;
		}

		public List<String> getWords() {
			return words;
		}

		private boolean nextPat() {
			pos++;

			if (pos >= regexs.size())
				return false;

			currTest = regexs.get(pos);
			test = Pattern.compile("(" + currTest + ")");

			Matcher mat = test.matcher(text);
			if (!mat.find())
				throw new RuntimeException("no match");

			target = mat.group(1);
			System.out.printf("regex hlada: pat: '%s' slovo:'%s'\r\n", currTest, target);

			return true;
		}

		public boolean tryHit(String word) {
			try {
				System.out.printf("skus: mam '%s' cakam '%s'\r\n", word, target);
				if (words.indexOf(word) < 0)
					throw new NoHitException("No word in list");

				if (word.compareTo(target) != 0)
					throw new NoHitException("Bad word");

				nextPat();
				hitCount++;

				return true;

			} catch (NoHitException e) {
				if (tryCnt++ == 2) {
					tryCnt++;
					nextPat();
				}
				return false;
			}
		}

		public boolean isWin() {
			return hitCount == regexs.size();
		}
		
		public int tryCount() {
			return tryCnt;
		}
		
		public int hitCount() {
			return hitCount;
		}
		
		public int regexCount() {
			return regexs.size();
		}		

	}
}
