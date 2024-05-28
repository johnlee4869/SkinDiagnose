import numpy as np
import onnxruntime as ort
import torch
import torchvision
from torch import nn
from PIL import  Image

from torchvision import transforms

toTensor = transforms.ToTensor()

def getImgTensor(image):
    img = Image.open(image)
    test_transformer = transforms.Compose(
        [
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.7635216, 0.54612774, 0.57052994], std=[0.08962788, 0.11830781, 0.13295422])
        ]
    )
    img_tensor = test_transformer(img)
    img_tensor = img_tensor.unsqueeze(0)
    return img_tensor


device = torch.device('cpu')
model_path = 'cosine-SGD_0.8406.pth'
model = torchvision.models.resnet50().to(device).eval()
model.fc = nn.Linear(model.fc.in_features, 7)
model.fc.out_features = 7
model.load_state_dict(torch.load(model_path, map_location='cpu'))

img_tensor = getImgTensor('../test_imgs/VASC.png')


pytorch_output = model(img_tensor)
probability = torch.nn.functional.softmax(pytorch_output, dim=1)
probability = np.round(probability.cpu().detach().numpy(), 4)

print('pytorch result:', pytorch_output)
print('pytorch:', probability)

img = Image.open('../test_imgs/VASC.png').convert('RGB')
img = img.resize((224, 224))
img = np.array(img)
img = np.transpose(img, (2, 0, 1))
img = np.expand_dims(img, axis=0)
img = img.astype(np.float32)
img = img / 255.0
img[:, 0, :, :] = (img[:, 0, :, :] - 0.7635216) / 0.08962788
img[:, 1, :, :] = (img[:, 1, :, :] - 0.54612774) / 0.11830781
img[:, 2, :, :] = (img[:, 2, :, :] - 0.57052994) / 0.13295422


input_names = ["input"]
output_names = ["output"]
# translate your pytorch model to onnx
torch.onnx.export(model, img, "model_8406.onnx", verbose=True, input_names=input_names,
                      output_names=output_names,  dynamic_axes={"input": [0],  "output": [0]})

ort_session = ort.InferenceSession("model_8406.onnx")
outputs = ort_session.run(None, {'input': img.numpy()})


def softmax(x):
    """
    Compute softmax values for each sets of scores in x.

    Arguments:
    x -- A numpy array of shape (n_samples, n_classes)

    Returns:
    A numpy array of shape (n_samples, n_classes) containing the softmax values.
    """
    # Subtract the maximum value of each row for numerical stability
    exp_x = np.exp(x - np.max(x, axis=1, keepdims=True), dtype=np.float64)

    # Compute softmax probabilities
    softmax_probs = exp_x / np.sum(exp_x, axis=1, keepdims=True, dtype=np.float64)

    return softmax_probs
print('onnx result:', outputs[0])
print('onnx:', softmax(outputs[0]))