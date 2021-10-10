import requests

# enter ip address here
host_url = "http://localhost:8181"


def create_files_data_request(file_names):
    files_request = []
    for name in file_names:
        with open('data/' + name, 'rb') as f:
            files_request.append(('file', (name, f.read())))
    return files_request


def upload_files_old():
    files = [
        ('file', ('file one.pdf', open('data/file one.pdf', 'rb'))),
        ('file', ('file two.pdf', open('data/file one.pdf', 'rb')))
    ]
    return requests.post(host_url + "/uploadMulti", files=files)


def upload_files():
    files = create_files_data_request(['file one.pdf', 'file two.pdf'])
    return requests.post(host_url + "/uploadMulti", files=files)


def delete_files():
    return requests.delete(host_url + "/deleteFiles", data={'fileNames': ['file one.pdf', 'file two.pdf']})


def list_files():
    return requests.get(host_url + "/list")


def health_check():
    return requests.get(host_url + "/q/health/live")


def extract_content():
    return requests.get(host_url + "/extract", {'fileName': 'file one.pdf'})
