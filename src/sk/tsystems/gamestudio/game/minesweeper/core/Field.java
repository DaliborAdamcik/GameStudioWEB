package sk.tsystems.gamestudio.game.minesweeper.core;

import sk.tsystems.gamestudio.game.minesweeper.core.Tile.State;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     * Tile[rowCount][columnCount];
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }
            else
            if(tile instanceof Clue && ((Clue)tile).getValue()==0)
            	openAdjacentTiles(row, column);

            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
        }
    }
    
    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        Tile tile = getTile(row, column);
        
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);

            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
        }
        else
        if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
        }
    }

    /** 
     * Open 
     * @param row
     * @param column
     */
    private void openAdjacentTiles(int row, int column)
    {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                    	Tile ti = tiles[actRow][actColumn]; 
                    	Tile tileOrig = tiles[row][column];
                        if (ti instanceof Clue &&  ((Clue)tileOrig).getValue()==0 && ((Clue)ti).getState()==State.CLOSED) {
                            tiles[actRow][actColumn].setState(State.OPEN);
                            openAdjacentTiles(actRow, actColumn);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Generates playing field.
     */
    private void generate() {
        java.util.Random rnd = new java.util.Random();
        
        int cnt = mineCount;
        while(cnt>0)
        {
        	// Tile[rowCount][columnCount];
        	int col = rnd.nextInt(getColumnCount());
        	int row = rnd.nextInt(getRowCount());
        	if(this.tiles[row][col]==null)
			{
				this.tiles[row][col] = new Mine();
				cnt--;
			}
        }
        
        for(int row=0;row<getRowCount();row++)
        {
        	for(int col=0;col<getColumnCount();col++)
        	{
            	if(this.tiles[row][col]==null)
    			{
            		this.tiles[row][col]= new Clue(countAdjacentMines(row, col));
    			}
        		
        	}
        }
    }
    
    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {
        return	(this.getColumnCount()*this.getRowCount() - this.getNumberOf(State.OPEN) == this.getMineCount()); 
    }
    
    private int getNumberOf(Tile.State state)
    {
    	int cnt=0;
    	for(int row = 0;row<this.getRowCount();row++)
    		for(int col = 0; col<this.getColumnCount();col++)
    		{
    			if(this.getTile(row, col).getState() == state)
    				cnt++;
    		}
    			
    	return cnt;
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }
    
    public int getRemainingMineCount()
    {
    	return this.getMineCount() - this.getNumberOf(State.MARKED);
    }

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getMineCount() {
		return mineCount;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
    
	public Tile getTile(int row, int col)
	{
    	if(row<0 || row>=getRowCount() || col<0 || col>=getColumnCount())
    		return null;

    	// Tile[rowCount][columnCount];
		return this.tiles[row][col];
	}
}
