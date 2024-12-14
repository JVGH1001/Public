import java.util.*;
/**
 * Creates a City in which the flights are either going to or coming from.
 * @author Jeffrey VanMeter
 */
public class City
{
    Random rgen = new Random();//Random number for city selection.
    private String cities[];//Sets a list of cities.
    
    /**
     * Constructor for City sets up the list of City.
     */
    public City() 
    {
        this.cities = new String[] {"Chicago", "Houston", "Philadelphia", "New York"};
    }
    
    /**
     * Returns a random city name based on a random number.
     */
    public String genCity()
    {
        return cities[rgen.nextInt(cities.length)];
    }
}
