
#ifndef PROFILE_METHOD_H
#define PROFILE_METHOD_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>

class ProfileMethod{
	public:
		ProfileMethod();
	private:
		std::vector<ProfileBranch> myBranches;
};

#endif
