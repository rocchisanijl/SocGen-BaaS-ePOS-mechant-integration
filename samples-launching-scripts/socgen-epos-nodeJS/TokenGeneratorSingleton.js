//use sync-request to avoid conccurence problems
var request = require('sync-request');

const SG_URL = 'SG_URL';
const username = 'username';
const password = 'password';

var token;
var dateRequest;
var expires_in;

class TokenGeneratorSingleton {
	constructor() {
	}
		
	getSGConnectToken() {
		if (isFirstRequest() || isExpired()) {
			retreiveToken();
		}
		return token;
	}
}

function retreiveToken() {
	console.log('retreiveToken');
	var res = request('POST', SG_URL, {
		headers: {
			'content-type': 'application/x-www-form-urlencoded',
			'Authorization': 'Basic ' + Buffer.from(username + ':' + password).toString('base64')
		},
		body: ''
	});
	dateRequest =new  Date();
	expires_in = JSON.parse(res.body).expires_in;
	token = JSON.parse(res.body).access_token;
	console.log('token1 ' + token);
}

function isFirstRequest() {
	console.log('isFirstRequest ' + (typeof dateRequest == 'undefined'));
	return (typeof dateRequest == 'undefined');
}

function isExpired() {
	var testDate = addSeconds();
	const currentDate = new Date();
	if (currentDate > testDate) {
		console.log('isExpired true');
		return true;
	}
	console.log('isExpired false');
	return false;
}

function addSeconds() {
	const date = new Date(dateRequest);
	date.setSeconds(date.getSeconds() + (expires_in));
	console.log('request date '+dateRequest);
	console.log('new date '+date);
	return date;
}

//Node.JS will cache and reuse the same object each time it’s required
module.exports = new TokenGeneratorSingleton();


