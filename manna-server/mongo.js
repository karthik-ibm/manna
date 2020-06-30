var fs = require('fs');
const MongoClient = require('mongodb').MongoClient
const mongodbHost = 'dbaas240.hyperp-dbaas.cloud.ibm.com'  // Specify Hostname of your DBaaS server
const mongodbPort = '28212'                                // DBaaS server Port number
const mongodbname = 'admin?replicaSet=XXX&ssl=true'       // Replace XXX with database name
const authenticate = 'admin:XXX@'                         // Replace XXX with database password
var certFileBuf = fs.readFileSync('cert.pem');            // Update certificate filename and path if needed
var options = {
    replset: { sslCA: certFileBuf },
    useNewUrlParser: true
  }
const url = 'mongodb://'+authenticate+mongodbHost+':'+mongodbPort + '/' + mongodbname + '';

const dbName = 'Manna'
let db
let user, request, catalog

// Geodistance - get the distance between 2 co-ordinates
const Distance = require('geo-distance');

// Fire app messaging
//const send = require('./send');

const MongoDBConnect = () => {
    return new Promise((resolve, reject) => {
    MongoClient.connect(url,options,(err,client)=>{
        if(err){
            return console.log(err);
            reject(err);
        } else {
            // Store reference to the database for later use
            db = client.db(dbName)
            console.log(`Connected to Mongodb: ${url}`)
            console.log(`Database: ${dbName}`)
            resolve(db);
        }})
    })
}

// Initialize the DB when this module is loaded
(function getDbConnection() {
    console.log('Initializing Mongo connection...', 'getDbConnection()');
    MongoDBConnect().then((database) => {
        console.log('Mongo connection initialized.', 'getDbConnection()');
        db = database;
        user = db.collection('mannauser')
        request = db.collection('mannarequest')
        catalog = db.collection('mannacatalog')
        lastid = db.collection('mannalastusedid')
    }).catch((err) => {
        console.log('Error while initializing DB: ' + err.message, 'getDbConnection()');
        throw err;
    });
})();

/**
 * Check if the user is registered Manna User
 * @param {String} mannaID
 * @param {String} phone
 * 
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile, userrequests, all requests
 * reject(): the err object from data store
 */
function finduser(mannaID,phone,lat,lon){
     return new Promise((resolve,reject) => {
        let selectparams = {}
        selectparams['mannaID'] = mannaID;
        selectparams['mobile'] = phone;
        var user_coord = {
            lat: lat,
            lon: lon
          };         
        user.find(selectparams,{ projection: {_id:0,deviceID:0,type:0}}).toArray(function(err, userresults) {
        if (err) {
            reject(err);
        } else {
            if(userresults.length > 0){
                findAllRequests()
                .then(data1 => {
                        allreq1 = data1.allrequest;
                        var allreq = [];
                        const rad = '5 km'
                        allreq = neighbours(user_coord,allreq1,rad)
                        // Retrieve user requests if available
                        findUserRequests(mannaID,phone)
                        .then(data => {                         
                            userreq = data.myrequest
                            // Get catalog entries
                            getCatalogEntries()
                            .then(catdata => { 
                                catitems = catdata.cat_entries                                                  

                                resolve({ status: "OK",statusCode: 200, user_profile: userresults, my_request:userreq, all_requests:allreq, cat_items:catitems });                                                                     

                            })
                        })
                    })
                } else {
                resolve({ status: "USER NOT FOUND",statusCode: 200, user_profile: userresults});
            }
        }
     });
 });
}

/**
 * Check if the user is registered Manna User
 * @param {String} mannaID
 * @param {String} phone
 * 
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile, userrequests, all requests
 * reject(): the err object from data store
 */
function findUserRequests(mannaID,phone){
    return new Promise((resolve,reject) => {
        let selectparams = {}
        selectparams['mannaID'] = mannaID;
        if(phone.length > 0) {
            selectparams['mobile'] = phone;
        }

        //Get requests submitted by user
        request.find(selectparams,{ projection: {_id:0}}).toArray(function(err, userrequests) {
            if (err) {
                reject(err);
            } else {  
                let length = userrequests.length
                resolve({ No_UserRequest : length , myrequest: userrequests}); 
            }
        });
    }); 
}

/**
 * Get all requests submitted
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile, userrequests, all requests
 * reject(): the err object from data store
 */
