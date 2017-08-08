#include <iostream>

#include "DataAggregator.h"


int main(int argc, char** argv){
	//Output the data files we are using.
	std::cout << "Static Data File: " << argv[1] << '\n';
	std::cout << "Profile Data File: " << argv[2] << '\n';
	
	//Aggregate them.
	DataAggregator a_d(argv[1],argv[2]);
}
