
#ifndef PROFILE_BRANCH_H
#define PROFILE_BRANCH_H

#include <iostream>
#include <string>
#include <fstream>
#include <exception>
#include <unordered_map>
#include <vector>

class ProfileBranch{
	public:
		ProfileBranch();
		ProfileBranch(int i, int t, int u);
		int getIndex();
		int getTaken();
		int getUntaken();
		
		bool operator<(ProfileBranch rhs);
	private:
		int myIndex;
		int taken;
		int untaken;
};

#endif
