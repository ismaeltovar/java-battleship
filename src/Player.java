import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/** Legend for storing ships:
 * 0 = Destroyer
 * 1 = Submarine
 * 2 = Cruiser
 * 3 = Battleship
 * 4 = Carrier
 * */

public class Player
{
    private Ship[] ships;
    private JLabel gridLbl;
    private JButton[][] grid;
    private JButton[] btns;
    private final String pName;
    private Ship selectedShip;
    private ActionListener aL;

    public Player(String name, ActionListener a) {
        pName = name;
        grid = new JButton[8][8];
        btns = new JButton[5];
        //initializing ships
        ships = new Ship[5];
        ships[0] = new Destroyer();
        ships[1] = new Submarine();
        ships[2] = new Cruiser();
        ships[3] = new Battleship();
        ships[4] = new Carrier();

        aL = a;
        //setup grid
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                createGridBtn(r, c);
            }
        }

        //setup btns
        for (int i = 0; i < 5; i++)
        {
            createShipBtn(i);
        }

        //setup grid lbl
        createGridLbl();
    }

    /** Initialization methods */

    private void createGridBtn(int r, int c) {
        JButton btn = new JButton("" + r + c);
        btn.setBackground(Color.blue);
        btn.setForeground(Color.blue);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.addActionListener(aL);

        //setting bounds
        int gridXStart = pName.equals("Player 1") ?  100 : 750;
        int gridYStart = 220;
        int x = (r * 20) + ((r + 1) * 20) + gridXStart;
        int y = (c * 20) + ((c + 1) * 20) + gridYStart;
        btn.setBounds(x, y, 40, 40);

        grid[r][c] = btn;
    }

    private void createShipBtn(int index) {
        String txt = "";
        if (index == 0)
            txt = "Destroyer";
        else if (index == 1)
            txt = "Submarine";
        else if (index == 2)
            txt = "Cruiser";
        else if (index == 3)
            txt = "Battleship";
        else if (index == 4)
            txt = "Carrier";
        JButton btn = new JButton(txt);
        btn.setBackground(Color.lightGray);
        btn.setForeground(Color.black);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.addActionListener(aL);
        btn.setEnabled(false);

        //setting bounds
        int xStart = pName.equals("Player 1") ? -80 : 525;
        int x = ((index + 1) * 110) + xStart;
        int y = 700;
        btn.setBounds(x, y, 100, 50);

        btns[index] = btn;
    }

    private void createGridLbl() {
        gridLbl = new JLabel(pName);
        if (pName.equals("Player 1"))
            gridLbl.setBounds(250, 50, 200, 100);
        else if (pName.equals("Player 2"))
            gridLbl.setBounds(900, 50, 200, 100);
        Font font = new Font("Arial", Font.BOLD, 20);
        gridLbl.setFont(font);
        gridLbl.setForeground(Color.white);
    }

    /** Action methods */

    public void attackGridBtn(int r, int c) {
        int shipNum = isShipHere(r, c);

        if (shipNum >= 0)
        {
            grid[r][c].setBackground(Color.red);
            grid[r][c].setForeground(Color.red);
            ships[shipNum].attacked();
        } else {
            grid[r][c].setBackground(Color.GRAY);
            grid[r][c].setForeground(Color.GRAY);
        }
        grid[r][c].setEnabled(false);
        if (shipNum > -1)
            if (ships[shipNum].isSinked())
                btns[shipNum].setBackground(Color.red);
    }

    public int isShipHere(int row, int col) {
        int shipNum = -1;
        for (Ship s:ships)
        {
            boolean valid = s.validCoordinate(row, col);
            if (valid)
                shipNum = s.getShipNum();
        }
        System.out.println("Ship here: " + shipNum);
        return shipNum;
    }

    public void setGridEnabled(boolean enable) {
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                grid[r][c].setEnabled(enable);
            }
        }
    }

    public void setPlayerEnabled(boolean enable) {
        setGridEnabled(enable);
        if (enable) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (grid[r][c].getBackground() == Color.red || grid[r][c].getBackground() == Color.GRAY)
                        grid[r][c].setEnabled(false);
                }
            }
        }
        System.out.println(pName + " enabled: " + enable);
    }

    public boolean isPlayerDefeated() {
        int shipsSunk = 0;

        for (Ship s:ships)
        {
            if (s.isSinked())
                shipsSunk++;
        }

        return shipsSunk == 5;
    }

    public boolean allShipsSelected() {
        boolean coordinateNotSet = false;

        for (Ship s:ships) {
            if (!s.allCoordinatesSet())
                coordinateNotSet = true;
        }

        return !coordinateNotSet;
    }

    public void startShipSelection() {
        selectedShip = ships[0];
        btns[0].setEnabled(true);
    }

    public void endShipSelection() {
        selectedShip = null;
        btns[4].setEnabled(false);
    }

    public void hideShips() {
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                grid[r][c].setBackground(Color.blue);
                grid[r][c].setEnabled(true);
            }
        }
    }

    /** Setters */

