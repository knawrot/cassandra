# cassandra
Projekt ten jest podmudulem dla projektu [lambda-stack](https://github.com/PuszekSE/lambda-stack). 

## Wstep
Niniejszy projekt, oprócz funkcjonalnosci zwiazanych z obsluga Cassandry jest takze punktem startowym aplikacji, w ktorym dokonuje sie wstepna konfiguracja. 

## Architektura
### Main
Metoda startowa programu prezentuje sie nastepujaco (pewne nieistotne fragment zostaly pominiete - ```(...)```):
```java
public static void main(String[] args) {
		(...)
		
		QueriesManager queriesManager = new QueriesManager(scanner, dbHandler);
		queriesManager.operate();
		
		(...)
		
		while (!shouldQuit) {
			(...)
			try {
				queryId = new Integer(text);
				queries.put(queryId, queriesManager.getQuery(queryId));
			(...)
		}
		
		runOnLocalhost();
		
		(...)
		
		ResultsManager resultsManager = new ResultsManager(scanner, dbHandler, queries);
		resultsManager.operate();

		(...)
}
```

## Użycie
