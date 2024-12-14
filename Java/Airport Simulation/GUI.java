import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.text.*;
/**
 * Displays the Simulation in a nice form.
 * 
 * @author Jeffrey VanMeter 
 * @version (4/8/2014)
 */
public class GUI extends JFrame implements Actor
{
    private int time;
    private int day;
    private FlightController fc;
    private ButtonGroup group;
    private JPanel north;
    private JPanel west;
    private JPanel south;
    private JTextArea textArea;
    private Images images;
    private JPanel center;
    private JLabel timeLabel;
    private JRadioButton radioButton;
    private JLabel statGatePercUsedLabel;
    private JLabel statAvgGateTimeLabel;
    private JLabel statAvgDelayLabel;
    private JLabel numIncFlightsLabel;
    private JLabel numOutFlightsLabel;
    private JLabel avgDailyOpLabel;
    private JLabel projAnnualOpLabel;
    private JLabel crashedLabel;
    private JLabel planesMissingLabel;
    private final static String newLine = "\n";
    /** Tick Speed Changer */
    private SettingPan span;
    private TickLoop tickLoop;

    /**
     * Creates the GUI and shows it on the screen
     */
    public GUI(FlightController fc, TickLoop tickLoop)
    {
        this.fc = fc;
        this.tickLoop = tickLoop;
        this.span = new SettingPan(tickLoop);
        makeFrame();
    }

    /**
     * Display the current state
     */
    public void act(int tick)
    {
        time = tick;
        prepareUpdate();
        getContentPane().repaint();
    }

