import java.util.ArrayList;

public class Ship
{
    private int length;
    private int numAtt;
    private int shipNum;
    private String shipName;
    //Array list contains: {row, column, shipNum} (ship number info in Player file)
    private ArrayList<int[]> coordinates;

    public Ship(int length, int shipNum, String name) {
        this.length = length;
        numAtt = 0;
        shipName = name;
        this.shipNum = shipNum;
        coordinates = new ArrayList<int[]>();
    }

    public boolean isSinked() {
        return length == numAtt;
    }

    public boolean allCoordinatesSet() {
        return length == coordinates.size();
    }

    public int getLength()
    {
        return length;
    }

    public boolean validCoordinate(int r, int c) {
        boolean valid = false;
        for (int[] coor:coordinates)
        {
            if (coor[0] == r && coor[1] == c)
                valid = true;
        }
        return valid;
    }

    public void addCoordinate(int[] coor, int shipNum) {
        if (length != coordinates.size()) {
            int[] coordinate = new int[3];
            coordinate[0] = coor[0];
            coordinate[1] = coor[1];
            coordinate[2] = shipNum;
            coordinates.add(coordinate);
        }
    }

    public void attacked() {
        numAtt++;
    }

    public int getShipNum() {
        return shipNum;
    }

    public String getShipName() {
        return shipName;
    }

    public int getNumAtt() {
        return numAtt;
    }

    public ArrayList<int[]> getCoordinates()
    {
        return coordinates;
    }
}
