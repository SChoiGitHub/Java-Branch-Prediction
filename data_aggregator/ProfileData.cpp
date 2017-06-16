#include "ProfileData.h"

ProfileData::ProfileData(){
	
}

ProfileData::~ProfileData(){
	
}
ProfileData::ProfileData(std::string s){
	methodName_to_branches = std::unordered_map<std::string,std::vector<ProfileBranch>>();
	inFile.open(s); //open new file.
	
	bool found_total_number_of_methods_queued = false;
	bool found_total_number_of_methods_compiled = false;
	bool found_branch_statistics_message = false;
	
	
	if(inFile.is_open()){
		while(getline(inFile,line)){
			//std::cout << line << '\n';
			if(line.substr(0,30) == "Total number of methods queued"){
				//This should be first
				//std::cout << "found tnomq\n";  //Debug, the thing that is found is the acronym
				found_total_number_of_methods_queued = true;
			}
			if(line.substr(0,32) == "Total number of methods compiled"){
				//This should be second
				//std::cout << "found tnomc\n"; //Debug, the thing that is found is the acronym
				found_total_number_of_methods_compiled = found_total_number_of_methods_queued;
			}
			if(line == "Print Method Branch Statistics"){
				//this should be third
				//std::cout << "found pmbs\n";  //Debug, the thing that is found is the acronym
				found_branch_statistics_message = found_total_number_of_methods_compiled;
			}		
			if(found_branch_statistics_message && found_total_number_of_methods_compiled && found_total_number_of_methods_queued){
				//This is where the parsing of the data begins.
				beginParse();
				break;
			}
		}
		std::cout << "Parsing Profiling File Complete\n";
	}else{
		throw(std::runtime_error("Error: Missing profile data file to parse"));
	}
	inFile.close();
}
void ProfileData::beginParse(){
	while(getline(inFile,line)){
		if(line.length() > 2){
			//std::cout << line << '\n';
			parseMethod(line);
		}
	}
}
void ProfileData::parseMethod(std::string s){
	std::string method_name = s;
	std::stringstream temp_stream;
	std::string temp;
	
	//std::cout << "Method: " << line << '\n'; //Debug
	
	methodName_to_branches[s] = std::vector<ProfileBranch>();
	
	while(getline(inFile,line)){
		if(line.length() > 3){
			temp_stream = std::stringstream(line);
			
			temp_stream >> temp; //Get BCI:
			temp_stream >> temp; // Get the index number, but it has a comma at its end
			temp = temp.substr(0,temp.length()-1); //cut off the comma.
			
			int index = std::stoi(temp);
			//std::cout << index << '\t'; //DEBUG
			
			temp_stream >> temp; //Get Taken:
			temp_stream >> temp; // Get the taken number, but it has a comma at its end
			temp = temp.substr(0,temp.length()-1); //cut off the comma.
			
			int taken = std::stoi(temp);
			//std::cout << taken << '\t'; //DEBUG
			
			temp_stream >> temp; //Get Not-Taken:
			temp_stream >> temp; // Get the not taken number, but it has a comma at its end
			temp = temp.substr(0,temp.length()-1); //cut off the comma.
			
			int untaken = std::stoi(temp);
			//std::cout << untaken << '\n'; //DEBUG
			
			methodName_to_branches[s].push_back(ProfileBranch(index,taken,untaken));
			
			continue; //we're done.
		}else{
			break;
		}
	}
	//Sort the method's branches, they are not in order sometimes.
	std::sort(methodName_to_branches[s].begin(),methodName_to_branches[s].end());
	
	/*
	for(std::vector<ProfileBranch>::iterator a = methodName_to_branches[s].begin(); a != methodName_to_branches[s].end(); a++){
		std::cout << '\t' << a->getIndex() << '\n'; //Debug
	}
	*/
}
std::unordered_map<std::string,std::vector<ProfileBranch>>& ProfileData::data(){
	return methodName_to_branches;
}