    /**
     * Create the Swing frame and its content.
     */
    public void makeFrame()
    {
        setTitle("CCC Airport Simulator");
        makeMenuBar();

        /**Frame container and layout*/
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));

        /**North panel*/
        north = new JPanel();
        north.setLayout(new GridLayout(0,1));

        //Time label
        timeLabel = new JLabel("  Time: " + time + "  Day: " + day);
        north.add(timeLabel);

        //Tick Speed Pan
        north.add(span);

        contentPane.add(north, BorderLayout.NORTH);

        /**West panel*/
        west = new JPanel();
        //BoxLayout grid = new BoxLayout(west, BoxLayout.Y_AXIS);
        west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));

        //Status Pane with text area
        textArea = new JTextArea(15,42);
        JScrollPane statusPane = new JScrollPane(textArea);
        new SmartScroller(statusPane);
        textArea.setEditable(false);
        west.add(statusPane);

        //Percentage of Gates Used
        statGatePercUsedLabel = new JLabel("Percentage of Gates Used: ");
        west.add(statGatePercUsedLabel);

        //Average Gate Time Label
        statAvgGateTimeLabel = new JLabel("Average Gate Time: ?");
        west.add(statAvgGateTimeLabel);

        //Average Delay Label
        statAvgDelayLabel = new JLabel("Average Delay Time: 0");
        west.add(statAvgDelayLabel);

        //Num of Incoming Flights
        numIncFlightsLabel = new JLabel("Total Incoming Flights: 0");
        west.add(numIncFlightsLabel);

        //Num of Outbound Flights
        numOutFlightsLabel = new JLabel("Total Outbound Flights: 0");
        west.add(numOutFlightsLabel);

        //Daily Operations
        avgDailyOpLabel = new JLabel("Average Daily Operations: 0");
        west.add(avgDailyOpLabel);

        //Projected Yearly Operations
        projAnnualOpLabel = new JLabel("Projected Annual Operations: 0");
        west.add(projAnnualOpLabel);
        
        //Projected Yearly Operations
        crashedLabel = new JLabel("Planes Crashed and Burned: 0");
        west.add(crashedLabel);
        
        //Projected Yearly Operations
        planesMissingLabel = new JLabel();
        west.add(planesMissingLabel);

        contentPane.add(west, BorderLayout.WEST);

        /** Center Panel */
        //Image pane with graphics
        center = new JPanel();

        images = new Images();
        center.add(images);

        contentPane.add(images);

        /** South Panel*/
        south = new JPanel();
        south.setLayout(new GridLayout(1,0));

        group = new ButtonGroup();
        //Normat setting
        radioButton = new JRadioButton("Normal",true);
        radioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.changeFlightProbability(.1); fc.setDelay(10);}
            });

        group.add(radioButton);
        south.add(radioButton);
        //Increased Flights
        radioButton = new JRadioButton("Increase Flights");
        radioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.changeFlightProbability(.2); }
            });
        group.add(radioButton);
        south.add(radioButton);
        //Stormy Weather
        radioButton = new JRadioButton("Stormy Weather");
        radioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.setDelay(20); fc.changeFlightProbability(.05); }
            });
        group.add(radioButton);
        south.add(radioButton);

        contentPane.add(south, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        fullscreen();
    }

    public void fullscreen()
    {
        GraphicsDevice gd;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
    }

    /**
     * Create the main frame's menu bar.
     * @param frame   The frame that the menu bar should be added to.
     */
    public void makeMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        // create the File menu
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        JMenuItem playItem = new JMenuItem("Play");
        playItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.play(); }
            });
        fileMenu.add(playItem);

        JMenuItem pauseItem = new JMenuItem("Pause");
        pauseItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { tickLoop.pause(); }
            });
        fileMenu.add(pauseItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.quit(); }
            });
        fileMenu.add(quitItem);

        //Creates the menu for FlightController to act upon
        JMenu settingMenu = new JMenu("Settings");
        menubar.add(settingMenu);

        JMenuItem addGateItem = new JMenuItem("Add Gate");
        addGateItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.addGate(1); }
            });
        settingMenu.add(addGateItem);

        JMenuItem removeGateItem = new JMenuItem("Remove Gate");
        removeGateItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.removeGate(1); }
            });
        settingMenu.add(removeGateItem);

        JMenuItem addFlightItem = new JMenuItem("Add Flight");
        addFlightItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.addFlight(1); }
            });
        settingMenu.add(addFlightItem);

        JMenuItem removeFlightItem = new JMenuItem("Remove Flight");
        removeFlightItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.removeFlight(1); }
            });
        settingMenu.add(removeFlightItem);

        JMenuItem addRunwayItem = new JMenuItem("Add Runway");
        addRunwayItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.addRunway(1); }
            });
        settingMenu.add(addRunwayItem);

        JMenuItem removeRunwayItem = new JMenuItem("Remove Runway");
        removeRunwayItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { fc.removeRunway(1); }
            });
        settingMenu.add(removeRunwayItem);
    }

    /**
     * Prepare for a new round of updates (repaints) to the GUI
     */
    public void prepareUpdate()
    {
        timeUpdate();     
    }

    /**
     * Quit function: quit the application.
     */
    public void quit()
    {
        System.exit(0);
    }

    public void statusUpdate(String s)
    {
        textArea.append("[" + convertTime(time) + "] " + s + newLine);
    }

    public void timeUpdate()
    {
        String newTime = convertTime(time);
        timeLabel.setText("  Time: " + newTime + "    Day: " + day);
    }

    public void gatePercUsed(double percent)
    {
        statGatePercUsedLabel.setText("Gate Utilization: " + (int) percent + "%");
    }

    public void avgGateTimeUpdate(int avgGateTime)
    {
        statAvgGateTimeLabel.setText("Average Gate Time: " + avgGateTime);
    }

    public void avgDelayUpdate(double avgDelay)
    {
        statAvgDelayLabel.setText("Average Delay Time: " + Math.round(avgDelay));
    }

    public void avgDailyOpUpdate(int totalOperations)
    {
        if(day > 0){
            avgDailyOpLabel.setText("Average Daily Operations: " + (totalOperations / (day)));
            projAnnualOpUpdate(totalOperations / day);
        }
    }

    public void projAnnualOpUpdate(int projYearly)
    {
        projAnnualOpLabel.setText("Projected Yearly Operations: " + (projYearly * 365));
    }

    public void incFlights(int incFlights)
    {
        numIncFlightsLabel.setText("Total Flights In: " + incFlights);
    }

    public void outFlights(int outFlights)
    {
        numOutFlightsLabel.setText("Total Flights Out: " + outFlights);
    }
    
    public void crashedUpdate(int crashed)
    {
        crashedLabel.setText("Planes Crashed and Burned: " + crashed);
    }
    
    public void planeMissing()
    {
        planesMissingLabel.setText("Flight 370 missing");
    }

    public void addFlight(Flight f)
    {
        images.addFlight(f);
    }

    public void removeFlight(Flight f)
    {
        images.removeFlight(f);
    }

    public String convertTime(int m)
    {        
        //Calculating days (1440 minutes in a 24 hour day)
        day = m / 1440;
        //If it's been over 24 hours, reset minutes to 0
        if(day < 1)
            m -= (1440 * day);

        //Calculating hours
        int h = m / 60;
        //Reseting minutes to 0 if it's been more than one hour
        if(h > 0)
            m -= (60 * h);
        //Resetting hours to 0 if it's been more than 24 hours
        if(h > 23)
            h -= (24 * day);

        //Convertnig h to a String
        String h2;
        //Adding a 0 in front of the hours if it's less than 10
        if(h < 10)
            h2 = "0" + h;
        else
            h2 = "" + h;

        //Converting m to a String
        String m2;
        //Adding a 0 in front of the minutes if it's less than 10
        if(m < 10)
            m2 = "0" + m;
        else
            m2 = "" + m;

        return h2 + ":" + m2;
    }
}