/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

/**
 *
 * @author Maria Pantazi
	Ilias Iliadis
 */
public class UnsupportedFileFormatException extends java.lang.Exception{
    
    public UnsupportedFileFormatException(){
        super();
    }
    
    public UnsupportedFileFormatException(String msg){
        super(msg);
    }
}
