#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_
#include "NegativeGraphCycle.hpp"
#include<iostream>
#include <list>
#include<iterator>
#include<fstream>
#include <vector>
#include<bits/stdc++.h>

using namespace std;
template<typename T>

struct Edge {
  T from;
  T to;
  int dist;
  Edge(T f, T t, int d): from(f), to(t), dist(d) {
  }
  bool operator<(const Edge<T>& e) const;
  bool operator>(const Edge<T>& e) const;
  template<typename U>
  friend std::ostream& operator<<(std::ostream& out, const Edge<U>& e);
};

template<typename T>
std::ostream& operator<<(std::ostream& out, const Edge<T>& e) {
  out << e.from << " -- " << e.to << " (" << e.dist << ")";
  return out;
}

template <typename T>
class Graph {  
  vector< vector<Edge<T>*> > adjArray;
  vector<T> graphVrtx;
  bool isDirectedGraph;
  int numOfEdges;
  
public:
  Graph(bool isDirectedGraph = true);
  bool contains(const T& info);
  bool addVtx(const T& info);
  bool rmvVtx(const T& info);
  bool addEdg(const T& from, const T& to, int distance);
  bool rmvEdg(const T& from, const T& to);
  list<T> dfs(const T& info) const;
  list<T> bfs(const T& info) const;
  list<Edge<T>> mst();
  
  bool print2DotFile(const char *filename) const;
  list<T> dijkstra(const T& from, const T& to);
  list<T> bellman_ford(const T& from, const T& to);
  bool containsEdge(const T& from, const T& to);
  int vrtxMapping(const T& info) const;
  void deleteAll();
  void dfsRec(const Edge<T>* info, bool visited[], list<T>& dfsTraversal) const;
  int minWeight(int weight[], bool mstIncluded[]);
};

template<typename T>
//Constructor
Graph<T>::Graph(bool isDirectedGraph){
    this->isDirectedGraph = isDirectedGraph;
    numOfEdges = 0;

}

//This function returns true if the given vertex is included
//in the Graph, otherwise false is returned.
template<typename T>
bool Graph<T>::contains(const T& info){

    for(auto it=graphVrtx.cbegin(); it!=graphVrtx.cend(); it++){
        if(*it == info)
            return(true);
    }

    return(false);
}

//This function is inserting a given node to the
//Graph.If the procedure was successful, the function
//returns true, otherwise false.
template<typename T>
bool Graph<T>::addVtx(const T& info){
    
    if(contains(info))
        return(false);
    
    //push back the given vertex in the vertex list
    graphVrtx.push_back(info);
    
    //Increase adjacency Matrix rows by 1. 
    adjArray.resize(adjArray.size() + 1);

    //create new row and column in the adjacency matrix and initialize
    //them to NULL.
    for (unsigned int i = 0; i < adjArray.size(); i++){
        adjArray[i].push_back(NULL);

        //This will be executed when we'll reach the last row of the Matrix.
        if(i == adjArray.size()-1 && adjArray.size() != 1){
            for (unsigned int j = 0; j < adjArray.size()-1; j++)
                adjArray[i].push_back(NULL);
        }
    }

    return(true);
}

//This is a utility method which's mapping a given vertex to an integer index 
//from the adjacency Matrix.The return value is the index.
template<typename T>
int Graph<T>::vrtxMapping(const T& info) const {
    unsigned int i;

    for(i=0; i<graphVrtx.size(); i++){
        if(graphVrtx[i] == info)
            break;
    }

    return(i);
}

//This function removes a given vertex from the Graph.If the vertex was deleted
//successfully, the return value is true, otherwise false is returned.
template<typename T>
bool Graph<T>::rmvVtx(const T& info){
    unsigned int pos;
    unsigned int curr;
    unsigned int counter;

    if(!contains(info))
        return(false);
    
    //find index position of vertex in the adjacency Matrix.
    pos = vrtxMapping(info);
    counter = pos;
    
    //move the given vertex to the end of the vector 
    //by shifting it with it's left element
    while(counter < graphVrtx.size() - 1){
        graphVrtx[counter] = graphVrtx[counter + 1];
        counter++;
    }

    //resize vector
    graphVrtx.resize(graphVrtx.size() - 1);
  
        //removing vertex position from the adjacency Matrix 
        counter = pos;
        while (counter < adjArray.size() - 1) {

            //shifting the rows to left side 
            for (curr = 0; curr < adjArray.size(); ++curr) { 
                adjArray[curr][counter] = adjArray[curr][counter + 1]; 
            } 
  
            //shifting the columns upwards 
            for (curr = 0; curr < adjArray.size(); ++curr) { 
                adjArray[counter][curr] = adjArray[counter + 1][curr]; 
            } 
            counter++; 
        } 

        //decreasing the number of vertices by 1
        adjArray.resize(adjArray.size() - 1);
    
        for (unsigned int i = 0; i < adjArray.size(); ++i)
            adjArray[i].resize(adjArray[i].size() - 1);

        return(true);
    
}

