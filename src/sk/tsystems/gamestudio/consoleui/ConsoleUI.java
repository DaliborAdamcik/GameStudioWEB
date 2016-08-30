package sk.tsystems.gamestudio.consoleui;

import java.util.Date;
import java.util.List;
import java.util.Random;

import sk.tsystems.gamestudio.consoleui.GameUiRunner.RunTarget;
import sk.tsystems.gamestudio.entity.CommentEntity;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.RatingEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.GameService;
import sk.tsystems.gamestudio.services.RatingService;
import sk.tsystems.gamestudio.services.ScoreService;
import sk.tsystems.gamestudio.services.UserService;
import sk.tsystems.gamestudio.services.CommentService;


public class ConsoleUI extends ConsoleInput {
	private GameService games;
	private UserService users;
	private CommentService comments;
	private ScoreService scores;
	private RatingService ratings;
	
	public ConsoleUI(GameService games, UserService users, CommentService comments, ScoreService scores, RatingService ratings) {
		super();
		this.games = games;
		this.users = users;
		this.comments = comments;
		this.scores = scores;
		this.ratings = ratings;
	}
	
	public void run()
	{
		String[] asks = null; 
		do {
			if(asks!=null)
			{
				System.out.println("*** Bad name or password");
				if(!readYN("Retry login"))
					return;
			}
			asks = askMyName();
		}
		while (!users.auth(asks[0], asks[1]));
		System.out.println(":>> "+users.me().getName()+" welcome in game center!");
		
		boolean running = true;
		Object menuOpt;
		List<GameEntity> gamelist = games.listGames();
		do
		{
			printMainMenu(gamelist);
			menuOpt = getMenuOption(gamelist.size());
			if(menuOpt instanceof Integer) // selected game
			{
				System.out.println();
				runGame(gamelist.get(((Integer) menuOpt)-1));
			}
			
			if (menuOpt instanceof Character) {
				switch(((Character) menuOpt).charValue())
				{
					case 'q': 
						running = false;
						break;
					case 'r': // refresh game list
						System.out.println(":: refresh game list");
						gamelist = games.listGames();
						break;
					case 'c': 
						changepass();
						break;
						
						
						
///////////////////////////////////////////////////// TEMP MENU ITEMS ///////////////////////////////////////////////////////////						
					case 's': // TODO temp
						commentsShow(gamelist.get(0));
						break;
					case 'a': // TODO temp
						commentAdder(gamelist.get(0));
						commentsShow(gamelist.get(0));
						break;
						
					case 'w': // TODO test add score into table, temp
						ScoreEntity sco = new ScoreEntity(gamelist.get(0), users.me(), 10);
						scores.addScore(sco);
						for(ScoreEntity s : scores.topScores(gamelist.get(0)))
						{
							System.out.println(s);
						}
						break;
						
					case 't': // TODO test add rate into table temp
						Random ra = new Random();
						UserEntity usr = users.addUser("rnd_usr_"+ra.nextInt(100));
						
						System.out.println(usr);

						RatingEntity rat = new RatingEntity(gamelist.get(0), usr, 10);
						ratings.addRating(rat);
						ratings.gameRating(gamelist.get(0));
						break;
					default: // invalid option
						System.out.println("!! Ivalid menu option.");
						break;
				}
			}
			System.out.println();
		}
		while(running);
		System.out.println("....> Bye bye :-)");
	}
	
	private void printMainMenu(List<GameEntity> gamelist)
	{
		System.out.println("...:> GAME STUDIO <:... maiN");
		int optcount = 1;
		System.out.println("\tq: Quit game studio");
		System.out.println("\tr: Refresh game list");
		System.out.println("\tc: Change password");
		
		for(GameEntity game: gamelist)
			System.out.printf("\t%2d: RUN %s game (id: %d, cls: %s, let: %s) rating %f\r\n", optcount++, game.getName(), game.getID(), game.className(), game.getServletPath(), ratings.gameRating(game));
	}
	
