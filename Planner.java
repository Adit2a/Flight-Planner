import java.util.*;

public class Planner {
    private List<Flight> flights;
    private Map<Integer, List<Flight>> flightsByStartCity;
    
    /**
     * The Planner
     * 
     * @param flights A list of information of all the flights (objects of class Flight)
     */
    public Planner(List<Flight> flights) {
        this.flights = flights;
        this.flightsByStartCity = new HashMap<>();
        
        // Build adjacency list for efficient lookup
        for (Flight flight : flights) {
            flightsByStartCity.computeIfAbsent(flight.getStartCity(), k -> new ArrayList<>()).add(flight);
        }
    }
    
    /**
     * Return List<Flight>: A route from start_city to end_city, which departs after t1 (>= t1) and
     * arrives before t2 (<=) satisfying: 
     * The route has the least number of flights, and within routes with same number of flights, 
     * arrives the earliest
     */
    public List<Flight> leastFlightsEarliestRoute(int startCity, int endCity, int t1, int t2) {
        // BFS to find shortest path with earliest arrival time as tiebreaker
        Queue<RouteState> queue = new LinkedList<>();
        Map<Integer, Integer> bestArrivalTime = new HashMap<>();
        
        queue.offer(new RouteState(startCity, t1, new ArrayList<>()));
        bestArrivalTime.put(startCity, t1);
        
        List<Flight> bestRoute = null;
        int minFlights = Integer.MAX_VALUE;
        int earliestArrival = Integer.MAX_VALUE;
        
        while (!queue.isEmpty()) {
            RouteState current = queue.poll();
            
            if (current.city == endCity && current.route.size() <= minFlights) {
                if (current.route.size() < minFlights || 
                    (current.route.size() == minFlights && current.time < earliestArrival)) {
                    minFlights = current.route.size();
                    earliestArrival = current.time;
                    bestRoute = new ArrayList<>(current.route);
                }
                continue;
            }
            
            // Pruning: if we already found a better solution, skip
            if (current.route.size() >= minFlights) continue;
            
            List<Flight> availableFlights = flightsByStartCity.get(current.city);
            if (availableFlights != null) {
                for (Flight flight : availableFlights) {
                    if (flight.getDepartureTime() >= current.time && flight.getArrivalTime() <= t2) {
                        // Check if this path to the destination city is better
                        Integer prevBest = bestArrivalTime.get(flight.getEndCity());
                        if (prevBest == null || flight.getArrivalTime() <= prevBest + 1) {
                            bestArrivalTime.put(flight.getEndCity(), flight.getArrivalTime());
                            
                            List<Flight> newRoute = new ArrayList<>(current.route);
                            newRoute.add(flight);
                            queue.offer(new RouteState(flight.getEndCity(), flight.getArrivalTime(), newRoute));
                        }
                    }
                }
            }
        }
        
        return bestRoute;
    }
    
