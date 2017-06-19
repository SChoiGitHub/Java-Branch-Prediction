#include <iostream>

#include "DataAggregator.h"

void DataAggregator::dataFusion(std::vector<int*>& heuristics, std::vector<ProfileBranch>& acutal, const std::string& method_name, int heuristic_count, std::string* heuristic_names){
	//Method specific data.
	int* methodSuccess = new int[heuristic_count];
	int* methodPredictions = new int[heuristic_count];
	
	//initialize method specific data.
	for(int x = 0; x < heuristic_count; x++){
		methodSuccess[x] = 0;
		methodPredictions[x] = 0;
	}
	
	//output the first row.
	csv_out << method_name << '\n';
	csv_out << "Index,";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << heuristic_names[x] << ',';		
	}
	csv_out << "Actual Taken,Actual Untaken,Total Branches\n";
	
	//Now output the data, all of it.
	if(heuristics.size() == acutal.size()){
		for(uint y = 0; y < heuristics.size(); y++){
			//Index output
			csv_out << acutal[y].getIndex() << ',';
			for(int x = 0; x < heuristic_count; x++){
				//So, what's the guess?
				if(heuristics[y][x] == 0){
					//Zero means the prediction did not work at all, no guesses here.
					csv_out << "N/A";
				}else if(heuristics[y][x] == 1){
					//One means guess untaken. Add the untaken count to the successes and the total to the total predictions.
					csv_out << acutal[y].getUntaken();
					methodSuccess[x] += acutal[y].getUntaken();
					methodPredictions[x] += acutal[y].getUntaken() + acutal[y].getTaken();
				}else if(heuristics[y][x] == 2){
					//Two means guess taken. Similar to the above, but with taken instead of untaken.
					csv_out << acutal[y].getTaken();
					methodSuccess[x] += acutal[y].getTaken();
					methodPredictions[x] += acutal[y].getUntaken() + acutal[y].getTaken();
				}
				
				if(x != heuristic_count-1){
					//A comma goes where its not the last item to be outputted.
					csv_out << ',';
				}
				
			}
			//Non-Heuristic data output.
			csv_out << ',' << acutal[y].getTaken() << ',' << acutal[y].getUntaken() << ',' << (acutal[y].getUntaken()+acutal[y].getTaken()) << '\n';
		}
	}else{
		throw std::runtime_error("Error: unequal vector sizes");
	}
	
	//Method specific output; data summary.
	csv_out << "Successful Predictions";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << ',' << methodSuccess[x];
		totalSuccess[x] += methodSuccess[x]; //The total data must include this method's information.
	}
	csv_out << "\nTotal Predictions";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << ',' << methodPredictions[x];
		totalPrediction[x] += methodPredictions[x]; //The total data must include this method's information.
	}
	csv_out << "\nPercent Correct";
	for(int x = 0; x < heuristic_count; x++){
		if(methodPredictions[x] != 0){
			//We have made at least one prediction.
			csv_out << ',' << (100.0*methodSuccess[x]/methodPredictions[x]);
		}else{
			//We cannot output, or else we divide by zero.
			csv_out << ",N/A";
		}
	}
	csv_out << "\n\n";
	
	//Begone old data.
	delete[] methodSuccess;
	delete[] methodPredictions;
}

void DataAggregator::aggregate(StaticData& s, ProfileData& p){
	totalSuccess = new int[s.get_heuristic_count()];
	totalPrediction = new int[s.get_heuristic_count()];
	
	//initialize the arrays for total data.
	for(int x = 0; x < s.get_heuristic_count(); x++){
		totalSuccess[x] = 0;	
		totalPrediction[x] = 0;	
	}
	
	
	
	for(auto& static_pair : s.data()){
		for(auto& profile_pair : p.data()){
			if(profile_pair.first.find(static_pair.first) != std::string::npos){
				dataFusion(static_pair.second, profile_pair.second, static_pair.first, s.get_heuristic_count(), s.get_heuristic_names());
				break;
			}
		}
	}
	
	//A lot of outputting.
	csv_out << "Aggregated Data";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << ',' << s.get_heuristic_names()[x];		
	}
	csv_out << "\nSuccessful Predictions";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << ',' << totalSuccess[x];
	}
	csv_out << "\nTotal Predictions";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << ',' << totalPrediction[x];
	}
	csv_out << "\nPercent Correct";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		if(totalPrediction[x] != 0){
			//Total percentage of correct guesses.
			csv_out << ',' << (100.0*totalSuccess[x]/totalPrediction[x]);
		}else{
			//No guesses at all!?
			csv_out << ",N/A";
		}
	}
	
	//Delete old data.
	delete[] totalSuccess;
	delete[] totalPrediction;
}

DataAggregator::DataAggregator(std::string static_data, std::string profile_data){
	//Make the data structures for the input files.
	StaticData sd(static_data);
	ProfileData pd(profile_data);
	//The file name really only makes sense if you use my naming structure.
	csv_out.open(static_data.substr(0,static_data.length()-3) + "_cd.csv");
	//Finally aggregate the data.
	aggregate(sd,pd);
	//File is done.
	csv_out.close();
}


