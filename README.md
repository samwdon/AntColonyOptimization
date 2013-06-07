Ant Colony Optimization
=================


#About
This is a personal project that I started after reading about different graph solving heuristics including the ant colony optimization algorithm. This is my implementation of that algorithm.

#Algorithm
Given a graph of nodes connected by edges an Ant traverses the graph randomly until it finds the goal or it dies from traveling for too long. Each ant can remember the path it took to find the goal and then retraces its path leaving a portion of its available pheromone at each edge. The shorter the path, the more pheromone that is left at each edge. When the next ant traverses the graph, it favors nodes that have more pheromone. Eventually, the shortest path with have the most pheromone and future ants will be able to take that path to get to the goal. After each ant traverses the graph, a certain amount of pheromone evaporates from the graph, this prevents bad paths from being taken over and over again. Although none of the ants know anything about the graph, as a collective group they are able to find the optimized path.

I chose to not have ants die when they have to retrace their own path (i.e. they get stuck in a corner) but if there is an edge they have not traversed yet they will take it over one they have traversed.

#Graph Building
For this algorithm to work, the graph needs to be complete such that there exists a path from any node to any other node. When the graph is being constructed, new Nodes will be automatically attached to an existing node insuring a complete graph. Then edges are added until the desired sparseness is met.

#Dijkstras 
I implemented Dijkstra's shortest path algorithm to compare the heuristic solution to the analytic solution.

#Future Work and Contact
In the future I will look into directed graphs and ants that die when there are no new edges to traverse. In the meantime, feel free to contact me at sam.donohue@wustl.edu with any questions, comments, or advice. Feedback is appreciated!

