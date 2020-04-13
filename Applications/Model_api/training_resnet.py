import keras
from keras.layers import Input, Lambda, Dense, Flatten,GlobalAveragePooling2D
from keras.models import Model
from keras.applications.resnet50 import ResNet50,preprocess_input
from keras.preprocessing import image
from keras.preprocessing.image import ImageDataGenerator
import numpy as np
from glob import glob
import matplotlib.pyplot as plt
from keras.callbacks import EarlyStopping
from keras.callbacks import ModelCheckpoint

IMAGE_SIZE = [224, 224]

train_path = 'Training'
valid_path = 'Testing'

base_model = ResNet50(weights='imagenet', include_top=False)
x = base_model.output
x = GlobalAveragePooling2D()(x)
prediction = Dense(2, activation='softmax')(x)
model = Model(inputs=base_model.input, outputs=prediction)
for layer in base_model.layers:
    layer.trainable = False
model.compile(
  loss='categorical_crossentropy',
  optimizer='adam',
  metrics=['accuracy']
)

from keras.preprocessing.image import ImageDataGenerator

train_datagen = ImageDataGenerator(rescale = 1./255,
                                   shear_range = 0.2,
                                   zoom_range = 0.2,
                                   horizontal_flip = True)

test_datagen = ImageDataGenerator(rescale = 1./255)

training_set = train_datagen.flow_from_directory('Training',target_size = (224, 224),batch_size = 12,class_mode = 'categorical')

test_set = test_datagen.flow_from_directory('Testing',target_size = (224, 224),batch_size = 12,class_mode = 'categorical')

callbacks = [EarlyStopping(monitor='val_loss', patience=2),ModelCheckpoint(filepath='covid19.h5', monitor='val_loss', save_best_only=True)]

# fit the model
model.fit_generator(training_set,steps_per_epoch=45,validation_data=test_set,epochs=20,validation_steps=18)
model.save('covid19.h5')
# loss
# plt.plot(r.history['loss'], label='train loss')
# plt.plot(r.history['val_loss'], label='val loss')
# plt.legend()
# plt.show()
# plt.savefig('LossVal_loss')

# # accuracies
# plt.plot(r.history['acc'], label='train acc')
# plt.plot(r.history['val_acc'], label='val acc')
# plt.legend()
# plt.show()
# plt.savefig('AccVal_acc')

# import tensorflow as tf

# from keras.models import load_model

