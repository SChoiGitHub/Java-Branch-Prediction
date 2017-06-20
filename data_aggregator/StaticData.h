
#ifndef STATIC_DATA_H
#define STATIC_DATA_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>



class StaticData{
	public:
		StaticData();
		~StaticData();
		StaticData(std::string s);
		
		std::unordered_map<std::string,std::vector<int*>>& data();
		int get_heuristic_count();
		std::string* get_heuristic_names();
	
	private:
		void read_columns();
		void parse_method(std::string method_name, int lines);
		void process_method_name(std::string& method_name);
		
		int column_count;
		std::string* names_of_heuristics;
		std::ifstream inFile;
		std::unordered_map<std::string,std::vector<int*>> method_name_to_taken;
};

#endif
