#include <iostream>

#include "ProfileData.h"
#include "StaticData.h"

class DataAggregator{
	public:
		//Preconditions: static_data and profile_data are the names of the files for the static data and profile data respectively.
		//Postconditions: DataAggregator created.
		DataAggregator(std::string static_data, std::string profile_data);
		//Preconditions: For a specific method, information about the heuristics, the name of the method, and the names and number of the heuristics...
		//Postconditions: Aggregates the data.
		void dataFusion(std::unordered_map<int,BCI_Heuristic_Pair>& heuristics, std::vector<ProfileBranch>& actual, const std::string& method_name, int heuristic_count, std::string* heuristic_names);
		//Preconditions: static_data and profile_data exist
		//Postconditions: Creates combined data created along with raw aggregation data.
		void aggregate(StaticData& s, ProfileData& p);
	private:
		std::ofstream csv_out;
		std::ofstream cd_out;
		int* totalSuccess;
		int* totalPrediction;
		int* methodGuessType;
};
