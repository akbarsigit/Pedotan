from flask import Flask, request, jsonify
from flask_restful import Resource, Api
import firebase_admin
from firebase_admin import auth, credentials, firestore
from google.cloud import storage
import jwt
from PIL import Image
import numpy as np
from keras.models import load_model
from tensorflow.keras.utils import img_to_array
from io import BytesIO
import requests 

app = Flask(__name__) # Flask
api = Api(app)

cred = credentials.Certificate('auth/credentials/firebase_credentials.json') # Import firebase Credentials
firebase_admin.initialize_app(cred) # Initializing firebase admin

# Custom JWT Secret Key
jwt_secret = 'PEDOTAN'

class RegisterResource(Resource):
    def post(self):
        # Register for user using email and password

        # Fetch request data
        email = request.json.get('email')
        name = request.json.get('name')
        password = request.json.get('password')

        try:
            # Register User in firebase
            user = auth.create_user(
                email=email,
                display_name=name,
                password=password
            )

            # Save default user data in firestore (user data) collection
            db = firestore.client()
            db.collection('user data').document(user.uid).set({'email' : email, 'name': name})

            return {'message': 'User Created Successfully'}, 201
        except auth.EmailAlreadyExistsError:
            return {'message': 'Email Already Exists'}, 409

class RegisterGoogleResource(Resource):
    def post(self):
        # Register for user using Google provider

        # Fetch request data
        email = request.json.get('email')
        name = request.json.get('name')

        try:
            # Get user data from firebase admin
            user = auth.get_user_by_email(email)

            #save default user data in firestore (user data) collection
            db = firestore.client()
            db.collection('user data').document(user.uid).set({'email' : email, 'name': name})

            return {'message': 'User Created Successfully'}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401        

def generate_session_token(user_uid):
    # Generate session token using JWT module and custom secret key
    payload = {
        'uid': user_uid,
    }
    session_token = jwt.encode(payload, jwt_secret, algorithm='HS256')
    return session_token

class AuthTokenResource(Resource):
    def post(self):
        # Generate session token for user

        # Fetch request data
        email = request.json.get('email')

        try:
            # Authenticate User
            user = auth.get_user_by_email(email)

            # Generate session token
            session_token = generate_session_token(user.uid)

            # Store the session token in firestore (session_tokens) collection
            db = firestore.client()
            session_token_doc = {
                'token': session_token
            }
            db.collection('session_tokens').document(user.uid).set(session_token_doc)

            return {'token': session_token}, 200
        except auth.InvalidIdTokenError:
            return {'message': 'Invalid credentials.'}, 401
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

def authorize_request(func):
    # Authenticate user request
    def wrapper(*args, **kwargs):
        token = request.headers.get('Authorization')

        if not token or not token.startswith('Bearer '):
            return jsonify({'message': 'Missing or invalid token'}), 401

        session_token = token.split(' ')[1]

        # Verify and decode the session token
        try:
            decoded_token = jwt.decode(session_token, jwt_secret, algorithms=['HS256'])
            user_uid = decoded_token.get('uid')
        except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
            return jsonify({'message': 'Invalid token'}), 401

        # Check if the session token exists in Firestore or any other database
        db = firestore.client()
        session_token_doc = db.collection('session_tokens').document(user_uid).get()
        if not session_token_doc.exists or session_token_doc.to_dict().get('token') != session_token:
            return jsonify({'message': 'Invalid token'}), 401

        # Add the user UID to the request context for further use if needed
        request.user_uid = user_uid

        return func(*args, **kwargs)

    return wrapper

def upload(photo):
    # Upload Image file to Cloud Bucket and return a public link for the image uploaded
    client = storage.Client.from_service_account_json('auth/credentials/pedotanimage_credentials.json')
    bucket = client.get_bucket('pedotanimage')
    blob = bucket.blob(photo.filename)

    blob.upload_from_string(photo.read(), content_type=photo.content_type)
    blob.make_public()

    photo_link = blob.public_url
    print('Uploaded image link:', photo_link)

    return photo_link


