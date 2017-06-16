DataAggregator : main.o StaticData.o ProfileBranch.o ProfileData.o
	g++ -std=c++11 -g -Wall main.o StaticData.o ProfileBranch.o ProfileData.o -o DataAggregator 

main.o: main.cpp
	g++ -std=c++11 -g -Wall -c main.cpp

StaticData.o: StaticData.cpp
	g++ -std=c++11 -g -Wall -c StaticData.cpp

ProfileBranch.o: ProfileBranch.cpp
	g++ -std=c++11 -g -Wall -c ProfileBranch.cpp
	
ProfileData.o: ProfileData.cpp
	g++ -std=c++11 -g -Wall -c ProfileData.cpp
	
clean:
	rm *.o *.gch DataAggregator 
