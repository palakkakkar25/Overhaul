USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_InsertDriver', 'P') IS NOT NULL
    DROP PROCEDURE usp_InsertDriver
GO

CREATE PROCEDURE [dbo].[usp_InsertDriver]
	@UserName varchar(50),
	@PassWord varchar(50),
	@Name varchar(50),
	@PhoneNumber varchar(50),
	@DriverLicenseNumber varchar(50),
	@DateRegistered datetime2(7),

	@CarMake varchar(50),
	@CarModel varchar(50),
	@CarYear int,
	@LicensePlateNumber varchar(50),
	@LoadCapacity float,

	@BankAccountNumber varchar(50),
	@RoutingNumber varchar(50),
	@BillingName varchar(50)
AS
	
	IF EXISTS (SELECT 1 FROM DriverInfo WHERE UserName = @UserName)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: The user name already exists in the system.');
	END

	INSERT INTO DriverInfo(UserName, [PassWord], [Name], PhoneNumber, DriverLicenseNumber, DateRegistered, IsActive)                      
	VALUES (@UserName, @PassWord, @Name, @PhoneNumber, @DriverLicenseNumber, @DateRegistered,1)

	DECLARE @idDriver int = (SELECT SCOPE_IDENTITY())

	INSERT INTO Vehicle(id_Driver, CarMake, CarModel, CarYear, LicensePlateNumber, LoadCapacity)
	VALUES (@idDriver, @CarMake, @CarModel, @CarYear, @LicensePlateNumber, @LoadCapacity)

	INSERT INTO DriverPaymentInfo(id_Driver, BankAccountNumber, RoutingNumber, BillingName)
	VALUES (@idDriver, @BankAccountNumber, @RoutingNumber, @BillingName)
GO


