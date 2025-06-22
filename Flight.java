public class Flight {
    private int flightNo;
    private int startCity;
    private int departureTime;
    private int endCity;
    private int arrivalTime;
    private int fare;
    
    /**
     * Class for the flights
     * 
     * @param flightNo Unique ID of each flight
     * @param startCity The city no. where the flight starts
     * @param departureTime Time at which the flight starts
     * @param endCity The city no where the flight ends
     * @param arrivalTime Time at which the flight ends
     * @param fare The cost of taking this flight
     */
    public Flight(int flightNo, int startCity, int departureTime, int endCity, int arrivalTime, int fare) {
        this.flightNo = flightNo;
        this.startCity = startCity;
        this.departureTime = departureTime;
        this.endCity = endCity;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
    }
    
    // Getters
    public int getFlightNo() {
        return flightNo;
    }
    
    public int getStartCity() {
        return startCity;
    }
    
    public int getDepartureTime() {
        return departureTime;
    }
    
    public int getEndCity() {
        return endCity;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public int getFare() {
        return fare;
    }
    
    // Setters
    public void setFlightNo(int flightNo) {
        this.flightNo = flightNo;
    }
    
    public void setStartCity(int startCity) {
        this.startCity = startCity;
    }
    
    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }
    
    public void setEndCity(int endCity) {
        this.endCity = endCity;
    }
    
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public void setFare(int fare) {
        this.fare = fare;
    }
    
    @Override
    public String toString() {
        return String.format("Flight %d: City %d -> City %d (Depart: %d, Arrive: %d, Fare: %d)", 
                           flightNo, startCity, endCity, departureTime, arrivalTime, fare);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Flight flight = (Flight) obj;
        return flightNo == flight.flightNo;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(flightNo);
    }
}

/*
If there are n flights, and m cities:

1. Flight No. will be an integer in {0, 1, ... n-1}
2. Cities will be denoted by an integer in {0, 1, .... m-1}
3. Time is denoted by a non negative integer - we model time as going from t=0 to t=inf
*/