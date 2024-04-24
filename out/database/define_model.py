import pandas as pd
from surprise import Reader, Dataset, SVD, accuracy, dump

from surprise.model_selection import train_test_split,cross_validate
import os


csv = pd.read_csv('./defined.csv', names=['userID','itemID','rating','timestamp'], header=None)  # 사용자-아이템 상호작용 데이터

reader = Reader(rating_scale=(0.5, 5.0)) # 리더 설정, 평점 범위 0.5 ~ 5.0

data = Dataset.load_from_df(csv[['userID', 'itemID', 'rating']], reader) # 데이터 로드


# 모델 학습
if os.path.isfile('dump_file'):
    _, algo = dump.load(file_name)
else:
    algo = SVD(n_factors=50, random_state=5) # 요소 50개

algo.fit(data.build_full_trainset())


file_name = os.path.expanduser('dump_file')
dump.dump(file_name,algo=algo)

csv = pd.read_csv('./unseen.csv', names=['userID','itemID'], header=None)  # 비시청 목록

csv['pred'] = csv.apply(lambda row : algo.predict(row['userID'],row['itemID'],verbose=False)[3],axis=1)
for i in range(0,csv.shape[0]):
    print(csv.iloc[i,0],csv.iloc[i,1],csv.iloc[i,2])
