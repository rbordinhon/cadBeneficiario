# Cadastro Beneficiario

# Arquitetura

    A arquitetura do código é baseada no padrao RestFULL, utilizando como 
    build a ferramenta maven.
    Onde:
       O metodo PUT estou utilizabdo para operação de atualização do beneficiario
       O metodo GET nas operaçoes de busca beneficiario
       O metodo DELETE nas operaçoes de remoção de registro
       O metodo POST nas operaçoes de atualização de registro

    
# Swagger
Para obter a documentação dos endpoints acessar o endpoint 
http://localhost:8080/swagger-ui/index.html 
ou atraves do arquivo swagger.json
, para acessar não é preciso fazer autenticação com o usuário
    
# Build 
    Necessario instalar a versão 17 na máquina
    Para o build do projeto executar o comando mvnw clean install

# Nota 1
    Caso tenha mais de um java instalado, utilizar o java Versao 17 e configurar a variavel de ambiente 
    JAVA_HOME com o caminho da versao do java no computador
