#include <iostream>

#include "DataAggregator.h"


int main(int argc, char** argv){
	std::cout << "Static Data File: " << argv[1] << '\n';
	std::cout << "Profile Data File: " << argv[2] << '\n';
	
	DataAggregator a_d(argv[1],argv[2]);
}
