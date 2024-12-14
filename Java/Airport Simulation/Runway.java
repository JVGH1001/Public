import java.util.*;
/**
 * Shuttles an arriving flight to a gate, and from a gate to takeoff.
 * Might replace some methods from gate class.
 * @author Jeffrey VanMeter 
 * @version (.2)
 */
public class Runway
{
    private boolean runwayAvailability;//If runway is open or not.
    private int runwayNumber;//number of runways
    private Flight flight;//importing flights
    private Gate gate;//importing gates
    private ArrayList<Flight> flights;//For all possible flights.
    private int MAX_CAPACITY = 1;//Max number of Flights to a runway.

    /**
     * Constructor for objects of class Runway
     */
    public Runway(int runwayNumber)
    {
        this.runwayNumber = runwayNumber;
        runwayAvailability = true;
        flights = new ArrayList<Flight>();
    }
    
    /**
     * Sets a flight to the runway and makes runway unavailable.
     */
    public void setFlight(Flight flight)
    {
        runwayAvailability = false;
        this.flight = flight;
    }
    
    /**
     * Removes flight from runway and makes runway available.
     */
    public void unsetFlight()
    {
        runwayAvailability = true;
        this.flight = null;
    }
    
    /**
     * Removes flight from ArrayList flights. May not be used recheck.
     */
    public void remove(Flight flight)
    {
        flights.remove(flight);
    }
    
    /**
     * States if runways is available for flights using MAX_CAPACITY.
     */
    public boolean isClear()
    {
        return (flights.size() < MAX_CAPACITY);
    }
    
    /**
     * Returns the Runway number.
     */
    public int getRunwayNumber()
    {
        return runwayNumber;
    }
    
    /**
     * Accessor method to return gate availability.
     */
    public boolean getRunwayAvailability()
    {
        return runwayAvailability;
    }
    
    /**
     * Gets flight.
     */
    public Flight getFlight()
    {
        return flight;
    }
}