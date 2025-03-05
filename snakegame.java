import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends JFrame {
    public SnakeGame() {
        setTitle("Snake Game");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
    public static void main(String[] args) {
        new SnakeGame();
    }
}
class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25; // Size of each tile
    private final int GAME_WIDTH = 600;
    private final int GAME_HEIGHT = 600;
    private final int TOTAL_TILES = (GAME_WIDTH * GAME_HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int[] x = new int[TOTAL_TILES];
    private final int[] y = new int[TOTAL_TILES];
    private int snakeLength;
    private int foodX, foodY;
    private char direction = 'R'; // Initial direction: R = right, L = left, U = up, D = down
    private boolean running = false;
    private Timer timer;
    public GamePanel() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }
    private void startGame() {
        snakeLength = 3; // Initial snake length
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 100 - i * TILE_SIZE;
            y[i] = 100;
        }
        spawnFood();
        running = true;
        timer = new Timer(100, this); // Speed of the game
        timer.start();
    }
    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(GAME_WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(GAME_HEIGHT / TILE_SIZE) * TILE_SIZE;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            // Draw snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Head of the snake
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Body of the snake
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
            // Draw grid (optional)
            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < GAME_WIDTH / TILE_SIZE; i++) {
                g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, GAME_HEIGHT);
            }
            for (int i = 0; i < GAME_HEIGHT / TILE_SIZE; i++) {
                g.drawLine(0, i * TILE_SIZE, GAME_WIDTH, i * TILE_SIZE);
            }
        } else {
            gameOver(g);
        }
    }
    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String message = "Game Over!";
        g.drawString(message, (GAME_WIDTH - metrics.stringWidth(message)) / 2, GAME_HEIGHT / 2);
    }
    private void move() {
        for (int i = snakeLength - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break;
            case 'D': y[0] += TILE_SIZE; break;
            case 'L': x[0] -= TILE_SIZE; break;
            case 'R': x[0] += TILE_SIZE; break;
        }
    }
    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++;
            spawnFood();
        }
    }
    private void checkCollisions() {
        // Check if the snake collides with itself
        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // Check if the snake hits the wall
        if (x[0] < 0 || x[0] >= GAME_WIDTH || y[0] < 0 || y[0] >= GAME_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
            case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }
}
