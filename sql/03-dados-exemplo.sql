-- OPCIONAL: dados de exemplo para testes rápidos (rode depois que as tabelas existirem).
INSERT INTO categoria (nome_categoria, descricao_categoria) VALUES
  ('Roupas', 'Roupas usadas em bom estado'),
  ('Livros', 'Livros usados');

INSERT INTO produto (nome_produto, descricao_produto, preco_produto, quantidade_estoque, id_categoria) VALUES
  ('Jaqueta jeans', 'Tamanho M', 35.90, 10, (SELECT id_categoria FROM categoria WHERE nome_categoria = 'Roupas')),
  ('Dom Casmurro',  'Machado de Assis', 12.00, 3, (SELECT id_categoria FROM categoria WHERE nome_categoria = 'Livros'));
