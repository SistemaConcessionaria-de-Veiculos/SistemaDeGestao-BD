USE concessionaria;

CREATE TABLE IF NOT EXISTS historico_preco_veiculos (
    id_historico BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    chassi CHAR(17) NOT NULL,
    valor_anterior DECIMAL(12,2) NOT NULL,
    valor_novo DECIMAL(12,2) NOT NULL,
    data_alteracao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_historico_preco_veiculos
        PRIMARY KEY (id_historico)
);

DROP TRIGGER IF EXISTS trg_historico_preco_veiculos;

DELIMITER $$

CREATE TRIGGER trg_historico_preco_veiculos
AFTER UPDATE ON veiculos
FOR EACH ROW
BEGIN
    IF OLD.valor_veiculo <> NEW.valor_veiculo THEN
        INSERT INTO historico_preco_veiculos (
            chassi,
            valor_anterior,
            valor_novo,
            data_alteracao
        )
        VALUES (
            OLD.chassi,
            OLD.valor_veiculo,
            NEW.valor_veiculo,
            CURRENT_TIMESTAMP
        );
    END IF;
END$$

DELIMITER ;