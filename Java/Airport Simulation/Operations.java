import java.util.*;
/**
 * Main creation class. Creates almost all needed objects.
 * @author Jeffrey VanMeter
 */
public class Operations implements Actor
{
    Random rgen;// = new random();
    private  ArrayList<Actor> actors;//A list of Actors.
    private  ArrayList<Gate> gates;//A list of Gates.
    private ArrayList<Runway> runways;//A List of Runways.
    private FlightController fc;//Instance of FlightController.
    private  Stats st;//Instance of Stats.
    private GUI gui;//Instance of GUI.
    private City ct;//Instance of City.
    private Flight flight;//Instance of Flight.
    private TickLoop tickLoop;//Instance of TickLoop.
    private ArrayList<Flight> inbound;  //These are the flights that are on their way to our airport
    private ArrayList<Flight> grounded;  //These are the flights that are landed at our airport

    private static double IN_BOUND_FLIGHT_PROBABILITY = .1;//(Default.2) Probability of making in bound Flights.
    private static final double GROUNDED_FLIGHT_PROBABILITY = .6;//(Default.6) Probability of making grounded Flights for beginning of Simulation.
    private static final int AMOUNT_INITIAL_GATES = 100;//Number of Gates.
    private static int AMOUNT_INITAL_RUNWAYS = 2;//Number of Runways.
    private int delay = 10; //For Delay Times

    /**
     * Constructor for Operations intializes almost all objects, List,
     * and intial conditions for Simulation.
     */
    public Operations(TickLoop tickLoop) 
    {
        this.tickLoop = tickLoop;
        rgen = new Random();
        ct = new City();
        fc = new FlightController(this,tickLoop);
        gui = new GUI(fc, tickLoop);
        st = new Stats(gui);
        actors = new ArrayList<Actor>();
        gates = new ArrayList<Gate>();
        runways = new ArrayList<Runway>();
        inbound = new ArrayList<Flight>();
        grounded = new ArrayList<Flight>();
        actors.add(gui);
        actors.add(this);
        createGates(AMOUNT_INITIAL_GATES);
        createRunways(AMOUNT_INITAL_RUNWAYS);
        groundedFlights();
    }

    /**
     * Adds Gate to ArrayList gates.
     */
    private void addGate(Gate g){
        gates.add(g);
    }

    /**
     * Returns ArrayList actors.
     */
    public ArrayList<Actor> getActors()
    {
        return actors;
    }

    /**
     *method to get the first open gate we can find.
     */
    public Gate getOpenGate(){
        for(Gate g: gates){
            if(g.getGateAvailability()){ return g; }
        }
        return null;
    }

    /**
     * Set's Inbound Flight probability
     */
    public void inBoundProbability(double inbound)
    {
        IN_BOUND_FLIGHT_PROBABILITY = inbound;
    }

    /**
     * Returns total number of gates
     */
    public int getTotalGates()
    {
        return gates.size();
    }

    /**
     * Method to find the first open Runway.
     */
    public Runway getOpenRunway()
    {
        for(Runway r: runways)
        {
            if(r.getRunwayAvailability()){return r;}
        }
        return null;
    }

    /**
     * Creates a specific number of Gates based on "num".
     */
    public void inBoundFlights(int num){
        for(int i=0;i<num;i++){
            makeInBoundFlight();
        }
    }

    /**
     * Creates a specific number of Flights already at Gates based on a
     * conditional statement.
     */
    private void groundedFlights(){
        for(int i=0; i < gates.size()/2; i++){
            if(rgen.nextDouble() <= (GROUNDED_FLIGHT_PROBABILITY)){
                makeGroundedFlight();
                st.calcPercGateUsed(getTotalGates(), true);
            }
        }
    }

    /**
     * Creates a specific number of Gates based on "num".
     */
    public void createGates(int num){
        for(int i=1;i<=num;i++){
            Gate g = new Gate(i);
            gates.add(g);
        }
    }

    /**
     * Creates a specific number of Runways based on "num".
     */
    public void createRunways(int num)
    {
        for(int i=1; i<=num;i++) {
            Runway r = new Runway(i);
            runways.add(r);
        }
    }

