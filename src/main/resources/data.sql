INSERT INTO clientes (data_criacao , email , senha)
SELECT * FROM (SELECT now(), 'teste@logado.com', '$2a$10$4naPBHdml5hXzOXYcKDeg.OO56YfLRzX1QFKS/2PrqH0AfpIXcMga') AS tmp
WHERE NOT EXISTS (
    SELECT email FROM clientes WHERE email = 'teste@logado.com'
) LIMIT 1;