function findAllRequests(){
    return new Promise((resolve,reject) => {

        //Get all requests submitted
        request.find({},{ projection: {_id:0}}).toArray(function(err, allrequests) {
            if (err) {
                reject(err);
            } else {  
                let length = allrequests.length
                resolve({ No_AllRequest : length , allrequest: allrequests}); 
            }
        });
    }); 
}

/**
 * Create Manna User
 * @param {String} phone
 * @param {String} lat
 * @param {String} lon
 * 
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile
 * reject(): the err object from data store
 */
function createuser(phone,lat,lon){
    return new Promise((resolve,reject) => {
        //let newID = '0004'
        //let insertparams = { mannaID: newID, mobile: phone };
        getnextID("mannaID")
        .then(data =>{
            newID = data        
            let insertparams = {}
            insertparams['mannaID'] = newID;
            insertparams['mobile'] = phone; 
            insertparams['latitude'] = lat; 
            insertparams['longitude'] = lon; 
        
            //resolve({id:newID})
            console.log(`In createuser phone : ${phone}`)
            user.insertOne(insertparams, function(err, result){
            if (err) {
                reject(err);
            } else { 
                console.log("Added 1 user") 
                updateNewID("mannaID") 
                resolve({id:newID})
            }
        })
        })    
    })
}

/**
 * Create and retrieve new user details and all reaquest
 * @param {String} phone
 * 
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile and all requests
 * reject(): the err object from data store
 */
function getNewUser(phone,lat,lon){
    return new Promise((resolve,reject) => {
        console.log(`In getNewUser phone : ${phone}`)
        createuser(phone,lat,lon)
        .then(data1 => {
            id = data1.id
           //Call finduser function to get user profile, user requests and all requests
           finduser(id, phone,lat,lon)
           .then(data2 => {
               if (data2.statuscode = 200) {
                new_user_profile = data2.user_profile;
                all_req = data2.all_requests;
                catitems = data2.cat_items;
                resolve({ status: "User Created",statusCode: 200, user_profile: new_user_profile, all_requests:all_req, cat_items:catitems }); 
               } else {
                resolve({ status: "Error in User Creation",statusCode: 200})
               }  
           })
        })    
    })
}

/**
 * Get the nearest neighbours based on current co-ordinates
 * @param {json} user_coord  -- Current Users Co-ordinates
 * @param {json} allrequests -- all share and need requests 
 * @param {string}  rad -- limit radius      
 * 
 * @return {json} nearest_neighbour-- requests within the radius 
 * 
 * 
 */
function neighbours(user_coord,allrequests,rad){
    var A = user_coord
    var B = {}
    const no_req = allrequests.length
    nearest_neighbour = []
    for (var i=0; i<no_req; i++){      
        B['lat'] = allrequests[i].pickupCoord.latitude
        B['lon'] = allrequests[i].pickupCoord.longitude

        const AtoB = Distance.between(A, B);

        if (AtoB < Distance(rad)) {
            //console.log(AtoB.human_readable())
            nearest_neighbour.push(allrequests[i]);
        }  
    }
    return  nearest_neighbour
}

/**
 * Get all catalog entries
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile, userrequests, all requests
 * reject(): the err object from data store
 */
function getCatalogEntries(){
    return new Promise((resolve,reject) => {

        //Get all catalog entries
        catalog.find({},{ projection: {_id:0}}).toArray(function(err, cat_entries) {
            if (err) {
                reject(err);
            } else {                 
                resolve({cat_entries: cat_entries}); 
            }
        });
    }); 
}

/**
 * Create a share or need request
 * @param {json} newrequest -- share or need requests 
 * 
 * @return {Promise} Promise -
 * resolve(): Objects with json for userprofile, userrequests, all requests
 * reject(): the err object from data store
 * 
 */
