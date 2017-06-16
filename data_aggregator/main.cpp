#include <iostream>

#include "ProfileData.h"
#include "StaticData.h"

void dataFusion(std::vector<int*>& heuristics, std::vector<ProfileBranch>& acutal, const std::string& method_name, int heuristic_count, std::string* heuristic_names){
	//std::cout << "Method: " << method_name << '\n';
	if(heuristics.size() == acutal.size()){
		for(int x = 0; x < heuristic_count; x++){
			std::cout << "\tHeuristic " << heuristic_names[x] << '\n';
			for(uint y = 0; y < heuristics.size(); y++){
				std::cout << "\t\tBranch Index " << acutal[y].getIndex() << ": ";
				
				if(heuristics[y][x] == 0){
					std::cout << "No Prediction";
				}else if(heuristics[y][x] == 1){
					std::cout << "Predict Untaken ";
					std::cout << '(' << acutal[y].getUntaken() << '/'  << (acutal[y].getUntaken()+acutal[y].getTaken()) << ')';
					
				}else if(heuristics[y][x] == 2){
					std::cout << "Predict Taken ";
					std::cout << '(' << acutal[y].getTaken() << '/'  << (acutal[y].getUntaken()+acutal[y].getTaken()) << ')';
				}
				
				
				
				std::cout << '\n';
			}
		}
	}else{
		throw std::runtime_error("Error: unequal vector sizes");
	}
}

void aggregate(StaticData& s, ProfileData& p){
	for(auto& static_pair : s.data()){
		for(auto& profile_pair : p.data()){
			if(profile_pair.first.find(static_pair.first) != std::string::npos){
				std::cout << static_pair.first << "\t\t" << profile_pair.first << "\n";
				dataFusion(static_pair.second, profile_pair.second, static_pair.first, s.get_heuristic_count(), s.get_heuristic_names());
			}
		}
	}
}

int main(int argc, char **argv)
{
	std::cout << "Static Data File: " << argv[1] << '\n';
	std::cout << "Profile Data File: " << argv[2] << '\n';
	StaticData sd(argv[1]);
	ProfileData pd(argv[2]);
	aggregate(sd,pd);
}