class LogoutResource(Resource):
    @authorize_request
    def post(self):
        # User Logout, user need an authorized request to logout

        # Fetch request data
        email = request.json.get('email')

        try:
            # Get user data
            user = auth.get_user_by_email(email)    

            # Delete the session token from firestore database
            db = firestore.client()
            db.collection('session_tokens').document(user.uid).delete()

            return {'message': 'Logout successful'}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401


class DataUserResource(Resource):
    @authorize_request
    def post(self):
        # Input data user to firestore database

        # Get request data (form-type)
        name = request.form.get('name')
        email = request.form.get('email')
        noHandphone = request.form.get('noHandphone')
        nik = request.form.get('nik')
        photo = request.files.get('photo')
        location = request.form.get('location')

        # Upload image to get image link
        photo_link = upload(photo)
        print(photo_link)

        try:
            # Get user data
            user = auth.get_user_by_email(email)
            
            user_data = {
                'name': name,
                'noHandphone': noHandphone,
                'nik': nik,
                'photo': photo_link,
                'location': location
            }

            # Upload user data to firestore database
            db = firestore.client()
            db.collection('user data').document(user.uid).update(user_data)

            return {'message': 'User Data Has Been Saved'}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
    
    @authorize_request
    def get(self):
        # Get user data from firestore database

        # Get request data from query
        email = request.args.get('email')

        try: 
            # Get user data from firebase admin
            user = auth.get_user_by_email(email)
            
            # Get a full user data saved in firestore database
            db = firestore.client()
            user_data = db.collection('user data').document(user.uid).get()

            if user_data.exists:
                return user_data.to_dict(), 200
            else:
                return 'User data not found', 404
        except auth.UserNotFoundError:
            return 'User not found', 404
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401   


class FarmDataResource(Resource):
    @authorize_request
    def post(self):
        # Input user's farm data

        # Get request data
        email = request.json.get('email')
        farm_name = request.json.get('farm_name')
        commodity = request.json.get('commodity')
        location = request.json.get('location')   
        area = request.json.get('area')
        status = "baik"

        db = firestore.client()
        try:
            # Get user data
            user = auth.get_user_by_email(email)
            # Create user data (email)
            user_doc_ref = db.collection('user farm').document(user.uid)
            user_doc_ref.set({'email': email})

            # Create farm document inside "farms" subcollection with auto generate farm_id
            farms_collection_ref = user_doc_ref.collection('farms')
            farm_doc_ref = farms_collection_ref.document()
            # Set farm data to firestore database
            farm_doc_ref.set({
                'farm_name': farm_name,
                'commodity': commodity,
                'location': location,
                'area': area,
                'status': status
            })

            # Return farm_id
            return {'message': 'Farm Data Has Been Saved', 'farm_id': farm_doc_ref.id}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
    

    @authorize_request
    def get(self):
        # Get all of the user's available farm data

        # Get query parameters
        email = request.args.get('email')

        try:
            # Get user data
            user = auth.get_user_by_email(email)
            # Get "farms" collection from user data
            db = firestore.client()
            farm_data = db.collection("user farm").document(user.uid).collection("farms").get()
            
            # Create user's farm data list
            farm_data_list = []
            for farm_doc in farm_data:
                if farm_doc.exists:
                    farm_id = farm_doc.id
                    data = farm_doc.to_dict()
                    data['farm_id'] = farm_id
                    farm_data_list.append(data)
            return farm_data_list
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
    
    @authorize_request
    def patch(self):
        # Update user's farm data

        # Get email dan farm_id data
        email = request.json.get('email')
        farm_id = request.json.get('farm_id')
        
        # Get farm data
        farm_name = request.json.get('farm_name')
        commodity = request.json.get('commodity')
        location = request.json.get('location')   
        area = request.json.get('area')
        status = request.json.get('status')

        # Setting up farm data
        farm_data = {
            'farm_name': farm_name,
            'commodity': commodity,
            'location': location,
            'area': area,
            'status': status
        }

        try:
            # Upload farm data to firestore database
            update_farm_data(email, farm_id, farm_data)
            return {'message': 'Farm Data Has Been Updated'}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

    @authorize_request
    def delete(self):
        # Delete user's farm data based on farm_id

        # Get request data
        email = request.json.get('email')
        farm_id = request.json.get('farm_id')

        try:
            # Delete user's farm data from firestore database
            delete_farm_data(email, farm_id)
            return {'message': 'Farm Data Has Been Deleted'}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

