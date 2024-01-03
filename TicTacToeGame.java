import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToeGame extends JFrame {
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private boolean xTurn = true;
    private boolean gameOver = false;
    private int xScore = 0;
    private int oScore = 0;
    private int drawScore = 0;
    private Color xColor = Color.BLUE;
    private Color oColor = Color.RED;
    private Color bgColor = Color.BLACK;

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        scoreLabel = new JLabel("Player X: 0  Player O: 0  Draw: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(scoreLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBackground(bgColor);
        buttons = new JButton[3][3];
        initializeButtons(boardPanel);

        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeButtons(JPanel boardPanel) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 40));
                button.setForeground(Color.WHITE);
                button.setBackground(bgColor);
                buttons[i][j] = button;
                boardPanel.add(button);

                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onButtonClick(row, col);
                    }
                });
            }
        }
    }

    private void onButtonClick(int row, int col) {
        if (!gameOver && buttons[row][col].getText().isEmpty()) {
            buttons[row][col].setText("X");
            buttons[row][col].setForeground(xColor);
            xTurn = false;

            if (checkWin(row, col, "X")) {
                xScore++;
                updateScoreLabel();
                displayWinner("Player X");
                restartGame();
            } else if (isBoardFull()) {
                drawScore++;
                updateScoreLabel();
                displayDraw();
                restartGame();
            } else {
                playComputerTurn();
            }
        }
    }

    private void playComputerTurn() {
        if (!gameOver) {
            // Simulate a simple AI for the computer
            Random rand = new Random();
            int row, col;

            // Check if computer can win in the next move
            for (row = 0; row < 3; row++) {
                for (col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().isEmpty()) {
                        buttons[row][col].setText("O");
                        if (checkWin(row, col, "O")) {
                            oScore++;
                            updateScoreLabel();
                            displayWinner("Player O");
                            restartGame();
                            return;
                        }
                        buttons[row][col].setText("");
                    }
                }
            }

            // Check if player can win in the next move and block
            for (row = 0; row < 3; row++) {
                for (col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().isEmpty()) {
                        buttons[row][col].setText("X");
                        if (checkWin(row, col, "X")) {
                            buttons[row][col].setText("O");
                            buttons[row][col].setForeground(oColor);
                            xTurn = true;
                            return;
                        }
                        buttons[row][col].setText("");
                    }
                }
            }

            // If no winning move, choose a random empty cell
            do {
                row = rand.nextInt(3);
                col = rand.nextInt(3);
            } while (!buttons[row][col].getText().isEmpty());

            buttons[row][col].setText("O");
            buttons[row][col].setForeground(oColor);
            xTurn = true;

            if (checkWin(row, col, "O")) {
                oScore++;
                updateScoreLabel();
                displayWinner("Player O");
                restartGame();
            } else if (isBoardFull()) {
                drawScore++;
                updateScoreLabel();
                displayDraw();
                restartGame();
            }
        }
    }

    private boolean checkWin(int row, int col, String player) {
        // Check row
        if (buttons[row][0].getText().equals(player) && buttons[row][1].getText().equals(player) && buttons[row][2].getText().equals(player)) {
            return true;
        }
        // Check column
        if (buttons[0][col].getText().equals(player) && buttons[1][col].getText().equals(player) && buttons[2][col].getText().equals(player)) {
            return true;
        }
        // Check diagonals
        if (row == col && buttons[0][0].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][2].getText().equals(player)) {
            return true;
        }
        if (row + col == 2 && buttons[0][2].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][0].getText().equals(player)) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void displayWinner(String winner) {
        gameOver = true;
        JOptionPane.showMessageDialog(this, winner + " wins!");
    }

    private void displayDraw() {
        gameOver = true;
        JOptionPane.showMessageDialog(this, "It's a draw!");
    }

    private void updateScoreLabel() {
        scoreLabel.setText("<html><font color='white'>Player X: " + xScore + "  Player O: " + oScore + "  Draw: " + drawScore + "</font></html>");
    }

    private void restartGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        xTurn = true;
        gameOver = false;

        // If the computer starts, make its first move
        if (!xTurn) {
            playComputerTurn();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TicTacToeGame game = new TicTacToeGame();
            }
        });
    }
}
