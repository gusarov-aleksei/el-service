"""It holds functions for communications with El Service over HTTP"""
import requests


def get_el_service_url(deploy):
    """Define El Service url

    Depending on deployment type deploy, it performs logic for ip retrieval.
    """
    if deploy == "all_local":
        return "http://localhost:8181"
    else:
        from gcp_ops import GcpFacade
        gcp = GcpFacade("algus-project-382", "us-east1-b")
        return f"http://{gcp.get_external_ip()}:8080"


def create_files_data_request(file_names):
    """Reads all data from files in file_names"""
    files_request = []
    for name in file_names:
        with open('data/' + name, 'rb') as f:
            files_request.append(('file', (name, f.read())))
    return files_request


class ElServiceFacade:
    """Class contains most common parts required for communication with remote El Service"""

    def __init__(self, url):
        self.url = url

    def upload_files_old(self):
        files = [
            ('file', ('file one.pdf', open('data/file one.pdf', 'rb'))),
            ('file', ('file two.pdf', open('data/file one.pdf', 'rb')))
        ]
        return requests.post(self.url + "/uploadMulti", files=files)

    def upload_files(self):
        files = create_files_data_request(['file one.pdf', 'file two.pdf'])
        return requests.post(self.url + "/uploadMulti", files=files)

    def delete_files(self):
        return requests.delete(self.url + "/deleteFiles", data={'fileNames': ['file one.pdf', 'file two.pdf']})

    def list_files(self):
        return requests.get(self.url + "/list")

    def health_check(self):
        return requests.get(self.url + "/q/health/live")

    def extract_content(self):
        return requests.get(self.url + "/extract", {'fileName': 'file one.pdf'})
