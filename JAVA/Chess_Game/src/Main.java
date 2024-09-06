/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author 30694
 */

import chessClasses.Board;
import chessClasses.Game;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("######## WELCOME TO CHESS GAME ########");
        
        Game game = new Game();
        
        game.play();
    }
    
}
