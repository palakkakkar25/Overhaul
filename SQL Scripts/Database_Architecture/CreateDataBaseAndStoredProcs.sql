USE [OverHaul_Main]
GO
/****** Object:  Table [dbo].[CustomerInfo]    Script Date: 4/17/2018 11:55:02 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CustomerInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[UserName] [varchar](50) NOT NULL,
	[PassWord] [varchar](50) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[PhoneNumber] [varchar](50) NOT NULL,
	[DateRegistered] [datetime2](7) NOT NULL,
	[IsActive] [bit] NOT NULL,
 CONSTRAINT [PK_CustomerInfo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CustomerPaymentInfo]    Script Date: 4/17/2018 11:55:03 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CustomerPaymentInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Customer] [int] NOT NULL,
	[CardNumber] [varchar](50) NOT NULL,
	[BillingAddress] [varchar](500) NOT NULL,
	[ExpirationMonth] [varchar](50) NOT NULL,
	[ExpirationYear] [varchar](50) NOT NULL,
	[CVV] [varchar](50) NOT NULL,
	[BillingName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_UserPaymentInfo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CustomerRating]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CustomerRating](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Customer] [int] NOT NULL,
	[id_DriverWhoRated] [int] NOT NULL,
	[id_ServiceRequest] [int] NOT NULL,
	[Rating] [int] NOT NULL,
	[Comment] [varchar](500) NULL,
 CONSTRAINT [PK_CustomerRating] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DriverInfo]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DriverInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[UserName] [varchar](50) NOT NULL,
	[PassWord] [varchar](50) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[PhoneNumber] [varchar](50) NOT NULL,
	[DriverLicenseNumber] [varchar](50) NOT NULL,
	[Picture] [varbinary](max) NULL,
	[DateRegistered] [datetime2](7) NOT NULL,
	[IsActive] [bit] NOT NULL,
	[AverageRating] [float] NOT NULL,
	[NumberRatings] [int] NOT NULL,
	[CurrentLocation] [varchar](500) NULL,
 CONSTRAINT [PK_DriverInfo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DriverPaymentInfo]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DriverPaymentInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Driver] [int] NOT NULL,
	[BankAccountNumber] [varchar](50) NOT NULL,
	[RoutingNumber] [varchar](50) NOT NULL,
	[BillingName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_DriverPaymentInfo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DriverRating]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DriverRating](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Driver] [int] NOT NULL,
	[id_CustomerWhoRated] [int] NOT NULL,
	[id_ServiceRequest] [int] NOT NULL,
	[Rating] [int] NOT NULL,
	[Comment] [varchar](500) NULL,
 CONSTRAINT [PK_DriverRating] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Event]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Event](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Title] [varchar](50) NOT NULL,
	[Description] [varchar](500) NOT NULL,
	[Enum] [int] NOT NULL,
 CONSTRAINT [PK_Event] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[EventLog]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[EventLog](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[idServiceRequest] [int] NOT NULL,
	[idCustomer] [int] NOT NULL,
	[idDriver] [int] NOT NULL,
	[idEvent] [int] NOT NULL,
	[Description] [varchar](500) NOT NULL,
	[isActive] [int] NOT NULL,
 CONSTRAINT [PK_EventLog] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Location]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Location](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_ServiceRequest] [int] NOT NULL,
	[StartLocation] [varchar](50) NOT NULL,
	[EndLocation] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Location] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ServiceRequest]    Script Date: 4/17/2018 11:55:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ServiceRequest](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Customer] [int] NOT NULL,
	[id_DriverWhoCompleted] [varchar](50) NULL,
	[Title] [varchar](50) NOT NULL,
	[Description] [varchar](50) NOT NULL,
	[TotalWeight] [float] NOT NULL,
	[DatePosted] [datetime2](7) NOT NULL,
	[DateClosed] [datetime2](7) NULL,
	[Price] [float] NOT NULL,
	[LoadHelp] [bit] NOT NULL,
	[UnloadHelp] [bit] NOT NULL,
	[Picture] [varbinary](max) NULL,
	[isCompleted] [bit] NOT NULL,
	[inProgress] [bit] NOT NULL,
 CONSTRAINT [PK_Transaction] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Vehicle]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Vehicle](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_Driver] [int] NOT NULL,
	[CarMake] [varchar](50) NOT NULL,
	[CarModel] [varchar](50) NOT NULL,
	[CarYear] [int] NOT NULL,
	[LicensePlateNumber] [varchar](50) NOT NULL,
	[LoadCapacity] [float] NOT NULL,
	[Picture] [varbinary](max) NULL,
 CONSTRAINT [PK_Vehicle] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[ServiceRequest] ADD  CONSTRAINT [DF_ServiceRequest_isCompleted]  DEFAULT ((0)) FOR [isCompleted]
GO
ALTER TABLE [dbo].[ServiceRequest] ADD  CONSTRAINT [DF_ServiceRequest_inProgress]  DEFAULT ((0)) FOR [inProgress]
GO
ALTER TABLE [dbo].[CustomerPaymentInfo]  WITH CHECK ADD  CONSTRAINT [FK_CustomerPaymentInfo_CustomerInfo] FOREIGN KEY([id_Customer])
REFERENCES [dbo].[CustomerInfo] ([id])
GO
ALTER TABLE [dbo].[CustomerPaymentInfo] CHECK CONSTRAINT [FK_CustomerPaymentInfo_CustomerInfo]
GO
ALTER TABLE [dbo].[CustomerRating]  WITH CHECK ADD  CONSTRAINT [FK_CustomerRating_CustomerInfo] FOREIGN KEY([id_Customer])
REFERENCES [dbo].[CustomerInfo] ([id])
GO
ALTER TABLE [dbo].[CustomerRating] CHECK CONSTRAINT [FK_CustomerRating_CustomerInfo]
GO
ALTER TABLE [dbo].[DriverPaymentInfo]  WITH CHECK ADD  CONSTRAINT [FK_DriverPaymentInfo_DriverInfo] FOREIGN KEY([id_Driver])
REFERENCES [dbo].[DriverInfo] ([id])
GO
ALTER TABLE [dbo].[DriverPaymentInfo] CHECK CONSTRAINT [FK_DriverPaymentInfo_DriverInfo]
GO
ALTER TABLE [dbo].[DriverRating]  WITH CHECK ADD  CONSTRAINT [FK_DriverRating_CustomerInfo] FOREIGN KEY([id_CustomerWhoRated])
REFERENCES [dbo].[CustomerInfo] ([id])
GO
ALTER TABLE [dbo].[DriverRating] CHECK CONSTRAINT [FK_DriverRating_CustomerInfo]
GO
ALTER TABLE [dbo].[DriverRating]  WITH CHECK ADD  CONSTRAINT [FK_DriverRating_DriverInfo] FOREIGN KEY([id_Driver])
REFERENCES [dbo].[DriverInfo] ([id])
GO
ALTER TABLE [dbo].[DriverRating] CHECK CONSTRAINT [FK_DriverRating_DriverInfo]
GO
ALTER TABLE [dbo].[DriverRating]  WITH CHECK ADD  CONSTRAINT [FK_DriverRating_Transaction] FOREIGN KEY([id_ServiceRequest])
REFERENCES [dbo].[ServiceRequest] ([id])
GO
ALTER TABLE [dbo].[DriverRating] CHECK CONSTRAINT [FK_DriverRating_Transaction]
GO
ALTER TABLE [dbo].[EventLog]  WITH CHECK ADD  CONSTRAINT [FK_EventLog_CustomerInfo] FOREIGN KEY([idCustomer])
REFERENCES [dbo].[CustomerInfo] ([id])
GO
ALTER TABLE [dbo].[EventLog] CHECK CONSTRAINT [FK_EventLog_CustomerInfo]
GO
ALTER TABLE [dbo].[EventLog]  WITH CHECK ADD  CONSTRAINT [FK_EventLog_DriverInfo] FOREIGN KEY([idDriver])
REFERENCES [dbo].[DriverInfo] ([id])
GO
ALTER TABLE [dbo].[EventLog] CHECK CONSTRAINT [FK_EventLog_DriverInfo]
GO
ALTER TABLE [dbo].[EventLog]  WITH CHECK ADD  CONSTRAINT [FK_EventLog_Event] FOREIGN KEY([idEvent])
REFERENCES [dbo].[Event] ([id])
GO
ALTER TABLE [dbo].[EventLog] CHECK CONSTRAINT [FK_EventLog_Event]
GO
ALTER TABLE [dbo].[EventLog]  WITH CHECK ADD  CONSTRAINT [FK_EventLog_ServiceRequest] FOREIGN KEY([idServiceRequest])
REFERENCES [dbo].[ServiceRequest] ([id])
GO
ALTER TABLE [dbo].[EventLog] CHECK CONSTRAINT [FK_EventLog_ServiceRequest]
GO
ALTER TABLE [dbo].[Location]  WITH CHECK ADD  CONSTRAINT [FK_Location_Transaction] FOREIGN KEY([id_ServiceRequest])
REFERENCES [dbo].[ServiceRequest] ([id])
GO
ALTER TABLE [dbo].[Location] CHECK CONSTRAINT [FK_Location_Transaction]
GO
ALTER TABLE [dbo].[ServiceRequest]  WITH CHECK ADD  CONSTRAINT [FK_ServiceRequest_CustomerInfo] FOREIGN KEY([id_Customer])
REFERENCES [dbo].[CustomerInfo] ([id])
GO
ALTER TABLE [dbo].[ServiceRequest] CHECK CONSTRAINT [FK_ServiceRequest_CustomerInfo]
GO
ALTER TABLE [dbo].[Vehicle]  WITH CHECK ADD  CONSTRAINT [FK_Vehicle_DriverInfo] FOREIGN KEY([id_Driver])
REFERENCES [dbo].[DriverInfo] ([id])
GO
ALTER TABLE [dbo].[Vehicle] CHECK CONSTRAINT [FK_Vehicle_DriverInfo]
GO
/****** Object:  StoredProcedure [dbo].[usp_InsertCustomer]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_InsertDriver]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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

	ELSE
	BEGIN

		INSERT INTO DriverInfo(UserName, [PassWord], [Name], PhoneNumber, DriverLicenseNumber, DateRegistered, IsActive, AverageRating, NumberRatings)                      
		VALUES (@UserName, @PassWord, @Name, @PhoneNumber, @DriverLicenseNumber, @DateRegistered,1, 5.0, 1)

		DECLARE @idDriver int = (SELECT SCOPE_IDENTITY())

		INSERT INTO Vehicle(id_Driver, CarMake, CarModel, CarYear, LicensePlateNumber, LoadCapacity)
		VALUES (@idDriver, @CarMake, @CarModel, @CarYear, @LicensePlateNumber, @LoadCapacity)

		INSERT INTO DriverPaymentInfo(id_Driver, BankAccountNumber, RoutingNumber, BillingName)
		VALUES (@idDriver, @BankAccountNumber, @RoutingNumber, @BillingName)
	END
GO
/****** Object:  StoredProcedure [dbo].[usp_InsertDriverRating]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_InsertServiceRequest]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_UpdateCustomerMainInfo]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_UpdateCustomerPaymentInfo]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_UpdateDriverMainInfo]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_UpdateDriverPaymentInfo]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
/****** Object:  StoredProcedure [dbo].[usp_UpdateVehicle]    Script Date: 4/17/2018 11:55:05 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
