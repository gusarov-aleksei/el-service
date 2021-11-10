import el_service_requests
import json
import unittest


class ElServiceTest(unittest.TestCase):
    def test_health_check(self):
        # when
        response = el_service_requests.health_check()
        # then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['status'], "UP")

    def test_upload_files(self):
        # when
        response = el_service_requests.upload_files()
        # then
        self.assertTrue(response.ok, 'Response is not OK')
        self.assertEqual(response.status_code, 200, 'Response is not 200')
        self.assertEqual(response.text, '["file two.pdf","file one.pdf"]', 'List of uploaded files is not matched')
        # clean up
        el_service_requests.delete_files()

    def test_get_list(self):
        # given
        el_service_requests.upload_files()
        # when
        response = el_service_requests.list_files()
        response_body = response.json()
        # then
        # print(json.dumps(response_body, indent=2))
        self.assertEqual(type(response_body), list, 'Wrong response type')
        self.assertGreater(len(response_body), 0, 'Wrong response length')
        self.assertTrue("file two.pdf" in response_body)
        self.assertTrue("file one.pdf" in response_body)
        # clean up
        el_service_requests.delete_files()

    def test_extract_content(self):
        # given
        el_service_requests.upload_files()
        # when
        response = el_service_requests.extract_content()
        # then
        self.assertEqual(response.status_code, 200)
        response_body = response.json()
        # assert structure of text extracted from pdf file
        self.assertTrue("cultureNotes" in response_body)
        self.assertTrue("glossary" in response_body)
        self.assertTrue("whatElse" in response_body)
        self.assertTrue(response_body['cultureNotes'].startswith('Why Do We Carry on Smoking'))
        self.assertTrue(response_body['cultureNotes'].endswith('justify their habit.'))
        self.assertTrue("Habits context" in response_body['whatElse'])
        self.assertTrue("On reason of smoking" in response_body['whatElse'])
        self.assertTrue(response_body['whatElse']['Habits context'].startswith('Most smokers who think'))
        self.assertTrue(response_body['whatElse']['Habits context'].endswith('every day of our lives.'))
        self.assertTrue(response_body['whatElse']['On reason of smoking'].startswith('Let me emphasize'))
        self.assertTrue(response_body['whatElse']['On reason of smoking'].endswith('why we are still smoking.'))
        self.assertTrue("EASYWAY" in response_body['glossary'])
        self.assertTrue("About enjoyment" in response_body['glossary'])
        self.assertTrue(response_body['glossary']['EASYWAY'].startswith('Easy way to stop smoking'))
        self.assertTrue(response_body['glossary']['About enjoyment'].startswith('Some say cigarettes are'))
        self.assertTrue(response_body['glossary']['About enjoyment'].endswith('Enjoyment has nothing to do with it.'))
        # validate metadata dictionary
        self.assertTrue(response_body['metadata'] is not None)
        self.assertTrue(response_body['metadata']['fileName'] == 'file one.pdf')
        self.assertTrue(response_body['metadata']['fileSize'] == '215630')
        self.assertTrue(response_body['metadata']['fileName'] == 'file one.pdf')
        self.assertTrue('timeCreated' in response_body['metadata'])
        # print(json.dumps(response_body, indent=2))
        # clean up
        el_service_requests.delete_files()


if __name__ == '__main__':
    unittest.main()
