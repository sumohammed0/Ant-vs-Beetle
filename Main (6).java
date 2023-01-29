// Name: Safa Mohammed
// Net-id: sxm200175

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws IOException {
		//int lineNum;
		Creature[][] grid = new Creature[10][10];
		char beetleRep;
		char antRep;
		int turns;
		String fileName;
		FileInputStream inputFile = null;
        Scanner scnr = new Scanner(System.in);
        
        // get file name from user 
        System.out.println("Enter file name: ");
        fileName = scnr.next();
        
        // open input file and link scanner
        inputFile = new FileInputStream(fileName);
        
        System.out.println("Enter char to represent ants: ");
        antRep = scnr.next().charAt(0);
        
        System.out.println("Enter char to represent beetles: ");
        beetleRep = scnr.next().charAt(0);
       
		System.out.println("Number of turns: ");
		turns = scnr.nextInt();
		
		createGrid(grid, inputFile, beetleRep, antRep);
	    
		System.out.println();
		
		int turnNum = 1;
		while (turns > 0) {
		    findAntNeighbors(grid);
		    beetleMoves(grid);
		    antMoves(grid);
		    // ants breed every 3 turns
		    if ((turnNum % 3) == 0) {
		        antsBreed(grid, beetleRep, antRep);
		    }
		    // check if beetle should starve turn 5 and onwards
		    if (turnNum >= 5) {
		        beetleStarve(grid);
		    }
		    // beetles breed every 8 turns
		    if ((turnNum % 8) == 0) {
		        beetlesBreed(grid, beetleRep, antRep);
		    }
		    // print out turn
		    System.out.println("TURN " + turnNum);
		    printGrid(grid, beetleRep, antRep);
		    System.out.println();
		    turnNum++;
		    turns--;
		}
	}
	
	// read input file and create grid
	public static void createGrid(Creature[][] gridArray, FileInputStream inFile, char bRep, char aRep) {
	     Scanner infs = new Scanner(inFile);
	     String line;
	     int lineNum = 0;
	     while(infs.hasNext()) {
	         line = infs.nextLine();
	         // take each line and parse character by character
	         for(int x = 0; x < line.length(); x++) {
	             char currentCharacter = line.charAt(x);
	             switch(currentCharacter) {
	                 case ' ': 
	                     gridArray[lineNum][x] = null;
	                     break;
	                 case 'a': // create new ant 
	                     gridArray[lineNum][x] = new Ant();
	                     gridArray[lineNum][x].setRep(aRep);
	                   // add update x and y position if allowed
	                     break;
	                 case 'B': // create new beetle
	                     gridArray[lineNum][x] = new Beetle();
	                     gridArray[lineNum][x].setRep(bRep);
	                       // add update x and y position if allowed
	                     break;
	                       
	                   default:
	                       break;
	                }
	            }
	        
	         lineNum++;
	     }
	     
	}
	
	// print state of board using the chosen characters
	public static void printGrid(Creature[][] gridArray, char bRep, char aRep) {
	    for (int r = 0; r < 10; r++) {
		    for (int c = 0; c < 10; c++) {
		        if (gridArray[r][c] != null && gridArray[r][c].getRep() == bRep) {
		            System.out.print(bRep);
		        }
		        else if (gridArray[r][c] != null && gridArray[r][c].getRep() == aRep) {
		            System.out.print(aRep);
		        }
		        else if (gridArray[r][c] == null) {
		            System.out.print(" ");
		        }
		    }
		    System.out.println();
		}
	}
	
	// calculate number of neighbors for each ant
	public static void findAntNeighbors(Creature[][] gridArray) {
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            // if ant present then find its neighbors
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Ant) {
	                int antNeigbors = 0;
	                if (row != 0 && gridArray[row-1][col] != null && gridArray[row-1][col] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (row != 9 && gridArray[row+1][col] != null && gridArray[row+1][col] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (col != 0 && gridArray[row][col-1] != null && gridArray[row][col-1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (col != 9 && gridArray[row][col+1] != null && gridArray[row][col+1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (row != 0 && col != 0 && gridArray[row-1][col-1] != null && gridArray[row-1][col-1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (row != 9 && col != 9 && gridArray[row+1][col+1] != null && gridArray[row+1][col+1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (row != 9 && col != 0 && gridArray[row+1][col-1] != null && gridArray[row+1][col-1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                if (row != 0 && col != 9 && gridArray[row-1][col+1] != null && gridArray[row-1][col+1] instanceof Ant) {
	                    antNeigbors++;
	                }
	                
	                // store neighbors inside of the ant
	                ((Ant)gridArray[row][col]).setAntNeighbors(antNeigbors); 
	            }
	        }
	    }
	}
	
	// move beetle
	public static void beetleMoves(Creature[][] gridArray) {
	    int[][] distanceArray = new int[4][4];
	    int[] distanceEdgesArray = new int[4];
	    
	    // set all beetles to has not moved for current turn
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Beetle) {
	                ((Beetle)gridArray[row][col]).setMoved(false);
	            }
	        }
	    }
	    // iterate through array 
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            // for each beetle 
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Beetle && !(((Beetle)gridArray[row][col]).getMoved())) {
	                // search for closest ant in north direction 
	                for (int n = row; n >= 0; n--) {
	                    //if (row == 1 && col == 2) {gridArray[0][2] = null;}
	                    if (gridArray[n][col] != null && gridArray[n][col] instanceof Ant) {
	                        distanceArray[0][0] = row - n; // store distance of closest northern ant
	                        distanceArray[1][0] = ((Ant)gridArray[n][col]).getAntNeighbors(); // store number of neighbors for that ant
	                        n = -1;
	                    }
	                    else {distanceArray[0][0] = -1;} // no ant in this direction
	                }
	                // search for closest ant in south direction 
	                for (int s = row; s < 10; s++) {
	                    if (gridArray[s][col] != null && gridArray[s][col] instanceof Ant) {
	                        distanceArray[0][1] = s - row; // store distance of closest southern ant
	                        distanceArray[1][1] = ((Ant)gridArray[s][col]).getAntNeighbors(); // store number of neighbors for that ant
	                        s = 11;
	                    }
	                    else {distanceArray[0][1] = -1;} // no ant in this direction
	                }
	                // search for closest ant in east direction 
	                for (int e = col; e < 10; e++) {
	                    if (gridArray[row][e] != null && gridArray[row][e] instanceof Ant) {
	                        distanceArray[0][2] = e - col; // store distance of closest eastern ant
	                        distanceArray[1][2] = ((Ant)gridArray[row][e]).getAntNeighbors(); // store number of neighbors for that ant
	                        e = 11;
	                    }
	                    else {distanceArray[0][2] = -1;} // no ant in this direction
	                }
	                // search for closest ant in west direction 
	                for (int w = col; w >= 0; w--) {
	                    if (gridArray[row][w] != null && gridArray[row][w] instanceof Ant) {
	                        distanceArray[0][3] = col - w; // store distance of closest western ant
	                        distanceArray[1][3] = ((Ant)gridArray[row][w]).getAntNeighbors(); // store number of neighbors for that ant
	                        w = -1;
	                    }
	                    else {distanceArray[0][3] = -1;} // no ant in this direction
	                }
	                
	                // get direction to move in
	                int direction = ((Beetle)gridArray[row][col]).move(distanceArray);
	                //System.out.println("Direction for this beetle is: " + direction);
	                
	                // north
	                if (direction == 0) {
	                    if (row != 0 && gridArray[row-1][col] instanceof Ant) {
	                        gridArray[row-1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        //((Beetle)gridArray[row-1][col]).setNumEatenAnts(); // increase number of eaten ants by one
	                        ((Beetle)gridArray[row-1][col]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row-1][col]).setTimer(0); // timer reset
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (row != 0 && gridArray[row-1][col] == null) {
	                        gridArray[row-1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Beetle)gridArray[row-1][col]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row-1][col]).setTimer(((((Beetle)gridArray[row-1][col]).getTimer()) + 1)); // timer increase
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (row != 0 && gridArray[row-1][col] != null) { // if beetle can't move due to space being occupied
	                        ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                    }
	                }
	                // south
	                else if (direction == 1) {
	                    if (row != 9 && gridArray[row+1][col] instanceof Ant) {
	                        gridArray[row+1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        //((Beetle)gridArray[row+1][col]).setNumEatenAnts(); // increase number of eaten ants by one
	                        ((Beetle)gridArray[row+1][col]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row+1][col]).setTimer(0); // timer reset
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (row != 9 && gridArray[row+1][col] == null) {
	                        gridArray[row+1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Beetle)gridArray[row+1][col]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row+1][col]).setTimer(((((Beetle)gridArray[row+1][col]).getTimer()) + 1)); // timer increase
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (row != 9 && gridArray[row+1][col] != null){
	                        ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                    }
	                }
	                // east
	                else if (direction == 2) {
	                    if (col != 9 && gridArray[row][col+1] instanceof Ant) {
	                        gridArray[row][col+1] = gridArray[row][col]; // assign new array position with the beetle
	                        //((Beetle)gridArray[row][col+1]).setNumEatenAnts(); // increase number of eaten ants by one
	                        ((Beetle)gridArray[row][col+1]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row][col+1]).setTimer(0); // timer reset
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (col != 9 && gridArray[row][col+1] == null) {
	                        gridArray[row][col+1] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Beetle)gridArray[row][col+1]).setTimer(((((Beetle)gridArray[row][col+1]).getTimer()) + 1)); // timer increase
	                        ((Beetle)gridArray[row][col+1]).setMoved(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (col != 9 && gridArray[row][col+1] != null) {
	                        ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                    }
	                }
	                // west
	                else if (direction == 3) {
	                    if (col != 0 && gridArray[row][col-1] instanceof Ant) {
	                        gridArray[row][col-1] = gridArray[row][col]; // assign new array position with the beetle
	                        //((Beetle)gridArray[row][col-1]).setNumEatenAnts(); // increase number of eaten ants by one
	                        ((Beetle)gridArray[row][col-1]).setMoved(true); // indicate beetle has already moved this turn
	                        ((Beetle)gridArray[row][col-1]).setTimer(0); // timer reset
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (col != 0 && gridArray[row][col-1] == null) {
	                        gridArray[row][col-1] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Beetle)gridArray[row][col-1]).setTimer(((((Beetle)gridArray[row][col-1]).getTimer()) + 1)); // timer increase
	                        ((Beetle)gridArray[row][col-1]).setMoved(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                    else if (col != 0 && gridArray[row][col-1] != null) {
	                        ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1));
	                    }
	                }
	                else if (direction == -1) {
	                    // move to furthest edge function
	                    distanceEdgesArray[0] = 0 + row + 1; // distance to northern edge
	                    distanceEdgesArray[1] = 10 - row; // distance to southern edge
	                    distanceEdgesArray[2] = 10 - col; // distance to eastern edge
	                    distanceEdgesArray[3] = 0 + col + 1; // distance to western edge
	                    
	                    int[] tiesEdges = new int[4]; // to store the edge directions that are tied
	                    int furthest = distanceEdgesArray[0]; // closest is first value
	                    
	                    // search for the closest value
	                    for (int dir = 0; dir < 4; dir++) {
	                        if (distanceEdgesArray[dir] > furthest) {
	                            furthest = distanceEdgesArray[dir];
	                        }
	                    }
	                    
	                    // search for any ties
	                    int tieCountEdge = -1;
	                    for (int dir = 0; dir < 4; dir++) {
	                        if (distanceEdgesArray[dir] == furthest) {
	                            tieCountEdge++;
	                            tiesEdges[tieCountEdge] = dir; // stores ties in array
	                        }
	                    }
	                    
	                    if (tieCountEdge == 0) {
	                        // move to furthest edge
	                        if(tiesEdges[tieCountEdge] == 0) { 
	                            // move north
	                            if (row != 0 && gridArray[row-1][col] == null) {
	                                gridArray[row-1][col] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row-1][col]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row-1][col]).setTimer(((((Beetle)gridArray[row-1][col]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	                            }
	                            else if (row != 0 && gridArray[row-1][col] != null) { // can't move
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                        else if (tiesEdges[tieCountEdge] == 1) {
	                            // move south
	                            if (row != 9 &&gridArray[row+1][col] == null) {
	                                gridArray[row+1][col] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row+1][col]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row+1][col]).setTimer(((((Beetle)gridArray[row+1][col]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	                            }
	                            else if (row != 9 && gridArray[row+1][col] != null) { // can't move
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                        else if (tiesEdges[tieCountEdge] == 2) {
	                            // move east
	                            if (col != 9 && gridArray[row][col+1] == null) {
	                                gridArray[row][col+1] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row][col+1]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row][col+1]).setTimer(((((Beetle)gridArray[row][col+1]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	                            }
	                            else if (col != 9 && gridArray[row][col+1] != null) { // can't move
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increasr
	                            }
	                        }
	                        else if (tiesEdges[tieCountEdge] == 3) {
	                            // move west
	                            if (col != 0 && gridArray[row][col-1] == null) {
	                                gridArray[row][col-1] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row][col-1]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row][col-1]).setTimer(((((Beetle)gridArray[row][col-1]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	                            }
	                            else if (col != 0 && gridArray[row][col-1] != null) { // can't move
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                    }
	                    
	                    else { // if furthst edge has tie
	                        int edgeDirection = edgeTiedDirectionToMove(tieCountEdge, tiesEdges);
	                        
	                        if (edgeDirection == 0) {
	                            if (row != 0 && gridArray[row-1][col] == null) {
	                                gridArray[row-1][col] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row-1][col]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row-1][col]).setTimer(((((Beetle)gridArray[row-1][col]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	   
	                            }
	                            else if (row != 0 && gridArray[row-1][col] != null) { 
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                        
	                        else if (edgeDirection == 1) {
	                            if (row != 9 && gridArray[row+1][col] == null) {
	                                gridArray[row+1][col] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row+1][col]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row+1][col]).setTimer(((((Beetle)gridArray[row+1][col]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	   
	                            }
	                            else if (row != 9 && gridArray[row+1][col] != null) {
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                        else if (edgeDirection == 2) {
	                            if (col != 9 && gridArray[row][col+1] == null) {
	                                gridArray[row][col+1] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row][col+1]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row][col+1]).setTimer(((((Beetle)gridArray[row][col+1]).getTimer()) + 1)); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	   
	                            }
	                            else if (col != 9 && gridArray[row][col+1] != null) {
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                        else if (edgeDirection == 3) {
	                            if (col != 0 && gridArray[row][col-1] == null) {
	                                gridArray[row][col-1] = gridArray[row][col]; // assign new array position with the beetle
	                                ((Beetle)gridArray[row][col-1]).setMoved(true); // indicate beetle has already moved this turn
	                                ((Beetle)gridArray[row][col-1]).setTimer((((Beetle)gridArray[row][col-1]).getTimer()) + 1); // timer increase
	                                gridArray[row][col] = null; // set previous space to empty
	   
	                            }
	                            else if (col != 0 && gridArray[row][col-1] != null) {
	                                ((Beetle)gridArray[row][col]).setTimer(((((Beetle)gridArray[row][col]).getTimer()) + 1)); // timer increase
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	}
	
	// if beetle has tied edges move in order of N, E, S, W 
	public static int edgeTiedDirectionToMove(int tce, int[] te) {
	    for(int t = 0; t < tce + 1; t++) {
	        if (te[t] == 0) {
	            return 0;
	        }
	    }
	    for(int t = 0; t < tce + 1; t++) {
	        if (te[t] == 2) {
	            return 2;
	        }
	    }
	    for(int t = 0; t < tce + 1; t++) {
	        if (te[t] == 1) {
	            return 1;
	        }
	    }
	    for(int t = 0; t < tce + 1; t++) {
	        if (te[t] == 3) {
	            return 3;
	        }
	    }
	    return -1;
	}
	
	// move ant
	public static void antMoves (Creature[][] gridArray) {
	    int[][] distanceArrayAnt = new int[4][4];
	    // set all ants to has not moved for current turn
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Ant) {
	                ((Ant)gridArray[row][col]).setMovedA(false);
	            }
	        }
	    }
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            // for each ant 
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Ant && !(((Ant)gridArray[row][col]).getMovedA())) {
	                // search for closest beetle in north direction 
	                for (int n = row; n >= 0; n--) {
	                    if (gridArray[n][col] != null && gridArray[n][col] instanceof Beetle) {
	                        distanceArrayAnt[0][0] = row - n; // store distance of closest northern beetle
	                        n = -1;
	                    }
	                    else {distanceArrayAnt[0][0] = -1;} // no beetle in this direction
	                }
	                // search for closest beetle in south direction 
	                for (int s = row; s < 10; s++) {
	                    if (gridArray[s][col] != null && gridArray[s][col] instanceof Beetle) {
	                        distanceArrayAnt[0][1] = s - row; // store distance of closest southern beetle
	                        s = 11;
	                    }
	                    else {distanceArrayAnt[0][1] = -1;} // no beetle in this direction
	                }
	                // search for closest beetle in east direction 
	                for (int e = col; e < 10; e++) {
	                    if (gridArray[row][e] != null && gridArray[row][e] instanceof Beetle) {
	                        distanceArrayAnt[0][2] = e - col; // store distance of closest eastern beetle
	                        e = 11;
	                    }
	                    else {distanceArrayAnt[0][2] = -1;} // no beetle in this direction
	                }
	                // search for closest beetle in west direction 
	                for (int w = col; w >= 0; w--) {
	                    if (gridArray[row][w] != null && gridArray[row][w] instanceof Beetle) {
	                        distanceArrayAnt[0][3] = col - w; // store distance of closest western beetle
	                        w = -1;
	                    }
	                    else {distanceArrayAnt[0][3] = -1;} // no beetle in this direction
	                }
	                
	                int directionAnt = ((Ant)gridArray[row][col]).move(distanceArrayAnt);
	                
	                if (directionAnt == 0) {
	                    if (row != 0 && gridArray[row-1][col] == null) {
	                        gridArray[row-1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Ant)gridArray[row-1][col]).setMovedA(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                }
	                // south
	                else if (directionAnt == 1) {
	                    if (row != 9 && gridArray[row+1][col] == null) {
	                        gridArray[row+1][col] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Ant)gridArray[row+1][col]).setMovedA(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                }
	                // east
	                else if (directionAnt == 2) {
	                    if (col != 9 && gridArray[row][col+1] == null) {
	                        gridArray[row][col+1] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Ant)gridArray[row][col+1]).setMovedA(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                }
	                // west
	                else if (directionAnt == 3) {
	                    if (col != 0 && gridArray[row][col-1] == null) {
	                        gridArray[row][col-1] = gridArray[row][col]; // assign new array position with the beetle
	                        ((Ant)gridArray[row][col-1]).setMovedA(true); // indicate beetle has already moved this turn
	                        gridArray[row][col] = null; // set previous space to empty
	                    }
	                }
	                
	            }
	        }
	    }
	}
	
	public static void antsBreed(Creature[][] gridArray, char bRep, char aRep) {
	    boolean[] isEmptyDir = new boolean[4];
	    
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Ant) {
	                ((Ant)gridArray[row][col]).setIsNewA(false);
	            }
	        }
	    }
	    
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            // for each ant 
	            isEmptyDir[0] = false;
	            isEmptyDir[1] = false;
	            isEmptyDir[2] = false;
	            isEmptyDir[3] = false;
	            
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Ant && !(((Ant)gridArray[row][col]).getIsNewA())) {
	                // check if north space is empty
	                if(row != 0 && gridArray[row-1][col] == null) {
	                    isEmptyDir[0] = true;
	                }
	                // check if south space is empty
	                if(row != 9 && gridArray[row+1][col] == null) {
	                    isEmptyDir[1] = true;
	                }
	                // check if east space is empty
	                if(col != 9 && gridArray[row][col+1] == null) {
	                    isEmptyDir[2] = true;
	                }
	                // check if west space is empty
	                if(col != 0 && gridArray[row][col-1] == null) {
	                    isEmptyDir[3] = true;
	                }
	                
	                int directionToBreed = (((Ant)gridArray[row][col]).breed(isEmptyDir));
	                
	                    if (directionToBreed == 0) {
	                        // add ant above
	                        if (row != 0 && gridArray[row-1][col] == null) {
	                            gridArray[row-1][col] = new Ant();
	                            ((Ant)gridArray[row-1][col]).setIsNewA(true);
	                            gridArray[row-1][col].setRep(aRep);
	                        }
	                    }
	                    else if (directionToBreed == 1) {
	                        // add ant below
	                        if (row != 9 && gridArray[row+1][col] == null) {
	                            gridArray[row+1][col] = new Ant();
	                            ((Ant)gridArray[row+1][col]).setIsNewA(true);
	                            gridArray[row+1][col].setRep(aRep);
	                        }
	                    }
	                    else if (directionToBreed == 2) {
	                        // add ant to right
	                        if (col != 9 && gridArray[row][col+1] == null) {
	                            gridArray[row][col+1] = new Ant();
	                            ((Ant)gridArray[row][col+1]).setIsNewA(true);
	                            gridArray[row][col+1].setRep(aRep);
	                        }
	                    }
	                    else if (directionToBreed == 3) {
	                        // add ant to left
	                        if (col != 0 && gridArray[row][col-1] == null) {
	                            gridArray[row][col-1] = new Ant();
	                            ((Ant)gridArray[row][col-1]).setIsNewA(true);
	                            gridArray[row][col-1].setRep(aRep);
	                        }
	                    }
	            }
	        }
	    }
	}
	
	// beetles starve
	public static void beetleStarve(Creature[][] gridArray) {
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Beetle) {
	                boolean shouldStarve = (((Beetle)gridArray[row][col]).starve());
	                if (shouldStarve) {
	                    gridArray[row][col] = null;
	                }
	            }
	        }
	    }
	}
	
	public static void beetlesBreed(Creature[][] gridArray, char bRep, char aRep) {
	    boolean[] isEmptyDir = new boolean[4];
	    
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Beetle) {
	                ((Beetle)gridArray[row][col]).setIsNewB(false);
	            }
	        }
	    }
	    
	    for (int col = 0; col < 10; col++) {
	        for(int row = 0; row < 10; row++) {
	            // for each ant 
	            isEmptyDir[0] = false;
	            isEmptyDir[1] = false;
	            isEmptyDir[2] = false;
	            isEmptyDir[3] = false;
	            
	            if (gridArray[row][col] != null && gridArray[row][col] instanceof Beetle && !(((Beetle)gridArray[row][col]).getIsNewB())) {
	                // check if north space is empty
	                if(row != 0 && gridArray[row-1][col] == null) {
	                    isEmptyDir[0] = true;
	                }
	                // check if south space is empty
	                if(row != 9 && gridArray[row+1][col] == null) {
	                    isEmptyDir[1] = true;
	                }
	                // check if east space is empty
	                if(col != 9 && gridArray[row][col+1] == null) {
	                    isEmptyDir[2] = true;
	                }
	                // check if west space is empty
	                if(col != 0 && gridArray[row][col-1] == null) {
	                    isEmptyDir[3] = true;
	                }
	                
	                int directionToBreed = (((Beetle)gridArray[row][col]).breed(isEmptyDir));
	                
	                    if (directionToBreed == 0) {
	                        // add beetle above
	                        if (row != 0 && gridArray[row-1][col] == null) {
	                            gridArray[row-1][col] = new Beetle();
	                            ((Beetle)gridArray[row-1][col]).setIsNewB(true);
	                            //((Beetle)gridArray[row-1][col]).setTimer(0);
	                            gridArray[row-1][col].setRep(bRep);
	                        }
	                    }
	                    else if (directionToBreed == 1) {
	                        // add beetle below
	                        if (row != 9 && gridArray[row+1][col] == null) {
	                            gridArray[row+1][col] = new Beetle();
	                            ((Beetle)gridArray[row+1][col]).setIsNewB(true);
	                            gridArray[row+1][col].setRep(bRep);
	                        }
	                    }
	                    else if (directionToBreed == 2) {
	                        // add beetle to right
	                        if (col != 9 && gridArray[row][col+1] == null) {
	                            gridArray[row][col+1] = new Beetle();
	                            ((Beetle)gridArray[row][col+1]).setIsNewB(true);
	                            gridArray[row][col+1].setRep(bRep);
	                        }
	                    }
	                    else if (directionToBreed == 3) {
	                        // add beetle to left
	                        if (col != 0 && gridArray[row][col-1] == null) {
	                            gridArray[row][col-1] = new Beetle();
	                            ((Beetle)gridArray[row][col-1]).setIsNewB(true);
	                            gridArray[row][col-1].setRep(bRep);
	                        }
	                    }
	            }
	        }
	    }
	}
}
