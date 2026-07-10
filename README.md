# Split Bill API for allobank

Simple REST API to calculate split bill settlement between participants.

Built with:

- Java 17
- Spring Boot 3.5.16
- Maven

Monetary calculations use `BigDecimal` to avoid floating point precision issues.

---

## Build

```bash
./mvnw clean package
```

or

```bash
mvn clean package
```

---

## Run

Using Maven

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

Using jar

```bash
java -jar target/split-bill-api-0.0.1-SNAPSHOT.jar
```

---

## Docker

Build image

```bash
docker build -t split-bill-api .
```

Run container

```bash
docker run -p 4110:4110 split-bill-api
```

Application will be available at

```
http://localhost:4110
```

---

## Run Tests

```bash
./mvnw test
```

---

## API

### Calculate Settlement

```
POST /api/v1/settlements
```

Request

```json
{
  "participants": [
    {
      "name": "Alice",
      "paid": 120.00
    },
    {
      "name": "Bob",
      "paid": 60.00
    },
    {
      "name": "Charlie",
      "paid": 0.00
    }
  ]
}
```

Response

```json
{
  "totalExpense": 180.00,
  "amountPerPerson": 60.00,
  "serviceChargePct": 3,
  "serviceChargeAmount": 5.40,
  "settlements": [
    {
      "from": "Charlie",
      "to": "Alice",
      "amount": 60.00
    }
  ]
}
```

---

## Example curl

```bash
curl --location 'http://localhost:4110/api/settlements' \
--header 'Content-Type: application/json' \
--data '{
    "participants":[
        {
            "name":"Alice",
            "paid":120
        },
        {
            "name":"Bob",
            "paid":60
        },
        {
            "name":"Charlie",
            "paid":0
        }
    ]
}'
```

---

## Validation

Current validation rules:

- participants must not be empty
- participant name must not be blank
- paid amount must not be null
- paid amount cannot be negative
- duplicate participant names are rejected

---

## Service Charge

Service charge percentage is calculated dynamically from the configured GitHub username.

Formula:

```
serviceChargePct = (sum of ASCII values of github username) % 10
```

The percentage is never hardcoded.

---

## GitHub Username

```
renskyfall
```

Calculated service charge:

```
3%
```

---

## Tests

Included tests:

- SettlementServiceImplTest
- SettlementUtilTest
- SplitBillApplicationTests

---

## Notes

- Uses BigDecimal for all money calculations.
- Settlement is calculated using the minimum number of transfers based on creditor/debtor balances.
- No database is used since all calculations are performed in-memory for each request.


## Submission Question

"What was the hardest design decision you made while building this, and what trade-off did you accept?"

```
The hardest design decision was choosing what not to build. Given the limited time, I focused on getting the core settlement logic, validation, and tests right instead of implementing extra features like multiple split strategies or persistence. I accepted the trade-off of delivering a smaller but complete and maintainable solution that can be extended later without major changes.
```