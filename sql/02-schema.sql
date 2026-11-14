-- Execute conectado ao banco "bazar".
-- OPCIONAL: a aplicação já cria estas tabelas sozinha (spring.jpa.hibernate.ddl-auto=update).
-- Use este script se preferir criar as tabelas manualmente ANTES de subir a API.

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria        BIGSERIAL PRIMARY KEY,
    nome_categoria      VARCHAR(50)  NOT NULL UNIQUE,
    descricao_categoria VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS produto (
    id_produto         BIGSERIAL PRIMARY KEY,
    nome_produto       VARCHAR(100)   NOT NULL,
    descricao_produto  VARCHAR(255),
    preco_produto      NUMERIC(10, 2) NOT NULL CHECK (preco_produto > 0),
    quantidade_estoque INTEGER        NOT NULL CHECK (quantidade_estoque >= 0),
    id_categoria       BIGINT         NOT NULL REFERENCES categoria (id_categoria),
    CONSTRAINT uk_produto_nome_categoria UNIQUE (nome_produto, id_categoria)
);

CREATE TABLE IF NOT EXISTS venda (
    id_venda    BIGSERIAL PRIMARY KEY,
    id_produto  BIGINT         NOT NULL REFERENCES produto (id_produto),
    quantidade  INTEGER        NOT NULL CHECK (quantidade >= 1),
    valor_total NUMERIC(10, 2) NOT NULL,
    data_venda  TIMESTAMP      NOT NULL
);
