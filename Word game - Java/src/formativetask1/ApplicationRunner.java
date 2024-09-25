
package formativetask1;

import java.io.*;
import java.util.*;
public class ApplicationRunner {
    
    /*Created a set of global variables that can only be accessed within current
    class:*/
    
    // word played by user
    private static String playerWord = null;
    // Scanner object
    private static Scanner input = null;
    // stores the value of word, resets after each turn
    private static int wordTotal = 0;
    // everytime a word is played and valid, wordTotal is added to gameTotal
    private static int gameTotal = 0;
    // stores the lastLetter of the previous word played, has to be beginning 
    // of next word to be played
    private static char lastLetter = '0';
    // keep track of who's turn it is, increases by 1 after each word played
    private static int turns = 1;
    // stores the words played and validated in the ArrayList
    private static ArrayList<String> usedWords = new ArrayList<>();
    // becomes true when word input is valid, resets to false at end of turn
    private static boolean inputValidated = false;
    
    public static void main(String[] args){
        
        String headOfTheTable = 
            "------------------------------------------------------------\n" +
            String.format("| %-21s|%16s |%16s |\n", "word", "word total", "running total") +
            "------------------------------------------------------------\n";
        
        // string to store table lines
        String tableLines = ""; 
        
        // path to the file
        String dataFile = System.getProperty("user.dir") + File.separator + "datafile.txt";
        
        // declare and initialize a file object using the path
        File fileInput = new File(dataFile);
        // declare and initialize a Scanner object
        
        // obtain the array of data which contain all the words in the file
        String[] data = getArrayOfWords(fileInput);
        
        while(gameTotal < 200) {
            // read data from user
            promptUser(data);
            
            // when input is valid
            if(inputValidated){
                // add the score of a word to the total
                gameTotal += wordTotal;
                // adds the new line to the table
                tableLines += printComputedLine();
                // print table and lines
                System.out.println(headOfTheTable + tableLines);
                // method changes variables values accordingly
                advanceGame();
            }
        }
        
        // determine who won
        determineWinner();
    }
    
    // reads the words from file, puts them into an array
    public static String[] getArrayOfWords(File fileToRead){
        //obtain how many words are in the file
        int getLengthArray = getNumberOfElements(fileToRead);
        
        String[] arrayOfWords = new String[getLengthArray];
        // used as index to refer the array
        
        try{
            input = new Scanner(fileToRead);
            
            int i = 0;
            while(input.hasNext()) {
                arrayOfWords[i] = input.nextLine().trim();
                i++;
            }
        } 
        catch (FileNotFoundException fnf) {
            System.out.println("File not found ...");
            System.exit(0);
        } 
        finally {
            // always close the input
            input.close();
        }
        
        return arrayOfWords;
    }
    
    /*A method to get how many elements are in the text file, considering
    there is one element per line, for every line read, increases the count by
    one, and when there are no more lines, it returns count. Takes as argument
    the path to the file.*/
    public static int getNumberOfElements(File fileToRead){
        int count = 0;
        
        try{
            input = new Scanner(fileToRead);
            
            while(input.hasNext()) {
                input.nextLine();
                count++;
            }
        } 
        catch (FileNotFoundException fnf) {
            System.out.println("File not found ...");
            System.exit(0);
        } 
        finally {
            input.close();
        }
        
        return count;
    }
    
    /* method calls other methods in a relevant order to be executed, is being
     called in promptUser method */
    public static void checkWordValidity(String[] data){ 
        // checks if the player gives up
        gaveUp();
        // set the total score of a word in global variable
        setWordTotal();
        // test if word entry is correct
        invalidInput(data);
        // checks if word entry was used
        wasItUsed (data);
    }
    
    /* Checks if the input is "*", when a player gives up, displays relevant
    messages. */
    public static void gaveUp(){
        String gaveUpSymbol = "*";
        if (playerWord.equals(gaveUpSymbol)){
            System.out.println(((turns % 2 == 0) ? "Player 2 " : "Player 1 ") + 
                    "gave up.");
            System.out.println(((turns % 2 != 0) ? "Player 2 " : "Player 1 ") + 
                    "wins the game.");
            
            System.exit(0);
        }
    }
    
    // Sets the total of numerical values of chars from word entry
    public static void setWordTotal(){
        char characterOfString;
        
        for(int i = 0; i < playerWord.length(); i++){
            characterOfString = playerWord.charAt(i);
            wordTotal += getNumericalValue(characterOfString);
        }
    }
    
    /* method called to catch unwanted inputs, if any evaluates true, returns
    message and prompt user again, takes argument Array of valide words that is
    used to check if the word exists */
    public static void invalidInput(String[] data){
        // check correctitude of length
        if (playerWord.length() == 3){
            for (int i = 0; i < playerWord.length(); i++){
                char characterToCheck = playerWord.charAt(i);
                // check word for capital letters
                if (characterToCheck >= 'A' && characterToCheck <= 'Z'){
                    System.out.println("No capitals allowed, try again!");
                    wordTotal = 0;
                    promptUser(data);
                }
            }
            
            // check in against data array if word exists
            for(int i = 0; i <= data.length; i++){
                
                if(i == data.length) {
                    System.out.println("That is no word, try again!");
                    wordTotal = 0;
                    promptUser(data);
                }
                // if exists, set true in global variable and break loop
                else if(playerWord.equals(data[i])){
                    inputValidated = true;
                    break;
                }
            }
            
             /* case where is not the first turn and the word doesn't begin with 
            previous word's ending letter*/
            if (turns != 1 && lastLetter != playerWord.charAt(0)){
                System.out.println("The word " + "\"" + playerWord + "\"" + " does not " +
                "begin with " + lastLetter + ". Try again!");
                wordTotal = 0;
                promptUser(data);
            }
            /*case where is the first turn, and word's value has to be lower 
            than 20*/
            else if (turns == 1 && wordTotal > 20){
                System.out.println("The word " + "\"" + playerWord + "\"" + " has a value " +
                "greater than 20. Try again!");
                wordTotal = 0;
                promptUser(data);
            }
        }
         // when length isn't right
        else {
            System.out.println("Review your word, is not three letter long.");
            wordTotal = 0;
            promptUser(data);
        }
    }
    