    /**
     * Return List<Flight>: A route from start_city to end_city, which departs after t1 (>= t1) and
     * arrives before t2 (<=) satisfying: 
     * The route is a cheapest route
     */
    public List<Flight> cheapestRoute(int startCity, int endCity, int t1, int t2) {
        // Modified Dijkstra's algorithm for cheapest path
        PriorityQueue<RouteState> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.cost, b.cost));
        Map<Integer, Integer> minCostToCity = new HashMap<>();
        
        pq.offer(new RouteState(startCity, t1, new ArrayList<>(), 0));
        minCostToCity.put(startCity, 0);
        
        while (!pq.isEmpty()) {
            RouteState current = pq.poll();
            
            if (current.city == endCity) {
                return current.route;
            }
            
            // Skip if we've found a better path to this city
            if (minCostToCity.containsKey(current.city) && minCostToCity.get(current.city) < current.cost) {
                continue;
            }
            
            List<Flight> availableFlights = flightsByStartCity.get(current.city);
            if (availableFlights != null) {
                for (Flight flight : availableFlights) {
                    if (flight.getDepartureTime() >= current.time && flight.getArrivalTime() <= t2) {
                        int newCost = current.cost + flight.getFare();
                        
                        if (!minCostToCity.containsKey(flight.getEndCity()) || 
                            newCost < minCostToCity.get(flight.getEndCity())) {
                            minCostToCity.put(flight.getEndCity(), newCost);
                            
                            List<Flight> newRoute = new ArrayList<>(current.route);
                            newRoute.add(flight);
                            pq.offer(new RouteState(flight.getEndCity(), flight.getArrivalTime(), newRoute, newCost));
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Return List<Flight>: A route from start_city to end_city, which departs after t1 (>= t1) and
     * arrives before t2 (<=) satisfying: 
     * The route has the least number of flights, and within routes with same number of flights, 
     * is the cheapest
     */
    public List<Flight> leastFlightsCheapestRoute(int startCity, int endCity, int t1, int t2) {
        // BFS to find shortest path with cheapest cost as tiebreaker
        Queue<RouteState> queue = new LinkedList<>();
        Map<Integer, RouteInfo> bestRouteToCity = new HashMap<>();
        
        queue.offer(new RouteState(startCity, t1, new ArrayList<>(), 0));
        bestRouteToCity.put(startCity, new RouteInfo(0, 0));
        
        List<Flight> bestRoute = null;
        int minFlights = Integer.MAX_VALUE;
        int cheapestCost = Integer.MAX_VALUE;
        
        while (!queue.isEmpty()) {
            RouteState current = queue.poll();
            
            if (current.city == endCity && current.route.size() <= minFlights) {
                if (current.route.size() < minFlights || 
                    (current.route.size() == minFlights && current.cost < cheapestCost)) {
                    minFlights = current.route.size();
                    cheapestCost = current.cost;
                    bestRoute = new ArrayList<>(current.route);
                }
                continue;
            }
            
            // Pruning: if we already found a better solution, skip
            if (current.route.size() >= minFlights) continue;
            
            List<Flight> availableFlights = flightsByStartCity.get(current.city);
            if (availableFlights != null) {
                for (Flight flight : availableFlights) {
                    if (flight.getDepartureTime() >= current.time && flight.getArrivalTime() <= t2) {
                        int newFlights = current.route.size() + 1;
                        int newCost = current.cost + flight.getFare();
                        
                        RouteInfo prevBest = bestRouteToCity.get(flight.getEndCity());
                        boolean shouldExplore = false;
                        
                        if (prevBest == null) {
                            shouldExplore = true;
                        } else if (newFlights < prevBest.flights) {
                            shouldExplore = true;
                        } else if (newFlights == prevBest.flights && newCost <= prevBest.cost) {
                            shouldExplore = true;
                        }
                        
                        if (shouldExplore) {
                            bestRouteToCity.put(flight.getEndCity(), new RouteInfo(newFlights, newCost));
                            
                            List<Flight> newRoute = new ArrayList<>(current.route);
                            newRoute.add(flight);
                            queue.offer(new RouteState(flight.getEndCity(), flight.getArrivalTime(), newRoute, newCost));
                        }
                    }
                }
            }
        }
        
        return bestRoute;
    }
    
    // Helper classes
    private static class RouteState {
        int city;
        int time;
        List<Flight> route;
        int cost;
        
        RouteState(int city, int time, List<Flight> route) {
            this.city = city;
            this.time = time;
            this.route = route;
            this.cost = 0;
        }
        
        RouteState(int city, int time, List<Flight> route, int cost) {
            this.city = city;
            this.time = time;
            this.route = route;
            this.cost = cost;
        }
    }
    
    private static class RouteInfo {
        int flights;
        int cost;
        
        RouteInfo(int flights, int cost) {
            this.flights = flights;
            this.cost = cost;
        }
    }
}