import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = Arrays.asList(
            new Flight(0, 0, 0, 1, 30, 50),      // City 0 to 1
            new Flight(1, 0, 0, 3, 80, 200),     // City 0 to 3
            
            new Flight(2, 1, 40, 2, 60, 20),     // City 1 to 2
            new Flight(3, 1, 50, 2, 100, 120),   // City 1 to 2
            
            new Flight(4, 2, 120, 4, 200, 100),  // City 2 to 4
            
            new Flight(5, 3, 100, 4, 150, 500),  // City 3 to 4
            new Flight(6, 3, 100, 4, 250, 300)   // City 3 to 4
        );
        
        Planner flightPlanner = new Planner(flights);
        
        // The three tasks
        List<Flight> route1 = flightPlanner.leastFlightsEarliestRoute(0, 4, 0, 300);
        List<Flight> route2 = flightPlanner.cheapestRoute(0, 4, 0, 300);
        List<Flight> route3 = flightPlanner.leastFlightsCheapestRoute(0, 4, 0, 300);
        
        // Expected outputs
        List<Flight> expectedRoute1 = Arrays.asList(flights.get(1), flights.get(5));    // 0->3->4, 2 flights, arrives at t=150
        List<Flight> expectedRoute2 = Arrays.asList(flights.get(0), flights.get(3), flights.get(4)); // 0->1->2->4, 270 fare
        List<Flight> expectedRoute3 = Arrays.asList(flights.get(1), flights.get(6));    // 0->3->4, 2 flights, 500 fare
        
        // Note that for this given example there is a unique solution, but it may
        // not be true in general
        if (route1 != null && listsEqual(route1, expectedRoute1)) {
            System.out.println("Task 1 PASSED");
        } else {
            System.out.println("Task 1 FAILED");
            if (route1 != null) {
                System.out.println("Got: " + route1);
                System.out.println("Expected: " + expectedRoute1);
            } else {
                System.out.println("Got: null");
            }
        }
        
        if (route2 != null && listsEqual(route2, expectedRoute2)) {
            System.out.println("Task 2 PASSED");
        } else {
            System.out.println("Task 2 FAILED");
            if (route2 != null) {
                System.out.println("Got: " + route2);
                System.out.println("Expected: " + expectedRoute2);
            } else {
                System.out.println("Got: null");
            }
        }
        
        if (route3 != null && listsEqual(route3, expectedRoute3)) {
            System.out.println("Task 3 PASSED");
        } else {
            System.out.println("Task 3 FAILED");
            if (route3 != null) {
                System.out.println("Got: " + route3);
                System.out.println("Expected: " + expectedRoute3);
            } else {
                System.out.println("Got: null");
            }
        }
        
        // Print the routes for verification
        System.out.println("\nRoute Details:");
        if (route1 != null) {
            System.out.println("Route 1 (Least flights, earliest): " + routeToString(route1));
        }
        if (route2 != null) {
            System.out.println("Route 2 (Cheapest): " + routeToString(route2));
        }
        if (route3 != null) {
            System.out.println("Route 3 (Least flights, cheapest): " + routeToString(route3));
        }
    }
    
    private static boolean listsEqual(List<Flight> list1, List<Flight> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getFlightNo() != list2.get(i).getFlightNo()) {
                return false;
            }
        }
        
        return true;
    }
    
    private static String routeToString(List<Flight> route) {
        if (route == null || route.isEmpty()) {
            return "No route";
        }
        
        StringBuilder sb = new StringBuilder();
        int totalCost = 0;
        
        for (int i = 0; i < route.size(); i++) {
            Flight flight = route.get(i);
            totalCost += flight.getFare();
            
            if (i == 0) {
                sb.append("City ").append(flight.getStartCity());
            }
            sb.append(" --[Flight ").append(flight.getFlightNo())
              .append(", Fare: ").append(flight.getFare())
              .append("]--> City ").append(flight.getEndCity());
        }
        
        Flight lastFlight = route.get(route.size() - 1);
        sb.append(" (Total: ").append(route.size()).append(" flights, ")
          .append("Cost: ").append(totalCost).append(", ")
          .append("Arrival: ").append(lastFlight.getArrivalTime()).append(")");
        
        return sb.toString();
    }
}