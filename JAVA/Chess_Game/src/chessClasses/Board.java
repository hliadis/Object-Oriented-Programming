/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessClasses;

import chessClasses.pieces.*;
/**
 *
 * @author 30694
 */
public class Board {
    
    private final int ROWS = 8;
    private final int COLS = 8;
    public boolean kingRemoved;
    
    private Piece[][] board;
    
    public Board(){
        this.board = new Piece[this.ROWS][this.COLS];
        this.init();
    
    }
    
    public void init(){
       
        // Place pieces for both players, starting from WHITE ones
        for(int i=0; i < 2; i++){
            
            Color color = i == 0 ? Color.WHITE : Color.BLACK;
            // Start with white pieces
            if(color == Color.WHITE){
                this.board[0][0] = new Rook(color, new Location(0,0), this);
                this.board[0][1] = new Knight(color, new Location(0,1), this);
                this.board[0][2] = new Bishop(color, new Location(0,2), this);
                this.board[0][3] = new Queen(color, new Location(0,3), this);
                this.board[0][4] = new King(color, new Location(0,4), this);
                this.board[0][5] = new Bishop(color, new Location(0,5), this);
                this.board[0][6] = new Knight(color, new Location(0,6), this);
                this.board[0][7] = new Rook(color, new Location(0,7), this);
                
                // Initialize white Pawns
                for (int j=0; j < this.COLS; j++){
                    this.board[1][j] = new Pawn(color, new Location(1,j), this);
                }
                    
            }
            
            // Initialize black pieces
            if(color == Color.BLACK){
                this.board[this.ROWS-1][0] = new Rook(color, new Location(this.ROWS-1,0), this);
                this.board[this.ROWS-1][1] = new Knight(color, new Location(this.ROWS-1,1), this);
                this.board[this.ROWS-1][2] = new Bishop(color, new Location(this.ROWS-1,2), this);
                this.board[this.ROWS-1][3] = new Queen(color, new Location(this.ROWS-1,3), this);
                this.board[this.ROWS-1][4] = new King(color, new Location(this.ROWS-1,4), this);
                this.board[this.ROWS-1][5] = new Bishop(color, new Location(this.ROWS-1,5), this);
                this.board[this.ROWS-1][6] = new Knight(color, new Location(this.ROWS-1,6), this);
                this.board[this.ROWS-1][7] = new Rook(color, new Location(this.ROWS-1,7), this);
                
                // Initialize black Pawns
                for (int j=0; j < this.COLS; j++){
                    this.board[this.ROWS-2][j] = new Pawn(color, new Location(this.ROWS-2,j), this);
                }  
            }
        }
        
        // Initialize empty board spaces
        for (int i=2; i < this.ROWS-2; i++) {
            for (int j=0; j < this.COLS; j++) {
                board[i][j] = null;
            }
        }
    }
    
    public Piece getPieceAt(Location loc) {
        return this.board[loc.getRow()][loc.getCol()];
    }
    
    public void movePiece(Location from, Location to) {
        Piece pieceToMove = this.getPieceAt(from);
        
        this.board[from.getRow()][from.getCol()] = null;
        this.board[to.getRow()][to.getCol()] = pieceToMove;
        pieceToMove.location = to;
    }
    
    public void movePieceCapturing(Location from, Location to) {
        
        if(this.getPieceAt(to) instanceof King)
            this.kingRemoved = true;
        
        this.board[to.getRow()][to.getCol()] = null;
        
        this.movePiece(from, to);
    }
    
    public boolean isOpponentPiece(Piece movePiece, Piece dstPiece) {
        return (movePiece.color != dstPiece.color);
    }
    // This method is used to check move of Rook or Queen.
    // It's already confirmed that from->to path is at the same row. 
    public boolean freeHorizontalPath(Location from, Location to) {
        
        boolean pieceDetected = false;
        int startCol = Math.min(from.getCol(), to.getCol());
        int endCol = Math.max(from.getCol(), to.getCol());
        //??? Should we consider the case where an opponent's piece
        // lies between from,to ???
        for(int j=startCol+1; j < endCol; j++){
            if(this.board[to.getRow()][j] != null)
                pieceDetected = true;
        }
        
        return !pieceDetected;
    }
    
    // This method is used to check move of Rook or Queen.
    // It's already confirmed that from->to path is at the same col. 
    public boolean freeVerticalPath(Location from, Location to) {
        
        boolean pieceDetected = false;
        int startRow = Math.min(from.getRow(), to.getRow());
        int endRow = Math.max(from.getRow(), to.getRow());
        //??? Should we consider the case where an opponent's piece
        // lies between from,to ???
        for(int i=startRow+1; i < endRow; i++){
            if(this.board[i][to.getCol()] != null)
                pieceDetected = true;
        }
        
        return !pieceDetected;
    }
    
    // This method is used to check move of Rook or Queen.
    // It's already confirmed that from->to path is at the same diagonal. 
    public boolean freeDiagonalPath(Location from, Location to) {
        
        boolean pieceDetected = false;
        int rowDir = (from.getRow() > to.getRow()) ? -1 : 1;
        int colDir = (from.getCol() > to.getCol()) ? -1 : 1;
        
        int curRow = from.getRow() + rowDir;
        int curCol = from.getCol() + colDir;
        
        while(curRow != to.getRow() && curCol != to.getCol()){
        
            if(this.board[curRow][curCol] != null)
                pieceDetected = true;
            
            curRow += rowDir;
            curCol += colDir;
        }
        
        return !pieceDetected;
    }

    // This method is used to check move of Rook or Queen.
    // It's already confirmed that from->to path is at the same anti-diagonal. 
    public boolean freeAntidiagonalPath(Location from, Location to) {
        
        boolean pieceDetected = false;
        int rowDir = (from.getRow() > to.getRow()) ? -1 : 1;
        int colDir = (from.getCol() > to.getCol()) ? 1 : -1;
        
        int curRow = from.getRow() + rowDir;
        int curCol = from.getCol() + colDir;
        
        while(curRow != to.getRow() && curCol != to.getCol()){
        
            if(this.board[curRow][curCol] != null)
                pieceDetected = true;
            
            curRow += rowDir;
            curCol += colDir;
        }
        
        return !pieceDetected;
    }
    
    @Override
    public String toString() {
    
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n       ").append(" abcdefgh \n");
        
        for(int i = this.ROWS-1; i >= 0; i--){
            
            buffer.append("       ").append(Integer.toString(i + 1));
            
            for(int j=0; j < this.COLS; j++){
                buffer.append((this.board[i][j] == null)? " " : board[i][j].toString());
            }
            
            buffer.append(Integer.toString(i + 1)).append('\n');
        }
        
        buffer.append("       ").append(" abcdefgh \n");
        
        return buffer.toString();
    }
    
}
