import java.util.*;
import java.io.*;

public class DominoThread implements Runnable {
    double avg = 0.0;
    long _interval = 0;
    Thread _myThread = null;
    int _myThreadID;
    DominoDisplay dominoDisplay = null;

    public DominoThread(long interval, int threadID)
    {
	_myThreadID = threadID;
	_interval = interval;
    }

    public void doRuns()
    {
	_myThread = new Thread(this);
	_myThread.start();
    }

    public void run()
    {
	int lastProgress = 0;
	
	for (long iters = 0; iters < _interval; iters++) {
	    // stores adjacent pairs of squares, i.e., where a domino could go
	    // this allows us to quickly determine if there are any legal placements left
	    TreeMap<Integer, Integer> availablePlacementMap = new TreeMap<Integer, Integer>();
	    int i;
	    // it is not possible to place a tile of size k in the final k-1 slots
	    for (i = 0; i < Domino.ARRAY_SIZE - (Domino.TILE_SIZE - 1); i++) {
		availablePlacementMap.put(i, i);
	    }

	    // an array of whether a square is covered or not. This allows us to to display the state of
	    // the array when the run is over. We only use this if _interval==1 since it's not useful
	    // for large numbers of trials
	    byte[] availableSquareArray = null;
	    if (Domino.GRAPHIC_MODE > 0 &&
		Domino.GRAPHIC_MODE <= 3) {
		availableSquareArray = new byte[Domino.ARRAY_SIZE];
	    }
	    if (Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_GUI_MANUAL ||
		Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_GUI_AUTO) {
		dominoDisplay = new DominoDisplay(Domino.ARRAY_SIZE);
	    }

	    int availableCount = Domino.ARRAY_SIZE;

	    List<Integer> keys = new ArrayList<Integer>(availablePlacementMap.keySet());
	    Collections.shuffle(keys);
	    Integer[] removalOrderArray = keys.toArray(new Integer[availablePlacementMap.size()]);
	    int removalOrderNextIndex = 0;

	    while(!availablePlacementMap.isEmpty()) {
	
		Integer dominoPlacementLocation = removalOrderArray[removalOrderNextIndex];

		// a next domino cannot be placed in either of the two squares that this domino occupies
		// and also it can't be placed starting in the square just previous to this domino
		while (availablePlacementMap.remove(dominoPlacementLocation) == null){
		    // We loop here because the removalOrderArray gets out of sync with the availablePlacementMap
		    // since we remove up to 3 entries from the map for each 1 out of the array
		    // This loop is to discover when we're operating on an array entry that's not in the map and we skip it
		    removalOrderNextIndex++;
		    dominoPlacementLocation = removalOrderArray[removalOrderNextIndex];
		}

		for (i = 1; i < Domino.TILE_SIZE; i++) {
		    // we can't place any future tiles starting at any of the remaining spaces occupied by this current tile
		    availablePlacementMap.remove(dominoPlacementLocation + i); //returns null if not found which is fine

		    // we also can't place any future tiles starting before this tile where the new tile would overlap with the current tile
		    availablePlacementMap.remove(dominoPlacementLocation - i); //returns null if not found which is fine
		}
		removalOrderNextIndex++;

		if (availableSquareArray != null) {
		    availableSquareArray[dominoPlacementLocation] = Domino.TILE_START;
		    for (i = 1; i < Domino.TILE_SIZE - 1; i++) {
			availableSquareArray[dominoPlacementLocation + i] = Domino.TILE_MID;
		    }
		    availableSquareArray[dominoPlacementLocation + (Domino.TILE_SIZE - 1)] = Domino.TILE_END;
		}
		availableCount -= Domino.TILE_SIZE;

		if (Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_TEXT) {
		    int printI = 0;
		    if (availableSquareArray != null) {
			for (printI = 0; printI < Domino.ARRAY_SIZE; printI++) {
			    if (availableSquareArray[printI] == Domino.TILE_EMPTY) {
				System.out.print(".");
			    }
			    else if (availableSquareArray[printI] == Domino.TILE_START) {
				System.out.print("[");
			    }
			    else if (availableSquareArray[printI] == Domino.TILE_MID) {
				System.out.print("=");
			    }
			    else if (availableSquareArray[printI] == Domino.TILE_END) {
				System.out.print("]");
			    }
			    else {
				System.out.print("error");
			    }
			}
			System.out.println("");
		    }
		}
		if (dominoDisplay != null) {
		    dominoDisplay.updateDisplay(availableSquareArray);
		    if (Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_GUI_MANUAL) {
			System.out.println("Press Enter to continue");
			Scanner scannerInput = new Scanner(System.in);
			scannerInput.nextLine();
		    }
		    else if (Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_GUI_AUTO) {
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		    }
		}
	    }

	    double freePct = ((availableCount * 1.0)/(Domino.ARRAY_SIZE * 1.0));
	    double occupyPct = 1.0 - freePct;

	    avg += occupyPct;
	    
	    if (Domino.GRAPHIC_MODE == Domino.GRAPHIC_MODE_NONE	&&
		Domino.TILE_SIZE_START == Domino.TILE_SIZE_END) {
		double progressD = (iters * 1.0)/(_interval * 1.0);
		int progress = (int)(progressD * 100.0);
		if (progress != lastProgress && progress % 5 == 0) {
		    System.out.println(_myThreadID + ":" + progress + "%");
		    lastProgress = progress;
		}
	    }
	    
	    if (dominoDisplay != null) {
		dominoDisplay.close(occupyPct);
	    }
	}
    }

    public double getResult()
    {
	try {
	    _myThread.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	avg /= (_interval * 1.0);
	return avg;
    }
}