def get_farm_data(email, farm_id):
    # Get user's farm data based on farm_id from firestore database
    user = auth.get_user_by_email(email)
    db = firestore.client()
    farm_data = db.collection("user farm").document(user.uid).collection("farms").document(farm_id).get()
    return farm_data

def update_farm_data(email, farm_id, farm_data):
    # Update user's farm data based on farm_id to firestore database
    user = auth.get_user_by_email(email)
    db = firestore.client()
    db.collection("user farm").document(user.uid).collection("farms").document(farm_id).update(farm_data)

def delete_farm_data(email, farm_id):
    # Delete user's farm data based on farm_id from firestore database
    user = auth.get_user_by_email(email)
    db = firestore.client()
    db.collection('user farm').document(user.uid).collection('farms').document(farm_id).delete()    


#=============================================#
# Load AI Model 
model1 = load_model("ai_model/model/model1.h5") # AI model for predicting crop commodity
model2 = load_model("ai_model/model/model2.h5") # AI model for predicting plant disease
model3 = load_model("ai_model/model/model3.h5") # AI model for predicting NPK value

# Plant disease class from AI model
disease_class = ["Apple black rot",
"Apple healthy"
"Apple rust",
"Apple scab",
"Chili healthy",
"Chili leaf_curl",
"Chili leaf_spot",
"Chili whitefly",
"Chili yellowish",
"Coffee cercospora_leaf_spot",
"Coffee healthy",
"Coffee red spider mite",
"Coffee rust",
"Corn common rust",
"Corn gray leaf spot",
"Corn healthy",
"Corn northern leaf blight",
"Grape black measles",
"Grape black rot",
"Grape healthy",
"Grape leaf blight isariopsis leaf spot",
"Rice brown spot",
"Rice healthy",
"Rice hispa",
"Rice leaf blast",
"Rice neck blast",
"Tomato bacterial spot",
"Tomato early blight",
"Tomato healthy",
"Tomato late blight",
"Tomato leaf mold",
"Tomato mosaic virus",
"Tomato septoria leaf spot",
"Tomato spider mites",
"Tomato target spot",
"Tomato yellow leaf curl virus"]

def preprocess_image(url):
    # Image preprocessing for the AI model from image url
    res = requests.get(url).content
    img = Image.open(BytesIO(res)).convert('RGB')
    img = img.resize((224, 224))

    image = img_to_array(img)
    image /= 255
    image = np.expand_dims(image, axis = 0)
    image = np.vstack([image])

    return image

def get_predicted_label_disease(pred_probabilities):
    # Turns an array of predictions probabilities into a label
    return disease_class[pred_probabilities.argmax()]

class PredictPlantDisease(Resource):
    @authorize_request
    def post(self):
        # Prediction for plant disease

        # Get user's email and farm_id data
        email = request.form.get('email')
        farm_id = request.form.get('farm_id')

        # Check if request contains image
        if 'image' not in request.files:
            return jsonify({'error': 'No image found in the request'}), 401
        
        try:
            # Get image url
            image = request.files['image']
            url = upload(image)

            # Preprocess the image
            image = preprocess_image(url)
            pred = model2.predict(image) # Image prediction
            max_pred = max(pred[0])

            # Threshold
            if max_pred > 0.8:
                pred_class = get_predicted_label_disease(pred[0])
                update_farm_data(email, farm_id, {'model2' : pred_class})

                # Get data kebun
                data_kebun = get_farm_data(email, farm_id)
                komoditas = data_kebun.get('commodity')
                
                # Conditional check for updating data kebun "status" in firestore database
                if 'model1' not in data_kebun.to_dict():
                    update_farm_data(email, farm_id, {'status': "kurang baik"})
                else:
                    data_model1 = data_kebun.get('model1')
                    if komoditas != data_model1:
                        update_farm_data(email, farm_id, {'status': "buruk"})
                    else:
                        update_farm_data(email, farm_id, {'status': "kurang baik"})
            else :
                pred_class = "sehat"
            
            return {'predict': pred_class}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
        
