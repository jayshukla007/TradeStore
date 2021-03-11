# TradeStore
There is a scenario where thousands of trades are flowing into one store, assume any way of
transmission of trades. We need to create a one trade store, which stores the trade in the following
order

Trade Id Version Counter-Party Id Book-Id Maturity DateCreated DateExpired
T1 1 CP-1 B1 20/05/2020 <todaydate> N
T2 2 CP-2 B1 20/05/2021 <todaydate> N
T2 1 CP-1 B1 20/05/2021 14/03/2015 N
T3 3 CP-3 B2 20/05/2014 <today date> Y

There are couples of validation, we need to provide.
1. During transmission if the lower version is being received by the store it will reject the trade and
throw an exception. If the version is same it will override the existing record.
2. Store should not allow the trade which has less maturity date then today date.
3. Store should automatically update the expire flag if in a store the trade crosses the maturity
date.

# Requirements

The project requires Java 1.8 or higher.

The project makes use of Gradle and uses the Gradle wrapper, which means you don't need Gradle installed.

# Useful Gradle commands

The project makes use of Gradle and uses the Gradle wrapper to help you out carrying some common tasks such as building the project or running it.

# List all Gradle tasks

List all the tasks that Gradle can do, such as build and test.

$ ./gradlew tasks

# Build the project

Compiles the project, runs the test and then creates an executable JAR file
$ ./gradlew build

# Run the tests

There are two types of tests, the unit tests and the functional tests. These can be executed as follows.

  Run unit tests only
  
    $ ./gradlew test
    
  Run functional tests only
  
    $ ./gradlew functionalTest
    
  Run both unit and functional tests
  
    $ ./gradlew check
    
# Run the application

Run the application which will be listening on port 8080.

$ ./gradlew bootRun

# API

Below is a list of API endpoints with their respective input and output. Please note that the application needs to be running for the following endpoints to work.
  
  # Store Trades
  
    Endpoint
    
    POST /trade
    
    Example of body
    {
    "tradeId": "T4",
    "version": 2,
    "counterPartyId": "CP-2",
    "bookId": "B1",
    "maturityDate" : "26/05/2021",
    "expired" : "N"  
    }
 
 # Get all trades
 
   Endpoint
   
   GET /trade 

    
   
