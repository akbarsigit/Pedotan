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

app = Flask(__name__)
api = Api(app)

cred = credentials.Certificate('auth/credentials/firebase_credentials.json') # Import firebase Credentials
firebase_admin.initialize_app(cred) # Initializing firebase admin


class FarmDataResource(Resource):
    def post(self):
        # Get request data
        email = request.json.get('email')
        farm_name = request.json.get('farm_name')
        commodity = request.json.get('commodity')
        location = request.json.get('location')   
        area = request.json.get('area')
        status = "baik"

        db = firestore.client()
        try:
            # Get the user by email and create user document if not exist
            user = auth.get_user_by_email(email)
            user_doc_ref = db.collection('user farm').document(user.uid)
            user_doc_ref.set({'email': email})

            # Create farm document within the user's subcollection
            farms_collection_ref = user_doc_ref.collection('farms')
            farm_doc_ref = farms_collection_ref.document()
            farm_doc_ref.set({
                'farm_name': farm_name,
                'commodity': commodity,
                'location': location,
                'area': area,
                'status': status
            })

            return {'message': 'Farm Data Has Been Saved', 'farm_id': farm_doc_ref.id}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
    
    def get(self):
        # Get query parameters
        email = request.args.get('email')

        # Get the reference to the farm document
        try:
            user = auth.get_user_by_email(email)
            db = firestore.client()
            farm_data = db.collection("user farm").document(user.uid).collection("farms").get()
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
    
    def patch(self):
        # Get data
        email = request.json.get('email')
        farm_id = request.json.get('farm_id')
        
        farm_name = request.json.get('farm_name')
        commodity = request.json.get('commodity')
        location = request.json.get('location')   
        area = request.json.get('area')
        status = request.json.get('status')

        farm_data = {
            'farm_name': farm_name,
            'commodity': commodity,
            'location': location,
            'area': area,
            'status': status
        }

        try:
            update_farm_data(email, farm_id, farm_data)
            return {'message': 'Farm Data Has Been Updated'}, 201
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401
    
    def delete(self):
        # Get data
        email = request.json.get('email')
        farm_id = request.json.get('farm_id')

        try:
            delete_farm_data(email, farm_id)
            return {'message': 'Farm Data Has Been Deleted'}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

def delete_farm_data(email, farm_id):
    user = auth.get_user_by_email(email)
    db = firestore.client()
    db.collection('user farm').document(user.uid).collection('farms').document(farm_id).delete()    

def get_farm_data(email, farm_id):
    user = auth.get_user_by_email(email)
    db = firestore.client()
    farm_data = db.collection("user farm").document(user.uid).collection("farms").document(farm_id).get()
    return farm_data

def update_farm_data(email, farm_id, farm_data):
    user = auth.get_user_by_email(email)
    db = firestore.client()
    db.collection("user farm").document(user.uid).collection("farms").document(farm_id).update(farm_data)

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

#=============================================#
# Load AI Model 
model1 = load_model("ai_model/model/model1.h5") # AI model for predicting crop commodity
model2 = load_model("C:/Users/Dzaki Putranto/Downloads/model2.h5") # AI model for predicting plant disease
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
    def post(self):
        # Prediction for plant disease
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
    def post(self):
        # Predict crop commodity

        # Get user's email data
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


api.add_resource(PredictPlantDisease, '/ai/predict-disease')
api.add_resource(PredictCropCommodity, '/ai/predict-crop')
api.add_resource(FarmDataResource, '/auth/data-kebun')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