# Crop commodity class from AI model
komoditas_class = ['apel', 
                   'kopi', 
                   'anggur', 
                   'jagung', 
                   'padi']

def get_predicted_label_commodity(pred_probabilities):
    # Turns an array of predictions probabilities into a label
    return komoditas_class[pred_probabilities.argmax()]

# NPK prediction class from AI model
npk_class = ['N', 'P', 'K']

def get_predicted_label_npk(pred_probabilities):
    # Turns an array of predictions probabilities into a label
    return npk_class[pred_probabilities.argmax()]

class PredictCropCommodity(Resource):
    @authorize_request
    def post(self):
        # Predict crop commodity

        # Get user's email and farm_id data
        email = request.json.get('email')
        farm_id = request.json.get('farm_id')
        json_data = request.json
        
        # Turn model data array for AI Model Prediction
        model_data = [
            [json_data['n'], 
             json_data['p'], 
             json_data['k'], 
             json_data['temperature'], 
             json_data['humidity'], 
             json_data['ph'], 
             json_data['rainfall']]
        ]

        try:
            # Crop prediction using model1
            pred = model1.predict(model_data)
            pred_class = get_predicted_label_commodity(pred[0])

            # Upload result
            update_farm_data(email, farm_id, {'model1' : pred_class})
            
            data_kebun = get_farm_data(email, farm_id)
            komoditas = data_kebun.get('commodity')

            # Conditional check for status update
            if 'model2' not in data_kebun.to_dict():
                if komoditas != pred_class:
                    update_farm_data(email, farm_id, {'status': "kurang baik"})
                else:
                    update_farm_data(email, farm_id, {'status': "baik"})
            else:
                if komoditas != pred_class:
                    update_farm_data(email, farm_id, {'status': "buruk"})
                else:
                    update_farm_data(email, farm_id, {'status': "kurang baik"})
            return {'predict': pred_class}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
        
class PredictCropNPK(Resource):
    def post(self):
        # Prediction for NPK value

        # Check if image attach to request
        if 'image' not in request.files:
            return jsonify({'error': 'No image found in the request'}), 401
        
        # Get image url
        image = request.files['image']
        url = upload(image)

        # NPK data initialization
        n = 80.0
        p = 80.0
        k = 80.0

        # Image preprocessing and prediction using model3
        image = preprocess_image(url)
        pred = model3.predict(image)
        max_pred = max(pred[0])

        # Threshold
        if max_pred > 0.8:
            pred_class = get_predicted_label_npk(pred[0])
        else :
            pred_class = "sehat"

        # Update NPK value based on prediction result
        if pred_class == "N":
            n = 20.0
        elif pred_class == "P":
            p = 20.0
        elif pred_class == "K":
            k = 20.0
        
        return {'n': n, 'p': p, 'k': k}, 200

# API endpoints
api.add_resource(RegisterResource, '/auth/register')
api.add_resource(RegisterGoogleResource, '/auth/google')
api.add_resource(AuthTokenResource, '/auth/login')
api.add_resource(DataUserResource, '/auth/datauser')
api.add_resource(FarmDataResource, '/auth/farmdata')
api.add_resource(LogoutResource, '/auth/logout')
api.add_resource(PredictPlantDisease, '/ai/predictdisease')
api.add_resource(PredictCropCommodity, '/ai/predictcrop')
api.add_resource(PredictCropNPK, '/ai/predictnpk')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
