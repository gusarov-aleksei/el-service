import googleapiclient
import googleapiclient.discovery


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