function newRequest(newrequest){
    return new Promise((resolve,reject) => {
        var newreq = newrequest
        //id = "req000000004"
        let id = ""
        getnextID("reqID")
        .then(data =>{
            id = data
            newreq.reqID = id
        
            //Get all catalog entries
            request.insertOne(newreq, function(err, result){
                if (err) {
                    reject(err);
                } else { 
                    updateNewID("reqID")  

                    // Send notification to share request which is embedded in need request
                    if(newreq.incomingReqs){
                    if(newreq.incomingReqs.reqID){
                        let in_req_mannaID = newreq.incomingReqs.mannaID
                        user.find({mannaID: in_req_mannaID}).toArray(function(err, result) {
                            if (err) throw err;
                            let in_deviceid = result.deviceID
                            let msg = newreq.fname + ' is requesting you to share :'
                            console.log(msg)
                            //send.mannaSendOne(in_deviceid,msg)
                        })
                    } }
                    
                    // Send notification to need request which is embedded in share request
                    if(newreq.outgoingReqs){
                    if(newreq.outgoingReqs.reqID){
                        let out_req_mannaID = newreq.outgoingReqs.mannaID
                        user.find({mannaID: out_req_mannaID}).toArray(function(err, result) {
                            if (err) throw err;
                            let out_deviceid = result.deviceID
                            let msg = newreq.fname + ' is willing to share the items you need'
                            console.log(msg)
                            //send.mannaSendOne(out_deviceid,msg)
                        })
                    } } 
                    
                    // Send notifications to all users in  5km vicinity when a request is created
                    let long = newreq.pickupCoord.longitude
                    let lati = newreq.pickupCoord.latitude
                    let neighbour_users = user_neighbours(long,lati)
                    if(neighbour_users){
                        no_users = neighbour_users.length
                        for (var i=0; i<no_users; i++){      
                            let deviceID = neighbour_users.deviceID
                            if(newreq.reqType == "N") {msg = newreq.fname + " is looking for items" }
                            if(newreq.reqType == "S") {msg = newreq.fname + " is willing to share items" } 
                            console.log(msg)                           
                            //send.mannaSendOne(out_deviceid,msg)
                        }
                    }                    
                    // Send response back to create function
                    resolve({status: 'Request Created Successfully', statusCode: 200}); 
                }
            })
        });
    }); 
}

/**
 * Get the next id that can be used
 * @param {string} field  -- Fieldname    
 * 
 * @return {string} next_ID 
 * 
 * 
 */
function getnextID(field){
    return new Promise((resolve,reject) => {
         const A = field
        
         if(!A){
             console.log("No field found")
             resolve({status:400});
         } else {
            lastid.find({}).toArray(function(err, n_id) {
                if(err){
                    reject(err)                
                 } else {
                     // Get next mannaID
                    if(A == "mannaID"){
                         const n_mannaID = String(n_id[0].last_mannaID+1)
                         let next_ID = n_mannaID.padStart(4, "0")   
                         resolve(next_ID)
                     }
 
                     // Get next request ID
                     if(A == "reqID"){
                         const n_reqID = String(n_id[0].last_reqID+1)
                         let next_ID = 'req' + n_reqID.padStart(10, "0")
                         resolve(next_ID)
                     }
 
                    // Get next category
                     if(A == "category"){
                         const n_cat = String(n_id[0].last_category+1)
                         let next_ID = 'cat' + n_cat.padStart(6, "0")
                         resolve(next_ID)
                     }

                    // Get next item id
                     if(A == "item_id"){
                         const n_item_id = String(n_id[0].last_item_id+1)
                         let next_ID = 'itm' + n_item_id.padStart(7, "0")
                         resolve(next_ID)
                     }               
                 } 
             })
         }
    });
}

/**
 * Update the last id used for creation of userid/request
 * @param {string} field  -- Fieldname    
 */
function updateNewID(field){   
     if(field == "mannaID"){
         var newvalues = { $inc: { last_mannaID: 1 } }        
     } 
     if(field == "reqID"){
         var newvalues = { $inc: { last_reqID: 1 } }
     }
     lastid.updateOne({}, newvalues, function(err, res) {
       if (err) throw err;      
     }); 
}


// Get all user neighbours
function user_neighbours(longitude,latitude){
    var rad = 5
    var A = {}
    A['lat'] = latitude
    A['lon'] = longitude
    var B = {}

    user.find({},{projection: {deviceID:1, mannaID:1, longitude:1, latitude:1}}).toArray(function(err, all_users){
        if(err) throw err
        const no_user = all_users.length
        nearest_neighbour = []

        for (var i=0; i<no_user; i++){      
            B['lat'] = all_users[i].latitude
            B['lon'] = all_users[i].longitude
    
            const AtoB = Distance.between(A, B);
    
            if (AtoB < Distance(rad)) {
                //console.log(AtoB.human_readable())
                nearest_neighbour.push(all_users[i]);
            }  
        }
        return  nearest_neighbour
    })
}

module.exports = {
    finduser: finduser,
    findUserRequests: findUserRequests,
    findAllRequests:findAllRequests,
    createuser:createuser,
    getNewUser:getNewUser,
    neighbours:neighbours,
    getCatalogEntries:getCatalogEntries,
    newRequest:newRequest,
    getnextID:getnextID
  };
