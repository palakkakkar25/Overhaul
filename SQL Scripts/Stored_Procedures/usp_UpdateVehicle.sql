USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_UpdateVehicle', 'P') IS NOT NULL
    DROP PROCEDURE usp_UpdateVehicle
GO

CREATE PROCEDURE [dbo].[usp_UpdateVehicle]
	@idVehicle int,
	@idDriver int,
	@CarMake varchar(50),
	@CarModel varchar(50),
	@CarYear int,
	@LicensePlateNumber varchar(50),
	@LoadCapacity varchar(50),
	@Picture varbinary(MAX)
	
AS
	
	IF NOT EXISTS (SELECT 1 FROM Vehicle WHERE id = @idVehicle)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: Invalid Vehicle Id.');
	END

	ELSE
	BEGIN

		UPDATE Vehicle
		SET CarMake = @CarMake, CarModel = @CarModel, CarYear = @CarYear, LicensePlateNumber = @LicensePlateNumber, LoadCapacity = @LoadCapacity, Picture = @Picture
		WHERE id = @idVehicle and id_Driver = @idDriver

	END
GO


