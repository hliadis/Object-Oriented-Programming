package chessClasses;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author 30694
 */
public enum Color {
    
    BLACK, WHITE;
    
    public Color nextColor() {
        
        return ( this == BLACK) ? WHITE : BLACK;
    }
    
}