	private Object getMenuOption(int maxmenu)
	{
		do // TODO filter there?
		{
			System.out.print("> Enter option: ");
			String option = this.readLine().trim().toLowerCase();
			
			try
			{
				int i = Integer.parseInt(option);
				if(i<1 || i>maxmenu)
					throw new NumberFormatException();
				return new Integer(i);
			}
			catch(Exception e) // TODO we need to check there values, ! Lambda??
			{
				if (option.length()<1)
					return null;
				
				return new Character(option.charAt(0)); 
			}
		
		}
		while(true);
	}
	
	private String[] askMyName()
	{
		String[] res = new String[2];
		System.out.println("Sign in GameCenter---->");
		System.out.print("User name (default 'dalik'): ");
		res[0]= this.readLine().trim(); // TODO check name for white spaces
		if(res[0].length()==0)
			res[0]= "dalik";

		System.out.print("Password: (default '1234'): ");
		res[1]= this.readLine().trim(); 
		if(res[1].length()==0)
			res[1]= "1234";
		
		return res;
	}
	
	private void runGame(GameEntity game)
	{
		System.out.println(">>>Run game: "+game.getName());
		scoreShow(game);

		try {
			int retScore = game.run(RunTarget.UI_CONSOLE); // try to run game, returns: 0 = exit, -1 fail, -2 and lower = undefined, 0+ score 
			
			if(retScore > 0)
			{
				ScoreEntity myScore = new ScoreEntity(game, users.me(), retScore);
				scores.addScore(myScore);
				scoreShow(game, myScore);
			}
			else
			scoreShow(game);
			
			if(this.readYN("Do you want add comment"))
				commentAdder(game);
			else
				if(this.readYN("Do you want show comments"))
					commentsShow(game);
			
			if(this.readYN("Do you want (re)rate this game"))
				ratingAdd(game);
				
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Cant run game: "+e.getMessage());
			// e.printStackTrace();
		}
	}
	
	private void ratingAdd(GameEntity game)
	{
		int rating = readInt("Enter rating ", 1, 5);
		ratings.addRating(new RatingEntity(game, users.me(), rating));
	}
	
	private void commentAdder(GameEntity game)
	{
		System.out.print("Enter your comment: ");
		String com = this.readLine().trim();
		if(com.length()>0)
		{
			comments.addComment(new CommentEntity(game, users.me(), com, 0, new Date()));
		}
		commentsShow(game);
	}
	
	private void commentsShow(GameEntity game)
	{
		for (CommentEntity commi : comments.commentsFor(game)) {
			System.out.printf("%d: %s %s %s\r\n", commi.getID(), commi.getUser().getName(), 
					commi.getDate().toString(), commi.getComment());
		}
	}
	
	private void printScore(ScoreEntity score, int idx)
	{
		System.out.printf("%2d. %d %s %s\r\n", idx, score.getScore(), score.getUser().getName(),  
				score.getDate().toString());
	}
	
	private void scoreShow(GameEntity game, ScoreEntity myScore)
	{
		scoreShow(game);
		System.out.print("\n\tYour score: ");
		printScore(myScore, 0);
	}
	
	private void scoreShow(GameEntity game)
	{
		System.out.printf("** TOP Scores for -> %s <- ***\r\n", game.getName());
		int cnt = 1;
		
		for (ScoreEntity score: scores.topScores(game)) {
			printScore(score, cnt++);
		}
		
	}
	
	private void changepass()
	{
		System.out.println("==== Change password ===\nOld password: ");
		String pass = readLine().trim();
		if(pass.compareTo(users.me().getPassword())!=0)
		{
			System.out.println("** Bad old password");
			return;
		}

		System.out.println("New password: ");
		pass = readLine().trim();
		if(pass.length()<3)
		{
			System.out.println("** new password must be at least 3 characters long");
			return;
		}
		users.me().setPassword(pass);
		if(users.updateUser(users.me()))
			System.out.println("New password set");
		else
			System.out.println("** failed to save new password");
	}
	

}
