
#ifndef PROFILE_DATA_H
#define PROFILE_DATA_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>

class ProfileData{
	public:
		ProfileData();
		~ProfileData();
		ProfileData(std::string s);
	
	private:
		std::ifstream inFile;
};

#endif
