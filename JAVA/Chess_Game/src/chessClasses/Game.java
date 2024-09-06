/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessClasses;
import java.io.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import chessClasses.exceptions.*;
import chessClasses.pieces.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 30694
 */
public class Game {
    
    private Color ColorToMove;
    private Board board;
    private List<String> moveHistory;
    private boolean activeGame;
    
    public Game(){
        this.ColorToMove = Color.WHITE;
        this.board = new Board();
        this.moveHistory = new ArrayList<>();
        this.activeGame = true;
    }
    
    public void play() {
        Scanner scanner = new Scanner(System.in);
        
        while(this.activeGame) {
            try {
                if(this.board.kingRemoved){
                    System.out.println(this.ColorToMove + " KING REMOVED! GAME OVER . . .");
                    break;
                }
                
                System.out.println(this.board.toString());
                System.out.println("Current player: " + this.ColorToMove);
                
                String command = scanner.nextLine();
                if(command.length() == 0){
                    System.out.println("No input given, please type a command: ");
                    continue;
                }
               
                
                switch(command) {
                    case(":x"):
                        this.exitGame();
                        break;
                    case(":h"):
                        this.printHelp();
                        break;
                    case(":o"):
                        this.openGame();
                        break;
                    case(":s"):
                        this.saveGame();
                        break;
                    default:
                        this.handleMove(command);
                }
            
            } catch(InvalidMoveException e) {
                System.out.println(e);
            } catch(InvalidLocationException e) {
                System.out.println(e);
            }
        }
    }
    
    private void handleMove(String command) throws InvalidLocationException, InvalidMoveException {
        try {
            
            if(command.length() != 4)
                throw new InvalidMoveException("move command must have 4 characters and format: e2e4 ");
            String fromStr = command.substring(0, 2);
            String toStr = command.substring(2, 4);
            
            Location from = new Location(fromStr);
            Location to = new Location(toStr);
            
            // Check if given from position is empty
            Piece pieceToMove = this.board.getPieceAt(from);
            if(pieceToMove == null)
                throw new InvalidMoveException("No piece at " + from);
            
            // Check if current player attempts to move opponents piece
            if(pieceToMove.color != this.ColorToMove)
                throw new InvalidMoveException("Not allowed to move opponents pieces! Valid color: " + this.ColorToMove);
            
            // Check if given command doesn't generate a move
            if(from.toString().equals(to.toString()))
                throw new InvalidMoveException("from and to locations match");
            
            // Perform move
            pieceToMove.moveTo(to);
            moveHistory.add(command);
            this.ColorToMove = this.ColorToMove.nextColor();
            
            
        } catch(InvalidLocationException e){
            throw new InvalidLocationException(e.getMessage());
        } catch(InvalidMoveException e){
            throw new InvalidMoveException(e.getMessage());
        }
    }
    public void openGame() {
        Scanner scanner = new Scanner(System.in);
        String response;
        
        if (this.activeGame) {  
            do {
                System.out.println("A game is in progress. Do you want to interrupt it? (yes/no): ");
                response = scanner.nextLine();

                if (response.equalsIgnoreCase("no")) {
                    return;
                } 
            } while(!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no"));
        }

        System.out.println("Enter the filename to load the game from: ");
        String filename = scanner.nextLine();

        try (BufferedReader in = new BufferedReader(new FileReader("" + "savedGames/" + filename))) {
            String line;
            moveHistory.clear();
            board = new Board();
            while ((line = in.readLine()) != null) {
                this.handleMove(line);  // Replay the moves without adding to history
            }
            this.activeGame = true;
            System.out.println("Game successfully loaded from " + filename);
        }
        catch(InvalidPathException ex) {
            System.out.println(ex);
        }
        catch (IOException e) {
            System.out.println("Failed to load the game: " + e.getMessage());
        } 
        catch (InvalidLocationException | InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void exitGame(){
        Scanner scanner = new Scanner(System.in);
        String response;
        
        if (this.activeGame) {  
            do {
                System.out.println("A game is in progress. Do you want to exit? (yes/no): ");
                response = scanner.nextLine();

                if (response.equalsIgnoreCase("yes")) {
                    this.activeGame = false;
                } 
            } while(!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no"));
        }
    }
    
    private void saveGame(){
        
        Scanner scanner = new Scanner(System.in);
        
        try{
            
            String directoryPath = "./savedGames";
            Path dirPath = Paths.get(directoryPath);
            
            Path absolutePath = dirPath.toAbsolutePath();
            
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Create file path
            String filename;
            System.out.println("Enter the filename to save the game: ");
            filename = scanner.nextLine();
            
            Path filePath = dirPath.resolve(filename);

            // Check if the file already exists
            if (Files.exists(filePath)) {
                throw new FileAlreadyExistsException("File " + filename + " already exists in the directory " + directoryPath+"\n");
            }
            
            try(PrintWriter writer = new PrintWriter(new File(filePath.toString()))) {
                for(String line: this.moveHistory) {
                    writer.format("%s\n", line);
                }
            }
            catch(FileNotFoundException ex) {
                System.out.format("Unable to open file '%s' for writting!\n", filename);
            }
            System.out.println("Game saved successfuly!!");
        } catch(FileAlreadyExistsException ex){
            System.out.println(ex);
        } 
        catch(InvalidPathException ex) {
            System.out.println(ex);
        }
        catch(IOException e){
            System.out.println(e);
          }
    }
    
    public void printHelp() {
        try (BufferedReader reader = new BufferedReader(new FileReader("chessInstructions"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}
