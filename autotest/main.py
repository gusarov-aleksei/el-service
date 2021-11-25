"""Main function module"""
import el_service_requests
import unittest

import global_var


# retrieves arguments from command line
def get_args():
    """Parse input command line arguments"""
    import argparse
    parser = argparse.ArgumentParser()
    deploy_type_help = 'deploy type: all_local - tests and service are at localhost, cloud - service in cloud and ' \
                       'tests at localhost , all_cloud - service and tests in cloud '
    parser.add_argument('--deploy_type', default='all_local', help=deploy_type_help)
    known_args, unknown_args = parser.parse_known_args()
    return known_args


# setup global variables
def init_global_var(args):
    """Define base global variables"""
    global_var.args = args
    print(f"Command line args = {global_var.args}")
    print(f"deploy_type = {global_var.args.deploy_type}")
    global_var.el_service_url = el_service_requests.get_el_service_url(global_var.args.deploy_type)
    print(f"global variable el_service_url = {global_var.el_service_url}")


def run_tests():
    """Discover, run tests cases and return the result"""
    loader = unittest.TestLoader()
    suite = loader.discover('./tests')
    result = unittest.TextTestRunner().run(suite)
    return result


def el_service_au_function(event, context):
    """Entry point function called by GCP. Function name is set in Cloud Function config

    Parameters:
    event : data came from trigger (for example, from Pub-Sub trigger, HTTP trigger, etc)
    context : some metadata about calling service (for example,
    {event_id: 587156057, timestamp: 2021-11-24T19:53:00.943Z, event_type:
    google.pubsub.topic.publish, resource: {'service': 'pubsub.googleapis.com', 'name':
    'projects/algus-project-382/topics/el-service-autotest-topic'}}) """
    import argparse
    args = argparse.Namespace()
    # because function is called from GCP, create args object and use 'deploy_type' argument with 'all_cloud' value
    args.__setattr__('deploy_type', 'all_cloud')
    init_global_var(args)
    run_tests()


def main():
    init_global_var(get_args())
    run_tests()


if __name__ == '__main__':
    main()
