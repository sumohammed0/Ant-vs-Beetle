public class Ant extends Creature {
    protected int currentPositionX;
    protected int currentPositionY;
    protected int numNeighbors;
    protected boolean hasMovedA = false;
    protected boolean isNewA = false;
   
    @Override
    public int breed(boolean[] empty) {
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
        
    @Override
    public int move(int[][] distAntArray) {
        int[] ties = new int[4];
        int[] tiesFurthest = new int[4];
        int closest = 10;
        boolean northEmpty = false;
        boolean southEmpty = false;
        boolean eastEmpty = false;
        boolean westEmpty = false;
        int furthest = 0;
        
        // find the closest distance
        for (int dir = 0; dir < 4; dir++) {
            if (distAntArray[0][dir] != -1 && distAntArray[0][dir] < closest) {
                closest = distAntArray[0][dir];
            }
        }
        
        // find any ties
        int tieCount = -1;
        for (int dir = 0; dir < 4; dir++) {
            if (distAntArray[0][dir] != -1 && distAntArray[0][dir] == closest) {
                tieCount++;
                ties[tieCount] = dir;
            }
        }
        
        if (tieCount == 0) {
            // return opposite direction to closest beetle
            if (ties[tieCount] == 0) {return 1;}
            else if (ties[tieCount] == 1) {return 0;}
            else if (ties[tieCount] == 2) {return 3;}
            else if (ties[tieCount] == 3) {return 2;}
        } 
        // if tied then move where there's no beetle
        else {
            for (int t = 0; t < tieCount + 1; t++) {
                if (ties[t] != 0 && distAntArray[0][0] == -1) { // if none of the tied directions are north then north is empty
                    northEmpty = true;
                }
                if (ties[t] != 1 && distAntArray[0][1] == -1) { // if none of the tied directions are south then south is empty
                    southEmpty = true;
                }
                if (ties[t] != 2 && distAntArray[0][2] == -1) { // if none of the tied directions are east then east is empty
                    eastEmpty = true;
                }
                if (ties[t] != 3 && distAntArray[0][3] == -1) { // if none of the tied directions are west then west is empty
                    westEmpty = true;
                }
            }
            
            if (northEmpty) {return 0;}
            else if (eastEmpty) {return 2;}
            else if (southEmpty) {return 1;}
            else if (westEmpty) {return 3;}
            else {
                // search for distance of furthest beetle
                for (int dir = 0; dir < 4; dir++) {
                    if (distAntArray[0][dir] > furthest) {
                        furthest = distAntArray[0][dir];
                    }
                }
                // search for ties 
                int tieCountF = -1;
                for (int dir = 0; dir < 4; dir++) {
                    if (distAntArray[0][dir] != -1 && distAntArray[0][dir] == furthest) {
                        tieCountF++;
                        tiesFurthest[tieCountF] = dir;
                    }
                }
                
                // return furthest direction
                if (tieCountF == 0) {
                    if (tiesFurthest[tieCountF] == 0) {return 0;}
                    else if (tiesFurthest[tieCountF] == 1) {return 1;}
                    else if (tiesFurthest[tieCountF] == 2) {return 2;}
                    else if (tiesFurthest[tieCountF] == 3) {return 3;}
                }
                else { // if tied then return direction in order of NESW; n = 0 , s = 1, e = 2, w = 3
                    for(int t = 0; t < tieCountF + 1; t++) {
                        if (tiesFurthest[t] == 0) {
                            return 0;
                        }
                    }
                    
                    for(int t = 0; t < tieCountF + 1; t++) {
                        if (tiesFurthest[t] == 2) {
                            return 2;
                        }
                    }
                    
                    for(int t = 0; t < tieCountF + 1; t++) {
                        if (tiesFurthest[t] == 1) {
                            return 1;
                        }
                    }
                    
                    for(int t = 0; t < tieCountF + 1; t++) {
                        if (tiesFurthest[t] == 3) {
                            return 3;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    // setters
    public void setAntNeighbors(int n) {
        numNeighbors = n;
    }
    public void setMovedA(boolean m) {
        hasMovedA = m;
    }
    public void setIsNewA(boolean a) {
        isNewA = a;
    }
    
    // getters
    public int getAntNeighbors() {
        return numNeighbors;
    }
    
    public boolean getMovedA() {
        return hasMovedA;
    }
    
    public boolean getIsNewA() {
        return isNewA;
    }
}