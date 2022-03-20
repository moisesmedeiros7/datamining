# IMPORTS
import pandas as pd
import numpy as np
from IPython.display import display

from sklearn.dummy import DummyClassifier

from sklearn.model_selection import StratifiedKFold
from sklearn.model_selection import cross_val_predict


from sklearn.metrics import accuracy_score #acuracia
from sklearn.metrics import precision_score #precision
from sklearn.metrics import recall_score #recall
from sklearn.metrics import roc_auc_score #area sob curva roc
from sklearn.metrics import roc_curve #curva roc
from sklearn.metrics import confusion_matrix #matriz de confusão
from matplotlib import pyplot #grafico


def warn(*args, **kwargs):    # eliminando os warnings
    pass
import warnings
warnings.warn = warn


# recebendo base de dados
dataset = pd.read_csv('ne-balanceada.csv')

# separando rótulo das classes dos demais dados
y = dataset['classe']
X = dataset.iloc[:, 0:25]

# modelo MLP
modelo = DummyClassifier()

SEED = 42
np.random.seed(SEED)
cv = StratifiedKFold(n_splits = 10, shuffle = True)

# gerando matriz de confusão e usando validação cruzada
y_pred = cross_val_predict(modelo, X, y, cv=cv)

print(y)
print(y_pred)

vn, fp, fn, vp = confusion_matrix(y, y_pred).ravel()
print ('======MATRIZ DE CONFUSÃO {}======'.format(modelo))
print("Verdadeiro Positivo: {}".format(vp) )
print("Falso Negativo: {}".format(fn))
print("Falso Positivo: {}".format(fp))
print("Verdadeiro Negativo:{}".format(vn))
# gerando métricas do classificador
acc = accuracy_score(y, y_pred)
prec = precision_score(y, y_pred)
recall = recall_score(y, y_pred)
especif = vn / (vn + fp)  # não necessariamente é preciso de uma função pronta
roc = roc_auc_score(y, y_pred)
print ('======MÉTRICAS {}======'.format(modelo))
print ("Acurácia: {:.3f}".format(acc))
print ("Precisão: {:.3f}".format(prec))
print ("Sensibilidade: {:.3f}".format(recall))
print ("Especificidade: {:.3f}".format(especif))
print ("Área ROC: {:.3f}".format(roc))


