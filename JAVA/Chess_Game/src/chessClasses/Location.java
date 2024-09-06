package chessClasses;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 30694
 */
import chessClasses.exceptions.InvalidLocationException;

public class Location {
  
    private int row;
    private int col;
    
    public Location (int r, int c) {
        this.row = r;
        this.col = c;
    }
    
    public Location(String loc) throws InvalidLocationException {
        
        // get row and column from location string
        char column = loc.charAt(0);
        char row = loc.charAt(1);
        
        if((row >= '1' && row <= '8') && (column >= 'a' && column <= 'h')) {
            this.col = column - 'a';
            this.row = Integer.parseInt(String.valueOf(row)) - 1;
        
        } else {
            throw new InvalidLocationException("Location out of bounds! Valid inputs: (a-h)(1-8)");
        }
    }
    
    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
    
    @Override
    public String toString() {
        return "" + (char)('a' + this.col) + Integer.toString(this.row + 1);
    }
}
