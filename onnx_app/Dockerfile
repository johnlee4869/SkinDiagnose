FROM bitnami/pytorch:2.2.1
WORKDIR /onnx_app
COPY . /onnx_app
RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt
EXPOSE 5000
CMD ["python", "app.py"]