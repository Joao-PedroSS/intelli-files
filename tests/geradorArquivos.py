import os
import random

# ==========================================
# CONFIGURAÇÃO DOS CENÁRIOS
# ==========================================
CAMINHO_DESKTOP = os.path.join(os.environ['USERPROFILE'], 'Desktop')

CENARIOS_QUANTIDADE = [10, 100, 1000, 10000]

# ==========================================
# MAPA DE EXTENSÕES → CATEGORIA
# (espelho do seu JSON de tipos de arquivo)
# ==========================================
EXTENSOES_POR_CATEGORIA = {
    "Imagens":     ["jpg", "jpeg", "png", "gif", "webp", "svg", "bmp", "ico", "tiff", "avif", "heic", "raw", "psd", "ai"],
    "Vídeos":      ["mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v", "3gp", "vob", "ogv"],
    "Áudios":      ["mp3", "wav", "flac", "aac", "ogg", "m4a", "wma", "opus", "aiff", "mid"],
    "Documentos":  ["pdf", "docx", "doc", "txt", "odt", "rtf", "xlsx", "xls", "ods", "pptx", "ppt", "odp", "epub", "md"],
    "Código":      ["js", "ts", "py", "java", "c", "cpp", "cs", "php", "rb", "go", "rs", "swift", "kt", "html", "css", "sh", "sql"],
    "Comprimidos": ["zip", "rar", "7z", "tar", "gz", "bz2", "xz", "zst"],
    "Dados":       ["json", "xml", "yaml", "yml", "csv", "toml", "ini", "env"],
    "Fontes":      ["ttf", "otf", "woff", "woff2"],
    "Sistema":     ["exe", "dmg", "apk", "iso", "dll", "so", "bin"],
}

# Lista plana de todas as extensões (para uso geral)
TODAS_EXTENSOES = [ext for exts in EXTENSOES_POR_CATEGORIA.values() for ext in exts]

# ==========================================
# MAPA DE CATEGORIAS → KEYWORDS
# (espelho do seu JSON de regras de organização)
# ==========================================
CATEGORIAS_KEYWORDS = {
    "Financas": [
        "boleto", "comprovante", "fatura", "recibo", "nubank", "pix",
        "extrato", "imposto", "irpf", "declaracao", "pagamento", "transferencia",
        "cartao", "nota_fiscal", "nfe", "danfe", "tributario", "financeiro",
        "investimento", "divida", "emprestimo", "mei", "cnpj",
    ],
    "Estudo": [
        "tcc", "resumo", "prova", "trabalho", "slide", "aula", "exercicio",
        "computacao", "monografia", "artigo", "referencia", "pesquisa",
        "universidade", "faculdade", "vestibular", "enem", "apostila",
        "materia", "disciplina", "semestre", "laboratorio", "relatorio",
        "metodologia", "educacao",
    ],
    "Esportes": [
        "volei", "campeonato", "treino", "tabela", "torneio", "futebol",
        "basquete", "natacao", "corrida", "academia", "jogo", "partida",
        "placar", "escalacao", "treinamento", "atletismo", "competicao",
        "classificacao", "planilha_treino",
    ],
    "Documentos_Pessoais": [
        "rg", "cnh", "cpf", "passaporte", "contrato", "curriculo",
        "certidao", "nascimento", "casamento", "titulo_eleitor", "diploma",
        "historico", "laudo", "atestado", "procuracao", "escritura",
        "comprovante_residencia", "seguro", "sus", "plano_saude", "inss",
    ],
    "Projetos": [
        "programacao", "desenvolvimento", "codigo", "projeto", "sistema",
        "backend", "frontend", "api", "banco_dados", "deploy", "github",
        "readme", "script", "teste", "documentacao", "arquitetura",
        "sprint", "backlog", "versao", "release", "bug", "feature",
        "devops", "mobile", "web",
    ],
    "Imagens_e_Fotos": [
        "foto", "fotografia", "album", "retrato", "evento", "viagem",
        "familia", "aniversario", "casamento", "formatura", "perfil",
        "logo", "banner", "design", "arte", "wallpaper", "screenshot",
    ],
    "Videos": [
        "video", "filmagem", "edicao", "vlog", "tutorial", "entrevista",
        "apresentacao", "gravacao", "clipe", "documentario", "render", "montagem",
    ],
    "Musica_e_Audio": [
        "musica", "audio", "podcast", "faixa", "album", "playlist",
        "gravacao", "mixagem", "masterizacao", "instrumental", "letra",
        "partitura", "som", "voz",
    ],
}

# Palavras que NÃO existem em nenhuma categoria — usadas para testar o caso "sem match"
PALAVRAS_SEM_MATCH = [
    "xpto", "arquivo", "dados", "temp", "backup", "old", "new", "misc",
    "untitled", "copia", "draft", "rascunho", "sem_nome", "arquivo2",
    "novo_arquivo", "item", "objeto", "coisa", "arquivo_antigo",
]

# Proporção de arquivos "sem match" dentro de cada lote (10 %)
PROPORCAO_SEM_MATCH = 0.10


# ==========================================
# HELPERS
# ==========================================
def extensoes_para_categoria_tipo(categoria_tipo: str) -> list[str]:
    """Retorna extensões que pertencem a uma categoria do JSON de tipos."""
    return EXTENSOES_POR_CATEGORIA.get(categoria_tipo, TODAS_EXTENSOES)


