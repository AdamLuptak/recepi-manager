# Recipe rest api app

## Documentation
  Application consist next layers:
   - Rest Api 
   - Service layer 
   - Repository layer

### Rest api

Api consist of basic CRUD operations, and advance filtering option
System is prepared to be used with multiple users, Permission and role management is
not part of this solution (in future we can use any identity provider). For interaction with api client have to specify `X-USERID` as 
header in every HTTP request.

| Endpoint            | Explanation                                       | Swagger link                                                                  |
|---------------------|---------------------------------------------------|-------------------------------------------------------------------------------|
| create recipe       | Will create recipe in the system                  | <http://localhost:8080/swagger-ui/index.html#/recipe-controller/createRecipe> |
| update recipe       | Will create recipe in the system it is idempotent | <http://localhost:8080/swagger-ui/index.html#/recipe-controller/updateRecipe> |
| get recipe by id    | Fetch recipe detail                               | <http://localhost:8080/swagger-ui/index.html#/recipe-controller/getRecipe>    |
| delete recipy by id | Remove recipe from the system                     | <http://localhost:8080/swagger-ui/index.html#/recipe-controller/deleteRecipe> |
| get recipies        | Fetch recipies by specific filter criteria        | <http://localhost:8080/swagger-ui/index.html#/recipe-controller/getAll>       |

### Database migration

Application use embedded liquid base migration tool, Use evolution scripts to migrate schemas.
Scripts are store in ``src/main/resources/changelog``

### Decision log

- Framework Spring boot, easy to work framework to build production ready application, framework is very popular,
- QueryDsl library for writing SQL queries in java dsl it is not coupled with specific database vendor
- Postgresql as main rdbms, Open source have rich features, which can be use in future

Above-mentioned tech stack is very common, there are lots of engineers familiar with the stack.
It is well-known and use be a thousand of companies in production env. 


## Next steps
 - Adding more test, there is only IT test and Junit test for min functional requirements, not all edge classes are cover
 - Use full-text search capabilities from postgresql or any other technology. For reading endpoints to have fast responses
 - integrate with Identity management system (KeyCloack) implement roles, permissions.
 - Create event bus to produce events about entities.

# Build and run details

### Run integration tests
``./gradlew clean build integrationTest``

### Run postgresql
```docker-compose up```

### Run application from gradle
```./gradlew bootRun```

### Build container with JIB./gradlew 
```./gradlew jibDockerBuild```

### Run application locally as docker container
```
docker run \
-e  SPRING_DATASOURCE_URL='jdbc:postgresql://postgres:5432/postgres' \
-e  SPRING_DATASOURCE_USERNAME='postgres' \
-e  SPRING_DATASOURCE_PASSWORD='postgres' \
--network=host\
-p 8080:8080  recipe-be:latest
```