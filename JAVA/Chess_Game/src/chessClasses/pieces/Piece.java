/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessClasses.pieces;

import chessClasses.Location;
import chessClasses.exceptions.InvalidMoveException;
import chessClasses.Color;
import chessClasses.Board;

public abstract class Piece {
    
    public final Color color;
    public Location location;
    public final Board board;
    
    public Piece(Color color, Location location, Board board) {
        this.color = color;
        this.location = location;
        this.board = board;
    }
    
    public abstract void moveTo(Location loc) throws InvalidMoveException;
}

