var vtigerMApiPlugin = {
    sendHttpRequest: function(url, userName, password, successCallback, errorCallback) {
        alert("#### vtigerMApiPlugin.js...sendHttpRequest");
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'VtigerMApiPlugin', // mapped to our native Java class called "VtigerMApiPlugin"
            'sendHttpRequest', // with this action name
            [{                  // and this array of custom arguments to sendHttpRequest
                'url': url,
                'userName': userName,
                'password': password,
            }]
        ); 
     }
}
module.exports = vtigerMApiPlugin;