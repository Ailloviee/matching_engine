package matching_engine;
import java.util.*;

public class Main {
	// Constants for the whole project
	public static final String TYPE_BUY = "BUY";
	public static final String TYPE_SELL = "SELL";
	
	public static void main(String[] args) {
		MatchingEngine me = new MatchingEngine();
		Scanner scanner = new Scanner(System.in);
		
		// Parse input line by line and instantiate Order objects from the input info. Then, invoke processOrder() to process the orders
		while (scanner.hasNextLine()) {
			String newLine = scanner.nextLine();
			String[] cleanedInput = newLine.trim().split("\\s++");
			assert cleanedInput.length == 5: "Invalid input format";
			Order newOrder = new Order(cleanedInput[0], cleanedInput[1], cleanedInput[2], Integer.parseInt(cleanedInput[3]), Integer.parseInt(cleanedInput[4]));
			me.processOrder(newOrder);
		}
		scanner.close();
		
		System.out.println("");
		// Unfinished SELL orders
		me.getUnrankedSellOrders().forEach((k, v) -> System.out.println(v));
		// Unfinished BUY orders
		me.getUnrankedBuyOrders().forEach((k, v) -> System.out.println(v));
	}
}
