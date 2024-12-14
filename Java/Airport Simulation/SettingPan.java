import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
/**
 * Used to create slider that controls Simulation Speed
 * @author Jeffrey VanMeter
 */
public class SettingPan extends JPanel
{
    JSlider slider;//Instance of JSlider.
    FlowLayout layout; //Instance of FlowLayout.
    TickLoop tickLoop;//Instance of TickLoop.
    
    /**
     * Constructor for SettingPan.
     */
    public SettingPan(TickLoop tickLoop)
    {
        this.tickLoop = tickLoop;
        makePanel();
        setVisible(true);
    }
    
    /**
     * Actually creates slider. Sets it in FlowLayout and tells the slider what it will change.
     */
    private void makePanel(){
        layout = new FlowLayout(FlowLayout.LEFT);
        slider = new JSlider(-2000, -5, ((tickLoop.getSpeed())*-1));
        slider.setSize(50, 20);
        setLayout(layout);
        add(new JLabel("Clock Speed:"));
        add(slider); 
        slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    tickLoop.setSpeed( slider.getValue() );
                }
            });

    }

}
