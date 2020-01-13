
import java.io.File;

public class ApplicationRunner {

    public static void main(String[] args) {
		System.out.println("Word Guessing Game");
        String dataFile = System.getProperty("user.dir") + File.separator + "wordlist.txt";
        Game game = new Game(new File(dataFile));
        game.init();
    }

}
