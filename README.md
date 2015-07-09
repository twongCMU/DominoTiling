DominoTiling
============

Randomly place k-tile dominos into an N-length array until no more tiles can be placed and determine the occupation percentage

I wrote this code for fun on my own time. I was not paid for this nor was it related to my regular employment.

I wrote this code to illustrate the work that my dad, a math professor, and his student were doing. Their work was to determine a function that would return the expected percentage of tiles covered by randomly placing k-sized dominos on an N-length array until no more tiles could be placed.

This work was presented at the 2015 Joint Mathematics Meetings conference on January 13, 2015:
<pre>
http://jointmathematicsmeetings.org/meetings/national/jmm2015/2168_progfull.html

Expected Portion filled by k-Tiles.
Maxwell Christopher Chomas*, Washington & Jefferson College
Roman Wong, Washington & Jefferson College
Terrence Wong, Carnegie Mellon University
(1106-VX-1235)

http://jointmathematicsmeetings.org/amsmtgs/2168_abstracts/1106-vx-1235.pdf

Benchmarks:
Intel Core i7-4770K CPU
32GB Ram (although RAM is not really important for this)

For 1 million trials, domino size 2

Array Size | time
100 | 8 sec
1,000 | 1:13
10,000 | 11:37
100,000 | 3:09:42
</pre>
Any larger domino size will cause the time to drop since it requires placing fewer tiles
