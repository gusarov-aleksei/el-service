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
def init_global_var():
    """Define base global variables"""
    global_var.args = get_args()
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


if __name__ == '__main__':
    init_global_var()
    run_tests()
