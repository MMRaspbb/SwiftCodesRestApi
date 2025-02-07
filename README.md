### SwiftCodesRestApi
A SWIFT code, also known as a Bank Identifier Code (BIC), is a unique identifier of a bank's branch or headquarter. It ensures that international wire transfers are directed to the correct bank and branch, acting as a bank's unique address within the global financial network.
SwiftCodesRestApi uses https://docs.google.com/spreadsheets/d/1iFFqsu_xruvVKzXAadAAlDBpIuU51v-pfIEU5HeGa8w/edit?usp=drivesdk spreadsheet as initial data for the database, the data from the spreadsheet is parsed and stored. Application exposes 4 endpoints:
 - GET: /v1/swift-codes/{swift-code} - returns information about bank with given swift-code
 - GET:  /v1/swift-codes/country/{countryISO2code} - returns information about all banks in country with given countryISO2 code
 - POST:  /v1/swift-codes - adds bank with data from body to the database (body):
    ```json
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool,
    "swiftCode": string,
    }
    ```
- DELETE:  /v1/swift-codes/{swift-code} - removes bank with given swift-code from the database
## How to run
Application has been contenerized and build, to run it you have to have docker installed on your PC, open terminal in the projects directory and run the command:
```
docker-compose up --build -d
```
After the building process is completed the application is ready to be used, it's endpoints are avalible on:
```
localhost:8080
```
