import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;

    static final int WIDTH = 550;
    static final int HEIGHT = 550;
    static final int UNIT_SIZE = 25;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // hold x and y coordinates for body parts of the snake
    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];

    // initial length of the snake
    int length = 5;
    int apple;
    int appleX;
    int appleY;
    char moveDirection = 'D';
    boolean playing = false;
    Random random;
    Timer timer;

    SnakePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addApple();
        playing = true;

        timer = new Timer(90, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            // move the snake to the direction you want to create a move
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if (moveDirection == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (moveDirection == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        } else if (moveDirection == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else {
            y[0] = y[0] + UNIT_SIZE;
        }
    }

    // check and add food & length
    public void checkApple() {
        if(x[0] == appleX && y[0] == appleY) {
            length++;
            apple++;
            addApple();
        }
    }

    public void draw(Graphics graphics) {

        if (playing) {
            graphics.setColor(new Color(210, 50, 10));
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            graphics.setColor(Color.white);
            graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 1; i < length; i++) {
                graphics.setColor(new Color(0, 150, 250));
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + apple, (WIDTH - metrics.stringWidth("Score: " + apple)) / 2, graphics.getFont().getSize());

        } else {
            gameOver(graphics);
        }
    }

    public void addApple() {
        appleX = random.nextInt((int)(WIDTH / UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(HEIGHT / UNIT_SIZE))*UNIT_SIZE;
    }


    // checking if head runs into the snake body
    public void checkHit() {
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                playing = false;
            }
        }

        // checking if head runs into the walls
        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
            playing = false;
        }

        if(!playing) {
            timer.stop();
        }
    }


    // game over panel
    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.PLAIN, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + apple, (WIDTH - metrics.stringWidth("Score: " + apple)) / 2, graphics.getFont().getSize());

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (playing) {
            move();
            checkApple();
            checkHit();
        }
        repaint();
    }


    // Game controller
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (moveDirection != 'R') {
                        moveDirection = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (moveDirection != 'L') {
                        moveDirection = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (moveDirection != 'D') {
                        moveDirection = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (moveDirection != 'U') {
                        moveDirection = 'D';
                    }
                    break;
            }
        }
    }
}