    /**
     * Creates a Flight that is already "in the air" and adds it to actors
     * ArrayList and inbound ArrayList.
     */
    private void makeInBoundFlight(){
        Flight f = new Flight(this,ct, gui, st, genFlightNum(), rgen.nextInt(15) + 1, 5, rgen.nextInt(550) + 11, 5, delay, false);
        actors.add(f);
        inbound.add(f);
        st.calcTotalInc();
    }

    /**
     * Creates a Flight that is already "on the ground" and add it to actors
     * ArrayList and grounded ArrayList.
     */
    private void makeGroundedFlight(){
        Flight f = new Flight(this,ct, gui, st, genFlightNum(), rgen.nextInt(15) + 1, 5, rgen.nextInt(560) + 1, 5, delay, true);
        actors.add(f);
        grounded.add(f);
    }

    /**
     * Generates flight number and makes sure no flights have the same.
     */
    public int genFlightNum()
    {
        boolean approved = false;
        int num = 0;
        while(!approved)
        {
            num = rgen.nextInt(9999) + 1; //Randomly generate a number between 1-9999
            boolean isDouble = false; //If number is doubled
            for(Flight f : inbound)
            {
                if(f.getNum() == num)
                {
                    isDouble = true; //Number is doubled
                    break;
                }
            }
            for(Flight f : grounded)
            {
                if(f.getNum() == num)
                {
                    isDouble = true; //Number is doubled
                    break;
                }
            }
            if(!isDouble)
            {
                approved = true;
            }    
        }
        return num;
    }

    /**
     *  This switches the flight from the arraylist of incoming flights, to the 
     *  arraylist of grounded flights.
     */
    public void switchList(Flight f) 
    {
        inbound.remove(f);
        grounded.add(f);
    }

    /**
     *  This removes fligths from the grounded list once they takeoff
     */
    public void removeFlight(Flight f)
    {
        actors.remove(f);
        grounded.remove(f);
        st.calcTotalOut();
    }

    /**
     * Creates a specific number of Gates based on "num".
     * Consider modifiyng original creator to reduce duplication!!!
     */
    public void addGates(int num)
    {
        for(int i=1; i<=num;i++) {
            Gate r = new Gate(gates.size()+1);
            gates.add(r);
            gui.statusUpdate("Gate " + r.getGateNumber() + " has been created");
        }
    }

    /**
     * Removes Gates from simulation
     */
    public void removeGates(int num)
    {
        for(int i=0;num > i;i++) {
            Gate r = gates.get(gates.size()-1);
            gates.remove(r);
            gui.statusUpdate("Gate " + r.getGateNumber() + " has been removed");
        }
    }

    /**
     * Removes an inbound Flight from simulation
     */
    public void removeFlights(int num)
    {
        for (int i=0; num > i;i++) {
            if(inbound.size() != 0) {
                flight = inbound.get(inbound.size()-1);
                inbound.remove(flight);
                actors.remove(flight);
                st.removeFlightInc();
                gui.statusUpdate("Flight " + flight.getNum() +" has been deleted");
            }
            else 
                gui.statusUpdate("No inbound flights to be cancelled");
        }
    }

    /**
     * Creates a specific number of Runways based on "num".
     * Consider modifiyng original creator to reduce duplication!!!
     */
    public void addRunways(int num)
    {
        for(int i=1; i<=num;i++) {
            Runway r = new Runway(runways.size()+1);
            runways.add(r);
            gui.statusUpdate("Runway " + r.getRunwayNumber() + " has been created");
        }
    }

    /**
     * Removes Runways from simulation
     */
    public void removeRunways(int num)
    {
        for(int i=0;num > i;i++) {
            Runway r = runways.get(runways.size()-1);
            runways.remove(r);
            gui.statusUpdate("Runway " + r.getRunwayNumber() + " has been removed");
        }
    }

    /**
     * Modifies int Delay.
     */
    public void setDelay(int delay)
    {
        this.delay = delay;
        for(Flight f: grounded)
            f.setDelay(delay);
        for(Flight f: inbound)
            f.setDelay(delay);
    }

    /**
     * Allows a random number of incoming Flights to be created every tick.
     */
    public void act(int tick)
    {
        if (rgen.nextDouble() <= IN_BOUND_FLIGHT_PROBABILITY){
            inBoundFlights(1);
        }
    }
}
