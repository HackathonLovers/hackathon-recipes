This script gets your contacts from your Linkedin account and put them in a CartoDB table. So, you can see your contacts in a map.

Take into account that:
    - You need to install Python libraries: requests, python-linkedin
    - You need to define your Linkedin API_KEY, API_SECRET, USER_TOKEN, USER_SECRET and RETURN_URL as environment variables
    - You need to define your CartoDB API_KEY and TABLE as environment variables.
    - Your CartoDB table needs to be PUBLIC.

After running the script, you should be able to see all your geolocated Linkedin contacts in your CartoDB table,

More info at:
    - http://ozgur.github.io/python-linkedin/
    - http://docs.cartodb.com/cartodb-platform/sql-api.html
