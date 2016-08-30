package sk.dalik;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.tsystems.gamestudio.services.jdbc.creator.CreatorOracle;

/**
 * Servlet implementation class ColdStart
 */
@WebServlet("/ColdStart")
public class ColdStart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.setOut(new PrintStream(response.getOutputStream(), true));
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
