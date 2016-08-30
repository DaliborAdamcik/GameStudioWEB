/**
 * 
 */
package sk.tsystems.gamestudio.game.minesweeper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Dalibor AdamËÌk
 *
 */
public class Settings implements Serializable {
	static final long serialVersionUID = 1;
	
	private final int rowCount;
	private final int columnCount;
	private final int mineCount; 
	
	public static Settings BEGINNER = new Settings(9, 9, 10);
	public static Settings INTERMEDIATE = new Settings(16, 16, 40);
	public static Settings EXPERT = new Settings(16, 30, 99);
	
	private static final String SETTING_FILE  = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";
	/**
	 * 
	 */
/*	public Settings() {
		this(8,8,10);
	}*/
	
	public Settings(int rowCount, int columnCount, int mineCount)
	{
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
	}
	
	public void save()
	{
		// TODO sem eöte pridaù File
		
		try
		{
			FileOutputStream strm = new FileOutputStream(SETTING_FILE);
			ObjectOutputStream os = new ObjectOutputStream(strm);
			try
			{
				os.writeObject(this);
			}
			finally
			{
				strm.close();
				os.close();
			}
		}
		catch(Exception e)
		{
			System.err.println("Cant save settings: "+e.getMessage());
		}
	}
	
	public static Settings load()
	{
		try
		{
			FileInputStream strm = new FileInputStream(SETTING_FILE);
			ObjectInputStream os = new ObjectInputStream(strm);
			try
			{
				Settings set = (Settings) os.readObject();
				return set;
			}
			finally
			{
				strm.close();
				os.close();
			}
		}
		catch(Exception e)
		{
			System.err.println("Cant load settings: "+e.getMessage());
			return BEGINNER;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Settings))
			return false;
		Settings s = (Settings)o;

		return this.getRowCount()== s.getRowCount() 
				&& this.getColumnCount()== s.getColumnCount()
				&& this.getMineCount()== s.getMineCount();
	}
	
	@Override
	public int hashCode() 
	{
		return getColumnCount()*getMineCount()*getRowCount();
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

}
