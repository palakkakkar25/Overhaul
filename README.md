# Overhaul

The initial commit into the master has the login screen already built for the application.

The main files you will be focused on are the...

AndroidManifest.xml -> deals with application specific permissions like location, camera, or bluetooth, etc.
Java folder -> the code to handle the logic and functionality of the xml files
res -> layout -> Deals with the look and feel of the application 

res -> values -> strings.xml -> hard coded strings should be placed inside this file and referenced inside the code 
								String fooString = getResources().getString(R.string.foolabel)


Functional requirements :
	Driver interface 
	Driver Profile
	Client Interface
	Communication interface (location, destination and description of goods, etc.)
	Login (Drive and user)
	Registration (Driver and user)
	
	