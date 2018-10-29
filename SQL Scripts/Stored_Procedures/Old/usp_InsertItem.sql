-------------------------------------------------------------------------------------------------
--
-- Project - OverHaul
-- Author - Brandon Royal
-- Date - 3/26/2018
-- Description - Insert an item
-- Script Type - Stored Procedure
--
-------------------------------------------------------------------------------------------------

USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[usp_InsertItem]
	@idTransaction int,
	@Name varchar(50),
	@Description varchar(500),
	@Weight float,
	@Height float,
	@Length float,
	@Width float
AS

	INSERT INTO [Item](id_Transaction, Name, Description, Weight, Height, Length, Width)
	VALUES (@idTransaction, @Name, @Description, @Weight, @Height, @Length, @Width)

GO


