package matching_engine;
import java.time.LocalDateTime;

public class Order implements Comparable<Order>{
	String orderID;
	String orderType;
	String orderInstrument;
	int orderQuantity;
	int orderPrice;
	LocalDateTime orderTimeStamp;
	
	// Initialize a BUY/SELL order
	public Order(String id, String type, String instrument, int quantity, int price) {
		orderID = id;
		orderType = type;
		orderInstrument = instrument;
		orderQuantity = quantity;
		orderPrice = price;
		orderTimeStamp = LocalDateTime.now();
	}
	
	// Getters
	public String getOrderID() {return orderID;}
	public String getOrderType() {return orderType;}
	public String getOrderInstrument() {return orderInstrument;}
	public int getQuantity() {return orderQuantity;}
	public int getPrice() {return orderPrice;}
	public LocalDateTime getTimeStamp() {return orderTimeStamp;}
	
	// Setters
	public void setQuantity(int quantity) {orderQuantity = quantity;}
	
	// Comparator (Compare according to price. If price is the same, time stamp is compared)
	public int compareTo(Order other) {
		// If prices are equal, earlier order is prioritized
		if (this.getPrice() == other.getPrice()) {
			return this.getTimeStamp().compareTo(other.getTimeStamp());
		}
		else {
			// For BUY orders, higher price is prioritized
			if (this.getOrderType().equals(Main.TYPE_BUY)) {
				return other.getPrice() - this.getPrice();
			}
			// For SELL orders, lower price is prioritized
			else {
				return this.getPrice() - other.getPrice();
			}
		}
	}
	
	public String toString() {
		return orderID + " " + orderType + " " + orderInstrument + " " + orderQuantity + " " + orderPrice;
	}
}
