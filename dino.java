import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DinosaurGame extends JFrame implements KeyListener {
    private int maxX = 40;
    private int maxY = 15;
    private int playerX = 2;
    private int playerY = maxY - 3;
    private int jump = 0;
    private int jumpHeight = 2;
    private int highJumpHeight = 4;
    private int obstacleX = maxX;
    private int score = 0;
    private Timer timer;
    private boolean shiftPressed = false;

    private GamePanel gamePanel;

    public DinosaurGame() {
        setTitle("Dinosaur Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        addKeyListener(this);

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                gamePanel.repaint();
            }
        });
        timer.start();
    }

    private void update() {
        obstacleX--;

        if (playerX == obstacleX && playerY == maxY - 3) {
            int choice = JOptionPane.showConfirmDialog(this, "¡Has perdido! ¿Quieres jugar de nuevo?", "Replay", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                restart();
            } else {
                exitGame();
            }
        }

        if (obstacleX == 0) {
            score++;
            obstacleX = maxX;
        }

        if (playerX == obstacleX) {
            jump = 0;
        }

        if (jump != 0) {
            if (jump == 1) {
                playerY -= jumpHeight;
                if (playerY <= 0) {
                    jump = 0;
                }
            } else if (jump == 2) {
                playerY -= highJumpHeight;
                if (playerY <= 0) {
                    jump = 0;
                }
            }
        } else if (playerY < maxY - 3) {
            playerY += 3; // Ajuste para aumentar la velocidad de caída del personaje
            if (playerY > maxY - 3) {
                playerY = maxY - 3; // Limitar la posición del jugador al suelo
            }
        }
    }

    private void restart() {
        playerX = 2;
        playerY = maxY - 3;
        jump = 0;
        obstacleX = maxX;
        score = 0;
        timer.start();
    }

    private void exitGame() {
        dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (playerY == maxY - 3 && e.getKeyCode() != KeyEvent.VK_SHIFT) {
            jump = 1;
        }

        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
            jump = 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (int i = 0; i < maxX; i++) {
                g.drawString("_", i * 10, (maxY - 3) * 20);
                g.setColor(Color.GREEN);
                g.drawString("&", playerX * 20, playerY * 20);
                g.setColor(Color.RED);
                g.drawString("X", obstacleX * 20, (maxY - 3) * 20);
                g.setColor(Color.YELLOW);
                g.drawString("Score: " + score, 10, 20);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DinosaurGame().setVisible(true);
            }
        });
    }
}
