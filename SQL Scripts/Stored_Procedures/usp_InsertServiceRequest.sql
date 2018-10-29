USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


IF OBJECT_ID('usp_InsertServiceRequest', 'P') IS NOT NULL
    DROP PROCEDURE usp_InsertServiceRequest
GO

CREATE PROCEDURE [dbo].[usp_InsertServiceRequest]
	@idCustomer int,
	@Title varchar(50),
	@Description varchar(500),
	@TotalWeight float,
	@DatePosted datetime2(7),
	@Price float,
	@LoadHelp bit,
	@UnloadHelp bit,
	@Picture varbinary(MAX),

	@StartLocation varchar(50),
	@EndLocation varchar(50),

	@idServiceRequest int Output
AS

	INSERT INTO ServiceRequest(id_Customer, Title, [Description], TotalWeight, DatePosted, Price, LoadHelp, UnloadHelp, Picture, isCompleted, inProgress)
	VALUES (@idCustomer, @Title, @Description, @TotalWeight, @DatePosted, @Price, @LoadHelp, @UnloadHelp, @Picture, 0, 0)

	SET @idServiceRequest = (SELECT SCOPE_IDENTITY())

	INSERT INTO [Location](id_ServiceRequest, StartLocation, EndLocation)
	VALUES (@idServiceRequest, @StartLocation, @EndLocation)

GO


