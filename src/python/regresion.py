import os
import sys
import warnings
import keras
import joblib
import numpy as np
from keras.losses import MeanSquaredError

# Suprimir advertencias de TensorFlow
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

# Suprimir advertencias de scikit-learn
warnings.filterwarnings("ignore", category=UserWarning, module='sklearn')

# Reconfigurar la salida estándar para UTF-8
sys.stdout.reconfigure(encoding='utf-8')

# Registrar la métrica personalizada
keras.utils.get_custom_objects().update({"mse": MeanSquaredError()})

# Cargar el modelo de Keras y el scaler de joblib
modelo_cargado = keras.models.load_model(r"src\python\Regresion.h5")
ajuste_cargado = joblib.load(r"src\python\scaler.pkl")

# Compilar el modelo para resolver la advertencia sobre las métricas compiladas
modelo_cargado.compile(optimizer='adam', loss='mean_squared_error', metrics=[MeanSquaredError()])

# Leer los argumentos de entrada
args = sys.argv[1:]  # Ignorar el primer argumento que es el nombre del script

# Verificar que se recibieron exactamente 9 argumentos
if len(args) != 9:
    print("Error: Se esperaban 9 argumentos de entrada, pero se recibieron", len(args))
    sys.exit(1)

try:
    # Convertir los argumentos de entrada en una matriz numpy
    X_new = np.array([list(map(float, args))])

    # Normalizar los datos de entrada
    X = ajuste_cargado.transform(X_new)

    # Predecir con el modelo cargado
    y_pred = modelo_cargado.predict(X)

    # Imprimir la predicción
    print("Predicción del modelo:", y_pred[0][0])
except ValueError as e:
    print("Error al convertir los argumentos de entrada:", e)
    sys.exit(1)
