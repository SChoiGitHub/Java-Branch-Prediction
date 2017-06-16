#include "ProfileBranch.h"

ProfileBranch::ProfileBranch(){
	throw std::runtime_error("Error: Blank profile branch.");
}
ProfileBranch::ProfileBranch(int i, int t, int u){
	myIndex = i;
	taken = t;
	untaken = u;
}
int ProfileBranch::getIndex(){
	return myIndex;
}
int ProfileBranch::getTaken(){
	return taken;
}
int ProfileBranch::getUntaken(){
	return untaken;
}
bool ProfileBranch::operator<(ProfileBranch rhs){
	return (this->myIndex < rhs.myIndex);
}