//    public void setSelectedShip(String txt) {
//        if (txt.equals("Destroyer")) {
//            selectedShip = getDestroyer();
//        } else if (txt.equals("Submarine")) {
//            selectedShip = getSubmarine();
//        } else if (txt.equals("Cruiser")) {
//            selectedShip = getCruiser();
//        } else if (txt.equals("Battleship")) {
//            selectedShip = getBattleship();
//        } else if (txt.equals("Carrier")) {
//            selectedShip = getCarrier();
//        }
//    }

    public void nextShipSelect() {
        int index = 0;

        for (int i = 0; i < 5; i++)
        {
            if (selectedShip == ships[i])
                index = i;
        }
        btns[index].setEnabled(false);
        btns[index + 1].setEnabled(true);
        selectedShip = ships[index + 1];
    }

    public void setCoordinate(int[] coordinate, int coorSelected) {
        if (coorSelected == 0) {
            selectedShip.addCoordinate(coordinate, selectedShip.getShipNum());
            grid[coordinate[0]][coordinate[1]].setBackground(Color.GRAY);
            grid[coordinate[0]][coordinate[1]].setEnabled(false);
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (r == coordinate[0]) {
                        if (c >= (coordinate[1] + selectedShip.getLength()) || c <= (coordinate[1] - selectedShip.getLength()))
                            grid[r][c].setEnabled(false);
                    } else if (c == coordinate[1]) {
                        if (r >= (coordinate[0] + selectedShip.getLength()) || r <= (coordinate[0] - selectedShip.getLength()))
                            grid[r][c].setEnabled(false);
                    } else {
                        grid[r][c].setEnabled(false);
                    }
                    }
                }
        } else {
            selectedShip.addCoordinate(coordinate, selectedShip.getShipNum());
            grid[coordinate[0]][coordinate[1]].setBackground(Color.GRAY);
            grid[coordinate[0]][coordinate[1]].setEnabled(false);
        }
    }


    /** Getters */

    public Ship getDestroyer() {
        return ships[0];
    }

    public Ship getSubmarine() {
        return ships[1];
    }

    public Ship getCruiser() {
        return ships[2];
    }

    public Ship getBattleship() {
        return ships[3];
    }

    public Ship getCarrier() {
        return ships[4];
    }

    /**General getters*/

    public Ship[] getShips()
    {
        return ships;
    }

    public JButton[] getBtns()
    {
        return btns;
    }

    public JLabel getGridLbl()
    {
        return gridLbl;
    }

    public JButton[][] getGrid()
    {
        return grid;
    }

    public String getpName()
    {
        return pName;
    }

    public Ship getSelectedShip()
    {
        return selectedShip;
    }

    public ArrayList<Ship> getSunkShips() {
        ArrayList<Ship> sunkShips = new ArrayList<Ship>();
        for (Ship s:ships) {
            if (s.isSinked())
                sunkShips.add(s);
        }
        return sunkShips;
    }

    public void printShipCoordinates() {
        //debugging purposes
        System.out.println("\n******************************************\n");
        System.out.println("\n\n" + pName + " ships: \n");
        for (Ship s:ships)
        {
            System.out.println(s.getShipName() + " : ");
            for (int[] coor:s.getCoordinates())
            {
                System.out.println("[" + coor[0] + ", " + coor[1] + "]");
            }
        }
        System.out.println("\n******************************************\n");
    }
}
