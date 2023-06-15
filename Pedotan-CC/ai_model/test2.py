from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from PIL import Image
import numpy as np
from keras.models import load_model
from tensorflow.keras.utils import load_img, img_to_array
from io import BytesIO
import requests  # move requests to the end of the imports
from google.cloud import storage


model3 = load_model('./ai_model/model/model3.h5')
model1 = load_model('./ai_model/model/model1.h5')

disease_class = ['Nitrogen(N)', 'Phosphorus(P)', 'Potassium(K)']

def preprocess_image(url):
    img = Image.open(url).convert('RGB')

    # Resize the image
    img_resized = img.resize((224, 224))

    # Convert the image to numpy array and normalize the pixel values
    image = img_to_array(img_resized)
    image /= 255
    image = np.expand_dims(image, axis = 0)
    image = np.vstack([image])

    return image

def get_predicted_label(pred_probabilities):
    # Turns an array of predictions probabilities into a label

    return disease_class[pred_probabilities.argmax()]

komoditas_class = ['apple', 'coffee', 'grapes', 'corn', 'rice']

def get_predicted_label_komoditas(pred_probabilities):
    """
    Turns an array of predictions probabilities into a label
    """
    return komoditas_class[pred_probabilities.argmax()]

def predict():
    # if 'url' not in request.json
    image = preprocess_image("ai_model/content/tomat.jpg")
    pred = model3.predict(image)
    max_pred = max(pred[0])
    if max_pred > 0.8:
        pred_class = get_predicted_label(pred[0])
    else :
        pred_class = "sehat"
    
    n = 80
    p = 80
    k = 80

    print(pred)
    print(max_pred)
    print(pred_class)
    if pred_class == "Nitrogen(N)":
        print ("N = 20")
        n = 20
    elif pred_class == "Phosphorus(P)":
        print ("P = 20")
        p = 20
    elif pred_class == "Potassium(K)":
        print ("K = 20")
        k = 20

    print(n, p, k)

    json_data = {
    "n": 90, 
    "p": 42, 
    "k": 43, 
    "temperature": 14.23784, 
    "humidity":	92.002744, 
    "ph": 6.502985,
    "rainfall": 2002.935536
    }

    data = [[n, p, k, json_data['temperature'], json_data['humidity'], json_data['ph'], json_data['rainfall']]]
    
    pred = model1.predict(data)
    max_pred = max(pred[0])
    pred_class = get_predicted_label_komoditas(pred[0])
    print(max_pred)
    print(pred_class)
    print(n,p,k)

    return {'predict': pred_class}, 200

predict()