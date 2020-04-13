import numpy as np
from keras.preprocessing import image
from keras.models import load_model
import cv2

model = load_model('covid19.h5')

# test_image = image.load_img('positive.jpg', target_size=(224, 224))
image = cv2.imread('negative2.jpg')
# test_image = image.img_to_array(image)
test_image = np.expand_dims(image, axis = 0)
result = model.predict(test_image)
print(result)