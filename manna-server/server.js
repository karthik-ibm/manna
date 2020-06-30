//Express 
const express = require('express');
const bodyParser = require('body-parser');
const app = express();

let nearest_neighbour = {}

// Database - Mongo DB
const manna_db = require('./mongo.js');

// Bodyparser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}))

app.listen(3000,function(){
    console.log('listening on 3000')
})

/**  User validation from Splash screen
 * POST for PreFlight
 * Json object can be sent from jave only if method is POST
 */
app.post('/PreFlight', (req, res) => {    
  const phone = req.body.mobile
  const id = req.body.mannaID
  const lat = req.body.latitude
  const lon = req.body.longitude
  const device_id = req.body.deviceID

  if (!phone){
    console.log('API PreFlight : ERROR - Phone number is required');
    return res.status(400).send('Phone number is required')
  } 

  if (!id){
    console.log('API PreFlight : ERROR - Manna ID is required');
    return res.status(400).send('Manna ID is required')
  } 

  if (!lat || !lon){
    console.log('API PreFlight : ERROR - Co-ordinates are required');
    return res.status(400).send('Co-ordinates are required')
  } 

  console.log(`Test phone : ${phone}`)
  console.log(`Test Id : ${id}`)

  manna_db
    .finduser(id,phone,lat,lon)
    .then(data => {
      if (data.statusCode != 200) {
        res.sendStatus(data.statusCode)
      } else {
        res.json(data)
      }
    })
    .catch(err => handleError(res,err))
})

/* Generate OTP
 */
app.get('/getOTP', (req, res) => {
  const phone = req.query.mobile

  if (!phone){
    console.log('API getOTP : ERROR - Phone number is required');
    res.status(400).send('Phone number is required')
  } else {
    let otp = 123456
    console.log(`API getOTP - OTP Generated: ${otp}`);
    res.status(200).send(`OTP Generated: ${otp}`)
  }
})

/* Validate OTP
 */
app.post('/validateOTP',(req,res) => {
  const phone = req.body.mobile
  const lat = req.body.latitude
  const lon = req.body.longitude

  if (!phone){
    console.log('API validateOTP : ERROR - Phone number is required');
    return res.status(400).send('Phone number is required')
  } 

  if (!lat || !lon){
    console.log('API validateOTP : ERROR - Co-ordinates are required');
    return res.status(400).send('Co-ordinates are required')
  } 

  console.log(`Validating OTP for phone ${phone}`)
  console.log("OTP validated successfully");

  //Call function to create user and get the details of all requests
  manna_db
    .getNewUser(phone,lat,lon)
    .then(data => {
      if (data.statusCode != 200) {
        res.sendStatus(data.statusCode)
      } else {
        res.json(data)
      }      
    })
    .catch(err => handleError(res,err))
})


/* Fetch the nearest Need and Share requests to the user
 */
app.get('/fetchNearest', (req, res) => {
  const long = Number(req.query.longitude)
  const lat = Number(req.query.latitude)
  const err_msg = "ERROR - User Co-ordinates are required"

  if (!long){
    console.log(`API getOTP : ${err_msg}`);
    return res.status(400).send(err_msg)
  }
  if (!lat){
    console.log(`API getOTP : ${err_msg}`);
    return res.status(400).send(err_msg)
  }

  var A = {
    lat: lat,
    lon: long
  };
  //var B = {}
  const rad = '5 km'
  console.log(A)
  console.log(`Fetching requests in ${rad} km radius`);

  // Call function to get all share and need requests
  manna_db
  .findAllRequests()
  .then(data1 => {
    no_req = data1.No_AllRequest
    allreq = data1.allrequest;
    console.log(no_req)
    nearest_neighbour = []
    nearest_neighbour = manna_db.neighbours(A,allreq,rad)
    res.json({nearest_neighbour:nearest_neighbour})   
  })
  .catch(err => handleError(res,err))
})

/* Fetch the nearest Need and Share requests to the user
 */
app.get('/myRequests', (req, res) => {
  const mannaID = req.query.mannaID
  const phone = ""

  if (!mannaID){
    console.log('API myRequests : ERROR - Manna ID is required');
    return res.status(400).send('Manna ID is required')
  } 

  manna_db
  .findUserRequests(mannaID,phone)
  .then(data => {  
      delete data.No_UserRequest;                        
      res.json(data);                                                                     
  })
  .catch(err => handleError(res,err))
})

/* Fetch the catalog
 */
app.get('/getCatalog', (req, res) => {
  manna_db
  .getCatalogEntries()
  .then(data => { 
    res.json(data)
  })
  .catch(err => handleError(res,err))
})

/**
 * Create a Send or Need request
 */
app.post('/createRequest', (req, res) => {
  const newreq = req.body

  if(!Object.keys(newreq).length){
    console.log('API createRequest : Request details missing ')
    return res.status(400).json({ errors: "Request details missing"});
  } 

  // Check information is present in various fields. if not sent response 400

  manna_db
  .newRequest(newreq)
  .then(data => { 
    res.json(data)
  })  
  .catch(err => handleError(res,err))
})

/* *
* Logout
 */
app.get('/logout', (req, res) => {
  res.status(200).send('Logging out')
})
