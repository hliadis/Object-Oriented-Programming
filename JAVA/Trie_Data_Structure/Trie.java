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
public class Trie {
    TrieNode root;
    private int numOfWords;
    static final int ALPHABET_SIZE = 26;
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    
    public Trie(String [] words){
        root = new TrieNode(0);
        numOfWords = 0;
        int i;
        
        for(i=0; words[i] != null; i++){
           if(add(words[i])){
               numOfWords++;
           }
        }  
    };
    //This method is inserting words in the Trie data structure 
    //only if they haven't been inserted yet.  
    public boolean add(String word){
        int treeLevel;
        int index;
        TrieNode currentNode = root;
        
        if(contains(word)) 
            return(false);
        
        for(treeLevel=0; treeLevel<word.length(); treeLevel++){
            index = word.charAt(treeLevel) - 'a';
            
            if(currentNode.children[index] == null){
                currentNode.children[index] = new TrieNode(treeLevel + 1);
            }
            
            currentNode = currentNode.children[index];
        }
      
        //this node denotes the end of the word
        currentNode.isTerminal = true;
        
        return(true);
    }
    //This method is searching if a given word exists in the Trie data structure.
    //The return value is true if the word was found, otherwise false.
    public boolean contains(String word){
        int treeLevel;
        int index;
        TrieNode currentNode = root;
        
        for(treeLevel=0; treeLevel<word.length(); treeLevel++){
            index = word.charAt(treeLevel) - 'a';
            
            if(currentNode.children[index] == null)
                return(false);
                
            currentNode = currentNode.children[index];
        }
        
        return(currentNode.isTerminal);
}
    //This method returns the number of words logged in the Trie data structure
    public int size(){
        
        return(numOfWords);
    }
    //This method is invoked by the wordsOfPrefix method.It's traversing the sub-tree Trie determined 
    //by the given parameter root.Whenever this method finds a terminal node, it appends to the buffer
    //the prefix plus the character sequence found recursively in the specific path based on the traversal algorithm
    void prefixTraversal(TrieNode root, StringBuffer buffer, String prefix){
        int i;
        
        if(root.isTerminal){
            buffer.append(prefix);
            buffer.append("!");
        }
        for(i=0; i<ALPHABET_SIZE; i++){
            if(root.children[i] != null){
                prefix += ALPHABET.charAt(i);
                prefixTraversal(root.children[i], buffer, prefix);
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
    }
    //This method is invoked by the differByOne, the differBy and the toString method.Depending on the 
    //diffLimit parameter, it does different implementatios.If the parameter is 0, it implements the Pre-Order
    //traversal algorithm for the Trie, saving each letter that denotes the visited node in the CharSeq.If the
    //parameter is > 0, whenever it finds a terminal node, it's comparing each character of the buffer with
    //each character of the given word.If the words differ by 1,2,..diffLimit characters, it appends the buffer to 
    //the CharSeq.
    void findWordsInTrie (TrieNode node, String buffer, int diffLimit, String word, StringBuffer CharSeq){
        int i;
        int j;
        int counter;
    
        if((node.level > word.length()) && diffLimit != 0){
            return;
        }
        
        if(node.isTerminal == true){
            if(diffLimit != 0 && buffer.length() == word.length()){
                
                for(i=1; i<= diffLimit; i++){ 
                    counter = 0;
                    
                    for(j=0; j<word.length(); j++){
                        if(buffer.charAt(j) != word.charAt(j))
                            counter++;
                    }
                    
                    if(counter == i){
                        CharSeq.append(buffer);
                    }
                }
                
                    return;
            }
            
            if(diffLimit == 0){
                CharSeq.append("!");
            }
        }
                   
        for(i=0; i<ALPHABET_SIZE; i++){
            if(node.children[i] != null){
                if(diffLimit != 0){
                    buffer += ALPHABET.charAt(i);
                
                    findWordsInTrie(node.children[i], buffer, diffLimit, word, CharSeq);
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                
                else{
                    CharSeq.append(" ");
                    CharSeq.append(ALPHABET.charAt(i));
                    findWordsInTrie(node.children[i], buffer, diffLimit, word, CharSeq);
                }
            }
        }
    }
    
    //this method is returning an array of strings which are words from the Trie that differ by one 
    //character with the given word
    public String[] differByOne(String word){
        StringBuffer wordsInTrie = new StringBuffer();
        String[] differByOne = new String[numOfWords];
        String newString;
        int i;
        int pos = 0;
        TrieNode currentNode = root;
        
        findWordsInTrie(currentNode, "", 1, word, wordsInTrie);
        
        newString = wordsInTrie.toString();
        
        for(i=0; i<newString.length(); i = i+word.length()){
            differByOne[pos] = newString.substring(i, i + word.length());
            pos++;
        }
        
        return(differByOne);
    }
    //this methods returns an array of strings which are words that differ by 1 or 2 or until max characters
    //with the given word
    public String[] differBy(String word, int max){
        TrieNode currentNode = root;
        StringBuffer wordsInTrie = new StringBuffer();
        String[] differBy = new String[numOfWords*(max)];
        String newString;
        int i;
        int pos=0;
        
        findWordsInTrie(currentNode, "", max, word, wordsInTrie);
        
        newString = wordsInTrie.toString();
        
        for(i=0; i<newString.length(); i = i+word.length()){
            differBy[pos] = newString.substring(i, i + word.length());
            pos++;
        }
        
        return(differBy);
    }
    //this method returns a string which includes the Pre-Order representation of the Trie.Every terminal 
    //node is followed by a '!' character.
    @Override
    public String toString(){
        StringBuffer PreOrderBuffer = new StringBuffer();
        TrieNode currentNode = root;
        String PreOrderStr;
        
        findWordsInTrie(currentNode, "", 0, "", PreOrderBuffer);
        
        PreOrderStr = PreOrderBuffer.toString();
        
        return(PreOrderStr);
    }
    
    public String[] wordsOfPrefix(String prefix){
       TrieNode currentNode = root;
       int treeLevel;
       int index;
       int i;
       int temp = 0;
       int pos = 0;
       StringBuffer prefixBuffer = new StringBuffer();
       String[] wordsOfprefix = new String[numOfWords];
       String prefixStr;
       
       for(treeLevel=0; treeLevel<prefix.length(); treeLevel++){
            index = prefix.charAt(treeLevel) - 'a';
            
            if(currentNode.children[index] == null)
                return(null);
                
            currentNode = currentNode.children[index];
        }
       
      prefixTraversal(currentNode, prefixBuffer, prefix);
      prefixStr = prefixBuffer.toString();
      
      i = 0;
      
      while(i < prefixStr.length()){
          if(prefixStr.charAt(i) == '!'){
              wordsOfprefix[pos] =  prefixStr.substring(temp, i);
              temp = i + 1;
              pos++;
          }
          i++;
      }
      return(wordsOfprefix);
    }
    //this method is invoked by the toDotString method.It's helping to construct recursively
    //the dot String
    void toDotTraversal(TrieNode node, StringBuffer DotBuffer, int letter){
        int i;
        String helpStr;
        
        if(node != root){
            helpStr = node.hashCode() + " [label=\"" + ALPHABET.charAt(letter) + "\" ,shape=circle, color=";
            
            if(node.isTerminal)
                helpStr += "red]\n";
            
            else helpStr += "black]\n";
            DotBuffer.append(helpStr);
        }
        
        for(i=0; i<ALPHABET_SIZE; i++){
            if(node.children[i] != null){
                helpStr = node.hashCode() + " -- " + node.children[i].hashCode() + "\n";
                DotBuffer.append(helpStr);
                toDotTraversal(node.children[i], DotBuffer, i);
             }
        }
    }
    //this method returns a String that will be transformed into a dot file which 
    //depicts the Trie structure.
    String toDotString(){
        StringBuffer DotBuffer = new StringBuffer("graph Trie {\n");
        TrieNode currentNode = root;
        String DotStr;
    
        DotStr = currentNode.hashCode() + " [label=\"ROOT\" ,shape=circle, color=black]\n";
        DotBuffer.append(DotStr);
        toDotTraversal(currentNode, DotBuffer, 0);
        DotBuffer.append("}");
        
        return(DotBuffer.toString());
    }
}
