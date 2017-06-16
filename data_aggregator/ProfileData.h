
#ifndef PROFILE_DATA_H
#define PROFILE_DATA_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>
#include <sstream>
#include <algorithm>

#include "ProfileBranch.h"

class ProfileData{
	public:
		ProfileData();
		~ProfileData();
		ProfileData(std::string s);
	
		
		
	private:
		void beginParse();
		void parseMethod(std::string s);
		
		std::unordered_map<std::string,std::vector<ProfileBranch>> methodName_to_branches;
		std::string line;
		std::ifstream inFile;
};

#endif
