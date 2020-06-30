# Manna - Share N Care
# Created for Global Call for Code ( Topic - COVID-19)

This solution is created by technologists from IBM.

## Authors

- Karthik Gundaboena
- Rajashekar Vadiga
- Selma Jyothi Prakash

## Contents

1. [Overview](#overview)
2. [The idea](#the-idea)
3. [How it works](#how-it-works)
4. [Diagrams](#diagrams)
6. [Technology](#technology)
7. [Getting started](#getting-started)
8. [Resources](#resources)
9. [License](#license)

## Overview

### What's the problem?

One of the most disturbing sights in this pandemic is millions of people (Migrants/ daily wagers/ people who lost jobs) unable to fend for  themselves due to loss of livelihood.​

​Their only sources for bare necessities are Government programmes or NGOs or charitable organizations supporting them. 

### How can technology help?

 A mobile application using IBM Cloud services to build a bridge between those who want to share and those who are in need of commodities


## The idea

Manna – Share and Care, is a solution that will help all the 1.3 Billion Indians to freely give and receive anything. Using the mobile application, every citizen can share whatever little or more with his fellow neighbours/ citizens/ poor/ any one who is in need. A person in need of food/ clothing/ or anything can just open the app and access what is freely available near them.


## How it works

Sharing using Manna

A person who has an item to share will open Manna application. They can see all the Need and share requests that are in their vicinity. If he/she finds a need request for the same item, they can respond to that request with location,time and address where they are comfortable to meet. This will send a message to the person who raised the need request. Both can meet at the specified location and share the items and request can be updated. The sharer can share the acheivement in social media platforms if they like.

If the person does not find a need for the item he is willing to share, they can create a share request and submit.


Requesting using Manna
A person who is in need of an item will open Manna application. They can see all the Need and share requests that are in their vicinity. If there is matching share request, user can submit a request against that share request.

The user can submit a new need request if there are no share requests that are sharing the items he need.

## Diagrams

![Manna architecture diagram](/MannaArchitecture.png)

This solution uses IBM Cloud services like DBaaS for MongoDB for data storage, Kubernetes Servcies for deployment. The application uses Google Maps services to display the requests that are currently in the system. Firebase Messaging services are used to enable notifications to users when a request is submitted.


## Technology

Java for Android Applications
Node JS for Server Side Processing
Docker
SMS Gateway
### IBM Cloud Services
HyperProtect DBaaS for MongoDB
Kubernetes Cluster Service
### Google Cloud Services
Firebase  for cloud Messaging (Not enabled yet)
Google Maps


## Getting started

### Prerequisites

- Register for an [IBM Cloud](https://cloud.ibm.com/login) account.
- Install and configure [IBM Cloud CLI](https://cloud.ibm.com/docs/cli?topic=cloud-cli-getting-started#overview).

    - **Android on Windows**
        - [Node.js](https://nodejs.org/en/)
        - Docker 
        - [Android Studio](https://developer.android.com/studio/index.html) - add Android 9 (Pie) SDK & configure `ANDROID_HOME`
        - [Create an Android Virtual Device (AVD)](https://developer.android.com/studio/run/managing-avds.html) - with Pie image (API Level 28)
- Clone the [repository](https://github.com/Manna).

### Steps

1. [Run the server deployed in Kubernetes cluster](#1-run-the-server)
2. Set up an instance of Hyper Protect DBaaS for MongoDB  (if Kuberbetes cluster service is expired - Expiry Date 07/19/2020)
3. [Run the server in local / cloud (if Kuberbetes cluster service is expired - Expiry Date 07/19/2020)](#2-run-the-server).
4. [Run the mobile application](#3-run-the-mobile-application).

### 1. Run the server deployed in Kubernetes Cluster
1. The server deployed in Kubernetes Cluster is available in IP http://173.193.75.60:32092/

Note: The Kubernetes service used is a lite plan which is valid for 30 days. As on 06/28/2020 20 days are remiaining in the plan

### 2. Run the local server

To set up and launch the server application:

1. Go to the `manna-server` directory of the cloned repo.
2. Place "cert.pem" received from newly provisioned Hyper Protect DBaaS for MongoDB instance in the directory manna-server
3. Update the mongo.js file with details of newly provisioned Hyper Protect DBaaS for MongoDB instance. The variables mongodbHost, mongodbPort, mongodbname, authenticate and certFileBuf
4. From a terminal:
    1. Go to the `manna-server` directory of the cloned repo.
    2. Install the dependencies: `npm install`.
    3. Launch the server application locally or deploy to IBM Cloud:
        - To run locally:
            1. Start the application: `npm start`.
            2. The server can be accessed at <http://localhost:3000>.
        - To deploy to IBM Cloud:
            1. In server.js replace the port number 3000 with 8080
            2. Log in to your IBM Cloud account using the IBM Cloud CLI: `ibmcloud login -sso`. (-sso is specific for ibm employees)
            2. Target to default resource group: `ibmcloud target -g default`.
            2. Target a Cloud Foundry org and space: `ibmcloud target --cf`.
            3. Push the app to IBM Cloud: `ibmcloud app push`.
            4. The server can be accessed at a URL using the **name** given in the `manifest.yml` file (for example,  <https://my-app-name.bluemix.net>).

### 3. Run the mobile application

To run the mobile application using Android Studio Emulator:

1. Go to the `manna-mobile` directory of the cloned repo.
2. Launch the app in the simulator/emulator:
        - **Android only**: `npm run android`
            > **Note**: Your Android Studio needs to have the `Android 9 (Pie)` SDK and a `Pie API Level 28` virtual device

With the application running in the simulator/emulator, you should be able to navigate through the various screens as mentioned in the prototype defined in https://marvelapp.com/ea7ch37/screen/69469366


## Resources

- [IBM Cloud](https://www.ibm.com/cloud)
- [IBM Cloud Hyper Protect DBaaS for MongoDB](https://cloud.ibm.com/docs/hyper-protect-dbaas-for-mongodb)
- [IBM Kubernetes Service](https://cloud.ibm.com/docs/containers?topic=containers-getting-started)
- [Node.js](https://nodejs.org)
- [Google Maps](https://developers.google.com/maps/documentation/android-sdk/start)
- [Google Firebase Messaging](https://firebase.google.com/docs/cloud-messaging/android/client) (Not enabled yet)


## License
This solution is made available under the [MIT]."# manna" 
