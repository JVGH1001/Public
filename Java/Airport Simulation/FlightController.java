
/**
 * Allows us to interact with GUI and change status of Simulation while
 * running.
 * 
 * @author Jeffrey VanMeter
 * @version (a version number or a date)
 */
public class FlightController
{
    private Operations operations;
    private TickLoop tickLoop;

    /**
     * Constructor for objects of class FlightController
     */
    public FlightController(Operations operations, TickLoop tickLoop)
    {
        this.operations = operations;
        this.tickLoop = tickLoop;
    }

    /**
     * Reset function: Resets the timer
     */
    public void reset()
    {
        System.out.print("\u001b");
        System.out.flush();
        System.out.println("Reset");
    }

    /**
     * Quit function: quit the application.
     */
    public void quit()
    {
        System.exit(0);
    }

    /**
     * Calls the method to pause Simulation.
     */
    public void pause()
    {
        tickLoop.pause();
    }

    /**
     * Calls the method to resume the Simulation.
     */
    public void play()
    {
        tickLoop.play();
    }

    /**
     * Calls the FlightProbability mutator from Operations for GUI
     */
    public void changeFlightProbability(double inbound)
    {
        operations.inBoundProbability(inbound);
    }

    /**
     * Creates Gates via operations.
     */
    public void addGate(int num)
    {
        operations.addGates(num);
    }

    /**
     * Creates Flights via operations.
     */
    public void addFlight(int num)
    {
        operations.inBoundFlights(num);
    }

    /**
     * Creates Runways via operations.
     */
    public void addRunway(int num)
    {
        operations.addRunways(num);
    }

    /**
     * Removes Gates via operations.
     */
    public void removeGate(int num)
    {
        operations.removeGates(num);
    }

    /**
     * Removes Flights via operations.
     */
    public void removeFlight(int num)
    {
        operations.removeFlights(num);
    }

    /**
     * Removes Runways via operations.
     */
    public void removeRunway(int num)
    {
        operations.removeRunways(num);
    }

    /**
     * Modifies delay through Flight.
     */
    public void setDelay(int delay)
    {
        operations.setDelay(delay);
    }
}
