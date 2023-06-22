# POST
## Part of Speech Tagger

# Running PoST Locally
```sh
# See help + options for running api
$ python post/rest_api.py --help
```

1. Ensure all your dependencies are installed and your environment is activated
2. From the root level directory you can run `python post/rest_api.py`
3. Navigate to http://localhost:8000/docs to view the API Documentation and try out endpoints


# Running a 'PROD' like uvicorn server.
 ```uvicorn rest_api:app --host 0.0.0.0 --port 8000 --app-dir=post/```