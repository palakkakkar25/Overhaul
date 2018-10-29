USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_UpdateCustomerMainInfo', 'P') IS NOT NULL
    DROP PROCEDURE usp_UpdateCustomerMainInfo
GO

CREATE PROCEDURE [dbo].[usp_UpdateCustomerMainInfo]
	@IdCustomer int,
	@Name varchar(50),
	@PhoneNumber varchar(50),
	@DateRegistered datetime2(7),
	@IsActive bit
	
AS
	
	IF NOT EXISTS (SELECT 1 FROM CustomerInfo WHERE id = @IdCustomer)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: Invalid Customer Id.');
	END

	ELSE
	BEGIN
		
		UPDATE CustomerInfo
		SET [Name] = @Name, PhoneNumber = @PhoneNumber, DateRegistered = @DateRegistered, IsActive = @IsActive
		WHERE id = @IdCustomer

	END
GO


