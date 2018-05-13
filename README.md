# Trace File Viewer


Our network lab hosts the Auckland Satellite Simulator (http://sde.blogs.auckland.ac.nz/).
We use it to simulate Internet traffic to small Pacific islands that are connected
to the rest of the world via a satellite link.

To do this, the simulator has a number of machines (“source hosts”) on the
“world side” of the simulated satellite link that transmit data in the form of small
packets of up to 1500 bytes. These packets travel via a simulated
satellite link to the “island side”. Once on the island side, each packet ends up at
a machine there. These machines are called “destination hosts”.

The simulated satellite link delays packets and occasionally throws some away
when there are more packets arriving that it can deal with at the moment. When
the link throws packets away, the source hosts respond by sending less data for
a while before attempting to send more again.

On the island side of the satellite link, our simulator eavesdrops on the incoming
packets. It stores summary information about each packet in a trace file. The
trace file is plain text and each line contains the record for exactly one packet
(more on the file format in the next section).

What we would like to be able to do is get a graphical display of how much data
comes from a particular source host over time, or how much data goes to a
particular destination host over the course of an experiment. Experiments
typically take between 90 seconds and about 11 minutes.

# Trace File Format

Three trace files have been included for demonstration purposes: a tiny one with 18
lines, a small one with 148315 lines, and a large one with 651274 lines.
An example of some data is shown below.

108860 128.879102000 192.168.0.24 47928 10.0.0.5 5201 1514 1500 1448 …
108861 128.879885000 192.168.0.24 47928 10.0.0.5 5201 1514 1500 1448 …
108862 128.880603000 192.168.0.24 47928 10.0.0.5 5201 1514 1500 1448 …
108863 128.881481000 192.168.0.15 8000 10.0.1.25 59590 66 52 0 …
108864 128.881481000 192.168.0.9 8000 10.0.1.15 42081 66 52 0 …
108865 128.881481000 192.168.0.24 47928 10.0.0.5 5201 1514 1500 1448 …
108866 128.881495000 192.168.0.15 8000 10.0.1.25 59590 66 52 0 …
108867 128.882148000 192.168.0.3 8000 10.0.1.4 55442 1514 1500 1448 …
108868 128.882905000 192.168.0.3 8000 10.0.1.4 55442 1514 1500 1448 …
108869 128.883800000 192.168.0.3 8000 10.0.1.4 55442 1514 1500 1448 …

The lines here have been truncated to be able to accommodate them on the
page, but they show all the data you will need. Each line consists of a number of
fields separated by a single tab character. Note that fields may be empty, in
which case you get two successive tab characters.

The first field on the left is a just a sequential number for each packet that is
added by the eavesdropping program. The second field is a time stamp that is
also added by the eavesdropping program. Each trace file starts with a time
stamp of 0.000000000 for the first packet in the file.

The third field in each line is the IP address of the source host. IP addresses
identify machines on the network and help routers forward packets between
source and destination. Each IP address consists of four decimal numbers
between 0 and 255 separated by dots (full stops).

The trace files here only show packets heading towards destination hosts on the
island side, so all source host IP addresses start with “192.168.0.”, indicating
that they are “world side” addresses.

The fifth field is the IP address of the destination host from the island network.
All of the island addresses start with “10.0.”.

The fourth and the sixth field are the TCP ports on the hosts that the respective
packets travel between. They identify the applications that have sent or would
have received the packets.

Fields 7, 8 and 9 are packet sizes in bytes. The size we’re interested in here is
that in field 8, it’s the IP packet size. The size in field 7 is that of the whole
Ethernet frame that contains the IP packet, and field 9 is the TCP payload size
(the size of the content of the IP packet).

There are also a number of additional fields: various flags, packet sequence and
acknowledgment numbers. Note that some packets are not IP packets, meaning that
the IP packet size field can be empty.