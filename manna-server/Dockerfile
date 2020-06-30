FROM node:12

WORKDIR /app

# Install app dependencies
COPY package*.json /app/
RUN npm install

# Bundle app source
COPY . /app

#Expose port 3000 to the outside
EXPOSE 3000

# Define what to run on launching docket
CMD [ "node", "server.js" ]