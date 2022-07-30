# STEP 1: pull base image alpine from Dockerhub.
FROM alpine

# STEP 2: run command to update software, dependencies.
RUN apk update

# STEP 3: COPY all file from current dir to foder app inside containers.
COPY . /app

# STEP 4: RUN cript "/app/my-app.sh" when containers start up.
# Docker image cannot run script on "CMD" or "ENTRYPOINT" => Only run when container created from image.
CMD [ "/app/my-app.sh" ]