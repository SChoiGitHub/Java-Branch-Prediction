#include "ProfileBranch.h"

ProfileBranch::ProfileBranch();
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