    /** A method that returns the user input to main method, checks length
     * if everything valid, saves last letter into the relevant global variable*/
    public static void promptUser(String[] data){
        // method determines who's turn it is, displays messages
        determineTurn();
        
        // initialize input to read user data
        input = new Scanner(System.in);
        // reading user data using trim to avoid unwanted whitespaces
        // set it globally in playerWord
        playerWord = input.nextLine().trim();
        // check input, passing it the array of valide words
        checkWordValidity(data);
        
    }
    
    // Checks who's turn it is and displays relevant messages
    public static void determineTurn(){
        if (turns > 1){
            System.out.println(((turns % 2 == 0)? "Player 2 ":"Player 1 ") + 
                               "to choose the next word ...");
            System.out.print("Enter a 3-letter word (lower case), starting with " +
                               "the letter " + lastLetter + ", or enter * to give up " +
                               "> ");
            }
            else{
                System.out.println("Let's play ...");
                System.out.println("Player 1 to choose the first word ...");
                System.out.print("Enter a 3-letter word (lower case), which must " +
                                 "have a value of 20 or less, or enter * to give up " +
                                 "> ");
            }
    }
    
    /* Checks if word entry was used, display message and prompts user for 
    another word, takes as argument the array of valide words and passes it 
    to promptUser method */
    public static void wasItUsed (String[] data){
        for(int i = 0; i < usedWords.size(); i++){
            if (playerWord.equals(usedWords.get(i))){
                System.out.println("The word was already used, try again!");
                promptUser(data);
            }
        }
    }
    
    /* Returns a line of the table to the main method where is being called */
    public static String printComputedLine(){
        
        String result;
        /* is calling getWordString to format word entry in order to display 
        in order to display the mathematical operation performed for score */
        result = String.format("| %-21s|%16d |%16d |\n", getWordString(), wordTotal, gameTotal);
        result += "------------------------------------------------------------\n";
        
        return result;
    }
    
    /*The method getWordString returns a string that is to be displayed
    in the first column of the table with the mathematical expression in parantheses*/
    public static String getWordString(){
        
        // declare and initialize a result string to store the desired result
        String result = playerWord + " (";
        
        // declare a char variable to store each char of the string
        char characterOfString;
        
        // declare int variable to store the value of a char
        int letterValue;
        
        for(int i = 0; i < playerWord.length(); i++){
            // store the char on index "i" in CharacterOfString 
            characterOfString = playerWord.charAt(i);
            
            // assign value calling method getNumericalValue
            letterValue = getNumericalValue(characterOfString);
            
            /*will add the numerical value to the result string*/
            if (i == playerWord.length() - 1){
                result += letterValue + ")"; // close paranthesis
            }
            // case where is not the last iteration, adds plus symbol after number
            else {
                result += letterValue + " + ";
            }
        }
        
        return result;
    }
    
    /** The method takes a char argument, and returns its numerical value*/
    public static int getNumericalValue(char letter){
        int result = 0;
        // save the value in result
        switch(letter){
            case('a'): result = 1; break;
            case('b'): result = 2; break;
            case('c'): result = 3; break;
            case('d'): result = 4; break;
            case('e'): result = 5; break;   
            case('f'): result = 6; break;
            case('g'): result = 7; break;
            case('h'): result = 8; break;
            case('i'): result = 9; break;
            case('j'): result = 10; break;
            case('k'): result = 11; break;
            case('l'): result = 12; break;
            case('m'): result = 13; break;
            case('n'): result = 14; break;
            case('o'): result = 15; break;
            case('p'): result = 16; break;
            case('q'): result = 17; break;
            case('r'): result = 18; break;
            case('s'): result = 19; break;
            case('t'): result = 20; break;
            case('u'): result = 21; break;
            case('v'): result = 22; break;
            case('w'): result = 23; break;
            case('x'): result = 24; break;
            case('y'): result = 25; break;
            case('z'): result = 26;
        }
        
        // return the numerical value stored in result
        return result;
    }
    
    /* method executed at the end of the iteration to reset certain variables 
    in order to compute the next word entry*/
    public static void advanceGame (){
        // add word to usedWords ArrayList
        usedWords.add(playerWord);
        setLastLetter();
        // reset wordTotal for next entry
        wordTotal = 0; 
        // reset validation
        inputValidated = false;
        // change turns to next player
        turns++;
    }
    
    // sets last letter of the current word input from the user
    public static void setLastLetter(){
        
        // save the length into a variable
        int getLength = playerWord.length();

        if (getLength == 3)
            // sets the lastLetter of the current input
            lastLetter = playerWord.charAt(getLength - 1);
    }
    
    // called when score is greater than 200
    public static void determineWinner(){
        // checks who entered last word, returns plater + lost
        System.out.println("That word takes the total to 200+ ... " +
        ((turns % 2 != 0)? "Player 2":"Player 1") + " loses.");
        
        System.out.println(((turns % 2 == 0)? "Player 2":"Player 1") + 
                " wins the game.");
    }
}