import java.util.*;
/**
 * Creates the Thread "Tickloop".
 * Calls operations to collect actors to act.
 * 
 * @author Jeffrey VanMeter
 * @version 2012.04.08
 */
public class Simulator
{
    //Used to pause Simultion.
    public boolean paused = false;
    //Thread that runs allowing pausing and playing.
    Thread tickloop;
    /**
     * Start new simulation
     */
    public Simulator() throws InterruptedException
    {
        //operations = new Operations(this, tickloop);
        tickloop = new TickLoop(this);
        tickloop.start();
    }

}

