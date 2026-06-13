# Projeto Gerenciador de Arquivos 

### Arquitetura
A arquitetura de pastas pensadas para o projeto é a seguinte: 
> src/main/ <br>
> ├── java/com/fileorganizer/ <br>
> │   ├── App.java <br>
> │   ├── application/ <br>
> │   │   ├── controller/ -> Meio de comunicação do front/cli com o sistema, decide quais services chamar ex. FileController <br>
> │   │   └── service/ -> Maestro do progama, coordena scanner + regras + movimentação de arquivos ex. FileService, AIService<br>
> │   │ <br>
> │   ├── domain/ <br>
> │   │   ├── model/ -> Classes para abstração de objetos ex. FileItem, Directory<br> 
> │   │   └── rule/ -> Lógica para classificação dos arquivos ex. FileTypeRule, SizeRule, KeyWordRule<br> 
> │   │ <br>
> │   ├── infrastructure/ <br>
> │   │   ├── filesystem/ -> Classes que realizam operações com os aquivos ex. FileScanner, FileMover <br>
> │   │   └── ai/ -> (futuro)<br>
> │   │ <br>
> │   ├── presentation/ <br>
> │   │   ├── cli/ -> Atualmente como usa o programa ex. CommandRunner, CommandParser <br>
> │   │   └── ui/ -> UI feita com JavaFX (futuro) <br>
> │   │ <br>
> │   └── shared/ <br>
> │       └── util/ Classes utilizadas por todo sistem ex. Logger <br>
> │ <br>
> └── resources/ <br>
>     ├── config/ -> Arquivos de configuração do usuário ex. Extensions.json, que define pastas para determinadas extensões <br>
>     └── templates/ -> Opções de organizazões padrão <br>


### Funcionalidades
#### Organização de Diretórios
- Organização simples baseada apenas em lógica, os arquivos são divididos baseado no template escolhido
- Organização focada utilizando keywords, AI e lógica

#### Pesquisa de Arquivos
- Utiliza a pesquisa baseado no template

### Getting Started
Tenha certeza que a JDK Java versão 21, JavaFX
