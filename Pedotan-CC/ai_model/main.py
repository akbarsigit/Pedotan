import sys
sys.path.append('./')

from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from PIL import Image
import numpy as np
from keras.models import load_model
from tensorflow.keras.utils import img_to_array
from io import BytesIO
import requests 
from google.cloud import storage
import firebase_admin
from firebase_admin import auth, credentials, firestore
from auth.app import authorize_request

app = Flask(__name__)
api = Api(app)

model1 = load_model("./ai_model/model/model1.h5")
model2 = load_model("C:/Users/Dzaki Putranto/Downloads/model2.h5")
model3 = load_model("./ai_model/model/model3.h5")

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
    
def upload(photo):
    client = storage.Client.from_service_account_json('./auth/credentials/pedotanimage_credentials.json')
    bucket = client.get_bucket('pedotanimage')
    blob = bucket.blob(photo.filename)

    blob.upload_from_string(photo.read(), content_type=photo.content_type)
    blob.make_public()

    photo_link = blob.public_url
    print('Uploaded image link:', photo_link)

    return photo_link

class PredictPlantDisease(Resource):
    @authorize_request
    def post(self):
        email = request.form.get('email')

        if 'image' not in request.files:
            return jsonify({'error': 'No image found in the request'}), 401
        
        try:
            user = auth.get_user_by_email(email)

            image = request.files['image']
            url = upload(image)

            image = preprocess_image(url)
            pred = model2.predict(image)
            max_pred = max(pred[0])
            db = firestore.client()
            if max_pred > 0.8:
                pred_class = get_predicted_label_disease(pred[0])
                db.collection('data kebun').document(user.uid).update({'model2' : pred_class})
                
                data_kebun = db.collection('data kebun').document(user.uid).get()
                komoditas = data_kebun.get('commodity')
                
                if 'model1' not in data_kebun.to_dict():
                    db.collection('data kebun').document(user.uid).update({'status': "kurang baik"})
                else:
                    data_model1 = data_kebun.get('model1')
                    if komoditas != data_model1:
                        db.collection('data kebun').document(user.uid).update({'status': "buruk"})
            else :
                pred_class = "sehat"
            
            return {'predict': pred_class}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

komoditas_class = ['apel', 
                   'kopi', 
                   'anggur', 
                   'jagung', 
                   'padi']

def get_predicted_label_commodity(pred_probabilities):
    """
    Turns an array of predictions probabilities into a label
    """
    return komoditas_class[pred_probabilities.argmax()]


class PredictCropCommodity(Resource):
    @authorize_request
    def post(self):
        email = request.json.get('email')
        json_data = request.json
        
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
            user = auth.get_user_by_email(email)
            pred = model1.predict(model_data)
            pred_class = get_predicted_label_commodity(pred[0])
            db = firestore.client()
            db.collection('data kebun').document(user.uid).update({'model1' : pred_class})
            
            data_kebun = db.collection('data kebun').document(user.uid).get()
            komoditas = data_kebun.get('commodity')
            if 'model2' not in data_kebun.to_dict():
                if komoditas != pred_class:
                    db.collection('data kebun').document(user.uid).update({'status': "kurang baik"})
                else:
                    db.collection('data kebun').document(user.uid).update({'status': "baik"})
            else:
                if komoditas != pred_class:
                    db.collection('data kebun').document(user.uid).update({'status': "buruk"})
                else:
                    db.collection('data kebun').document(user.uid).update({'status': "kurang baik"})
            return {'predict': pred_class}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

npk_class = ['N', 'P', 'K']

def get_predicted_label_npk(pred_probabilities):
    # Turns an array of predictions probabilities into a label

    return npk_class[pred_probabilities.argmax()]

class PredictCropNPK(Resource):
    def post(self):
        if 'image' not in request.files:
            return jsonify({'error': 'No image found in the request'}), 401
        
        image = request.files['image']
        url = upload(image)

        # NPK data initialization
        n = 80.0
        p = 80.0
        k = 80.0

        image = preprocess_image(url)
        pred = model3.predict(image)
        max_pred = max(pred[0])

        if max_pred > 0.8:
            pred_class = get_predicted_label_npk(pred[0])
        else :
            pred_class = "sehat"

        if pred_class == "N":
            n = 20.0
        elif pred_class == "P":
            p = 20.0
        elif pred_class == "K":
            k = 20.0
        
        return {'n': n, 'p': p, 'k': k}, 200

class PredictCropCommodityAuto(Resource):
    @authorize_request
    def post(self):
        email = request.form.get('email')
        data = request.form
        
        if 'image' in request.files:
            image = request.files['image']
            url = upload(image)
            
            # NPK data initialization
            n = 80.0
            p = 80.0
            k = 80.0

            image = preprocess_image(url)
            pred = model3.predict(image)
            max_pred = max(pred[0])

            if max_pred > 0.8:
                pred_class = get_predicted_label_npk(pred[0])
            else :
                pred_class = "sehat"

            if pred_class == "N":
                n = 20.0
            elif pred_class == "P":
                p = 20.0
            elif pred_class == "K":
                k = 20.0
            
            model_data = [
            [n, 
             p, 
             k, 
             float(data.get('temperature')), 
             float(data.get('humidity')), 
             float(data.get('ph')), 
             float(data.get('rainfall'))]
            ]
        else:
            model_data = [
                [float(data.get('n')), 
                float(data.get('p')), 
                float(data.get('k')), 
                float(data.get('temperature')), 
                float(data.get('humidity')), 
                float(data.get('ph')), 
                float(data.get('rainfall'))]
            ]

        try:
            user = auth.get_user_by_email(email)
            pred = model1.predict(model_data)
            pred_class = get_predicted_label_commodity(pred[0])
            db = firestore.client()
            db.collection('data kebun').document(user.uid).update({'model1' : pred_class})
            
            data_kebun = db.collection('data kebun').document(user.uid).get()
            komoditas = data_kebun.get('commodity')
            if komoditas != pred_class:
                if 'model2' not in data_kebun.to_dict():
                    db.collection('data kebun').document(user.uid).update({'status': "kurang baik"})
                else:
                    db.collection('data kebun').document(user.uid).update({'status': "buruk"})
            return {'predict': pred_class}, 200
        except auth.EmailNotFoundError:
            return {'message': 'Email not found.'}, 401

api.add_resource(PredictPlantDisease, '/ai/predictdisease')
api.add_resource(PredictCropCommodityAuto, '/ai/predictcrop')
api.add_resource(PredictCropNPK, '/ai/predictnpk')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
