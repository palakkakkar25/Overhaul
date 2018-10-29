USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_InsertCustomer', 'P') IS NOT NULL
    DROP PROCEDURE usp_InsertCustomer
GO

CREATE PROCEDURE [dbo].[usp_InsertCustomer]
	@UserName varchar(50),
	@PassWord varchar(50),
	@Name varchar(50),
	@PhoneNumber varchar(50),
	@DateRegistered datetime2(7),

	@CardNumber varchar(50),
	@BillingAddress varchar(500),
	@ExpirationMonth varchar(50),
	@ExpirationYear varchar(50),
	@CVV varchar(50),
	@BillingName varchar(50)
AS
	
	IF EXISTS (SELECT 1 FROM CustomerInfo WHERE UserName = @UserName)
	BEGIN
		RAISERROR (200000,-1,-1, 'Error: The user name already exists in the system.');
	END

	ELSE
	BEGIN

		INSERT INTO CustomerInfo(UserName, PassWord, Name, PhoneNumber, DateRegistered, IsActive)
		VALUES (@UserName, @PassWord, @Name, @PhoneNumber, @DateRegistered, 1)

		DECLARE @idCustomer int = (SELECT SCOPE_IDENTITY())

		INSERT INTO CustomerPaymentInfo(id_Customer, CardNumber, BillingAddress, ExpirationMonth, ExpirationYear, CVV, BillingName)
		VALUES (@idCustomer, @CardNumber, @BillingAddress, @ExpirationMonth, @ExpirationYear, @CVV, @BillingName)
	END

GO


