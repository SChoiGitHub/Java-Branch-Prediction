#include "StaticData.h"
StaticData::StaticData(){
	
}
StaticData::~StaticData(){
	delete[] names_of_heuristics;
	for(auto& a : method_name_to_taken){
		for(auto& b : method_name_to_taken[a.first]){
			if(b.second.heuristic_info != nullptr){
				delete[] b.second.heuristic_info;
			}
		}
	}
}
StaticData::StaticData(std::string s){
	method_name_to_taken = std::unordered_map<std::string,std::unordered_map<int,BCI_Heuristic_Pair>>();
	column_count = 0;
	std::string line; //temp line var
	
	inFile.open(s); //open new file.
	if(inFile.is_open()){
		while(getline(inFile,line)){
			//std::cout << line << '\n';
			if(line.substr(0,6) == "[ToIB]"){ //We know it begins here.
				inFile >> column_count; //the table of information is beginning.
				getline(inFile,line); //Skip the line, so that read column can work.
				names_of_heuristics = new std::string[column_count]; //New array equal to the column count!
				//std::cout << column_count << '\n'; //DEBUG
				read_columns(); //fill the new array!
			}else if(line.substr(0,8) == "Method: "){
				std::string method_name = line.substr(8);
				method_name_to_taken.insert({method_name,std::unordered_map<int,BCI_Heuristic_Pair>()}); //get the method name
				//std::cout << "\tProcessing: " << method_name << "\n";
				getline(inFile,line); //get next line, that will be the number of lines we deal with;
				parse_method(method_name, std::stoi(line.substr(21)));
			}else if(line.substr(0,6) == "[ToIE]"){
				std::cout << "Parsing Static File Complete\n";
				break;
			}
		}
	}else{
		throw(std::runtime_error("Error: Missing static data file to parse"));
	}
	inFile.close();
}
void StaticData::read_columns(){
	std::string temp;
	for(int x = 0; x < column_count; x++){
		inFile >> names_of_heuristics[x];
		//std::cout << names_of_heuristics[x] << '\n'; //DEBUG
		getline(inFile,temp); //Remove the rest of the line.
	}
}
void StaticData::parse_method(std::string method_name, int lines){
	std::string temp;
	for(int x = 0; x < lines; x++){
		BCI_Heuristic_Pair h_data_for_this_line;
		inFile >> h_data_for_this_line.bytecode_index;
		h_data_for_this_line.heuristic_info = new int[column_count];
		
		for(int h = 0; h < column_count; h++){
			inFile >> h_data_for_this_line.heuristic_info[h];
			//std::cout << h_data_for_this_line[h] << ' ';
		}
		//std::cout << '\n';
		method_name_to_taken.at(method_name).insert({h_data_for_this_line.bytecode_index,h_data_for_this_line});
		getline(inFile,temp); //end the previous line.
	}
}
std::unordered_map<std::string,std::unordered_map<int,BCI_Heuristic_Pair>>& StaticData::data(){
	return method_name_to_taken;
}
int StaticData::get_heuristic_count(){
	return column_count;
}
std::string* StaticData::get_heuristic_names(){
	return names_of_heuristics;
}
