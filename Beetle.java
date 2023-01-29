public class Beetle extends Creature {
    protected boolean hasMoved = false; // keep track of whether the beetle has moved that turn
    protected int numEatenAnts = 0; // keep track of if the beetle has eaten ants 
    protected int timer = 0; // keep track of how many turns the beetle has not eaten 
    protected boolean isNewB = false; // keep track of whether the beetle is new in that turn  
    
    // setters
    public void setNumEatenAnts() {
        numEatenAnts = numEatenAnts + 1;
    }
    
    public void setMoved(boolean m) {
        hasMoved = m;
    }
    
     public void setTimer(int time) {
        timer = time;
    }
    
    public void setIsNewB(boolean b) {
        isNewB = b;
    }
    
    // getters
    public boolean getMoved() {
        return hasMoved;
    }
    
    public int getNumEatenAnts() {
        return numEatenAnts;
    }
   
    public int getTimer() {
        return timer;
    }
    
    public boolean getIsNewB() {
        return isNewB;
    }
    
    @Override
    public int move(int[][] distAntArray) {
        int closest = 10; // the closest ant can't be of distance 10 bc it's past the array
        int[] tiesClose = new int[4]; // hold tied values of closest ant
        int[] tiesNeighbors = new int[4]; // hold tied values of closest ant w/ most neighbors
        int mostNeighbors = 0; 
        
        // find the closest distance
        for (int dir = 0; dir < 4; dir++) {
            if (distAntArray[0][dir] != -1 && distAntArray[0][dir] < closest) {
                closest = distAntArray[0][dir]; // reset closest distance
            }
        }
        
        // find any ties for closest distance
        int tieCountClose = -1;
        for (int dir = 0; dir < 4; dir++) {
            if (distAntArray[0][dir]!= -1 && distAntArray[0][dir] == closest) {
                tieCountClose++;
                tiesClose[tieCountClose] = dir; // store ties
            }
        }
        
        if (tieCountClose == 0) {
            // return direction
            if (tiesClose[tieCountClose] == 0) {return 0;}
            else if (tiesClose[tieCountClose] == 1) {return 1;}
            else if (tiesClose[tieCountClose] == 2) {return 2;}
            else if (tiesClose[tieCountClose] == 3) {return 3;}
        } 
        else { // if tied then check for neighbors
            // find the greatest number of neighbors
            for (int ti = 0; ti < tieCountClose + 1; ti++) {
                if (distAntArray[1][tiesClose[ti]] > mostNeighbors) {
                    mostNeighbors = distAntArray[1][tiesClose[ti]];
                }
            }
            
            // find the ties 
            int tieCount = -1;
            for (int ti = 0; ti < tieCountClose + 1; ti++) {
                if (distAntArray[1][tiesClose[ti]]!= 0 && distAntArray[1][tiesClose[ti]] == mostNeighbors) {
                    tieCount++;
                    tiesNeighbors[tieCount] = tiesClose[ti];
                }
            }
            
            if (tieCount == 0) {
                // return direction
                if (tiesNeighbors[tieCount] == 0) {return 0;}
                else if (tiesNeighbors[tieCount] == 1) {return 1;}
                else if (tiesNeighbors[tieCount] == 2) {return 2;}
                else if (tiesNeighbors[tieCount] == 3) {return 3;}
            }
            else {
                // return direction in order of NESW; n = 0 , s = 1, e = 2, w = 3
                for(int t = 0; t < tieCount + 1; t++) {
                    if (tiesNeighbors[t] == 0) {
                        return 0;
                    }
                }
                
                for(int t = 0; t < tieCount + 1; t++) {
                    if (tiesNeighbors[t] == 2) {
                        return 2;
                    }
                }
                
                for(int t = 0; t < tieCount + 1; t++) {
                    if (tiesNeighbors[t] == 1) {
                        return 1;
                    }
                }
                
                for(int t = 0; t < tieCount + 1; t++) {
                    if (tiesNeighbors[t] == 3) {
                        return 3;
                    }
                }
            }
            
        }
        return -1;
    }
    
    @Override
    public int breed(boolean[] empty) {
        // breed in order of NESW
        if (empty[0]) {
            return 0;
        }
        else if (empty[2]) {
            return 2;
        }
        else if (empty[1]) {
            return 1;
        }
        else if (empty[3]) {
            return 3;
        }
        return -1;
    }
    
    // starve if timer is at 5
    public boolean starve() {
        if (timer == 5) {
            return true;
        }
        return false;
    }
}