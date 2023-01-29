public abstract class Creature {
    public char rep;
    
    abstract int move(int[][] distAntArray);
    abstract int breed(boolean[] empty);
    
    public void setRep(char r) {
        rep = r;
    }
    
    public char getRep() {
        return rep;
    }
}