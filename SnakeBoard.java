package snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeBoard
  extends JPanel
{
  static final long serialVersionUID = 1L;
  private Rectangle border;
  private Rectangle snake;
  Timer timer;
  JLabel GameOver;
  String Over;
  String SCORE;
  boolean up;
  boolean down;
  boolean left;
  boolean right;
  boolean showFood;
  int speed;
  int dY;
  int dX;
  int counter;
  int indexCounter;
  int difficulty;
  int score;
  Rectangle food = new Rectangle(0, 0, 15, 15);
  ArrayList<Integer> xPos = new ArrayList<Integer>();
  ArrayList<Integer> yPos = new ArrayList<Integer>();
  ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
  
  public SnakeBoard()
  {
    difficulty = 1;
    score = 0;
    SCORE = "";
    dY = 0;
    dX = 0;
    
    speed = 7;
    
    counter = 0;
    
    showFood = false;
    
    setPreferredSize(new Dimension(700, 710));
    setBackground(Color.BLACK);
    
    border = new Rectangle(0, 0, 700, 700);
    snake = new Rectangle(50, 50, 15, 15);
    
    Over = "";
    
    final JLabel GameOver = new JLabel(Over);
    
    GameOver.setFont(new Font("Serif", 1, 75));
    GameOver.setForeground(Color.WHITE);
    add(GameOver);
    
    rectangles.add(snake);
    
    
    //Actions performed every tick of the timer
    //Moves the snake, as well as body of snake accordingly
    //checks if hit wall, self, or food
    ActionListener actListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        move(snake, dX, dY);
        moveSnake(dX, dY);
        index();
        hitWall();
        hitSelf();
        hitFood();
        if (Over != "") {
          GameOver.setText("<html>" + Over + "<br>" + "       " + SCORE + "</html>");
        }
        spawnFood();
        repaint();
      }
    };
    timer = new Timer(speed, actListener);
    
    setBackground(Color.black);
    addKeyListener(new myKeyListener());
    setFocusable(true);
    
    timer.start();
  }
  //Draws rectangles of snake, snake body, food, background and boundary
  public void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);
    Graphics2D graphics2d = (Graphics2D)graphics;
    graphics2d.setStroke(new BasicStroke(10.0F));
    graphics2d.setColor(Color.WHITE);
    
    graphics2d.draw(border);
    graphics.setColor(Color.WHITE);
    
    graphics.setColor(Color.WHITE);
    graphics2d.fill(snake);
    
    graphics2d.setStroke(new BasicStroke(1.0F));
    if (showFood)
    {
      graphics.setColor(Color.RED);
      graphics2d.draw(food);
      graphics2d.fill(food);
    }
    if (rectangles.size() > 0) {
      for (int i = 0; i < rectangles.size(); i++)
      {
        graphics.setColor(Color.WHITE);
        graphics2d.fill(rectangles.get(i));
      }
    }
  }
  //handles key listening to move snake
  private class myKeyListener
    implements KeyListener
  {
    private myKeyListener() {}
    
    public void keyPressed(KeyEvent keyEvent)
    {
      switch (keyEvent.getKeyCode())
      {
      case 38: 
        move(snake, 0.0D, -3.0D);
        moveSnake(0.0D, -3.0D);
        dY = -3;
        dX = 0;
        up = true;
        left = false;
        right = false;
        
        break;
      case 40: 
        move(snake, 0.0D, 3.0D);
        moveSnake(0.0D, 3.0D);
        dY = 3;
        dX = 0;
        down = true;
        left = false;
        right = false;
        
        break;
      case 39: 
        move(snake, 3.0D, 0.0D);
        moveSnake(3.0D, 0.0D);
        dX = 3;
        dY = 0;
        right = true;
        up = false;
        down = false;
        
        break;
      case 37: 
        move(snake, -3.0D, 0.0D);
        moveSnake(-3.0D, 0.0D);
        dX = -3;
        dY = 0;
        left = true;
        up = false;
        down = false;
        
        break;
      case 10: 
        snake.setBounds(50, 50, 15, 15);
        dY = 0;
        dX = 0;
        up = false;
        down = false;
        left = false;
        right = false;
        for (int i = rectangles.size() - 1; i > 0; i--) {
          rectangles.remove(i);
        }
        Over = " ";
        SCORE = " ";
        score = 0;
        timer.restart();
        repaint();
        
        break;
      case 27: 
        System.exit(0);
      }
    }
    
    public void keyReleased(KeyEvent keyEvent) {}
    
    public void keyTyped(KeyEvent keyEvent) {}
  }
  //Moves a general rectangle by a dX and dY
  public void move(Rectangle rect, double dX, double dY)
  {
    rect.setBounds((int)(rect.getX() + dX), (int)(rect.getY() + dY), (int)rect.getWidth(), (int)rect.getHeight());
  }
  //checks for collision against wall
  public void hitWall()
  {
    if ((snake.getY() <= 0.0D) || (snake.getY() >= 690.0D) || (snake.getX() <= 0.0D) || (snake.getX() >= 690.0D))
    {
      timer.stop();
      Over = "GAME OVER";
      SCORE = ("Score: " + score);
      repaint();
    }
  }
  //checks for collision against food
  public void hitFood()
  {
    if (snake.intersects(food))
    {
      for (int i = 0; i <= difficulty; i++) {
        rectangles.add(new Rectangle(xPos.get(xPos.size() - 9 * (rectangles.size() + 1)).intValue(), yPos.get(yPos.size() - 9 * (rectangles.size() + 1)).intValue(), 15, 15));
      }
      showFood = false;
      score += 1;
    }
  }
  //spawns food at start and once it is hit by snake
  public void spawnFood()
  {
    if (!showFood)
    {
      int posX = (int)(Math.random() * 670.0D);
      int posY = (int)(Math.random() * 670.0D);
      food.setBounds(0, 0, 8, 8);
      move(food, posX, posY);
      showFood = true;
    }
  }
  //adds locations to snake to update body 
  public void index()
  {
    xPos.add(((int)snake.getX()));
    yPos.add(((int)snake.getY()));
  }
  //checks for self intersections
  public void hitSelf()
  {
    for (int i = 1; i < rectangles.size(); i++) {
      if (snake.intersects(rectangles.get(i)))
      {
        timer.stop();
        Over = "GAME OVER";
        SCORE = ("Score: " + score);
        repaint();
      }
    }
  }
  //moves head of snake
  public void moveSnake(double dX, double dY)
  {
    if (rectangles.size() > 1) {
      for (int i = 1; i < rectangles.size(); i++) {
        rectangles.get(i).setBounds(xPos.get(xPos.size() - 9 * i).intValue(), yPos.get(yPos.size() - 9 * i).intValue(), 15, 15);
      }
    }
  }
}
