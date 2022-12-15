package ce326.hw1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hlias
 */
public class TrieNode {
    static final int ALPHABET_SIZE = 26;
    TrieNode children [] = new TrieNode[ALPHABET_SIZE];
    boolean isTerminal;
    int level;
    
    public TrieNode(int initLevel){
        int i;
        level = initLevel;
        isTerminal = false;
        
        for(i=0; i<ALPHABET_SIZE; i++){
        children[i] = null;
        }
        
    };
    
}
