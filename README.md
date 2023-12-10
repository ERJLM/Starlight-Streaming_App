# Starlight

## Projeto no âmbito da unidade curricular de Programação de Dispositivos Movéis por Eliandro Melo e Patrick Daniel.

### Android App
##### Pontos importantes:
###### Utilizamos:
- O Exoplayer para o reprodutor de video.
- O NanoHTTPD como servidor embebido para a app.
- O Retrofit para fazer as requests da app para o Application Server.
- O algoritmo SHA256 para calcular e verificar as hashes dos chunks dos filmes.

### Cloud Services

Serviços desenvolvidos em ambiente Windows (11) e testados em ambiente Linux (Ubuntu 22.04 LTS).

#### Application-Server-API-Starlight (Request_solver): 
Servidor (Jetty) com a REST API (usando Jersey) na porta 8080 da VM1 (34.170.81.106) na Cloud, responsável por tratar dos pedidos que envolvem dados que estão na base de dados.

#### Application-Server-File-Uploader-Starlight (File_Receiver_Uploader): 
Servidor (Tomcat) com uma Servlet na porta 9090 da VM1 (34.170.81.106) na Cloud, responsável por tratar do Upload de vídeos (isto é, receber os vídeos, rodar FFMPEG, gerar as hashes SHA-256, inserir na BD e enviar os chunks para o Storage Volume "na VM2").

#### Storage-Volume-Starlight (Storage_Volume_File_Receiver): 
Servidor (Tomcat) com uma servlet na porta 6666 da VM2 (34.39.11.61) na Cloud, responsável por receber os chunks dos vídeos e colocar no diretório correto do Storage Volume (NGINX).

#### Database-Starlight: 
Base de dados SQL na VM1 com 3 tabelas (user_tbl, movie_tbl e chunk_tbl).

 
###### Todos os módulos foram compilados com o Maven.
