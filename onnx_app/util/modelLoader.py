import torch
import torchvision

import numpy as np
from torch import nn
import time
from PIL import Image
import onnxruntime as ort


ort_session = ort.InferenceSession("model/model_8406.onnx")

id_to_class = {0: 'MEL', 1: 'NV', 2: 'BCC', 3: 'AKIEC', 4: 'BKL', 5: 'DF', 6: 'VASC'}
skin_chinese = {'MEL': '黑色素瘤',
                'NV': '黑素细胞痣',
                'BCC': '基底细胞癌',
                'AKIEC': '光化性角化病',
                'BKL': '良性角化病',
                'DF': '皮肤纤维瘤',
                'VASC': '血管病变'}


def softmax(x):
    # 如果输入是一个列表或数组，先转换为 numpy 数组
    x = np.array(x)

    # 如果存在负值，对每个元素减去最大值
    if np.any(x < 0):
        x -= np.max(x)

    # 计算指数值
    exp_x = np.exp(x)

    # 计算 softmax
    softmax_x = exp_x / np.sum(exp_x)

    return softmax_x


def getImgTensor(image_path):
    img = Image.open(image_path).convert('RGB')
    img = img.resize((224, 224))
    img = np.array(img)
    img = np.transpose(img, (2, 0, 1))
    img = np.expand_dims(img, axis=0)
    img = img.astype(np.float32)
    img = img / 255.0
    img[:, 0, :, :] = (img[:, 0, :, :] - 0.7635216) / 0.08962788
    img[:, 1, :, :] = (img[:, 1, :, :] - 0.54612774) / 0.11830781
    img[:, 2, :, :] = (img[:, 2, :, :] - 0.57052994) / 0.13295422
    return img


def predict(img_tensor):
    start = time.time()
    pred = ort_session.run(None, {'input': img_tensor})[0][0]
    # res = torch.argmax(pre, dim=1).cpu().numpy()[0]   #唯一结果
    end = time.time()
    print(end - start)

    probability = softmax(pred)
    print(probability)

    data = []
    for i in range(0, 7, 1):
        data.append({'id': i, 'probability': round(float(probability[i]), 5)})

    return data


if __name__ == '__main__':
    tensor = getImgTensor('../VASC.png')

    #result = predict(tensor)



    start = time.time()
    model = torchvision.models.resnet50()
    end = time.time()

    print('torch:', end - start)


