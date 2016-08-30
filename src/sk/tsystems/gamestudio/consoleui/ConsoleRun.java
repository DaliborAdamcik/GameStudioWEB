package sk.tsystems.gamestudio.consoleui;
import sk.tsystems.gamestudio.services.GameService;
import sk.tsystems.gamestudio.services.RatingService;
import sk.tsystems.gamestudio.services.UserService;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.services.CommentService;
import sk.tsystems.gamestudio.services.ScoreService;


public class ConsoleRun{

	public static void main(String[] args) {
		
		/*StatisticsDTO dto = new StatisticsDTO();
		System.out.println(dto);
		System.exit(0);*/
		
		String driver = "jpa"; // default driver for use with application
		if(args.length>0)
		{
			switch (args[0].toLowerCase()) {
			case "jpa":
				driver = "jpa";
				break;

			case "jdbc":
				driver = "jdbc";
				break;

			default:
				System.err.printf("No suitable driver found: %s\n\t *Please specify a valid driver or\t\tleave application parameters empty.\n" +args[0]);
				break;
			}
		}
		
		String drivercls = String.format("sk.tsystems.gamestudio.services.%s.", driver);
		System.err.printf("Using driver: %s (%s*)\n", driver.toUpperCase(), drivercls);
		
		Class<?> gameCon = null;
		Class<?> userCon = null;
		Class<?> commCon = null;
		Class<?> scorCon = null;
		Class<?> rateCon = null;
		
		try
		{
			gameCon = Class.forName(drivercls+"GameSvc");
			userCon = Class.forName(drivercls+"UserSvc");
			commCon = Class.forName(drivercls+"CommentSvc");
			scorCon = Class.forName(drivercls+"ScoreSvc");
			rateCon = Class.forName(drivercls+"RatingSvc");
		}
		catch(SecurityException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		
		try(
				GameService game = (GameService) gameCon.newInstance();
				UserService user = (UserService) userCon.newInstance();
				CommentService comme = (CommentService) commCon.newInstance();
				ScoreService score =  (ScoreService) scorCon.newInstance();
				RatingService rating = (RatingService) rateCon.newInstance();
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
			
			ConsoleUI ui = new ConsoleUI(game, user, comme, score, rating);
			ui.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
