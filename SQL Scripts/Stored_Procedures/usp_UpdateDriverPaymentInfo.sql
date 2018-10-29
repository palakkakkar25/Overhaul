USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_UpdateDriverPaymentInfo', 'P') IS NOT NULL
    DROP PROCEDURE usp_UpdateDriverPaymentInfo
GO

CREATE PROCEDURE [dbo].[usp_UpdateDriverPaymentInfo]

	@idDriver int,
	@BankAccountNumber varchar(50),
	@RoutingNumber varchar(50),
	@BillingName varchar(50)
	
AS
	
	IF NOT EXISTS (SELECT 1 FROM DriverPaymentInfo WHERE id_Driver = @idDriver)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: Invalid Driver Id.');
	END

	ELSE
	BEGIN

		UPDATE DriverPaymentInfo
		SET BankAccountNumber = @BankAccountNumber, RoutingNumber = @RoutingNumber, BillingName = @BillingName
		WHERE id_Driver = @idDriver

	END
GO


