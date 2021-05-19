**Java Version**

java 15.0.2 2021-01-19\
Java(TM) SE Runtime Environment (build 15.0.2+7-27)\
Java HotSpot(TM) 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)

**To build and run the application**

1. In current directory(matching_engine), run `javac -d ./build ./src/*.java`.
2. Go to build directory (run `cd ./build`).
3. Then run `jar cfm matching_engine.jar manifest.txt matching_engine/*.class`.
4. Then run `cat ../input/Input.txt | java -jar ./matching_engine.jar`.

**Problem Assumptions**

1. The input is taken from stdin. Orders are put in the file `./input/Input.txt`.
2. Input is properly formatted (Unique order ID, BUY/SELL order type, positive order quantity and price).
3. Orders are treated as LIMIT orders (Buy orders can be filled AT or BELOW its price (lower price preferred); Sell orders can be filled AT or ABOVE its price (higher price preferred).

**Description of how I approached the problem**

Using OOP approach, I divided the project into three classes:

1. The `Order` class describes every BUY/SELL order with its properties(`ID`, `Type`, `Instrument`, etc.) as a comparable object that can be compared based on its price and timestamp as suggested in the requirement.
2. The `MatchingEngine` class sorts the `Order` objects, BUY and SELL separately, in a ranked way in `HashMap<String, PriorityQueue<Order>>`, where the key `String` is the instrument and the value `PriorityQueue` is the sorted queue of `Order` objects based on their prices and timestamps. Also, the class stores the `Order` objects, regardless of their instruments, in an unranked way in `LinkedHashMap<String, Order>`, where the key `String` is the order ID and the value `Order` is the corresponding order. Then, upon receiving new orders, the class can, through `addOrder()` method, add the orders to their corresponding BUY/SELL ranked and unranked lists. After that, the class can, through `matchOrder()` method, go through the ranked list of the contra orders with the same instrument to check if any matching contra orders exist. For every matching contra order found, the traded quantity is calculated and printed out. The partially filled orders will be updated with their remaining quantity.
3. The `Main` class parses the input from `stdin`, instantiates `Order` objects from the input, and invokes the `processOrder()` from the `MatchingEngine` class to add and match the new orders one by one. Then, after all inputs are consumed, it gets the unranked list from `MatchingEngine` to output the remaining unfilled orders chronologically.

**Time spent on the project**

~3 to 3.5 hours.
