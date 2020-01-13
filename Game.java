import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

//todo string to char and char to string, char array to string

public class Game{

	private Random r = new Random();
	private String wrongGuesses = "";
	private int wrongGuesCounter = 0;
	private List<String> data;

	public Game(File file){
		data = new ArrayList<>();
		String word;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((word = reader.readLine()) != null){
				data.add(word);
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//pick a random word from the dictionary
	private String getWord(){
		int rand = r.nextInt(data.size());
		return data.get(rand);
	}

	// generate censored word with each round
	private String generateCensoredWord(String word, char[] guesses){
		char[] wordArray = word.toCharArray();
		char[] censored = new char[wordArray.length];
		
		wrongGuesCounter = 0;
		wrongGuesses = "";

		for (int i = 0; i < wordArray.length; i++){
			censored[i] = "_".charAt(0);
		}
		if (guesses != null) {
			for (int i = 0; i < guesses.length; i++){
				boolean found = false;
				for (int j = 0; j < wordArray.length; j++){
					if (censored[j] == "_".charAt(0) && guesses[i] == wordArray[j]) {
						censored[j] = wordArray[j];
						found = true;
						break;
					}
				}
				if (!found) {
					wrongGuesCounter++;
					wrongGuesses += guesses[i];
				}
			}
		}
		
		return new String(censored);
	}

	// update the char array to be passed in the generateCensoredWord function
	// this helps to carter for array size, primarily
	private char[] updatedGuessArray(int round, char[] guesses){
		char[] tmp = new char[round+1];
		for (int i = 0; i <= round; i++){
			tmp[i] = guesses[i];
		}
		return tmp;
	}

	// instantiating the game
	public void init(){
		System.out.println("Play (1) or Exit (0) > ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		if (input.equals("1")) {
			play();
		}else if (input.equals("0"){
			return;
		}else{
			init();
		}
	}

	// a function to handle the main play session independently
	public void play(){
		String word = getWord();
		char[] guesses = new char[10];
		String censored = generateCensoredWord(word, null);
		for (int i = 0; i < 10; i++){
			System.out.println(censored+"\n"+wrongGuesCounter+" wrong guesses so far ["+ wrongGuesses+"]");
			System.out.println("Have a guess (lower case letter or * to give up) > ");
			Scanner scanner = new Scanner(System.in);
			char input = scanner.nextLine().toLowerCase().charAt(0);
			if (input == "*".charAt(0)) {
				init();
				return;
			}else{
				guesses[i] = input;
				censored = generateCensoredWord(word, updatedGuessArray(i, guesses));
			}
			if (censored.toLowerCase().equals(word.toLowerCase())) {
				break;
			}
		}
		System.out.println(censored+"\n"+wrongGuesCounter+" wrong guesses so far ["+ wrongGuesses+"]");
		if (censored.toLowerCase().equals(word.toLowerCase())) {
			System.out.println("The hidden word was "+word.toUpperCase()+"\nYou win :-D");
		}else{
			System.out.println("The hidden word was "+word.toUpperCase()+"\nYou lose :-(");
		}
		init();
	}
}