import os
import random

# ==========================================
# CONFIGURAÇÃO DOS CENÁRIOS
# ==========================================
# Descobre automaticamente o caminho da sua Área de Trabalho (Desktop) no Windows
CAMINHO_DESKTOP = os.path.join(os.environ['USERPROFILE'], 'Desktop')

# A lista de cenários que a sua professora pediu
CENARIOS_QUANTIDADE = [10, 100, 1000, 10000]

# Palavras que o nosso script vai misturar nos nomes dos arquivos
PALAVRAS_CHAVE = ["faculdade", "trabalho", "relatorio", "tcc", "comprovante", "boleto", "foto", "documento", "nota", "projeto"]
EXTENSOES = [".txt", ".pdf", ".docx", ".png", ".jpg", ".xlsx"]

# ==========================================
# EXECUÇÃO
# ==========================================
def gerar_arquivos_em_lote():
    print("Iniciando a geração de cenários de teste na Área de Trabalho...")
    print("-" * 50)
    
    # Faz um loop passando por cada quantidade (10, 100, 1000...)
    for qtd in CENARIOS_QUANTIDADE:
        nome_pasta = f"teste_carga_{qtd}"
        caminho_pasta = os.path.join(CAMINHO_DESKTOP, nome_pasta)
        
        # Cria a pasta específica para esta quantidade
        if not os.path.exists(caminho_pasta):
            os.makedirs(caminho_pasta)
            
        print(f"Gerando {qtd} arquivos na pasta: {nome_pasta}...")
        
        # Gera os arquivos dentro desta pasta
        for i in range(qtd):
            palavra_sorteada = random.choice(PALAVRAS_CHAVE)
            extensao_sorteada = random.choice(EXTENSOES)
            
            nome_arquivo = f"{palavra_sorteada}_teste_{i}{extensao_sorteada}"
            caminho_completo = os.path.join(caminho_pasta, nome_arquivo)
            
            with open(caminho_completo, 'w', encoding='utf-8') as arquivo:
                arquivo.write(f"Arquivo de teste simulado número {i}. Pertence à categoria {palavra_sorteada}.")
                
    print("-" * 50)
    print("Todos os cenários de teste foram gerados com sucesso na sua Área de Trabalho!")

if __name__ == "__main__":
    gerar_arquivos_em_lote()