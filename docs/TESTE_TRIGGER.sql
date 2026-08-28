USE concessionaria;

-- 1. checa o preço atual
SELECT chassi, marca, modelo, valor_veiculo
FROM veiculos
WHERE chassi = 'BRNOVA00000000001';

-- 2. alterar o preço: o Trigger será executado automaticamente
UPDATE veiculos
SET valor_veiculo = 95990.00
WHERE chassi = 'BRNOVA00000000001';

-- 3. checa o novo preço
SELECT chassi, valor_veiculo
FROM veiculos
WHERE chassi = 'BRNOVA00000000001';

-- 4. checa o registro criado pelo Trigger
SELECT *
FROM historico_preco_veiculos
WHERE chassi = 'BRNOVA00000000001'
ORDER BY id_historico DESC;

-- 5. restaura o preço original
UPDATE veiculos
SET valor_veiculo = 94990.00
WHERE chassi = 'BRNOVA00000000001';