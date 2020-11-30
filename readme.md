## Java GraphQL Federation Server Example With Spring Boot

This repo showcases Java GraphQL Federation Server with Spring Boot 

npm --proxy http://localhost:10809 install if using v2ray

Launch: 
- start four java services (`inventory`, `product`, `review`, `user`):
```
mvn install 
mvn run
```
or use any other means to start them~
- start graphql server. You must have node and npm installed. When in `./apollo-federation-js` run the following:
```
npm install
node gateway.js
```

Further reads: 
- https://blog.logrocket.com/the-what-when-why-and-how-of-federated-graphql/
- https://www.apollographql.com/blog/using-apollo-federation-with-local-schemas/
- https://www.apollographql.com/docs/federation/
- https://www.apollographql.com/docs/federation/implementing-services/

Query examples: 
```
{
  topProducts {
    name
    price
    upc
    reviews{
      author{
        id
        name
        username
        address {
          city
          country
        }
      }
      body
    }
  }
}
```
```
{
  trivia {
    id
    body
    author {
      id
      name
    }
  }
}
```