#include <iostream>

#include "DataAggregator.h"

DataAggregator::DataAggregator(std::string static_data, std::string profile_data){
	//Make the data structures for the input files.
	StaticData sd(static_data);
	ProfileData pd(profile_data);
	//The file names here really only makes sense if you use my naming structure.
	csv_out.open(static_data.substr(0,static_data.length()-3) + "_raw_aggregation.csv");
	cd_out.open(static_data.substr(0,static_data.length()-3) + "_cd");
	//Finally aggregate the data.
	aggregate(sd,pd);
	//File is done.
	csv_out.close();
	cd_out.close();
}

void DataAggregator::dataFusion(std::unordered_map<int,BCI_Heuristic_Pair>& heuristics, std::vector<ProfileBranch>& actual, const std::string& method_name, int heuristic_count, std::string* heuristic_names){
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
	csv_out << "Index\t";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << heuristic_names[x] << '\t';		
	}
	csv_out << "Actual Taken\tActual Untaken\tTotal Branches\n";
	
	//Now we actually fuse the data.
	for(int pb = 0; pb < (signed)actual.size(); pb++){
		//Output the index of this jump
		csv_out << actual[pb].getIndex() << '\t';
		try{
			for(int x = 0; x < heuristic_count; x++){
				if(heuristics.at(actual[pb].getIndex()).heuristic_info[x] == 0){
					//Zero means the prediction did not work at all, no guesses here.
					csv_out << "N/A";
				}else if(heuristics.at(actual[pb].getIndex()).heuristic_info[x] == 1){
					//One means guess untaken. Add the untaken count to the successes and the total to the total predictions.
					csv_out << actual[pb].getUntaken();
					methodSuccess[x] += actual[pb].getUntaken();
					methodPredictions[x] += actual[pb].getUntaken() + actual[pb].getTaken();
					
					methodGuessType[x] = 1;
				}else if(heuristics.at(actual[pb].getIndex()).heuristic_info[x] == 2){
					//Two means guess taken. Similar to the above, but with taken instead of untaken.
					csv_out << actual[pb].getTaken();
					methodSuccess[x] += actual[pb].getTaken();
					methodPredictions[x] += actual[pb].getUntaken() + actual[pb].getTaken();
					
					methodGuessType[x] = 2;
				}
				if(x != heuristic_count-1){
					//A comma goes where its not the last item to be outputted.
					csv_out << '\t';
				}
			}
			
			
			
			//Non-Heuristic data output.
			csv_out << '\t' << actual[pb].getTaken() << '\t' << actual[pb].getUntaken() << '\t' << (actual[pb].getUntaken()+actual[pb].getTaken()) << '\n';
		}catch(std::exception e){
			csv_out << "No Static Data Found" << '\n';
		}
	}
	
	
	
	/*
	//Now output the data, all of it.
	if(heuristics.size() == actual.size()){
		for(uint y = 0; y < heuristics.size(); y++){
			//Index output
			csv_out << actual[y].getIndex() << '\t';
			for(int x = 0; x < heuristic_count; x++){
				//So, what's the guess?
				if(heuristics[y].heuristic_info[x] == 0){
					//Zero means the prediction did not work at all, no guesses here.
					csv_out << "N/A";
				}else if(heuristics[y].heuristic_info[x] == 1){
					//One means guess untaken. Add the untaken count to the successes and the total to the total predictions.
					csv_out << actual[y].getUntaken();
					methodSuccess[x] += actual[y].getUntaken();
					methodPredictions[x] += actual[y].getUntaken() + actual[y].getTaken();
				}else if(heuristics[y].heuristic_info[x] == 2){
					//Two means guess taken. Similar to the above, but with taken instead of untaken.
					csv_out << actual[y].getTaken();
					methodSuccess[x] += actual[y].getTaken();
					methodPredictions[x] += actual[y].getUntaken() + actual[y].getTaken();
				}
				
				if(x != heuristic_count-1){
					//A comma goes where its not the last item to be outputted.
					csv_out << '\t';
				}
				
			}
			//Non-Heuristic data output.
			csv_out << '\t' << actual[y].getTaken() << '\t' << actual[y].getUntaken() << '\t' << (actual[y].getUntaken()+actual[y].getTaken()) << '\n';
		}
	}else{
		throw std::runtime_error("Error: unequal vector sizes\n" + method_name);
	}
	*/
	
	//Method specific output; data summary.
	csv_out << "Successful Predictions";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << '\t' << methodSuccess[x];
		totalSuccess[x] += methodSuccess[x]; //The total data must include this method's information.
	}
	csv_out << "\nTotal Predictions";
	for(int x = 0; x < heuristic_count; x++){
		csv_out << '\t' << methodPredictions[x];
		totalPrediction[x] += methodPredictions[x]; //The total data must include this method's information.
	}
	csv_out << "\nPercent Correct";
	for(int x = 0; x < heuristic_count; x++){
		if(methodPredictions[x] != 0){
			//We have made at least one prediction.
			csv_out << '\t' << (100.0*methodSuccess[x]/methodPredictions[x]);
		}else{
			//We cannot output, or else we divide by zero.
			csv_out << "\tN/A";
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
	methodGuessType = new int[s.get_heuristic_count()];
	
	//initialize the arrays for total data.
	for(int x = 0; x < s.get_heuristic_count(); x++){
		totalSuccess[x] = 0;	
		totalPrediction[x] = 0;	
	}
	
	
	int totalMethods = 0;
	for(auto& static_pair : s.data()){
		//Look at each static data pair.
		for(auto& profile_pair : p.data()){
			//Look at each profile data pair.
			try{
				if(profile_pair.first == static_pair.first){
					//std::cout << ("\tAggregating: " + static_pair.first + "\n");
					//If we can find the name of the static pair in the profile pair name, we fuse the data..
					dataFusion(static_pair.second, profile_pair.second, static_pair.first, s.get_heuristic_count(), s.get_heuristic_names());
					totalMethods++;
					break;
				}
			}catch(std::exception e){
				continue;
			}
		}
	}
	
	//A lot of outputting.
	csv_out << "Aggregated Data";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << '\t' << s.get_heuristic_names()[x];		
	}
	csv_out << "\nSuccessful Predictions";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << '\t' << totalSuccess[x];
	}
	csv_out << "\nTotal Predictions";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		csv_out << '\t' << totalPrediction[x];
	}
	csv_out << "\nPercent Correct";
	for(int x = 0; x < s.get_heuristic_count(); x++){
		if(totalPrediction[x] != 0){
			//Total percentage of correct guesses.
			csv_out << '\t' << (100.0*totalSuccess[x]/totalPrediction[x]);
			
			if(methodGuessType[x] == 1){
				cd_out << s.get_heuristic_names()[x] << " " << (1.0 - 1.0*totalSuccess[x]/totalPrediction[x]) << '\n';
			}else{
				cd_out << s.get_heuristic_names()[x] << " " << (1.0*totalSuccess[x]/totalPrediction[x]) << '\n';
			}
		}else{
			//No guesses at all!?
			csv_out << "\tN/A";
		}
	}
	
	
	
	
	csv_out << "\n\nTotal Number of Methods Analyzed\t" << totalMethods;
	//Delete old data.
	delete[] totalSuccess;
	delete[] totalPrediction;
	delete[] methodGuessType;
}




