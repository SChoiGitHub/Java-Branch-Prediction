
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
		//Preconditions: None
		//Postconditions: None
		ProfileBranch();
		//Preconditions: i is the bci, t is the # of taken branches, and u is the # of untaken branches.
		//Postconditions: None
		ProfileBranch(int i, int t, int u);
		//Preconditions: ProfileBranch exists
		//Postconditions: none
		//Returns: myIndex
		int getIndex();
		//Preconditions: ProfileBranch exists
		//Postconditions: none
		//Returns: taken
		int getTaken();
		//Preconditions: ProfileBranch exists
		//Postconditions: none
		//Returns: untaken
		int getUntaken();
		
		//Just a comparison of indexes
		bool operator<(ProfileBranch rhs);
	private:
		int myIndex;
		int taken;
		int untaken;
};

#endif
