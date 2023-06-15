from flask import Flask, request, jsonify
from flask_restful import Resource, Api
import firebase_admin
from firebase_admin import auth, credentials, firestore
import jwt

app = Flask(__name__)
api = Api(app)

cred = credentials.Certificate('auth/credentials/firebase_credentials.json')
firebase_admin.initialize_app(cred)
jwt_secret = 'your_jwt_secret_key'

def generate_session_token(user_uid):
    payload = {
        'uid': user_uid,
    }
    session_token = jwt.encode(payload, jwt_secret, algorithm='HS256')
    return session_token

class AuthTokenResource(Resource):
    def post(self):
        email = request.json.get('email')

        try:
            # Authenticate User
            user = auth.get_user_by_email(email)

            # Generate session token
            session_token = generate_session_token(user.uid)

            # Store the session token in Firestore or any other database
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

class GetUserData(Resource):
    @authorize_request
    def get(self):
        user_uid = request.user_uid
        # Access the user data using the user_uid
        # ...

        return {'message': 'Authorized request'}, 200

api.add_resource(AuthTokenResource, '/auth/token')
api.add_resource(GetUserData, '/auth/getdata')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)