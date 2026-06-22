@echo off
echo Preparando ambiente de testes...

:: Limpa as pastas antigas se elas existirem (evita erro se rodar duas vezes)
if exist tests\source rmdir /s /q tests\source
if exist tests\destino rmdir /s /q tests\destino

:: Cria as pastas do zero
mkdir tests\source
mkdir tests\destino

echo Criando arquivos de teste falsos...
type nul > tests\source\trabalho1.txt
type nul > tests\source\trabalho2.pdf
type nul > tests\source\imagem1.jpg
type nul > tests\source\imagem2.img

echo Executando o Intelli-File...
:: Executa o sistema apontando para as pastas relativas que acabamos de criar
mvn compile exec:java -Dexec.mainClass="com.intelli_file.App" -Dexec.args="organize-keyword tests\source tests\destino"

echo Teste finalizado! Confira a pasta tests\destino
pause