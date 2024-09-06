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
public class Bishop extends Piece {
    
    private String exceptionMsg;
    public Bishop(Color color, Location location, Board board) {
        super(color,location,board);
        this.exceptionMsg = "";
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        
        int DiffByRow = Math.abs(currentLoc.getRow() - newLoc.getRow());
        int DiffByCol = Math.abs(currentLoc.getCol() - newLoc.getCol());
        
        // In order for the Bishop's move to be valid it must be diagonal. 
        // Thus: DiffByRow == DiffByCol
        if(DiffByRow == DiffByCol) {
            
            // Check if direction is diagonal or anti-diagonal
            boolean isAntidiagonal = DiffByCol * DiffByRow < 0; 
            
            // Check if Diagonal or Antidiagonal path is free
            if(isAntidiagonal){
                
                if(!this.board.freeAntidiagonalPath(this.location, newLoc)){
                    this.exceptionMsg = "Cannot perform move: Bishop cannot move over pieces!";
                    throw new InvalidMoveException(this.exceptionMsg);
                }
            
            } else { // diagonal move
                
                if(!this.board.freeDiagonalPath(this.location, newLoc)){
                    this.exceptionMsg = "Cannot perform move: Bishop cannot move over pieces!";
                    throw new InvalidMoveException(this.exceptionMsg);
                }
            }
            
            Piece pieceAtnewLoc = this.board.getPieceAt(newLoc);
             
            if(pieceAtnewLoc != null){
                
                // Check color of piece exists at newLoc
                if(this.board.isOpponentPiece(this, pieceAtnewLoc)){
                    this.board.movePieceCapturing(this.location, newLoc);
                    return;
                
                } else 
                    this.exceptionMsg = "Cannot perform move: Piece with same color exists at new location!";
            
            } else { //newLoc has no piece
                this.board.movePiece(this.location, newLoc);
                return;
            }   
        
        }  else { // Invalid move according to assigned moves
            this.exceptionMsg = "Invalid move for Bishop:  must move along diagonals";
        }
        
        throw new InvalidMoveException(this.exceptionMsg);
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "B" : "b";
    } 
}
