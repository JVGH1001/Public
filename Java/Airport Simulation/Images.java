import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
/**
 * Attaches images to GUI
 * 
 * @author Jeffrey VanMeter 
 * @version (4/25/2014)
 */
public class Images extends JComponent
{
    static String PLANE_PATH = "resources/Plane.png";
    static String PLANE180_PATH = "resources/Plane180.png";
    static String AIRPORT_PATH = "resources/Airport.png";

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private BufferedImage plane;
    private BufferedImage plane180;
    private BufferedImage airport;
    private ArrayList<Flight> flights;

    /**
     * Constructor for objects of class Images
     */
    public Images()
    {
        flights = new ArrayList<Flight>();
        loadImages();
        setVisible(true);
    }

    /**
     * Loads Images onto screen
     */
    public void loadImages()
    {
        try{
            plane = ImageIO.read(new File(PLANE_PATH));
            plane180 = ImageIO.read(new File(PLANE180_PATH));
            airport = ImageIO.read(new File(AIRPORT_PATH));
        }catch(Exception e){
            System.out.println("ERROR");
        }
    }

    public void paint(Graphics g)
    {
        g.drawImage(airport, 0, 0, 932, 732, null); //932,763
        drawFlights(g);
        g.dispose();
    }

    public void drawFlights(Graphics g)
    {
        Iterator<Flight> itr = flights.iterator();
        while(itr.hasNext()){
            Flight f = itr.next();
            if(f.getRunwayNum() == 1 && !f.getReady()){
                g.drawImage(plane180, f.getxPos(), 373, 50, 50, null);}
            else if(f.getRunwayNum() == 2 && !f.getReady()){
                g.drawImage(plane, f.getxPos(), 471, 50, 50, null);}
            if(f.getRunwayNum() == 1 && f.getReady()){
                g.drawImage(plane, f.getxPos(), 373, 50, 50, null);}
            else if(f.getRunwayNum() == 2 && f.getReady()){
                g.drawImage(plane180, f.getxPos(), 471, 50, 50, null);}
        }
    }

    public void addFlight(Flight f)
    {
        flights.add(f);
    }

    public void removeFlight(Flight f)
    {
        flights.remove(f);
    }
}