import json

from flask import Flask, request

from util import modelLoader


app = Flask(__name__)


@app.route("/")
def hello():
    return "Hello! Welcome!"


@app.route("/detect", methods=['POST'])
def index():
    id_to_class = ['MEL', 'NV', 'BCC', 'AKIEC', 'BKL', 'DF', 'VASC']
    received_img = request.files.to_dict()['file']

    if received_img:
        img_tensor = modelLoader.getImgTensor(received_img)
        result = modelLoader.predict(img_tensor)
        result = json.dumps(result)

        json_data = json.loads(result)

        json_data.sort(key=lambda x: x['probability'], reverse=True)
        print(id_to_class[json_data[0]['id']])

        return result

    else:
        return 'failed'


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0')