//This is a utility method which's searching whether the given edge
//exists in the Graph or not.If the edge was found the return
//value is true, otherwise false will be returned.
template<typename T>
bool Graph<T>::containsEdge(const T& from, const T& to){
    for(auto& element: adjArray){
        for(auto& item: element){
            
            if(item != NULL){
                 if((item->from == from) && (item->to == to))
                    return(true);
            }

        }
    }

    return(false);
}

//This function updates the adjacency matrix by inserting
//an edge starting from the given vertex from, 
//and ending to the given vertex to.
//If the edge was added successfully, 
//the return value is true, otherwise false is returned.
template<typename T>
bool Graph<T>::addEdg(const T& from, const T& to, int cost){
    int fromPos, toPos;

    //if the given edge already exists, return false
    if((contains(from) && contains(to)) && containsEdge(from, to))
        return(false);
    
    //Finding the position of the involved vertixes
    fromPos = vrtxMapping(from);
    toPos = vrtxMapping(to);
    //allocate memory for the given vertex and insert it to the matrix
    adjArray[fromPos][toPos] = new struct Edge<T>(from, to, cost);
    numOfEdges++;
    
    if(!isDirectedGraph){
        //if the graph is not directed, the adjacency matrix is symmetric.
        adjArray[toPos][fromPos] = new struct Edge<T>(to, from, cost);
        numOfEdges++;
    }
    
    return(true);
}

//This function updates the adjacency matrix by removing
//an edge starting from the given vertex from, 
//and ending to the given vertex to.
//If the edge was deleted successfully, 
//the return value is true, otherwise false is returned.
template<typename T>
bool Graph<T>::rmvEdg(const T& from, const T& to){
    int fromPos, toPos;

    //if the edge doesn't exist, return false.
    if(!containsEdge(from, to))
        return(false);
    
    //Finding the position of the involved vertixes
    fromPos = vrtxMapping(from);
    toPos = vrtxMapping(to);

    //free memory allocated by the edge
    delete(adjArray[fromPos][toPos]);
    adjArray[fromPos][toPos] = NULL;
    numOfEdges--;
    
    if(!isDirectedGraph){
        //if the graph isn't directed , delete also the symmetric element
        //of the deleted edge.
        delete(adjArray[toPos][fromPos]);
        adjArray[toPos][fromPos] = NULL;
        numOfEdges--;
    } 
    
    return(true);
}

//This function creates a dot file named by the given char sequence.
//if the file construction fails, the return value is false, otherwise true is returned.
template<typename T>
bool Graph<T>::print2DotFile(const char *filename) const{
    ofstream myfile;

    myfile.open(filename);
    
    if(myfile.fail())
        return(false);

    myfile<<"digraph {\n";

    for(unsigned int row=0; row< adjArray.size(); row++){
        for(unsigned int col=0; col<adjArray[row].size(); col++){
            if(adjArray[row][col] != NULL){

                myfile<<adjArray[row][col]->from;
                myfile<<" -> ";

                myfile<<adjArray[row][col]->to;
                myfile<<";\n";
            }
        }
    }

    myfile<<"}";
    myfile.close();

    return(true);

}

//Before the main program terminates, this method is deleting
//any allocated memory, which's is inside the adjacency matrix.
template<typename T>
void Graph<T>::deleteAll(){
    for(auto& element:adjArray){
        for(auto& item:element){
            
            delete(item);
            item=NULL;
        }
    }
}

//This method implements a BFS traversal from a given 
//source vertex info.It traverses vertices  
//reachable from the given vertex.The method returns the 
//result of the bfs traversal as a list of vertex 
template<typename T>
list<T> Graph<T>::bfs(const T& info) const{
    bool visited[graphVrtx.size()];
    list<T> queue;
    list<T> bfsTraversal;
    T element;
    int pos;

    // Mark all the vertices as not visited 
    for(unsigned int i = 0; i <graphVrtx.size() ; i++) 
        visited[i] = false; 
    
    //Mark given Vertex as visited
    pos = vrtxMapping(info);
    visited[pos] = true;
    queue.push_back(info);

    while(!queue.empty()){

        // Dequeue a vertex from queue
        element = queue.front();
        
        bfsTraversal.push_back(element);
        queue.pop_front();
        
        pos = vrtxMapping(element);

        // Get all adjacent vertices of the dequeued 
        // vertex. If a adjacent has not been visited,  
        // then mark it visited and enqueue it. 
        for (unsigned int i = 0; i != adjArray.size(); i++){
            if(adjArray[pos][i] != NULL){
                if(!visited[i]){

                    visited[i] = true; 
                    queue.push_back(adjArray[pos][i]->to); 
                
                }
            }
        }
    }

    return(bfsTraversal);
    
}

