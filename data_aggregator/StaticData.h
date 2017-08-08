
#ifndef STATIC_DATA_H
#define STATIC_DATA_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>


struct BCI_Heuristic_Pair{
	int* heuristic_info;
	int bytecode_index;
};


class StaticData{
	public:
		//Preconditions: None
		//Postconditions: None
		StaticData();
		//Preconditions: StaticData exists
		//Postconditions: StaticData is deleted
		~StaticData();
		//Preconditions: StaticData exists and s is the name of the file of the static file
		//Postconditions: StaticData contains information from the static file.
		StaticData(std::string s);
		//Preconditions: StaticData exists
		//Postconditions: No Changes
		//Return: returns the data StaticData has.
		std::unordered_map<std::string,std::unordered_map<int,BCI_Heuristic_Pair>>& data();
		//Preconditions: StaticData exists
		//Postconditions: No Changes
		//Return: returns number of heuristics here.
		int get_heuristic_count();
		//Preconditions: StaticData exists
		//Postconditions: No Changes
		//Return: returns the array of heuristic names.
		std::string* get_heuristic_names();
	
	private:
		//Preconditions: StaticData exists
		//Postconditions: StaticData knows the name of the heuristics
		void read_columns();
		//Preconditions: StaticData exists and the method_name and the number of ifstmts it has exist
		//Postconditions: StaticData knows the method's ifstmts and what heuristics apply to them.
		void parse_method(std::string method_name, int lines);
		
		int column_count;
		std::string* names_of_heuristics;
		std::ifstream inFile;
		std::unordered_map<std::string,std::unordered_map<int,BCI_Heuristic_Pair>> method_name_to_taken;
};

#endif
