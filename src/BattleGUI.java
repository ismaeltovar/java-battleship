import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Some dead code throughout the project has been commented out for future reference.
 * */

public class BattleGUI extends JFrame implements ActionListener
{
    //Instance variables
    private Container window;
    private final ArrayList<Player> players;
    private final Player p1;
    private final Player p2;
    private Player activeP;
    private Player disabledP;
    private JButton startBtn;
    private Player attackedP;
    private boolean battleStart;
    private int coorSelected;

    public BattleGUI() {
        players = new ArrayList<Player>();
        players.add(new Player("Player 1", this));
        players.add(new Player("Player 2", this));
        p1 = players.get(0);
        p2 = players.get(1);
        activeP = p1;
        attackedP = p2;
        battleStart = false;
        coorSelected = 0;
        setupWindow();
        addComponentsToWindow();
    }

    public void setupWindow() {
        window = getContentPane();
        window.setLayout(null);
        window.setBackground(Color.darkGray);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 900);
        setResizable(false);
        setLocation(50, 50);
        setTitle("Java BattleShip");
    }

    public void addComponentsToWindow()
    {

        for (Player p:players)
        {
            //add ship btns
            for (JButton btn: p.getBtns())
                window.add(btn);
            //add grid btns
            for (JButton btns[] : p.getGrid())
                for (JButton btn:btns)
                    window.add(btn);
            //add lbl
            window.add(p.getGridLbl());
        }
        //disabled player 2
        p2.setPlayerEnabled(false);
        p1.startShipSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JButton) {
            JButton clickedBtn = (JButton) e.getSource();

            //For grid btns
            int[] gridCoor = isGridBtn(clickedBtn);

            if (gridCoor[0] >= 0) {
                if (battleStart)
                {
                    attackedP.attackGridBtn(gridCoor[0], gridCoor[1]);
                    attackedP.setPlayerEnabled(false);
                    attackedP = p1 == attackedP ? p2 : p1;
                    attackedP.setPlayerEnabled(true);
                }
                else
                    {
                        if (!activeP.allShipsSelected())
                        {
                            activeP.setCoordinate(gridCoor, coorSelected);
                            coorSelected++;
                            boolean allCSet = activeP.getSelectedShip().allCoordinatesSet();
                            System.out.println("All coordinates set? " + allCSet);

                            if (allCSet && activeP.getSelectedShip() != activeP.getShips()[4]) {
                                activeP.nextShipSelect();
                                coorSelected = 0;
                                activeP.setPlayerEnabled(true);
                            }
                            window.revalidate();
                            window.repaint();
                        }

                        if (p1.allShipsSelected() && !p2.allShipsSelected() && activeP == p1)
                        {
                            players.get(0).setPlayerEnabled(false);
                            activeP = p2;
                            p2.setPlayerEnabled(true);
                            p2.startShipSelection();
                            p1.endShipSelection();
                            p1.hideShips();
                            coorSelected = 0;
                            window.revalidate();
                            window.repaint();
                            p1.setPlayerEnabled(false);
                        } else if (p1.allShipsSelected() && p2.allShipsSelected() && activeP == p2)
                            {
                                p1.setPlayerEnabled(false);
                                p2.hideShips();
                                p2.endShipSelection();
                                p2.setPlayerEnabled(false);
                                window.revalidate();
                                window.repaint();
                                startBtn = new JButton("Start Battle");
                                startBtn.setBounds(500, 400, 200, 100);
                                startBtn.addActionListener(this);
                                startBtn.setFocusPainted(false);
                                window.add(startBtn);
                            }
                        }
                    } else {
                p1.setPlayerEnabled(false);
                System.err.println("Error: Invalid coordinate/button");
            }

            //For debugging purposes: checks if all ships have been placed in board for both players
            boolean allSelected1 = p1.allShipsSelected();
            boolean allSelected2 = p2.allShipsSelected();
            System.out.println("All Ships selected for Player 1? " + allSelected1);
            System.out.println("All Ships selected for Player 2? " + allSelected2);

            if (clickedBtn.equals(startBtn)) {
                attackedP.setPlayerEnabled(true);
                window.remove(startBtn);
                battleStart = true;
               p1.printShipCoordinates();
               p2.printShipCoordinates();
            }

            //debugging purposes
            for (Ship s: p2.getShips()) {
                System.out.println(s.getShipName() + " sunk : " + s.isSinked() + " " + s.getNumAtt());
            }

            if (p2.isPlayerDefeated()) {
                System.out.println("Player 1 Wins!");
                window.setEnabled(false);
                showDialog("Player 1");
            }
            else if (p1.isPlayerDefeated()) {
                System.out.println("Player 2 Wins!");
                window.setEnabled(false);
                showDialog("Player 2");
            }
        }
    }

    /** ActionListener Methods */

//    private String btnType(JButton btn) {
//        int[] gridBtn = isGridBtn(btn);
//
//        if (gridBtn[0] > -1) {
//            return "grid";
//        } else if (isShipBtn(btn) > -1) {
//            return "ship";
//        } else {
//            return null;
//        }
//    }

    private int[] isGridBtn(JButton btn) {
        int[] coordinate = {-1, -1};

        JButton[][] grid;

        if (battleStart)
             grid = attackedP.getGrid();
        else
            grid = activeP.getGrid();

        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (btn.equals(grid[r][c]))
                {
                    coordinate[0] = r;
                    coordinate[1] = c;
                }
        if (battleStart)
            System.out.println(attackedP.getpName() + Arrays.toString(coordinate));
        else
            System.out.println(activeP.getpName() + Arrays.toString(coordinate));
        return coordinate;
    }

//    private int isShipBtn(JButton btn) {
//        int shipNum = -1;
//
//        JButton[] btns = activeP.getBtns();
//        for (int i = 0; i < btns.length; i++)
//        {
//            if (btn.equals(btns[i]))
//                shipNum = i;
//        }
//
//        return shipNum;
//    }

    //Shows dialog to display winner at the end of the game
    public void showDialog(String winner) {
        String txt = winner.equals("Player 1") ? "Player 1 Wins!" : "Player 2 Wins!";
        int optionChosen = JOptionPane.showConfirmDialog(window, txt, "Game Over!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (optionChosen == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        BattleGUI program = new BattleGUI();
        program.setVisible(true);
    }
}