//This is a utility recursive function used by the main method dfs.
template<typename T>
void Graph<T>::dfsRec(const Edge<T>* info, bool visited[], list<T>& dfsTraversal) const {
    int pos = vrtxMapping(info->to);
    
    //Mark the current node as visited
    visited[pos] = true;
    dfsTraversal.push_back(info->to);
    
    //Recur for all the vertices adjacent 
    //to this vertex 
    for (unsigned int i = 0; i != adjArray.size(); i++){
        if(adjArray[pos][i] != NULL){
              if(!visited[i]){
                dfsRec(adjArray[pos][i], visited, dfsTraversal);
            }
        }
    }
}

//This method implements a dfs traversal of the vertices reachable from vertex info. 
//Every vertex that's been visited through the traversal is saved in a list which is
//the return value.
template<typename T>
list<T> Graph<T>::dfs(const T& info) const{
    bool visited[graphVrtx.size()];
    list<T> queue;
    list<T> dfsTraversal;
    T element;
    int pos = vrtxMapping(info);

    //Mark all the vertices as not visited 
    for(unsigned int i = 0; i <graphVrtx.size() ; i++) 
        visited[i] = false;

    // Mark the src vertex as visited
    visited[pos] = true;
    dfsTraversal.push_back(info);

    //Call the recursive helper function 
    //to implement DFS traversal recursively. 
    for(unsigned int i = 0; i <graphVrtx.size() ; i++){
        if(!visited[i] && adjArray[pos][i] != NULL)
            dfsRec(adjArray[pos][i], visited, dfsTraversal);
    }
    return(dfsTraversal);        
}

//This is a utility function to find the vertex with 
//minimum key value, from the set of vertices 
//not yet included in mst Set.
template<typename T> 
int Graph<T>::minWeight(int weight[], bool mstIncluded[]){

    // Initialize min value as infinite
    int min = INT_MAX;
    int pos = 0;

    for(unsigned int i = 0; i < graphVrtx.size(); i++){
        if(mstIncluded[i] == false  && min > weight[i]){
            pos = i;
            min = weight[i];
        }

    }
    return(pos);
}

//This method implements Prim's Minimum Spanning Tree (mst) algorithm.
//The return value is a list of edges, sorted from minimum distance to max distance.
template<typename T>
list<Edge<T>> Graph<T>::mst(){
    list<Edge<T>> mstSet;
    
    //Array to store constructed MST.
    int parent[graphVrtx.size()];
    
    //Key values used to pick minimum weight edge in cut.
    int weight[graphVrtx.size()];

    //represent set of vertices included in MST.
    bool mstIncluded[graphVrtx.size()];

    //In case the graph is directed, return empty list.
    if(isDirectedGraph)
        return(mstSet);

    //Initialize all keys as INFINITE(INT_MAX).
    for (unsigned int i = 0; i < graphVrtx.size(); i++){
        mstIncluded[i] = false; 
        weight[i] = INT_MAX;
    }

    //Make key 0 so that this vertex is picked as first vertex.
    weight[0] = 0;
    
    //The mst must include all the vertices
    for(unsigned int v = 0; v != graphVrtx.size() - 1; v++){
        
        //Pick the minimum key vertex from the 
        //set of vertices not yet included in MST 
        int pos = minWeight(weight, mstIncluded);

        mstIncluded[pos] = true;

        //Update key value and parent index of 
        //the adjacent vertices of the picked vertex
        for(unsigned int i = 0; i < adjArray.size(); i++){
            if(adjArray[pos][i] != NULL && mstIncluded[i] == false && adjArray[pos][i]->dist < weight[i]){
                parent[i] = pos;
                weight[i] = adjArray[pos][i]->dist;
            }
        }
    }

    //Find the edges pointed by the values both parent[i] and vertex i.
    for(unsigned int v = 1; v < graphVrtx.size(); v++){
    
        mstSet.push_back((v > (unsigned int )parent[v] ? *adjArray[parent[v]][v] : *adjArray[v][parent[v]]));
    }

    //Implement sorting of the list considering the distance of every edge.    
    mstSet.sort([](const Edge<T>& T1, const Edge<T>& T2){
        if(T1.dist < T2.dist)
            return(true);

        return(false);
    });

    return(mstSet);
}

