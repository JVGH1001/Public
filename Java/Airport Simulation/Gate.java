/**
 * Gate class that allows Flights to be at a gate.
 * @author Jeffrey VanMeter
 */
public class Gate 
{
    private boolean gateAvailability; // if gate is available or not.
    private int gateNumber;//Number Assigned to Gate.
    private Flight flight;//Instance of Flight.

    /**
     * Constructor for objects of class Gate.
     */
    public Gate(int gateNumber)
    {
        this.gateNumber = gateNumber;
        gateAvailability = true;
    }
    
    /**
     * Assigns a Flight to a gate and makes gate unavailable.
     */
    public void setFlight(Flight flight)
    {   
        gateAvailability = false;
        this.flight = flight;
    }
    
    /**
     * Removes a Flight from the gate and makes gate available.
     */
    public void unsetFlight(){
        gateAvailability = true;
        this.flight = null;
    }
    
    /**
     * Accessor method to get Flight from gate.
     */
    public Flight getFlight()
    {
        return flight;
    }
    
    /**
     * Accessor method to return gate number.
     */
    public int getGateNumber()
    {
        return gateNumber;
    }

    /**
     * Accessor method to return gate availability.
     */
    public boolean getGateAvailability()
    {
        return gateAvailability;
    }
    
    /**
     * Mutator method to assign gateAvailabiliy to true or false.
     */
    public void setGateAvailability(boolean gateAvailability)
    {
      this.gateAvailability = gateAvailability;  
    }
}
