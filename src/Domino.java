import java.util.*;
import java.io.*;


public class Domino {
    static int ARRAY_SIZE;
    static int TILE_SIZE;
    static int TILE_SIZE_START;
    static int TILE_SIZE_END;    
    static int GRAPHIC_MODE;

    static long TIMES = 1;
    static int THREADS = 1;

    static final byte TILE_EMPTY = 0;
    static final byte TILE_START = 1;
    static final byte TILE_MID = 2;
    static final byte TILE_END = 3;	

    static final byte GRAPHIC_MODE_NONE = 0;
    static final byte GRAPHIC_MODE_TEXT = 1;
    static final byte GRAPHIC_MODE_GUI_MANUAL = 2;
    static final byte GRAPHIC_MODE_GUI_AUTO = 3;

    static final int ARRAY_SIZE_LIMIT_FOR_GRAPHICS = 1000;

    public static void main(String[] args) {
	while (true) {
	    int i;
	    Scanner scannerInput = new Scanner(System.in);
	    System.out.println("Array size (<1000 for graphics options):");
	    ARRAY_SIZE = scannerInput.nextInt();
	    GRAPHIC_MODE = 0;
	    if (ARRAY_SIZE < ARRAY_SIZE_LIMIT_FOR_GRAPHICS) {
		System.out.println("Graphics Mode (0=none, 1=text, 2=manual continue, 3=auto continue):");
		GRAPHIC_MODE = scannerInput.nextInt();
	    }
	    String filename = "";
	    if (GRAPHIC_MODE <= 0 || GRAPHIC_MODE > 3) {
		System.out.println("Number of trials:");
		TIMES = scannerInput.nextLong();
		System.out.println("Domino size to start (2 if you don't know):");
		TILE_SIZE_START = scannerInput.nextInt();
		System.out.println("Domino size to end (2 if you don't know):");
		TILE_SIZE_END = scannerInput.nextInt();
		System.out.println("Number of threads (1 if you don't know):");
		THREADS = scannerInput.nextInt();
		scannerInput.nextLine();
		System.out.println("File name to save data in (hit enter for no file):");
		filename = scannerInput.nextLine();
	    }
	    else {
		System.out.println("Domino size:");
		TILE_SIZE_START = scannerInput.nextInt();
		TILE_SIZE_END = TILE_SIZE_START;
	    }

	    double[] allRes = new double[TILE_SIZE_END + 1];

	    long interval = TIMES / THREADS;
	    if ((TIMES % THREADS) != 0) { 	// round up so the last thread doesn't drop rows
		interval++;
	    }

	    DominoThread[] myThreads = new DominoThread[THREADS];
	    for (int kloop = TILE_SIZE_START; kloop <= TILE_SIZE_END; kloop++) {
		TILE_SIZE = kloop;
	
		for (i = 0; i < THREADS; i++) {
		    myThreads[i] = new DominoThread(interval, i);
		    myThreads[i].doRuns();
		}
	
		double avg = 0.0;
		for (i = 0; i < THREADS; i++) {
		    avg += myThreads[i].getResult();
		}
		avg /= (THREADS * 1.0);
		allRes[kloop] = avg;
		System.out.println("Tile size: " + kloop + " : Occupation " + avg);
	    }
	    //	`System.out.printf("average occupancy: %.6f\n", avg);
	
	    if (filename.length() > 0) {
		try{
		    PrintWriter writer = new PrintWriter(filename, "UTF-8");
		    for (int kloop = TILE_SIZE_START; kloop <= TILE_SIZE_END; kloop++) {
			writer.println(kloop + "," + allRes[kloop]);
		    }
		    writer.close();
		}
		catch (IOException ex) {
		    ;
		}
	    }
	    System.out.println("---------------------------------------");
	}
    }
}

	    
