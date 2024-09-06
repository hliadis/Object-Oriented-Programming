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
public class Queen extends Piece {
    
    public Queen(Color color, Location location, Board board) {
        super(color,location,board);
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        
        boolean VerticalCheck = currentLoc.getCol() == newLoc.getCol();
        boolean HorizontalCheck = currentLoc.getRow() == newLoc.getRow();
        int DiffByRow = Math.abs(currentLoc.getRow() - newLoc.getRow());
        int DiffByCol = Math.abs(currentLoc.getCol() - newLoc.getCol());
        boolean DiagCheck = DiffByRow == DiffByCol;
        
        // Check and validate type of Queens move: horizontal, vertical or diagonal.
        if(!(VerticalCheck || HorizontalCheck || DiagCheck))
            throw new InvalidMoveException("Cannot perform move: Queen cannot move like Knight!");
        if(DiagCheck) {
            boolean isAntidiagonal = DiffByCol * DiffByRow < 0; 
            
            // Check if Diagonal or Antidiagonal path is free
            if(isAntidiagonal){
                
                if(!this.board.freeAntidiagonalPath(this.location, newLoc)){
                    throw new InvalidMoveException("Cannot perform move: Queen cannot move over pieces!");
                }
            
            } else { // diagonal move
                
                if(!this.board.freeDiagonalPath(this.location, newLoc)){
                    throw new InvalidMoveException("Cannot perform move: Queen cannot move over pieces!");
                }
            }
        } 
        
        if(HorizontalCheck && !this.board.freeHorizontalPath(this.location, newLoc)) {
            throw new InvalidMoveException("Cannot perform move: Queen cannot move over pieces!");
        }
        
        if(VerticalCheck && !this.board.freeVerticalPath(this.location, newLoc)) {
            throw new InvalidMoveException("Cannot perform move: Queen cannot move over pieces!");
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
            
            
            
        //} else {
            //throw new InvalidMoveException("Invalid move for piece: " + this.toString());
        //}
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "Q" : "q";
    } 
}

