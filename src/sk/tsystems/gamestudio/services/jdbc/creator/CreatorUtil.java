package sk.tsystems.gamestudio.services.jdbc.creator;

public class CreatorUtil {

	public static void main(String[] args) {
		System.out.println("*** Oracle database structure creator ***");
		System.out.println("ver 1.0");
		try(CreatorOracle orcre = new CreatorOracle())
		{
			orcre.run();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n============== END ==============");
	}

}
