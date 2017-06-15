#include "ProfileData.h"

ProfileData::ProfileData();{
	throw(std::runtime_error("Error: Missing profile data file to parse"));
}
ProfileData::~ProfileData(){
	
}
ProfileData::ProfileData(std::string s){
	inFile.open(s); //open new file.
	
	
	if(inFile.is_open()){

	}else{
		throw(std::runtime_error("Error: Missing profile data file to parse"));
	}
	inFile.close();
}
