
#ifndef _GRAPH_UI_
#define _GRAPH_UI_
#include "Graph.hpp"
#include "NegativeGraphCycle.hpp"
#include<iostream>
#include<string>
#include <sstream>
#include <cstdlib>
using namespace std;

template <typename T>
int graphUI() {
  
  string option, line;
  int distance;
  bool digraph = false;
  
  cin >> option;
  if(!option.compare("digraph"))
    digraph = true;
  Graph<T> g(digraph);
  
  while(true) {
    
    std::stringstream stream;
    cin >> option;
    
    if(!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.addVtx(vtx))
        cout << "av " << vtx << " OK\n";
      else
	  	cout << "av " << vtx << " NOK\n";
	}

    else if(!option.compare("rv")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.rmvVtx(vtx))
        cout << "rv " << vtx << " OK\n";
      else
        cout << "rv" << vtx << " NOK\n";
    }
    else if(!option.compare("ae")) {
      getline(std::cin, line);
      stream << line;
      
      T from(stream);
      T to(stream);

      stream >> distance;

      if(g.addEdg(from, to, distance))
        cout << "ae " << from << " " << to << " OK\n";
      else
        cout << "ae " << from << " " << to << " NOK\n";
      
    }
    else if(!option.compare("re")) {
      getline(std::cin, line);
      stream << line;
      
      T from(stream);
      T to(stream);

      if(g.rmvEdg(from, to))
        cout << "re " << from << " " <<  to << " OK\n";
      else
        cout << "re " << from << " " << to << " NOK\n";

    }
    else if(!option.compare("dot")) {
		getline(std::cin, line);
		stream << line;
		
		string filename_str = stream.str();
		const char *filename = filename_str.c_str();

		if(g.print2DotFile(&filename[1]))
			cout << "dot file " << " OK\n";
		
		else
			cout << "dot file " << " NOK\n";
    }
    else if(!option.compare("bfs")) {
      getline(std::cin, line);
      stream << line;
      
      T vtx(stream);
      list<T> bfs = g.bfs(vtx);

      cout << "\n----- BFS Traversal -----\n";
        while(1){
          cout<<bfs.front();
          bfs.pop_front();
          
          cout<<" -> ";
          
          if(bfs.front() == bfs.back()){
            cout<<bfs.front();
            break;
          }
        }

      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dfs")) {
      getline(std::cin, line);
      stream << line;
      
      T vtx(stream);
      list<T> dfs = g.dfs(vtx);
      cout << "\n----- DFS Traversal -----\n";
      while(1){
          cout<<dfs.front();
          dfs.pop_front();
          
          cout<<" -> ";
          
          if(dfs.front() == dfs.back()){
            cout<<dfs.front();
            break;
          }
      }
      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dijkstra")) {
		getline(std::cin, line);
		stream << line;
		T from(stream);
		T to(stream);

		list<T> dijkstra = g.dijkstra(from, to);

		cout << "Dijkstra (" << from << " - " << to <<"): ";
			
		while(1){
			if(dijkstra.empty()){
				cout<<endl;
				break;
			}
		
			cout<<dijkstra.back();
			dijkstra.pop_back();
			
			cout<<", ";
			
			if(dijkstra.back() == dijkstra.front()){
				cout<<dijkstra.back()<<endl;
				break;
			}
		}
    }

    else if(!option.compare("bellman-ford")) {
		list<T> bellmanFord;
		NegativeGraphCycle ex;

        try{
			getline(std::cin, line);
        	stream << line;
        	T from(stream);
        	T to(stream);

        	cout << "Bellman-Ford (" << from << " - " << to <<"): ";
			bellmanFord = g.bellman_ford(from, to);

			while(1){
				if(bellmanFord.empty()){
					cout<<endl;
					break;
				}
			
				cout<<bellmanFord.back();
				bellmanFord.pop_back();
				
				cout<<", ";
				
				if(bellmanFord.back() == bellmanFord.front()){
					cout<<bellmanFord.back()<<endl;
					break;
				}
			}

        } catch(NegativeGraphCycle& ex){
			cout << ex.what() << endl;
		}
      
    }
    else if(!option.compare("mst")) {
		int sum = 0;
		list<Edge<T>> mstSet = g.mst();

		cout << "\n--- Min Spanning Tree ---\n";
		if(mstSet.empty())
			cout<<endl;
		
		while(!mstSet.empty()){
			
			cout << mstSet.front().from << " -- " << mstSet.front().to << " (" << mstSet.front().dist << ")" << endl;
			sum += mstSet.front().dist;

			mstSet.pop_front();
    	}
					
      	cout << "MST Cost: " << sum << endl;
    }
    else if(!option.compare("q")) {
      g.deleteAll();
      cerr << "bye bye...\n";
      return 0;
    }
    else if(!option.compare("#")) {
      string line;
      getline(cin,line);
      cerr << "Skipping line: " << line << endl;
    }
    else {
      cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;  
}

#endif
