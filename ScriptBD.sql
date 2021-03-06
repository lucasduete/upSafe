﻿CREATE TABLE Usuario(
	ID SERIAL,
	Nome VARCHAR(100) NOT NULL,
	Email VARCHAR(50) NOT NULL UNIQUE,
	Password VARCHAR(60) NOT NULL,
	CONSTRAINT Usuario_Id_PK Primary Key(Id),
	CONSTRAINT Verifica_Admin_Email CHECK (Email LIKE '%@%')
);

CREATE TABLE Arquivo(
	ID SERIAL,
	NOME VARCHAR(100) NOT NULL,
	TAMANHO INTEGER,
	IdUsuario INTEGER,
	CONTENT VARCHAR NOT NULL,
	CONSTRAINT Arquivo_Id_PK Primary Key(Id),
	CONSTRAINT Arquivo_Usuario_ID_FK FOREIGN KEY(IdUsuario) REFERENCES Usuario(Id) ON UPDATE CASCADE ON DELETE RESTRICT
);