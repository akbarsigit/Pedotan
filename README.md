# 🌱 PEDOTAN (Pengendalian Optimal Tanaman) 🌿 - Guardians of the Green (Team C23-PS379)
:earth_asia: Welcome to the Bangkit capstone project of Team C23-PS379, a game-changer designed to revolutionize the farming community and plant disease detection.

<p align="center">
  <img src="https://github.com/akbarsigit/Pedotan/assets/69757628/34a01426-9dd1-4b3f-8df0-69d64ba56388" alt="Group 3" width="400" height="300">
</p>

## 🎯 About 
Pedotan is a mobile application that aims to support farmers by providing them with financial aid, offers a guidance system to enhance field productivity, and serves as a platform to sell their crops to improve the well-being of farmers. Our mission is to help farmers get a better understanding of what happened to their crops and monitor the fields, enhancing market opportunities and farmers' economic levels. Pedotan can give monitoring and guidance through AI technology to help identify and address plant issues to  promote the adoption of sustainable and efficient farming practices 🧪🔬.


![Tensorflow](https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)
![Keras](https://img.shields.io/badge/Keras-FF0000?style=for-the-badge&logo=keras&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Google Cloud](https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white)


## 👥 Our Guardians
|            Member           | Student ID |        Path        |                    Role                    |                                                       Contacts                                                      |
| :-------------------------: | :--------: | :----------------: | :----------------------------------------: | :-----------------------------------------------------------------------------------------------------------------: |
| Akbar Sigit Putra  | M169DSX0378 |  Machine Learning  |Machine Learning Engineer |[akbarsigit](https://github.com/akbarsigit)|
| Muhammad Rifat Bagas Adikusuma | M169DSX0536  |  Machine Learning  | Machine Learning Engineer |   -  |
| Wiweka Yoga Sadewa| A169DSX1204 | Mobile Development | Android Mobile Developer | [wiweka24](https://github.com/wiweka24) |
| Ammar Raihan | A017DSX1052  | Mobile Development |          Android Mobile Developer          |    -   |
| Muhammad Dzaki Dwi Putranto  | C017DSX0699  |   Cloud Computing  |               Cloud Engineer              |  -         |
| Akmal Jauhar Sidqi | C017DSX0718  |   Cloud Computing  |  Cloud Engineer        | - |


## Learning Path Repository 📚
For more information, check out each learning path to see the individual documentation:
- [Mobile Development](https://github.com/wiweka24/Pedotan-MD)
- [Cloud Computing](https://github.com/mukiwito/Pedotan-CC)
- [Machine Learning](https://github.com/akbarsigit/Pedotan-MachineLearning)


## 🛠️ Technology Stack
1. **Machine Learning 🧠**: TensorFlow and Keras
2. **Mobile Development 📱**: Android (Java and Kotlin)
3. **Cloud Computing ☁️**: Google Cloud Platform (GCP)
4. **Data Management 💽**: Firebase Realtime Database 


## 🌟 Features
1. **Real-time Plant Disease Detection 📸**: Capture a photo of the plant and receive the disease diagnosis, nutrient deficiency, and ideal farm condition in an instant.
2. **Market Place 🛒🌧️**: Provide a platform for the farmer to sell their crops under “koperasi tani” management.
3. **Farm Monitoring 🌾**: Help farmers monitor growth with the help of AI technology to increase field productivity.
4. **Financial Support 💸**: Support farmers in acquiring financial aid to initiate agricultural land cultivation.
5. **Cloud Sync ☁️**: Access your data anytime, anywhere thanks to our cloud syncing capabilities.

### Endpoints
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


## 🚀 Our Model Performance
### Plant Disease Detection
#### Model Training Performance
![disease_model](https://github.com/akbarsigit/Pedotan-MachineLearning/assets/72943849/3bc7834a-53fd-4500-bab7-7bbf054b112e)
#### Performance after Fine Tuning
![finetuning_disease](https://github.com/akbarsigit/Pedotan-MachineLearning/assets/72943849/021fff2b-adc2-4ee6-8794-ae958849b807)

### Nutrient Deficiency
#### Model Training Performance
![image](https://github.com/akbarsigit/Pedotan-MachineLearning/assets/72943849/acf1c620-55b9-4073-b5fa-f7eff95ffc8d)
#### Performance after Fine Tuning
![image](https://github.com/akbarsigit/Pedotan-MachineLearning/assets/72943849/517c78e3-7b5d-4db4-9eb0-be574303d9b0)

### Ideal Farm Detection
#### Model Training Performance
![image](https://github.com/akbarsigit/Pedotan-MachineLearning/assets/72943849/bd0a0f40-e9aa-4dc9-89b0-4f37746bbc8c)

### Performance Summary
Models | Accuracy | Val Accuracy
------------ | ------------- | -------------
Plant Disease Detection | 97.02 % | 96.37 %
Nutrient Deficiency Detection | 99.08% | 94.78%
Ideal Farm Detection | 96.56% | 98.75%



