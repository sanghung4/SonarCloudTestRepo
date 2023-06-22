# TEST UI

Modified Dockerized React App originally authored by German David Gonzales

**Purpose:** Used to communicate current progress to testers/stakeholders

## Running

```sh
# Ensure you are in the test_ui dir
docker build -t test_ui .
docker run -p 80:80 test_ui
```

### Note about running on a server ⚠️

Make sure to modify the [Dockerfile](/test_ui/Dockerfile) environment variable for the API endpoint

For example, if running locally, change default.conf:
```
proxy_pass http://host.docker.internal:5000;
```

# Run FE without Docker.

1. Make sure you 're in `test_ui` folder
2. Make sure that `Node` is installed in your system
3. Install yarn using npm `npm install --global yarn`
4. Use `yarn install` command to install FE dependecies
5. Use `yarn start` command to start FE

- The environment variables set in Docker also need to be set in .env file in `gui` folder
- FE will be running on `http://localhost:3000/`

### Make sure that you 've set proper environment variables.
