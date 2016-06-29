DominoTiling
============

Randomly place k-length tiles (regular domino = 2) into an n-length array. Eventually no more tiles can be placed and there will be some number (possibly zero) of uncovered squares in the array into which tiles cannot fit. Determine the ratio of covered array squares to the total number of squares. 

I originally wrote this code in Java as a refresher of the language. The Java version runs multithreaded and can display the results graphically. Some of the features grew organically as we were writing the paper so it is not as well written as it could be. 

I also implemented the algorithm in Python 2.7 as an exercise. It can calculate the coverage percentage but does not dispaly results or run multithreaded.

I wrote this code for my dad, who is a math professor, and his student. Their work was to determine a mathematical function that would return the expected percentage of tiles covered. This code can graphically display one trial of the tile placing to illustrate the premise of their work or run millions of trials to show experimentally that the results of a large number of trials converge to the values predicted by their work.

This work was presented at the [2015 Joint Mathematics Meetings conference](http://jointmathematicsmeetings.org/meetings/national/jmm2015/2168_progfull.html) on January 13, 2015 in San Antonio, Texas ([abstract](http://jointmathematicsmeetings.org/amsmtgs/2168_abstracts/1106-vx-1235.pdf)):
<pre>
Expected Portion filled by k-Tiles.
Maxwell Christopher Chomas, Washington & Jefferson College
Roman Wong, Washington & Jefferson College
Terrence Wong, Carnegie Mellon University
</pre>

Running
-------
in the src directory, simply do:
<pre>
> javac Domino.java
> java Domino
</pre>


For large arrays, 1000+ by default, there are no graphics options since there would be too much to display. This option has two main modes:
   * Main Experiment (domino start size == domino end size)
      * Run a number of trials and report the resulting occupation percentage. Progress information is displayed which is useful for large arrays that might take a long time to run
   * Incremental Experiment (domino start size  < domino end size)
      * Do the Main Experiment, but automatically repeat it for each domino size in the provided range. No per-experiment progress information is displayed

For small arrays, <1000 by default, there are graphics options. Note that when graphics are used (modes 1-3), only a single trial of the Main Experiment is offered. This is designed to illustrate to an audience what is happening rather than provide experimental results
   * 0 (none): This is the same as the large arrays option above 
   * 1 (text): In the terminal, each line will represent one random tile placement of the Main Experiment. A [ ] represents a tile and a . represents an unoccupied square
   * 2 (manual continue): Display graphics and only progress when the user hits Enter. Be sure to resize the graphics window to show all of the tiles, and then be sure to return your mouse focus to the terminal window before hitting Enter on the keyboard
   * 3 (auto continue): Similar to manual continue but tiles are placed automatically every one second. Again, be sure to resize the graphics window to show all of the tiles.


Python (2.x):
<pre>
> python domino.py
</pre>
* The Python version has fewer features than the Java version.
     
Benchmarks (Java version)
----------
<pre>
Intel Core i7-4770K CPU (4 physical cores, 8 threads)

1 million trials, domino size 2

Array Size | time
100 | 8 sec
1,000 | 1:13
10,000 | 11:37
100,000 | 3:09:42
</pre>

