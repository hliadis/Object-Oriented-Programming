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
public class Knight extends Piece {
    
    private String exceptionMsg;
    
    public Knight(Color color, Location location, Board board) {
        super(color,location,board);
        this.exceptionMsg = "";
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        
        int DiffByRow = Math.abs(currentLoc.getRow() - newLoc.getRow());
        int DiffByCol = Math.abs(currentLoc.getCol() - newLoc.getCol());
        
        if( (DiffByRow == 2 && DiffByCol == 1) || (DiffByRow == 1 && DiffByCol == 2) ) {

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
        
        } else { // Invalid move according to assigned moves
            this.exceptionMsg = "Invalid move for Knight: must move in an L-shape pattern";
        }
        
        throw new InvalidMoveException(this.exceptionMsg);
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "N" : "n";
    } 
}
