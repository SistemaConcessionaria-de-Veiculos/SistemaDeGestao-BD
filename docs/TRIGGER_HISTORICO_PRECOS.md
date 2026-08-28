# Trigger de histórico de preços dos veículos

## Regra de negócio

Sempre que o preço de um veículo for alterado, o sistema deve guardar o valor anterior, o novo valor, o chassi do veículo e a data da alteração.

Essa auditoria permite acompanhar o histórico de preços sem depender de registros manuais realizados pelo usuário.

## Estrutura criada

O arquivo `init-scripts/04-create-triggers.sql` cria:

- a tabela `historico_preco_veiculos`;
- o Trigger `trg_historico_preco_veiculos`.

A tabela possui os campos:

- `id_historico`: identificador do registro;
- `chassi`: veículo que teve o preço alterado;
- `valor_anterior`: preço antes da alteração;
- `valor_novo`: preço depois da alteração;
- `data_alteracao`: data e hora da alteração.

## Funcionamento

O Trigger é executado depois de uma atualização na tabela `veiculos`.

Um registro é criado no histórico somente quando o conteúdo de `valor_veiculo` realmente muda. Alterações em outros dados do veículo não geram histórico de preço.

## Teste

Os comandos para demonstração estão disponíveis em:

`docs/TESTE_TRIGGER.sql`

O teste altera o preço do veículo de chassi `BRNOVA00000000001` e consulta a tabela de histórico para comprovar o funcionamento.