import java.util.*;
import java.io.*;

public class DominoThread implements Runnable {
    double pctSum = 0.0;
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
	    /*
	     * Each key in this treemap is the starting array index where a domino could go.
	     * If this is empty, there are no legal placements left and we know we're done
	     */
	    TreeMap<Integer, Integer> availablePlacementMap = new TreeMap<Integer, Integer>();
	    int i;
	    // it is not possible to place a tile of size k in the final k-1 slots
	    for (i = 0; i < Domino.ARRAY_SIZE - (Domino.TILE_SIZE - 1); i++) {
		availablePlacementMap.put(i, i);
	    }

	    /*
	     * an array of whether a square is covered or not. This allows us to to display the state of
	     * the array when the run is over. We only use this if _interval==1 since it's not useful
	     * for large numbers of trials
	     */
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

	    /*
	     * Store a shuffled sequence of the valid placement positions. This will be our
	     * ordering of randomized tile placements. Doing the shuffle once at the beginning
	     * is more efficient than doing some work to select a random element in each iteration
	     */
	    List<Integer> keys = new ArrayList<Integer>(availablePlacementMap.keySet());
	    Collections.shuffle(keys);
	    Integer[] removalOrderArray = keys.toArray(new Integer[availablePlacementMap.size()]);
	    keys = new ArrayList<Integer>();
	    int removalOrderNextIndex = 0;

	    while(!availablePlacementMap.isEmpty()) {
	
		Integer dominoPlacementLocation = removalOrderArray[removalOrderNextIndex];

		/*
		 * The removalOrderArray gets out of sync with the availablePlacementMap since
		 * each tile placement removes multiple entries from the Map. So, we cycle through
		 * the removalOrderArray until we find a tile placement that is still valid
		 */
		while (availablePlacementMap.remove(dominoPlacementLocation) == null){
		    removalOrderNextIndex++;
		    dominoPlacementLocation = removalOrderArray[removalOrderNextIndex];
		}

		/*
		 * Remove any placements that are now invalidated by this new tile's placement.
		 * A future tile cannot be placed starting in any of the squares that this tile occupies.
		 * It also can't be placed starting in the squares just previous to this tile where it would overlap with this tile
		 */
		for (i = 1; i < Domino.TILE_SIZE; i++) {
		    // we can't place any future tiles starting at any of the remaining spaces occupied by this current tile
		    availablePlacementMap.remove(dominoPlacementLocation + i); //returns null if not found which is fine

		    // we also can't place any future tiles starting before this tile where the new tile would overlap with the current tile
		    availablePlacementMap.remove(dominoPlacementLocation - i); //returns null if not found which is fine
		}
		removalOrderNextIndex++;

		// update graphics data, if needed
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

	    pctSum += occupyPct;
	    
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
	return pctSum;
    }
}
