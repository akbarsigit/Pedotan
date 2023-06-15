# Pedotan-CC Repository (# Team C23-PS379)
Member of Cloud Computing for Bangkit Academy Capstone Team C23-PS379 
| Member | Student ID | University |
|:------:|:----------:|:----------:|
| Muhammad Dzaki Dwi Putranto | C017DSX0699 | Bandung Institute of Technology |
| Akmal Jauhar Sidqi | C017DSX0718 | Bandung Institute of Technology |

**

**This repository contains API and AI-Model for deployment**

**

## User authentication and user data
For the implementation of user authentication features, PEDOTAN makes use of **the Firebase user authentication** feature. With this feature, user registration and authentication will be handled by **firebase**.

Users can register an account using both the email/password method and the Google provider method.

When the user makes a login, the user will be given a **unique token** that will be used to access other APIs. Without this unique token, the user may not get API access for other features.

Data of PEDOTAN users will be saved on to firestore database. Here is the detail of the stored data on firestore database.
 
 - User Data
	 - Email
	 - Name
	 - Location
	 - NIK
	 - Phone Number
	 - Photo
 - Session Tokens
 - User's Farm Data
 	 - Email
	 - "Farms"
	 	- Farm ID
	 	- Name
		- Commodity
	 	- Area
	 	- Location
	 	- Status

## AI-Model
For the implementation of AI-Model in PEDOTAN, we have **3 AI-Model** that have been developed by our AI Team. For further details on the model, please check out our AI Team Repository.

Accessing the AI-Model from PEDOTAN apps will use 2 APIs to **upload image** and **send data** required by the model. The APIs will preprocess the image and data to make sure the data that is used is suitable for the specification required by the AI-Model. In addition, there is another API for sending images to be used to get NPK value from the AI-Model.

After the prediction is made by the AI-Model, the data prediction will be used to update the user's farm data "status" according to the result of the data prediction.

## Endpoints
Here are the endpoints used by PEDOTAN-APP

 - **'/auth/register'**
	User registration using email/password method (saving user data in Firestore)
- **'/auth/google'**
	User registration using Google provider (saving user data in Firestore)
- **'/auth/login'**
	Creating a unique session token for the user
- **'/auth/datauser'**
	- POST
		Sending detailed user data on to Firestore database
	- GET
		Retrieving user data from the Firestore database
- **'/auth/farmdata'**
	- POST
		Sending a user's farm data on to Firestore database
	- GET
		Retrieving all of the user's farm data from the Firestore database
	- PATCH
		Update the user's farm data on the Firestore database
	- DELETE
   		Delete the user's farm data from the Firestore database
- **'/auth/logout'**
	Deleting the user's session token
- **'/ai/predictdisease'**
	Sending plant disease photos for AI prediction
- **'/ai/predictcrop'**
	Sending crop data for AI prediction
- **'/ai/predictnpk'**
	Sending image for NPK value AI prediction

## Deployment
**PEDOTAN APIs are deployed on Google Cloud Platform Compute Engine.**
Here is the detailed specification of  the compute engine used for deployment.

| Item | Specification |
|:-----:|:------------:|
| Type | Instance |
| Zone | asia-southeast2-a |
| Machine type | e2-medium |
| CPU Platform | Intel Broadwell |
| Architecture | x86/64 |
| Boot Disk | debian-11-bullseye |
