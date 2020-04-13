import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import keras
from keras.models import Model,load_model
import cv2
from flask import Flask,request,jsonify
import pyrebase

#Configuring Te Firebase
config = {
  "apiKey": "AIzaSyBCTULvyrph54v4daplZogA-wAXc5Wf5gI",
  "authDomain": "projectId.firebaseapp.com",
  "databaseURL": "https://karunafightersdoctors.firebaseio.com/",
  "storageBucket": "karunafightersdoctors.appspot.com",
  "serviceAccount": "karunafightersdoctors-firebase-adminsdk-bbos4-3f66d9ae94.json"
}
firebase = pyrebase.initialize_app(config)
db = firebase.database()
storage = firebase.storage()

#Setting up the model
model_path = 'covid19.h5'
model = load_model(model_path)

#Setting up the Classifier
data = pd.read_csv('DATA_refine.csv')
X = data.iloc[:,[0,1,2,3,4,5,6,9,10]].values
y = data.iloc[:,-1].values
from sklearn.model_selection import train_test_split
X_train,X_test,y_train,y_test = train_test_split(X,y,test_size=0.3,random_state=10)
from sklearn.ensemble import RandomForestClassifier
clf = RandomForestClassifier()
clf.fit(X_train, y_train)

#Setting the API
app = Flask(__name__)
app.config['DEBUG'] = True

@app.route('/',methods = ['GET'])
def home():
    return 'test'

@app.route('/get_hospitals',methods = ['GET','POST'])
def get_hospitals():
    output = []
    count = 1
    hospitals = db.child('Users').child('Hospitals').get().val()
    for hospital in hospitals:
        data = {}
        if hospital!="Doctors":
            if count==3:
                break
            data['Name'] = hospitals[hospital]['nameOFHosp']
            data['address'] = hospitals[hospital]['addre']
            data['phone'] = hospitals[hospital]['phoneNum']
            count+=1
            output.append(data)
    return jsonify(output)

@app.route('/chat',methods = ['GET','POST'])
def get_chat_result():
    data = request.get_json()
    fever = (1 if data['Fever']=='YES' else 0)
    tir = (1 if data['Tired']=='YES' else 0)
    dry = (1 if data['dry']=='YES' else 0)
    dif_br = (1 if data['difbr']=='YES' else 0)
    st = (1 if data['sr']=='YES' else 0)
    nc = (1 if data['nc']=='YES' else 0)
    rn = (1 if data['rn']=='YES' else 0)
    # area = data['Fever']
    iso = (1 if data['iso']=='YES' else 0)
    age = (1 if data['age']=='YES' else 0)
    print(data)
    print("AAAAAA")
    x = np.array([fever,tir,dry,dif_br,st,nc,rn,iso,age])
    y_pred = clf.predict([x])
    print(y_pred)
    if y_pred==0:

        result = "Not Positive"
    else:
        result = "Positive"
    return {"results":result}

def preprocess_image(image):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = cv2.resize(image, (224, 224))
    image = np.expand_dims(image, axis = 0)
    return image

def get_image(patient_id):
    image_name = str(patient_id)
    print(image_name)
    storage.child('images/'+ image_name).download(image_name)
    image = cv2.imread(image_name)
    return preprocess_image(image)

def predict(image):
    result = model.predict(image).argmax(axis=-1).tolist()[0]
    print(result)
    return result

@app.route('/get_results',methods = ['POST'])
def get_results():
    patient_id = request.args['id']
    image = get_image(patient_id)
    result = predict(image)
    return {'result':result}


app.run()