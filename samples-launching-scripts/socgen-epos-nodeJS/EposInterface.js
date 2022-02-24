var request = require('request');
var validUrl = require('valid-url');
var TokenGeneratorSingleton = require('./TokenGeneratorSingleton');
const eposURL = "Epos_URL" + "ProcessAndRedirect";

module.exports = {

	sendLoanApplication(application) {
		var token = TokenGeneratorSingleton.getSGConnectToken();
		var options = {
			'method': 'POST',
			'url': eposURL + '?access_token=' + token,
			'headers': {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(application)
		};

		request(options, function(error, response) {
			if (error) throw new Error(error);//handle error
			
			console.log(response.body);

			if (response.statusCode == 200) {
				if (validUrl.isUri(response.body)) {//HB flow 
					// redirect your customer to this url
					console.log("HB Flow");
				} else {
					//html response (FF journey) redirect your customer to this page
					console.log("FF Flow");
				}
			} else {//error occured
				console.log("Error...");
			}


		});
	}
}


