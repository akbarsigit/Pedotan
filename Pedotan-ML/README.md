# Pedotan-MachineLearning
# 🌱 PEDOTAN (Pengendalian Optimal Tanaman) 🌿 - Guardians of the Green (Team C23-PS379) - ML Repository

Pedotan Machine Learning Repository for Bangkit Capstone Project. Building Machine Learning Model to identify plant disease 
## 👥Guardian of Machine Learning Bangkit Academy Capstone Team C23-PS379
|            Member           | Student ID |        Path        |                    Role                    |                                                       Contacts                                                      |
| :-------------------------: | :--------: | :----------------: | :----------------------------------------: | :-----------------------------------------------------------------------------------------------------------------: |
| Akbar Sigit Putra  | M169DSX0378 |  Machine Learning  |Machine Learning Engineer |[akbarsigit](https://github.com/akbarsigit)|
| Muhammad Rifat Bagas Adikusuma | M169DSX0536  |  Machine Learning  | Machine Learning Engineer | [muhammadrifatba](https://github.com/muhammadrifatba) |

## Tech Stack
![TensorFlow](https://img.shields.io/badge/TensorFlow-%23FF6F00.svg?style=for-the-badge&logo=TensorFlow&logoColor=white)
![scikit-learn](https://img.shields.io/badge/scikit--learn-%23F7931E.svg?style=for-the-badge&logo=scikit-learn&logoColor=white)
![Keras](https://img.shields.io/badge/Keras-%23D00000.svg?style=for-the-badge&logo=Keras&logoColor=white)
![NumPy](https://img.shields.io/badge/numpy-%23013243.svg?style=for-the-badge&logo=numpy&logoColor=white)
![Pandas](https://img.shields.io/badge/pandas-%23150458.svg?style=for-the-badge&logo=pandas&logoColor=white)

## About 
We have 3 model in this repository
### Plant Disease Detection
[Plant disease Detection ](https://github.com/akbarsigit/Pedotan-MachineLearning/blob/main/crop_disease_detection.ipynb) 
(image classification) use InceptionV3 as the base model for transfer learning that is taken from [Keras](https://keras.io/api/applications/inceptionv3/). The model also contains an additional layer that received output from the based model. The model had 36 node to define its 36 classification categories.  [Plant Disease Classification Merged Dataset](https://www.kaggle.com/datasets/alinedobrovsky/plant-disease-classification-merged-dataset) that contains 18.96 GB images of various plant diseases. 

### Nutrient Deficiency Detection
- [Nutrient Deficiency Detection](https://github.com/akbarsigit/Pedotan-MachineLearning/blob/main/leafNutrient.ipynb) use Densenet-121 for the base model and an additional layer to capture specific patterns for nutrient deficiency problems. This model can predict three macronutrients from the picture that is for N, P, and K nutrients. In the fine-tuning process, the pre-trained Densenet-121 model, initially trained on a large-scale dataset, is further optimized by training it on the Crop Recommendation Dataset ([Nutrient-Deficiency-Symptoms-in-Rice](https://www.kaggle.com/datasets/guy007/nutrientdeficiencysymptomsinrice)) available on Kaggle. This dataset contains a diverse collection of images representing various crops and their associated nutrient deficiencies. By fine-tuning the model on this dataset, it becomes more adept at accurately detecting and classifying nutrient deficiencies in crops.

  
### Ideal Farm Detection
- [Ideal Farm Detection](https://github.com/akbarsigit/Pedotan-MachineLearning/blob/main/cropCNN.ipynb) utilizing a custom deep neural network architecture that analyzes various parameters such as pH levels, NPK nutrition, temperature, humidity, and rainfall density to predict the suitability of different crops for a given farm. By training the custom deep neural network on this [Crop Recommendation Dataset](https://www.kaggle.com/datasets/siddharthss/crop-recommendation-dataset), the model learns to correlate the provided parameters with the optimal conditions for various crops that predict up to five commodities.

  
## Model Performance
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


## Run the ipynb in Google Colab
Dont need to install anything just follow the steps below
1. Download or clone this repository.
2. Open google colab
3. Import the ipynb file
4. Run the code

## Run in Local

1. Download the ipynb file or clone this repostitory
2. Run this locally using ex: jupyter notebook
3. Install all the dependencis
  ```
  ! pip install -r requirements.txt
  ```
4. Run all the code

## References 
1. M. S. Hasan Talukder, A. Krishno Sarkar and M. Nuhi-Alamin, "An Improved Model for Nutrient Deficiency Diagnosis of Rice Plant by Ensemble Learning," 2022 4th International Conference on Sustainable Technologies for Industry 4.0 (STI), Dhaka, Bangladesh, 2022, pp. 1-6, doi: 10.1109/STI56238.2022.10103280.
2. B. Srinivasa Rao, R. Vijaya Kumar Reddy, D. Manogna, K. Akhil and D. D. Sree, "Identification of Nutrient Deficiency in Rice Leaves using DenseNet-121," 2022 International Conference on Edge Computing and Applications (ICECAA), Tamilnadu, India, 2022, pp. 1573-1578, doi: 10.1109/ICECAA55415.2022.9936191.
3. K. Liu and X. Zhang, "PiTLiD: Identification of Plant Disease From Leaf Images Based on Convolutional Neural Network," in IEEE/ACM Transactions on Computational Biology and Bioinformatics, vol. 20, no. 2, pp. 1278-1288, 1 March-April 2023, doi: 10.1109/TCBB.2022.3195291.
4. X. Guan, "A Novel Method of Plant Leaf Disease Detection Based on Deep Learning and Convolutional Neural Network," 2021 6th International Conference on Intelligent Computing and Signal Processing (ICSP), Xi'an, China, 2021, pp. 816-819, doi: 10.1109/ICSP51882.2021.9408806.

