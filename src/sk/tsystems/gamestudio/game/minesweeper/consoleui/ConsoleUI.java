package sk.tsystems.gamestudio.game.minesweeper.consoleui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.tsystems.gamestudio.game.minesweeper.Minesweeper;
import sk.tsystems.gamestudio.game.minesweeper.Settings;
import sk.tsystems.gamestudio.game.minesweeper.UserInterface;
import sk.tsystems.gamestudio.game.minesweeper.core.Field;
import sk.tsystems.gamestudio.game.minesweeper.core.GameState;

/**
 * Console user interface.
 */
public class ConsoleUI extends sk.tsystems.gamestudio.consoleui.ConsoleInput implements UserInterface {
    /** Playing field. */
    private Field field;
    private int badcmd = 0; // counts bad commands from app startup
    private boolean playing = true;

    
    /* (non-Javadoc)
	 * @see minesweeper.consoleui.UserInterface#newGameStarted(minesweeper.core.Field)
	 */
    @Override
	public void newGameStarted(Field field) {
        this.field = field;
       
        do {
            update();
            processInput();
        } while(field.getState().compareTo(GameState.PLAYING)==0 && playing);
        
        if(!playing)
        	return;

        update(); // show "solved/failed" mine-field
       
        switch(field.getState())
        {
			case FAILED: System.out.println("Auch, You found a MINE! (G A M E   O V E R)");	break; 
			case SOLVED: 
				System.out.println("You W O N :-)\r\n"); 
				break;
			default: break;
        }
    }
    
    /* (non-Javadoc)
	 * @see minesweeper.consoleui.UserInterface#update()
	 */
    @Override
	public void update() {
        for(int row = 0; row<field.getRowCount();row++)
        {
    		if(row==0)
    		{
    			int charcnt = 4+field.getColumnCount()*3;
    			StringBuilder s1 = new StringBuilder(charcnt);
    			StringBuilder s2 = new StringBuilder(charcnt);
    			s1.append("Mi  ");
    			s2.append("nes-");
    			
       			for(int col =0;col<field.getColumnCount();col++)
       			{
       				s1.append(String.format("%2d ", col));
       				s2.append("---");
       			}	
       			s1.append(" | Unmarked mines: "+field.getRemainingMineCount());
       			s2.append("-| Your time: ").append(printCurrentTime(Minesweeper.getInstance().getPlayingSeconds()));
       			
       			System.out.println(s1);
       			System.out.println(s2);
    		}

    		System.out.printf(" %c| ", row+'A');
    		
   			for(int col =0;col<field.getColumnCount();col++)
        	{
        		sk.tsystems.gamestudio.game.minesweeper.core.Tile tile = field.getTile(row, col);
        		
        		switch(tile.getState())// OPEN CLOSED MARKED
        		{
					case CLOSED:
		        		if(field.getState()!=GameState.FAILED)
						System.out.print("   ");
		        		else
		        			updatePrintTile(tile);
						break;
					case MARKED:
						{
			        		if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Mine)
			        			System.out.print(" X ");
			        		else
			        		if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Clue)
			        			System.out.print(" C ");
			        		else
			        			System.out.print(" ! ");
						}
						break;
					default:
						{
							updatePrintTile(tile);
						}
						break;
        		}
        		
        		
        	}
        	System.out.println();
        }
    }
    
    private String printCurrentTime(int cursec)
    {
    	if(cursec / 3600>0)
    		return String.format("%dhr ", cursec / 3600)+printCurrentTime(cursec%3600);
    	else
    	if(cursec / 60>0)
    		return String.format("%dmin ", cursec / 60)+printCurrentTime(cursec%60);
    	else
    	if(cursec>0)
    		return String.format("%dsec ", cursec);
    	else
    		return ""; 
    }
    
    private void updatePrintTile(sk.tsystems.gamestudio.game.minesweeper.core.Tile tile)
    {
		if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Mine)
			System.out.print(" * ");
		else
		if(tile instanceof sk.tsystems.gamestudio.game.minesweeper.core.Clue)
			System.out.print(String.format(" %1d ", ((sk.tsystems.gamestudio.game.minesweeper.core.Clue)tile).getValue()));
		else
			System.out.print(" ! "); // null on field
    }
    
    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() {
    	try
    	{
        	if((this.badcmd++%4==0)||this.badcmd==0) // shows help o start or when user types bad command more times
        		System.out.println("help: x,e:exit / m0a: mark tile A0 / o1b: open tile B1 / n: new game");

        	System.out.print(">"); // indicate user input
    		handleInput(this.readLine().toLowerCase());
    		badcmd=1;
    	}
    	catch(WrongFormatException ex)
    	{
    	  System.err.println(ex.getMessage());
    	  processInput();
    	}
    }
    
    private void handleInput(String input) throws WrongFormatException
    {
        Pattern pattern = Pattern.compile("[exn]|[om]([0-"+Integer.toString(field.getColumnCount()-1)+"])([a-"+
        		((char)('a'+field.getRowCount()-1))+"])");

        Matcher matcher;
        
       	matcher = pattern.matcher(input);
        
       	if(!matcher.matches())
       		throw new WrongFormatException("Bad input string (i dont know what to type here)");
        
        int irow=0;
        int icol=0;
        if("mo".indexOf(matcher.group(0).charAt(0))>=0) // better is assign its to local var as type it more times to attribute
        {
	        irow = (int)(matcher.group(2).charAt(0)-'a'); // do conversion
	        icol = Integer.parseInt(matcher.group(1)); // we dont need to check try/catch, number format is checked by regular word
        }
        
        switch(matcher.group(0).charAt(0)) // run actions
        {
        	case 'x':
        	case 'e': System.out.println("Bye...");playing = false; break;
	    	case 'm': field.markTile(irow, icol);break;
	    	case 'o': field.openTile(irow, icol);break;
	    	case 'n': handleNewGame(field);
	    	
        }
    }
    
    private int readint(String msg)
    {
    	int res=0;
    	do 
    	{
	    	System.out.printf(">%s: ", msg);
			String rd = this.readLine().trim();
    		try
    		{
    			res = Integer.parseInt(rd);
    		}
    		catch(NumberFormatException e)
    		{
    	    	res=0;
    		}
    	}
    	while(res<3 && res>10);
    	return res;
    }
    
    private void handleNewGame(Field field)
    {
    	int width = readint("field width");
    	int height = readint("field height");
    	int mines = readint("miness");
    	Settings set = new Settings(height, width, mines);
    	set.save();
    	field = new Field(height, width, mines); 
    	 
    }
}
