# ml-simios
Projeto símios para desafio proposto pela empresa Mercado Livre

Para executar a API basta fazer download do código fonte, e configurar apropriadamente o arquivo application.yml:
  - Caso queira utilizar uma base local Postgres, basta criar um arquivo .env e preencher valores para as variáveis JDBC_DATABASE_URL, JDBC_DATABASE_USERNAME e JDBC_DATABASE_PASSWORD;
  - Caso queira utilizar com uma base em memória H2, basta alterar o perfil ativo para "test" no arquivo application.yml, ficando da seguinte forma: 
     spring:
      profiles:
       active: test

Após isso, basta rodar o comando: "mvnw clean install spring-boot:run" em qualquer terminal e a aplicação estará disponível para uso.
