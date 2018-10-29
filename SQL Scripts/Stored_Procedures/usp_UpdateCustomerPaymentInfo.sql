USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_UpdateCustomerPaymentInfo', 'P') IS NOT NULL
    DROP PROCEDURE usp_UpdateCustomerPaymentInfo
GO

CREATE PROCEDURE [dbo].[usp_UpdateCustomerPaymentInfo]

	@IdCustomer int,
	@CardNumber varchar(50),
	@BillingAddress varchar(50),
	@ExpirationMonth varchar(50),
	@ExpirationYear varchar(50),
	@CVV varchar(50),
	@BillingName varchar(50)
	
AS
	
	IF NOT EXISTS (SELECT 1 FROM CustomerPaymentInfo WHERE id_Customer = @IdCustomer)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: Invalid Customer Id.');
	END

	ELSE
	BEGIN

		UPDATE CustomerPaymentInfo
		SET CardNumber = @CardNumber, BillingAddress = @BillingAddress, ExpirationMonth = @ExpirationMonth, ExpirationYear = @ExpirationYear, CVV = @CVV , BillingName = @BillingName
		WHERE id_Customer = @IdCustomer

	END
GO


