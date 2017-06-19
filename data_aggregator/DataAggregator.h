#include <iostream>

#include "ProfileData.h"
#include "StaticData.h"

class DataAggregator{
	public:
		void dataFusion(std::vector<int*>& heuristics, std::vector<ProfileBranch>& acutal, const std::string& method_name, int heuristic_count, std::string* heuristic_names);
		void aggregate(StaticData& s, ProfileData& p);
		DataAggregator(std::string static_data, std::string profile_data);
	private:
		std::ofstream csv_out;
		int* totalSuccess;
		int* totalPrediction;
};