//This is function that implements Dijkstra's single source shortest path algorithm. 
//The return value is a list of vertices, representing the path from source vertex: from
//to destination vertex: to.
template<typename T>
list<T> Graph<T>::dijkstra(const T& from, const T& to){
    list<T> dijkstra;
    int parent[graphVrtx.size()];
    int weight[graphVrtx.size()];
    bool mstIncluded[graphVrtx.size()];
    T backtrackVtx = to;
    
    //Initialize all keys as INFINITE(INT_MAX) and mark
    //all vetrices as not visited.
    for (unsigned int i = 0; i < graphVrtx.size(); i++){
        mstIncluded[i] = false; 
        weight[i] = INT_MAX;
    }

    //Make key of source vertex 0.
    weight[vrtxMapping(from)] = 0;

    //Find shortest path for all vertices.
    for(unsigned int v = 0; v != graphVrtx.size() - 1; v++){

        //Pick the minimum distance vertex from the set of vertices not 
        //yet processed. pos is always equal to source vertex index in the first iteration. 
        int pos = minWeight(weight, mstIncluded);

        //mark vertex as visited
        mstIncluded[pos] = true;

        // Update weight value of the adjacent vertices of the picked vertex.
        for(unsigned int i = 0; i < adjArray.size(); i++){
            if(adjArray[pos][i] != NULL && mstIncluded[i] == false && adjArray[pos][i]->dist + weight[pos] < weight[i]
                && weight[pos] != INT_MAX){

                parent[i] = pos;
                weight[i] = adjArray[pos][i]->dist + weight[pos];
            }
        }
    }

    //if source vertex has not path leading to destination vertex, return empty list. 
    if(weight[vrtxMapping(to)] == INT_MAX){
        dijkstra.clear();
        return(dijkstra);
    }
    
    //starting fom destination vertex, we try to find the path leading to source vertex with
    //backtracking.
    while(backtrackVtx != from){
        dijkstra.push_back(backtrackVtx);
        backtrackVtx = graphVrtx[parent[vrtxMapping(backtrackVtx)]];

    }

    //broke out from while loop, backtrackVtx is now pointing on source vertex.
    dijkstra.push_back(backtrackVtx);

    return(dijkstra);
}

//This method is implementing Bellman-Ford's single source 
//shortest path algorithm.It finds shortest distances from src
//vertex from to all other vertices.The method also detects negative
//weight cycle.
template<typename T>
list<T> Graph<T>::bellman_ford(const T& from, const T& to){
    
    list<T> bellmanFord;
    int parent[graphVrtx.size()];
    int weight[graphVrtx.size()];
    T backtrackVtx = to;
    NegativeGraphCycle ex;

    //Initialize distances from src to all other vertices as INT_MAX. 
    for (unsigned int i = 0; i < graphVrtx.size(); i++) 
        weight[i] = INT_MAX;

    weight[vrtxMapping(from)] = 0;

    //Relax all edges |V| - 1 times. A simple shortest 
    //path from src to any other vertex can have at-most 
    //|V| - 1 edges. 
    for(unsigned int v=0; v < graphVrtx.size() - 1; v++){
        for(auto& element: adjArray){
            for(auto& item: element){
                if(item != NULL){
                    int src = vrtxMapping(item->from);
                    int dest = vrtxMapping(item->to);
                    
                    if(weight[src] != INT_MAX && weight[src] + item->dist < weight[dest]){
                        parent[dest] = src;
                        weight[dest] = weight[src] + item->dist; 
                    }

                }
            }
        }
    }
 
    //The above procedure guarantees shortest distances if graph doesn't contain 
    // negative weight cycle.  If we get a shorter path, then there 
    // is a cycle. 
    for(auto& element : adjArray){
        for(auto& item : element){
            if(item != NULL){
                int src = vrtxMapping(item->from);
                int dest = vrtxMapping(item->to);
                    
                if(weight[src] != INT_MAX && weight[src] + item->dist < weight[dest]){
                    throw ex;//Negative cycle is detected, throw exception.
                    break;
                }

            }
        }
    }

    //if source vertex has not path leading to destination vertex, return empty list.    
    if(weight[vrtxMapping(to)] == INT_MAX){
        bellmanFord.clear();
        return(bellmanFord);
    }

    //starting fom destination vertex, we try to find the path leading to source vertex with
    //backtracking.
    while(backtrackVtx != from){
        bellmanFord.push_back(backtrackVtx);
        backtrackVtx = graphVrtx[parent[vrtxMapping(backtrackVtx)]];

    }

    //broke out from while loop, backtrackVtx is now pointing on source vertex.
    bellmanFord.push_back(backtrackVtx);

    return(bellmanFord);
}

#endif