from keras.models import load_model

# app = Flask(__name__)
# api = Api(app)

model = load_model('ai_model\model\model1.h5')

komoditas_class = ['apple', 'coffee', 'grapes', 'corn', 'rice']

json_data = {
    "n": 90, 
    "p": 42, 
    "k": 43, 
    "temperature": 20.879744, 
    "humidity":	82.002744, 
    "ph": 6.502985,
    "rainfall": 202.935536
    }

new_data = [
    [json_data['n'], json_data['p'], json_data['k'], json_data['temperature'], json_data['humidity'], json_data['ph'], json_data['rainfall']]
]

def get_predicted_label_komoditas(pred_probabilities):
    """
    Turns an array of predictions probabilities into a label
    """
    return komoditas_class[pred_probabilities.argmax()]

def predict_komoditas():
    data = new_data
    pred = model.predict(data)
    max_pred = max(pred[0])
    pred_class = get_predicted_label_komoditas(pred[0])
    print(max_pred)
    print(pred_class)
    return {'predict': pred_class}, 200

predict_komoditas()