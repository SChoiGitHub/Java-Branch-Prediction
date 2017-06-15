#include <iostream>
#include "StaticData.h"

int main(int argc, char **argv)
{
	std::cout << "Static Data File: " << argv[1] << '\n';
	std::cout << "Profile Data File: " << argv[2] << '\n';
	StaticData sd(argv[1]);
}

