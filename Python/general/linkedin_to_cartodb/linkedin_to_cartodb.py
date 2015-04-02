#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
    This script gets your contacts from your Linkedin account and put them in a CartoDB table. So, you can see
    your contacts in a map.

    Take into account that:
        - You need to install Python libraries: requests, python-linkedin
        - You need to define your Linkedin API_KEY, API_SECRET, USER_TOKEN, USER_SECRET and RETURN_URL as environment variables
        - You need to define your CartoDB API_KEY and TABLE as environment variables.
        - Your CartoDB table needs to be PUBLIC, and contain the fields 'name', 'industry' and 'living area'. 
          Of course, you can change the code to fit your needs

    After running the script, you should be able to see all your geolocated Linkedin contacts in your CartoDB table,

    More info at:
        - http://ozgur.github.io/python-linkedin/
        - http://docs.cartodb.com/cartodb-platform/sql-api.html
"""
import sys, json, requests
from os import environ
from linkedin import linkedin

def get_env_variable(var_name):
    try:
        return environ[var_name]
    except KeyError:
        msg = "Set the %s environment variable"
        error_msg = msg % var_name
        raise Exception(error_msg)


API_KEY = get_env_variable('API_KEY')
API_SECRET = get_env_variable('API_SECRET')
USER_TOKEN = get_env_variable('USER_TOKEN')
USER_SECRET = get_env_variable('USER_SECRET')
RETURN_URL = 'https://localhost'
CDB_USER = get_env_variable('CDB_USER')
CDB_APIKEY = get_env_variable('CDB_APIKEY')
CDB_TABLE = 'linkedin_connections'

cartodb_url_sql = "http://%s.cartodb.com/api/v2/sql"


# Instantiate the developer authentication class
authentication = linkedin.LinkedInDeveloperAuthentication(API_KEY, API_SECRET,
                                                          USER_TOKEN, USER_SECRET,
                                                          RETURN_URL, linkedin.PERMISSIONS.enums.values())

# Pass it in to the app...
application = linkedin.LinkedInApplication(authentication)

# Use the app....
connections = application.get_connections()

if 'values' in connections:

    for connection in connections['values']:
        insert_dict = {}

        if 'firstName' in connection and 'lastName' in connection:
            insert_dict['name'] = "'%s %s'" % (connection['firstName'], connection['lastName'])
        if 'industry' in connection:
            insert_dict['industry'] = "'%s'" % connection['industry']
        if 'location' in connection and 'name' in connection['location']:
            insert_dict['living_area'] = "'%s'" % connection['location']['name']

        sql = "insert into %s(%s) values(%s)" % (CDB_TABLE, ','.join(insert_dict.keys()),','.join(insert_dict.values()))


        #Insert entry in CartoDB
        payload = {'q': sql, 'api_key': CDB_APIKEY}
        r = requests.get(cartodb_url_sql % CDB_USER, params=payload)

        print r.url
