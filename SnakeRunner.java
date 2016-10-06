package snake;
import java.awt.Dimension;
import javax.swing.JFrame;

public class SnakeRunner
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame("Pong");
    
    frame.setDefaultCloseOperation(3);
    
    frame.getContentPane().add(new SnakeBoard());
    
    frame.setPreferredSize(new Dimension(710, 730));
    
    frame.setLocation(0, 0);
    
    frame.setVisible(true);
    
    frame.pack();
    
    frame.setResizable(false);
    
    frame.setLayout(null);
    
    SnakeBoard panel = new SnakeBoard();
    
    panel.setLayout(null);
  }
}
