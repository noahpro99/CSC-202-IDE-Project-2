import java.lang.*;

class Main {

  public static void main(String[] args) {
    // Main program loop which runs a test on each pattern and text combo
    String Text;
    String Pattern;
    Experiment exp = new Experiment();

    Text = "This would be the worst case scenario.";
    Pattern = "rio.";
    exp.runExperiment(Text, Pattern);

    Text = "This would be the best case scenario.";
    Pattern = "This";
    exp.runExperiment(Text, Pattern);

    Text = "Here is another example to show the functionality.";
    Pattern = "another example";
    exp.runExperiment(Text, Pattern);

    Text = "Here is another example to show the functionality.";
    Pattern = "to sh";
    exp.runExperiment(Text, Pattern);

  }
}

class Experiment{
  // Experiment class with only one method to time each algorithm as it operates on the text and pattern.
  void runExperiment(String Text, String Pattern){
    System.out.println("----------------------------------");
    System.out.println("Text: "+ Text);
    System.out.println("Pattern: "+ Pattern);
    

    System.out.println("-----Run Naïve algorithm-----");    
    Timer timer1 = new Timer();
    NVE naive = new NVE();
    timer1.startTimer();
    naive.naiveSearch(Text, Pattern);
    timer1.endTimer();

    System.out.println("-----Run Boyer-Moore algorithm-----");    
    Timer timer2 = new Timer();
    AWQ Boyer = new AWQ();
    char txt[] = Text.toCharArray();
    char pat[] = Pattern.toCharArray();
    timer2.startTimer();
    Boyer.search(txt, pat);
    timer2.endTimer();

    System.out.println("-----Run Rabin-Karp algorithm-----");  
    Timer timer3 = new Timer();
    RK Rabin = new RK();
    int q = 101;
    timer3.startTimer();
		Rabin.rabinKarpSearch(Pattern, Text, q);
    timer3.endTimer();
    System.out.println("----------------------------------");  



    System.out.print("For the Naive algorithm,       ");
    timer1.displayTimer();
    System.out.print("For the Boyer-Moore algorithm, ");
    timer2.displayTimer();
    System.out.print("For the Rabin-Karp algorithm,       ");
    timer3.displayTimer();
  }
}

class Timer {
  // Timer class to start and end the java stopwatch with methods
  
  long starttime;
  long endtime;

  void startTimer(){
    this.starttime = System.nanoTime();
  }
  void endTimer(){
    this.endtime = System.nanoTime();
  }
  // display method for results when needed
  void displayTimer(){
    System.out.println("Operation time was: " +(this.endtime - this.starttime)+" nanoseconds");  
  }

}

class NVE{
  // Implement the naiveSearch
  void naiveSearch(String txt, String pat) {
    int M = pat.length();
    int N = txt.length();
 
    /* A loop to slide pat one by one */
    for (int i = 0; i <= N - M; i++) {
 
      int j;
 
      /* For current index i, check for pattern match */
      for (j = 0; j < M; j++)
        if (txt.charAt(i + j) != pat.charAt(j))
          break;
 
      if (j == M) // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
        System.out.println("Pattern found at index " + i);
    }

  }

}

class AWQ{
  // Implement the Boyer-Moore from Geeks for Geeks
	static int NO_OF_CHARS = 256;
	
	//A utility function to get maximum of two integers
	static int max (int a, int b) { return (a > b)? a: b; }

	//The preprocessing function for Boyer Moore's
	//bad character heuristic
	static void badCharHeuristic( char []str, int size,int badchar[])
	{

	// Initialize all occurrences as -1
	for (int i = 0; i < NO_OF_CHARS; i++)
		badchar[i] = -1;

	// Fill the actual value of last occurrence
	// of a character (indeces of table are ascii and values are index of occurence)
	for (int i = 0; i < size; i++)
		badchar[(int) str[i]] = i;
	}

	/* A pattern searching function that uses Bad
	Character Heuristic of Boyer Moore Algorithm */
	static void search( char txt[], char pat[])
	{
	int m = pat.length;
	int n = txt.length;

	int badchar[] = new int[NO_OF_CHARS];

	/* Fill the bad character array by calling
		the preprocessing function badCharHeuristic()
		for given pattern */
	badCharHeuristic(pat, m, badchar);

	int s = 0; // s is shift of the pattern with
				// respect to text
	//there are n-m+1 potential allignments
	while(s <= (n - m))
	{
		int j = m-1;

		/* Keep reducing index j of pattern while
			characters of pattern and text are
			matching at this shift s */
		while(j >= 0 && pat[j] == txt[s+j])
			j--;

		/* If the pattern is present at current
			shift, then index j will become -1 after
			the above loop */
		if (j < 0)
		{
			System.out.println("Patterns occur at shift = " + s);
			s += (s+m < n)? m-badchar[txt[s+m]] : 1;

		}

		else
			s += max(1, j - badchar[txt[s+j]]);
	}
	}

}

class RK {
  // Implement the Rabin-Karp algorithm from Geeks for Geeks

	// d is the number of characters in the input alphabet
	public final static int d = 256;
	
	/* pat -> pattern
		txt -> text
		q -> A prime number
	*/
	static void rabinKarpSearch(String pat, String txt, int q)
	{
		int M = pat.length();
		int N = txt.length();
		int i, j;
		int p = 0; // hash value for pattern
		int t = 0; // hash value for txt
		int h = 1;
	
		// The value of h would be "pow(d, M-1)%q"
		for (i = 0; i < M-1; i++)
			h = (h*d)%q;
	
		// Calculate the hash value of pattern and first
		// window of text
		for (i = 0; i < M; i++)
		{
			p = (d*p + pat.charAt(i))%q;
			t = (d*t + txt.charAt(i))%q;
		}
	
		// Slide the pattern over text one by one
		for (i = 0; i <= N - M; i++)
		{
	
			// Check the hash values of current window of text
			// and pattern. If the hash values match then only
			// check for characters on by one
			if ( p == t )
			{
				/* Check for characters one by one */
				for (j = 0; j < M; j++)
				{
					if (txt.charAt(i+j) != pat.charAt(j))
						break;
				}
	
				// if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
				if (j == M)
					System.out.println("Pattern found at index " + i);
			}
	
			// Calculate hash value for next window of text: Remove
			// leading digit, add trailing digit
			if ( i < N-M )
			{
				t = (d*(t - txt.charAt(i)*h) + txt.charAt(i+M))%q;
	
				// We might get negative value of t, converting it
				// to positive
				if (t < 0)
				t = (t + q);
			}
		}
	}
	
	
}