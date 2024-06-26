import java.util.HashMap;
import java.util.Random;

public class LanguageModel {


    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char c;

        In in = new In(fileName);
        // Reads just enough characters to form the first window
        for (int i = 1; i <= windowLength; i++) {
            window += in.readChar();
        }

        // Processes the entire text, one character at a time
        while (!in.isEmpty()) {
            // Gets the next character
            c = in.readChar();
            // Checks if the window is already in the map
            List probs = new List();
            boolean exists = CharDataMap.containsKey(window);
            if (!exists) {
                CharDataMap.put(window, probs);
            }
            // Calculates the counts of the current character.
            CharDataMap.get(window).update(c);

            // Advances the window: adds c to the window’s end, and deletes the
            // window's first character.
            window = window.substring(1);
            window += c;
        }
        // The entire file has been processed, and all the characters have been counted.
        // Proceeds to compute and set the p and cp fields of all the CharData objects
        // in each linked list in the map.
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        // Sets a size of the original input
        int countElements = 0;
        CharData[] arr1 = probs.toArray();
        for (CharData charData : arr1) {
            countElements += charData.count;
        }

        // Sets p and cp fields
        CharData[] arr = probs.toArray();
        arr[0].p = (double) arr[0].count / countElements;
        arr[0].cp = arr[0].p;
        for (int i = 1; i < arr.length; i++) {
            arr[i].p = (double) arr[i].count / countElements;
            arr[i].cp = arr[i].p + arr[i-1].cp;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        CharData[] arr = probs.toArray();
        char c = 0;
        for (CharData charData : arr) {
            if (r < charData.cp) {
                c = charData.chr;
                break;
            }
        }
        return c;
    }

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param textLength - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        if (windowLength > initialText.length()) return initialText;
        else {
            int a = initialText.length() - windowLength;
            int b = initialText.length();
            String window = initialText.substring(a, b);
            StringBuilder generator = new StringBuilder(window);
            while (window.length() < textLength) {
                List curr = CharDataMap.get(window);
                if (curr == null) break;
                char c = getRandomChar(curr);
                generator.append(c);
                window = window.substring(1);
                window += c;
            }
            return generator.toString();
        }
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);

        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