def gerar_nome_com_match(i: int) -> tuple[str, str]:
    """
    Devolve (nome_arquivo, extensão) onde o nome contém uma keyword
    e a extensão é compatível com a categoria correspondente.
    """
    categoria = random.choice(list(CATEGORIAS_KEYWORDS.keys()))
    keyword   = random.choice(CATEGORIAS_KEYWORDS[categoria])

    # Escolhe uma extensão de tipo coerente com a categoria semântica quando possível
    mapa_categoria_tipo = {
        "Financas":          ["Documentos", "Dados"],
        "Estudo":            ["Documentos", "Imagens", "Vídeos"],
        "Esportes":          ["Documentos", "Imagens", "Vídeos"],
        "Documentos_Pessoais": ["Documentos", "Imagens"],
        "Projetos":          ["Código", "Dados", "Documentos", "Comprimidos"],
        "Imagens_e_Fotos":   ["Imagens"],
        "Videos":            ["Vídeos"],
        "Musica_e_Audio":    ["Áudios"],
    }

    # 70 % chance de extensão coerente, 30 % extensão aleatória (teste de cross-category)
    if random.random() < 0.70 and categoria in mapa_categoria_tipo:
        tipo_escolhido = random.choice(mapa_categoria_tipo[categoria])
        ext = random.choice(extensoes_para_categoria_tipo(tipo_escolhido))
    else:
        ext = random.choice(TODAS_EXTENSOES)

    sufixo   = random.choice(["", f"_{i}", f"_v{random.randint(1,5)}", f"_{random.randint(2020,2025)}"])
    nome_arquivo = f"{keyword}{sufixo}.{ext}"
    return nome_arquivo, ext


def gerar_nome_sem_match(i: int) -> tuple[str, str]:
    """
    Devolve (nome_arquivo, extensão) onde o nome NÃO contém nenhuma keyword
    registrada — testa o comportamento do organizador para arquivos desconhecidos.
    """
    palavra = random.choice(PALAVRAS_SEM_MATCH)
    ext     = random.choice(TODAS_EXTENSOES)
    sufixo  = random.choice(["", f"_{i}", f"_{random.randint(1,99)}"])
    nome_arquivo = f"{palavra}{sufixo}.{ext}"
    return nome_arquivo, ext


def conteudo_simulado(nome: str, ext: str) -> str:
    """Gera conteúdo de texto simples para arquivos que aceitam texto."""
    return (
        f"Arquivo de teste simulado: {nome}\n"
        f"Extensão: .{ext}\n"
        f"Gerado automaticamente para testes de organização de arquivos.\n"
    )


EXTENSOES_BINARIAS = {
    "jpg","jpeg","png","gif","webp","bmp","ico","tiff","avif","heic","raw","psd","ai",
    "mp4","mkv","avi","mov","wmv","flv","webm","m4v","3gp","vob","ogv",
    "mp3","wav","flac","aac","ogg","m4a","wma","opus","aiff","mid",
    "zip","rar","7z","tar","gz","bz2","xz","zst",
    "exe","dmg","apk","iso","dll","so","bin",
    "ttf","otf","woff","woff2",
}


# ==========================================
# EXECUÇÃO PRINCIPAL
# ==========================================
def gerar_arquivos_em_lote():
    print("Iniciando a geração de cenários de teste na Área de Trabalho...")
    print("-" * 60)

    for qtd in CENARIOS_QUANTIDADE:
        nome_pasta   = f"teste_carga_{qtd}"
        caminho_pasta = os.path.join(CAMINHO_DESKTOP, nome_pasta)

        if not os.path.exists(caminho_pasta):
            os.makedirs(caminho_pasta)

        qtd_sem_match = max(1, int(qtd * PROPORCAO_SEM_MATCH))
        qtd_com_match = qtd - qtd_sem_match

        print(f"Gerando {qtd} arquivos em '{nome_pasta}' "
              f"({qtd_com_match} com keyword, {qtd_sem_match} sem keyword)...")

        # ── Arquivos COM keyword (devem ser reconhecidos pelo organizador) ──
        for i in range(qtd_com_match):
            nome, ext = gerar_nome_com_match(i)
            caminho   = os.path.join(caminho_pasta, nome)

            if ext in EXTENSOES_BINARIAS:
                # Cria um arquivo vazio para extensões binárias (não grava texto)
                open(caminho, 'wb').close()
            else:
                with open(caminho, 'w', encoding='utf-8') as f:
                    f.write(conteudo_simulado(nome, ext))

        # ── Arquivos SEM keyword (devem cair em "Outros" ou sem categoria) ──
        for i in range(qtd_sem_match):
            nome, ext = gerar_nome_sem_match(i)
            caminho   = os.path.join(caminho_pasta, nome)

            if ext in EXTENSOES_BINARIAS:
                open(caminho, 'wb').close()
            else:
                with open(caminho, 'w', encoding='utf-8') as f:
                    f.write(conteudo_simulado(nome, ext))

    print("-" * 60)
    print("Todos os cenários foram gerados com sucesso na Área de Trabalho!")
    print(f"Pastas criadas: {[f'teste_carga_{q}' for q in CENARIOS_QUANTIDADE]}")


if __name__ == "__main__":
    gerar_arquivos_em_lote()