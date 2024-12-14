
/**
 * Is a Thread that runs the simulation by iterating through actors
 * allowing them to "Act".
 * 
 * @author Jeffrey VanMeter
 * @version (a version number or a date)
 */
public class TickLoop extends Thread
{
    //Speed at which each simulation tick will run in milliseconds.
    private int tickSpeed = 1000;
    //Simulation time.
    public int time;
    //Used to pause Simulation.
    public boolean paused = false; 
    //Used to play simulation.
    public boolean started = false;
    //Instance of Operations.
    Operations operations;
    //Instance of Simulator.
    Simulator sim;
    /**
     * Constructor for the TickLoop, uses an instance of Simulator.
     */
    public TickLoop(Simulator sim)
    {
        operations = new Operations(this);
        this.sim = sim;
    }
    /**
     * The main method that runs the Simulation by iterating Actors.
     */
    public void run()
    {
        started = true;
        while(true){
            tick();
            synchronized(this){
                if(paused == true){
                    try
                    {
                        wait();
                    } 
                    catch (InterruptedException e)
                    {
                        //ignore the exception
                    }
                }
            }
            hold(tickSpeed);

            for(int i=0; i < operations.getActors().size(); i++){
                operations.getActors().get(i).act(time);
            }
        }
    }

    /**
     * Increases time by adding 1 everytime method is called.
     */
    private void tick(){
        time++;
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to cause a small delay.
     * @param milliseconds The number of milliseconds to wait.
     */
    private void hold(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e)
        {
            // ignore the exception
        }
    }

    /**
     * Plays the simulation if paused.
     */
    public void play(){
        paused = false;

        if(started){
            synchronized(this){
                notify();
            }
        }else{
            start();
        }

    }

    /**
     * Sets variable paused to true to pause simulation.
     */
    public void pause()
    {
        paused = true;
    }

    /**
     * Returns the variable paused.
     */
    public boolean paused()
    {
        return paused;
    }

    /**
     * Takes an integer in and sets that tickSpeed to new integer.
     * Used for changing the speed of the simulation.
     */
    public void setSpeed(int sp)
    {
        tickSpeed = Math.abs(sp);
    }

    /**
     * Returns the tickSpeed.
     */
    public int getSpeed(){
        return tickSpeed;
    }
}
