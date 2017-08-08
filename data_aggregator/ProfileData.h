
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
		//Preconditions: None
		//Postconditions: None
		ProfileData();
		//Preconditions: ProfileData exists
		//Postconditions: ProfileData is deleted
		~ProfileData();
		//Preconditions: ProfileData exists and s is the name of the file of the profile file
		//Postconditions: ProfileData contains information from the profile file.
		ProfileData(std::string s);
		//Preconditions: ProfileData exists
		//Postconditions: No Changes
		//Return: returns the data ProfileData has.
		std::unordered_map<std::string,std::vector<ProfileBranch>>& data();
		
	private:
		//Preconditions: ProfileData exists
		//Postconditions: ProfileData begins parsing data.
		void beginParse();
		//Preconditions: ProfileData exists and s is a name of a method
		//Postconditions: The method name is altered so that it could be easily matched to static data.
		void parseMethod(std::string s);
		
		std::unordered_map<std::string,std::vector<ProfileBranch>> methodName_to_branches;
		std::string line;
		std::ifstream inFile;
};

#endif
