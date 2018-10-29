USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_UpdateDriverMainInfo', 'P') IS NOT NULL
    DROP PROCEDURE usp_UpdateDriverMainInfo
GO

CREATE PROCEDURE [dbo].[usp_UpdateDriverMainInfo]
	@idDriver int,
	@Name varchar(50),
	@PhoneNumber varchar(50),
	@DriverLicenseNumber varchar(50),
	@DateRegistered datetime2(7),
	@IsActive bit
	
AS
	
	IF NOT EXISTS (SELECT 1 FROM DriverInfo WHERE id = @idDriver)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: Invalid Driver Id.');
	END

	ELSE
	BEGIN
		
		UPDATE DriverInfo
		SET [Name] = @Name, PhoneNumber = @PhoneNumber, DriverLicenseNumber = @DriverLicenseNumber, DateRegistered = @DateRegistered, IsActive = @IsActive
		WHERE id = @idDriver

	END
GO


