# cassandra
Projekt ten jest podmudułem dla projektu [lambda-stack](https://github.com/PuszekSE/lambda-stack). 

## Wstep
Niniejszy projekt, oprócz funkcjonalności związanych z obsługa Cassandry, jest także punktem startowym aplikacji, w ktorym dokonuje się wstępna konfiguracja - definiowanie zapytań, przeglądanie rezultatów. 

## Architektura
### Tabele w bazie danych
Wykorzystywane przez nas tabele mają następujące definicje:

```sql
TABLE queries (
	id int,
	query varchar,
	PRIMARY KEY(id)
);

TABLE results (
	id int,
	result varchar,
	PRIMARY KEY(id)
);

```
### Main
Metoda startowa programu prezentuje się następująco (pewne nieistotne fragmenty zostały pominięte - ```(...)```):

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

Jak nie trudno zauważyć, można tu wyróżnić 4 etapy:
* zarządzanie zapytaniami (```queriesManager```):
** wyświetlanie istniejących w bazie zapytań wraz z ich ID, które posłuży przy pozyskiwaniu rezultatów
** dodanie nowego zapytania
* wybór z dostępnej listy zapytań tych, które zamierzamy odpalić na Stormie (```queries.put(...)```)
* uruchomienie aplikacji Storma ([podmoduł Storm projektu](https://github.com/michallorens/storm-kafka))
* przeglądanie rezultatów dla wykonywanych zapytań (```resultsManager```)
 
### DBHandler

Poniżej przedstawiono klasę obsługującą komunikację z bazą danych:

```java
public class DBHandler {

	public void insertQuery(String query);
	public int getResultsForId(int id);
	public Map<Integer,String> getAllQueries();
	
}
```

Metoda ```insertQuery()``` wstawia podane zapytanie do tabeli ```queries```, a także inicjalizuje powiązany z zapytaniem wiersz w tabeli ```results```.

Metoda ```getResultsForId()``` odpytuje tabelę ```results``` o wynik zapytania o podanym ID. Ponieważ w większości przypadków są to dane liczbowe, metoda zwraca typ całkowity.

Ostatnia z kluczowych metod - ```getAllQueries()``` - pobiera wszystkie istniejące w bazie zapytania i zwraca je w postaci kolekcji ```Map```: ``` {[id_zapytania], [zapytanie]} ```. Warto tu zaznaczyć, iż dane te są cache'owane w aplikacji i uaktualniane w przypadku dodanie kolejnego zapytania do bazy.

## Użycie
