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
public class Pawn extends Piece {
    
    private boolean isFirstMove;
    private String exceptionMsg;
    private String direction;
    
    public Pawn(Color color, Location location, Board board) {
        super(color,location,board);
        this.isFirstMove = true;
        this.exceptionMsg = "";
        direction = (this.color == Color.WHITE) ? "up" : "down"; 
    }
    
    @Override
    public void moveTo(Location newLoc) throws InvalidMoveException {
        Location currentLoc = this.location;
        String moveDirection;
        
        int DiffByRow = Math.abs(currentLoc.getRow() - newLoc.getRow());
        int DiffByCol = Math.abs(currentLoc.getCol() - newLoc.getCol());
        moveDirection = (currentLoc.getRow() - newLoc.getRow() < 0) ? "up" : "down";

        // Check first move
        if( this.isFirstMove && DiffByRow > 2) {
            this.exceptionMsg = "Cannot perform move: Pawns first vertical move must be max 2 sqares.";
            throw new InvalidMoveException(this.exceptionMsg);
        }
        
        // Check other vertical moves
        if(!this.isFirstMove && DiffByRow > 1) {
            this.exceptionMsg = "Cannot perform move: Pawns vertical move must be max 1 sqares.";
            throw new InvalidMoveException(this.exceptionMsg);
        } 
        
        if(DiffByRow * DiffByCol > 1){ //Check diagonal moves
            throw new InvalidMoveException("Cannot perform move: Invalid diagonal move for Pawn");
        }
        
        Piece pieceAtnewLoc = this.board.getPieceAt(newLoc);
        
        if(DiffByRow*DiffByCol == 1) { 
            
            // Attempts to remove opposite piece
            if(moveDirection != this.direction)
                throw new InvalidMoveException("Direction of pawn with color " + this.color + " should always be " +this.direction+"wards");
            
            // diagonal move does not target opposite piece
            if(pieceAtnewLoc == null || !this.board.isOpponentPiece(this, pieceAtnewLoc)){
                this.exceptionMsg = "Cannot perform move: Pawn's diagonal move does not target opponent's Piece!";
                throw new InvalidMoveException(this.exceptionMsg);
            }
            
            this.board.movePieceCapturing(this.location, newLoc);
        
        } else { //Vertical movement

            if(moveDirection != this.direction)
                throw new InvalidMoveException("Direction of pawn with color " + this.color + " should always be " +this.direction+"wards");
            
            if(!this.board.freeHorizontalPath(this.location, newLoc) || pieceAtnewLoc != null) {
                this.exceptionMsg = "Cannot perform move: Pawn cannot move over pieces!";
                throw new InvalidMoveException(this.exceptionMsg);
            }
            
            this.board.movePiece(this.location, newLoc);
        }
        
        this.isFirstMove = false;
    }
    
    @Override
    public String toString(){
        return (this.color == Color.WHITE) ? "P" : "p";
    } 
}