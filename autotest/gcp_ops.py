import googleapiclient
import googleapiclient.discovery
from google.cloud import storage


def get_gcp_compute_resource():
    """Creates instance of client for Compute Engine resources

    GOOGLE_APPLICATION_CREDENTIALS env variable is mandatory to perform this function
    """
    return googleapiclient.discovery.build('compute', 'v1')


class GcpFacade:
    """Facade over GCP communications"""
    def __init__(self, project_id, zone):
        self.compute = get_gcp_compute_resource()
        # "algus-project-382", "us-east1-b"
        self.project_id = project_id
        self.zone = zone
        self.storage_client = storage.Client()

    # TODO extend filtration with tags
    def list_instances(self):
        """List all compute instances in project and zone"""
        result = self.compute.instances().list(project=self.project_id, zone=self.zone).execute()
        return result['items'] if 'items' in result else None

    def get_external_ip(self):
        """Get simply first instance and first network interface from it
        (assuming simple configuration of deployed instances at present moment)
        """
        compute_instances = self.list_instances()
        return compute_instances[0]['networkInterfaces'][0]['accessConfigs'][0]['natIP']

    def get_blobs_bytes(self, bucket_name: str):
        """Retrieves data from blobs of bucket

        :param bucket_name: A name of the bucket
        :return: Tuple of file name and file data. Tuple(str, bytes)
        """
        blob_bytes_list = []
        blobs = self.storage_client.list_blobs(bucket_name)
        for blob in blobs:
            blob_bytes_list.append((self.__retrieve_file_name(blob.name), blob.download_as_bytes()))
        # print(len(blob_bytes_list))
        return blob_bytes_list

    @staticmethod
    def __retrieve_file_name(full_name: str):
        last_slash_in_path = full_name.rfind('/')
        return full_name[last_slash_in_path + 1:]
