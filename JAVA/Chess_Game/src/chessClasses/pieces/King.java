/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessClasses.pieces;

import chessClasses.Location;
import chessClasses.Board;
import chessClasses.Color;
import chessClasses.exceptions.InvalidMoveException;

public class King extends Piece {
    
    private String exceptionMsg;
    public King(Color color, Location location, Board board) {
        super(color,location,board);
        this.exceptionMsg = "";
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        
        int DiffByRow = Math.abs(currentLoc.getRow() - newLoc.getRow());
        int DiffByCol = Math.abs(currentLoc.getCol() - newLoc.getCol());
       
        
        if(DiffByRow < 2 && DiffByCol < 2)  {
            Piece pieceAtnewLoc = this.board.getPieceAt(newLoc);
            
            if(pieceAtnewLoc != null){
                if(this.board.isOpponentPiece(this, pieceAtnewLoc)){
                    this.board.movePieceCapturing(this.location, newLoc);
                    return;
                
                } else 
                    this.exceptionMsg = "Cannot perform move: Piece with same color exists at new location!";
            
            } else { 
                this.board.movePiece(this.location, newLoc);
                return;
            }
            
        } else {
            this.exceptionMsg = "Invalid move for King: cannot move more than one square";
        }
        
        throw new InvalidMoveException(this.exceptionMsg);
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "K" : "k";
    }
}
