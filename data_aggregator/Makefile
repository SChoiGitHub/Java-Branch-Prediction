DataAggregator : main.o StaticData.o
	g++ -std=c++11 -g -Wall main.o StaticData.o -o DataAggregator 

main.o: main.cpp
	g++ -std=c++11 -g -Wall -c main.cpp

StaticData.o: StaticData.cpp
	g++ -std=c++11 -g -Wall -c StaticData.cpp

clean:
	rm *.o *.gch DataAggregator 
