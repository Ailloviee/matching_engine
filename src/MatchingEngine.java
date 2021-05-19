package matching_engine;

import java.util.*;

public class MatchingEngine {
	// A HashMap of PriorityQueues for ranked BUY orders (Key: Instrument, Value: PriorityQueue of orders)
	HashMap<String, PriorityQueue<Order>> rankedBuyOrders;
	// A HashMap of PriorityQueues for ranked SELL orders (Key: Instrument, Value: PriorityQueue of orders)
	HashMap<String, PriorityQueue<Order>> rankedSellOrders;
	// A LinkedHashMap for unranked BUY orders (Key: OrderID, Value: Order)
	LinkedHashMap<String, Order> unrankedBuyOrders;
	// A LinkedHashMap for unranked SELL orders (Key: OrderID, Value: Order)
	LinkedHashMap<String, Order> unrankedSellOrders;
	
	public MatchingEngine() {
		rankedBuyOrders = new HashMap<String, PriorityQueue<Order>>();
		rankedSellOrders = new HashMap<String, PriorityQueue<Order>>();
		unrankedBuyOrders = new LinkedHashMap<String, Order>();
		unrankedSellOrders = new LinkedHashMap<String, Order>();
	}
	
	// Getters
	public LinkedHashMap<String, Order> getUnrankedBuyOrders() {return unrankedBuyOrders;}
	public LinkedHashMap<String, Order> getUnrankedSellOrders() {return unrankedSellOrders;}
	
	// Add an order to its unranked and ranked list
	private void addOrder(Order order) {
		String orderInstrument = order.getOrderInstrument();
		// For BUY order, add to BUY lists
		if (order.getOrderType().equals(Main.TYPE_BUY)) {
			unrankedBuyOrders.put(order.getOrderID(), order);
			PriorityQueue<Order> priorityQueue = null;
			if (rankedBuyOrders.containsKey(orderInstrument)) {
				priorityQueue = rankedBuyOrders.get(orderInstrument);
			}
			else {
				priorityQueue = new PriorityQueue<Order>();
				rankedBuyOrders.put(orderInstrument, priorityQueue);
			}
			priorityQueue.add(order);
		}
		// For SELL order, add to SELL lists
		else {
			unrankedSellOrders.put(order.getOrderID(), order);
			PriorityQueue<Order> priorityQueue = null;
			if (rankedSellOrders.containsKey(orderInstrument)) {
				priorityQueue = rankedSellOrders.get(orderInstrument);
			}
			else {
				priorityQueue = new PriorityQueue<Order>();
				rankedSellOrders.put(orderInstrument, priorityQueue);
			}
			priorityQueue.add(order);
		}
	}
	
	// Remove an order from its unranked and ranked list 
	private void removeOrder(Order order) {
		if (order.getOrderType().equals(Main.TYPE_BUY)) {
			rankedBuyOrders.get(order.orderInstrument).remove(order);
			unrankedBuyOrders.remove(order.getOrderID());
		}
		else {
			rankedSellOrders.get(order.orderInstrument).remove(order);
			unrankedSellOrders.remove(order.getOrderID());
		}
	}
	
	private void matchOrder(Order order) {
		String orderInstrument = order.getOrderInstrument();
		PriorityQueue<Order> priorityQueue = null;
		// If it is a BUY order, try to match SELL orders with it by loading the ranked orders
		if (order.getOrderType().equals(Main.TYPE_BUY)) {
			if (rankedSellOrders.containsKey(orderInstrument)) {
				priorityQueue = rankedSellOrders.get(orderInstrument);
			}
		}
		// If it is a SELL order, try to match BUY orders with it by loading the ranked orders
		else {
			if (rankedBuyOrders.containsKey(orderInstrument)) {
				priorityQueue = rankedBuyOrders.get(orderInstrument);
			}
		}
		// If no such contra orders exist with the same instrument, just return
		if (priorityQueue == null) {
			return;
		}
		
		// Start matching orders
		int orderQuantity = order.getQuantity();
		// While the order is not fully filled and there are still available contra orders, compare their prices and match accordingly
		while (orderQuantity > 0 && !priorityQueue.isEmpty()) {
			Order contraOrder = priorityQueue.peek();
			// If the prices match, produce matched result and continue
			// For BUY orders, match SELL orders equal or below its price
			// For SELL orders, match BUY orders equal or above its price
			if ((order.getOrderType().equals(Main.TYPE_BUY) && contraOrder.getPrice() <= order.getPrice())
					|| (order.getOrderType().equals(Main.TYPE_SELL) && contraOrder.getPrice() >= order.getPrice())) {
				int contraOrderQuantity = contraOrder.getQuantity();
				int filledQuantity = 0;
				if (contraOrderQuantity > orderQuantity) {
					contraOrderQuantity -= orderQuantity;
					filledQuantity = orderQuantity;
					orderQuantity = 0;
					// Update the quantity of the contra order
					contraOrder.setQuantity(contraOrderQuantity);
				}
				else {
					orderQuantity -= contraOrderQuantity;
					filledQuantity = contraOrderQuantity;
					// Contra order fully filled, remove it from its ranked and unranked list
					removeOrder(contraOrder);
				}
				//Output the matched TRADE
				System.out.println("TRADE " + orderInstrument + " " + order.getOrderID() + " " + contraOrder.getOrderID() + " " + filledQuantity + " " + contraOrder.getPrice());
			}
			// If prices don't match, stop the matching process
			else {
				break;
			}
		}
		// If the order is fully filled, remove it from its ranked and unranked list
		if (orderQuantity == 0) {
			removeOrder(order);
		}
		// If the order is not fully filled, update its quantity with what's remained
		else {
			order.setQuantity(orderQuantity);
		}
	}
	
	// Process the order by adding it to the engine and match if possible
	public void processOrder(Order order) {
		addOrder(order);
		matchOrder(order);
	}
}
