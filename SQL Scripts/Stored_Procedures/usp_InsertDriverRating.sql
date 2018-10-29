USE [OverHaul_Main]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_InsertDriverRating', 'P') IS NOT NULL
    DROP PROCEDURE usp_InsertDriverRating
GO
CREATE PROCEDURE [dbo].[usp_InsertDriverRating]
	@IdDriver int,
	@IdCustomerWhoRated int,
	@IdServiceRequest int,
	@Rating int,
	@Comment varchar(50),
	@NewDriverAverage float,
	@NewDriverCount int
AS

	INSERT INTO [DriverRating](id_Driver, id_CustomerWhoRated, id_ServiceRequest, Rating, Comment)
	VALUES (@idDriver, @IdCustomerWhoRated, @IdServiceRequest, @Rating, @Comment)

	UPDATE DriverInfo
	SET AverageRating = @NewDriverAverage, NumberRatings = @NewDriverCount
	WHERE id = @IdDriver

GO


