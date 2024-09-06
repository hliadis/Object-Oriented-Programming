/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessClasses.pieces;

import chessClasses.Color;
import chessClasses.Location;
import chessClasses.exceptions.InvalidMoveException;
import chessClasses.Board;
/**
 *
 * @author 30694
 */
public class Rook extends Piece {
    
    public Rook(Color color, Location location, Board board) {
        super(color,location,board);
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        
        boolean VerticalCheck = currentLoc.getCol() == newLoc.getCol();
        boolean HorizontalCheck = currentLoc.getRow() == newLoc.getRow();
        
        if(!(VerticalCheck || HorizontalCheck))
            throw new InvalidMoveException("Cannot perform move: Rook cannot move in diagonal direction!");
        // Check Vertical move
        if(VerticalCheck && !this.board.freeVerticalPath(this.location, newLoc)) {
            throw new InvalidMoveException("Cannot perform move: Rook cannot move over pieces!");
        } 
        
        // Check Horizontal move
        if(HorizontalCheck && !this.board.freeHorizontalPath(this.location, newLoc)) {
            throw new InvalidMoveException("Cannot perform move: Rook cannot move over pieces!");
        }
        
        Piece pieceAtnewLoc = this.board.getPieceAt(newLoc);

        if(pieceAtnewLoc != null){

            // Check color of piece exists at newLoc
            if(this.board.isOpponentPiece(this, pieceAtnewLoc)){
                this.board.movePieceCapturing(this.location, newLoc);
                return;

            } else 
                throw new InvalidMoveException("Piece with same color exists at new location!");

        } else { //newLoc has no piece
            this.board.movePiece(this.location, newLoc);
            return;
        }
        
        
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "R" : "r";
    } 
}

