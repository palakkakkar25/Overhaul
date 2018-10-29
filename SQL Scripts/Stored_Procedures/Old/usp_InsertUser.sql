USE [OverHaul_Main]
GO

/****** Object:  StoredProcedure [dbo].[usp_InsertUser]    Script Date: 4/5/2018 12:47:07 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('usp_InsertUser', 'P') IS NOT NULL
    DROP PROCEDURE usp_InsertUser
GO

CREATE PROCEDURE [dbo].[usp_InsertUser]
	@UserName varchar(50),
	@PassWord varchar(50),
	@FirstName varchar(50),
	@LastName varchar(50),
	@PhoneNumber varchar(50)
AS
	
	IF EXISTS (SELECT 1 FROM UserInfo WHERE UserName = @UserName)
	BEGIN
		RAISERROR (100,-1,-1, 'Error: The user name already exists in the system.');
	END

	INSERT INTO UserInfo(UserName, PassWord, FirstName, LastName, PhoneNumber, IsActive)
	VALUES (@UserName, @PassWord, @FirstName, @LastName, @PhoneNumber, 1)


GO


